package vn.iotstar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_orders")
public class Order_24110053 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Column(nullable = false, length = 100)
    private String recipient;
    @Column(nullable = false, length = 20)
    private String phone;
    @Column(nullable = false, length = 500)
    private String address;
    @Column(length = 500)
    private String note;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private OrderStatus_24110053 status;
    @Column(name = "payment_method", nullable = false, length = 10)
    private String paymentMethod = "COD";
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal total;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @OrderBy("id")
    private List<OrderItem_24110053> items = new ArrayList<>();

    public Integer getId() { return id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer value) { userId = value; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String value) { recipient = value; }
    public String getPhone() { return phone; }
    public void setPhone(String value) { phone = value; }
    public String getAddress() { return address; }
    public void setAddress(String value) { address = value; }
    public String getNote() { return note; }
    public void setNote(String value) { note = value; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCreatedAtText() { return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")); }
    public void setCreatedAt(LocalDateTime value) { createdAt = value; }
    public OrderStatus_24110053 getStatus() { return status; }
    public void setStatus(OrderStatus_24110053 value) { status = value; }
    public String getPaymentMethod() { return paymentMethod; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal value) { total = value; }
    public List<OrderItem_24110053> getItems() { return items; }
}
