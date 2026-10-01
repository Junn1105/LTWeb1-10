package vn.iotstar.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import vn.iotstar.entity.*;
import vn.iotstar.util.JpaUtil_24110053;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

/** Opt-in SQL Server integration test. Uses and removes only its own fixtures. */
@EnabledIfEnvironmentVariable(named = "SHOPPING_DB_TEST", matches = "true")
class ShoppingDatabaseTest {
    @Test void codTransactionsHistoryAndConcurrentStock() throws Exception {
        var service = new ShoppingService_24110053();
        int userId = 0, bookId = 0;
        try {
            try (EntityManager em = JpaUtil_24110053.entityManager()) {
                em.getTransaction().begin();
                var user = new User_24110053(); user.setEmail("test-" + UUID.randomUUID().toString().substring(0, 12) + "@example.com");
                user.setFullname("Shopping test"); user.setPasswd("test"); user.setAdmin(false); em.persist(user);
                var book = new Book_24110053(); book.setTitle("Shopping test"); book.setPrice(new BigDecimal("20.00")); book.setQuantity(3); em.persist(book);
                em.getTransaction().commit(); userId = user.getId(); bookId = book.getBookid();
            }
            final int uid = userId, bid = bookId;
            var prices = Map.of(bid, new BigDecimal("20.00"));
            assertThrows(IllegalArgumentException.class, () -> service.checkout(uid, Map.of(bid, 4), prices, "Test", "0901234567", "Test address", ""));
            assertEquals(3, service.stock(bid)); assertTrue(service.orders(uid, null).isEmpty());
            assertThrows(IllegalArgumentException.class, () -> service.checkout(uid, Map.of(bid, 1), Map.of(bid, BigDecimal.ONE), "Test", "0901234567", "Test address", ""));
            assertEquals(3, service.stock(bid));
            // Failure after locking/decrementing the first book must roll back everything.
            assertThrows(IllegalArgumentException.class, () -> service.checkout(uid, Map.of(bid, 1, Integer.MAX_VALUE, 1), prices, "Test", "0901234567", "Test address", ""));
            assertEquals(3, service.stock(bid)); assertTrue(service.orders(uid, null).isEmpty());
            int orderId = service.checkout(uid, Map.of(bid, 2), prices, "Nguyễn Văn A", "0901234567", "Thủ Đức", "Gọi trước");
            var orders = service.orders(uid, OrderStatus_24110053.NEW);
            assertEquals(1, orders.size()); assertEquals(0, new BigDecimal("40.00").compareTo(orders.getFirst().getTotal()));
            assertEquals("Nguyễn Văn A", orders.getFirst().getRecipient());
            assertEquals("COD", orders.getFirst().getPaymentMethod()); assertEquals(1, service.stock(bid));
            assertTrue(service.orders(-1, null).isEmpty());
            for (var status : OrderStatus_24110053.values()) {
                try (EntityManager em = JpaUtil_24110053.entityManager()) {
                    em.getTransaction().begin();
                    em.createNativeQuery("UPDATE customer_orders SET status = ?1 WHERE id = ?2").setParameter(1, status.name()).setParameter(2, orderId).executeUpdate();
                    em.getTransaction().commit();
                }
                assertEquals(status, service.orders(uid, status).getFirst().getStatus());
                if (status != OrderStatus_24110053.NEW) assertTrue(service.orders(uid, OrderStatus_24110053.NEW).isEmpty());
            }
            try (var executor = Executors.newFixedThreadPool(2)) {
                var start = new CountDownLatch(1);
                Callable<Boolean> buyer = () -> {
                    start.await();
                    try { service.checkout(uid, Map.of(bid, 1), prices, "Test", "0901234567", "Test address", ""); return true; }
                    catch (IllegalArgumentException ex) { return false; }
                };
                var first = executor.submit(buyer); var second = executor.submit(buyer); start.countDown();
                assertNotEquals(first.get(30, TimeUnit.SECONDS), second.get(30, TimeUnit.SECONDS));
            }
            assertEquals(0, service.stock(bid)); assertEquals(2, service.orders(uid, null).size());
            try (EntityManager em = JpaUtil_24110053.entityManager()) {
                em.getTransaction().begin(); em.find(Book_24110053.class, bid).setTitle("Changed title");
                em.find(Book_24110053.class, bid).setPrice(BigDecimal.ONE); em.getTransaction().commit();
            }
            assertEquals("Shopping test", service.orders(uid, null).getFirst().getItems().getFirst().getTitle());
            assertEquals(0, new BigDecimal("20.00").compareTo(service.orders(uid, null).getFirst().getItems().getFirst().getUnitPrice()));
        } finally {
            try (EntityManager em = JpaUtil_24110053.entityManager()) {
                em.getTransaction().begin();
                em.createNativeQuery("DELETE FROM order_items WHERE order_id IN (SELECT id FROM customer_orders WHERE user_id = ?1)").setParameter(1, userId).executeUpdate();
                em.createNativeQuery("DELETE FROM customer_orders WHERE user_id = ?1").setParameter(1, userId).executeUpdate();
                em.createNativeQuery("DELETE FROM books WHERE bookid = ?1").setParameter(1, bookId).executeUpdate();
                em.createNativeQuery("DELETE FROM users WHERE id = ?1").setParameter(1, userId).executeUpdate();
                em.getTransaction().commit();
            }
        }
    }
}
