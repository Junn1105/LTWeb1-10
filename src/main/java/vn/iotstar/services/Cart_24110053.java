package vn.iotstar.services;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/** Session cart; the controller serializes access on the session. */
public class Cart_24110053 implements Serializable {
    public static final int MAX_PER_BOOK = 99;
    private final Map<Integer, Integer> quantities = new TreeMap<>();
    public Map<Integer, Integer> snapshot() { return new TreeMap<>(quantities); }
    public void add(int id, int quantity, int stock) {
        validate(quantity, stock);
        long combined = (long) quantities.getOrDefault(id, 0) + quantity;
        if (combined > Math.min(MAX_PER_BOOK, stock)) throw new IllegalArgumentException("Số lượng vượt tồn kho hoặc giới hạn 99 cuốn mỗi đầu sách.");
        quantities.put(id, (int) combined);
    }
    public void update(int id, int quantity, int stock) {
        if (!quantities.containsKey(id)) throw new IllegalArgumentException("Sách không có trong giỏ hàng.");
        validate(quantity, stock);
        quantities.put(id, quantity);
    }
    public static void validate(int quantity, int stock) {
        if (quantity < 1 || quantity > Math.min(MAX_PER_BOOK, stock))
            throw new IllegalArgumentException("Số lượng phải từ 1 đến " + Math.max(0, Math.min(MAX_PER_BOOK, stock)) + " và không vượt tồn kho.");
    }
    public void remove(int id) { quantities.remove(id); }
    public void clear() { quantities.clear(); }
}
