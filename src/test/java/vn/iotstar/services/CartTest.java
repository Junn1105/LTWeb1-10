package vn.iotstar.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {
    @Test void addMergeUpdateRemoveAndClear() {
        var cart = new Cart_24110053();
        cart.add(1, 2, 5); cart.add(1, 3, 5);
        assertEquals(5, cart.snapshot().get(1));
        cart.update(1, 1, 5);
        assertEquals(1, cart.snapshot().get(1));
        cart.add(2, 1, 2); cart.remove(1);
        assertFalse(cart.snapshot().containsKey(1));
        cart.clear(); assertTrue(cart.snapshot().isEmpty());
    }
    @Test void invalidQuantitiesNeverMutateCart() {
        var cart = new Cart_24110053(); cart.add(1, 3, 5);
        for (int quantity : new int[]{0, -1, 6, 100, Integer.MAX_VALUE}) {
            assertThrows(IllegalArgumentException.class, () -> cart.update(1, quantity, 5));
            assertEquals(3, cart.snapshot().get(1));
        }
        assertThrows(IllegalArgumentException.class, () -> cart.add(1, 3, 5));
        assertThrows(IllegalArgumentException.class, () -> cart.add(2, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> cart.update(2, 1, 5));
        assertThrows(IllegalArgumentException.class, () -> cart.add(2, 100, 1000));
        cart.snapshot().clear(); assertEquals(3, cart.snapshot().get(1));
    }
    @Test void recipientValidation() {
        assertDoesNotThrow(() -> ShoppingService_24110053.validateRecipient("Nguyễn Văn A", "0901234567", "01 Võ Văn Ngân", ""));
        for (String phone : new String[]{"", "901234567", "09012345678", "0abcdefghi"})
            assertThrows(IllegalArgumentException.class, () -> ShoppingService_24110053.validateRecipient("A", phone, "B", ""));
        assertThrows(IllegalArgumentException.class, () -> ShoppingService_24110053.validateRecipient(" ", "0901234567", "B", ""));
        assertThrows(IllegalArgumentException.class, () -> ShoppingService_24110053.validateRecipient("A", "0901234567", "B", "x".repeat(501)));
    }
}
