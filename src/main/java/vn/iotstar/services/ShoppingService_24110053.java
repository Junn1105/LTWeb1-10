package vn.iotstar.services;

import jakarta.persistence.*;
import vn.iotstar.entity.*;
import vn.iotstar.util.JpaUtil_24110053;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class ShoppingService_24110053 {
    public static class Line {
        private final int bookId, quantity, stock;
        private final String title;
        private final BigDecimal price;
        public Line(int id, Book_24110053 book, int quantity) {
            bookId = id; this.quantity = quantity;
            stock = book == null || book.getQuantity() == null ? 0 : Math.max(0, book.getQuantity());
            title = book == null ? "Sách đã ngừng bán (#" + id + ")" : book.getTitle();
            price = book == null ? null : book.getPrice();
        }
        public int getBookId() { return bookId; }
        public int getQuantity() { return quantity; }
        public int getStock() { return stock; }
        public int getLimit() { return Math.min(stock, Cart_24110053.MAX_PER_BOOK); }
        public String getTitle() { return title; }
        public BigDecimal getPrice() { return price; }
        public BigDecimal getSubtotal() { return price == null ? BigDecimal.ZERO : price.multiply(BigDecimal.valueOf(quantity)); }
        public boolean isAvailable() { return price != null && price.signum() >= 0 && quantity >= 1 && quantity <= getLimit(); }
    }

    public List<Line> lines(Map<Integer, Integer> cart) {
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            List<Line> result = new ArrayList<>();
            cart.forEach((id, quantity) -> result.add(new Line(id, em.find(Book_24110053.class, id), quantity)));
            return result;
        }
    }

    public int stock(int id) {
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            Book_24110053 book = em.find(Book_24110053.class, id);
            if (book == null || book.getPrice() == null || book.getPrice().signum() < 0)
                throw new IllegalArgumentException("Sách không còn bán hoặc chưa có giá hợp lệ.");
            return book.getQuantity() == null ? 0 : Math.max(0, book.getQuantity());
        }
    }

    public static void validateRecipient(String recipient, String phone, String address, String note) {
        if (recipient == null || recipient.isBlank() || recipient.length() > 100
                || phone == null || !phone.matches("0[0-9]{9}")
                || address == null || address.isBlank() || address.length() > 500
                || note == null || note.length() > 500)
            throw new IllegalArgumentException("Nhập tên người nhận (tối đa 100 ký tự), số điện thoại 10 số bắt đầu bằng 0, địa chỉ và ghi chú tối đa 500 ký tự.");
    }

    public int checkout(int userId, Map<Integer, Integer> cart, Map<Integer, BigDecimal> quotedPrices,
                        String recipient, String phone, String address, String note) {
        validateRecipient(recipient, phone, address, note);
        if (cart.isEmpty()) throw new IllegalArgumentException("Giỏ hàng đang trống.");
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                Order_24110053 order = new Order_24110053();
                order.setUserId(userId); order.setRecipient(recipient); order.setPhone(phone);
                order.setAddress(address); order.setNote(note);
                order.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                order.setStatus(OrderStatus_24110053.NEW);
                BigDecimal total = BigDecimal.ZERO;
                // Stable lock ordering and one transaction prevent overselling and partial orders.
                for (var entry : new TreeMap<>(cart).entrySet()) {
                    Book_24110053 book = em.find(Book_24110053.class, entry.getKey(), LockModeType.PESSIMISTIC_WRITE);
                    Line line = new Line(entry.getKey(), book, entry.getValue());
                    if (!line.isAvailable()) throw new IllegalArgumentException("Tồn kho hoặc giá sách đã thay đổi. Vui lòng kiểm tra lại giỏ hàng.");
                    BigDecimal quoted = quotedPrices == null ? null : quotedPrices.get(entry.getKey());
                    if (quoted == null || quoted.compareTo(book.getPrice()) != 0)
                        throw new IllegalArgumentException("Giá sách đã thay đổi. Vui lòng kiểm tra và xác nhận lại thanh toán.");
                    OrderItem_24110053 item = new OrderItem_24110053();
                    item.setOrder(order); item.setBookId(book.getBookid()); item.setTitle(book.getTitle());
                    item.setUnitPrice(book.getPrice()); item.setQuantity(entry.getValue());
                    order.getItems().add(item); total = total.add(item.getSubtotal());
                    book.setQuantity(book.getQuantity() - entry.getValue());
                }
                order.setTotal(total); em.persist(order); tx.commit();
                return order.getId();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }

    public List<Order_24110053> orders(int userId, OrderStatus_24110053 status) {
        try (EntityManager em = JpaUtil_24110053.entityManager()) {
            var query = em.createQuery("select o from Order_24110053 o where o.userId = :userId"
                    + (status == null ? "" : " and o.status = :status") + " order by o.createdAt desc, o.id desc", Order_24110053.class);
            query.setParameter("userId", userId);
            if (status != null) query.setParameter("status", status);
            List<Order_24110053> result = query.getResultList();
            result.forEach(order -> order.getItems().size());
            return result;
        }
    }
}
