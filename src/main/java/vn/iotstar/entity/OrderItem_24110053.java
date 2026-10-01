package vn.iotstar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem_24110053 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id", nullable = false)
    private Order_24110053 order;
    // Snapshot values deliberately survive later book edits/deletion.
    @Column(name = "book_id", nullable = false)
    private Integer bookId;
    @Column(nullable = false, length = 200)
    private String title;
    @Column(name = "unit_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice;
    @Column(nullable = false)
    private Integer quantity;
    public Integer getId() { return id; }
    public void setOrder(Order_24110053 value) { order = value; }
    public Integer getBookId() { return bookId; }
    public void setBookId(Integer value) { bookId = value; }
    public String getTitle() { return title; }
    public void setTitle(String value) { title = value; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal value) { unitPrice = value; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer value) { quantity = value; }
    public BigDecimal getSubtotal() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
