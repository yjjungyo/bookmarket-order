package bookmarket;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long bookId;
    private Long qty;
    private String status;
    private Long customerId;

    @PostPersist
    public void onPostPersist(){
        Ordered ordered = new Ordered();
        BeanUtils.copyProperties(this, ordered);
        ordered.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        bookmarket.external.Payment payment = new bookmarket.external.Payment();
        payment.setOrderId(this.getId());
        payment.setStatus("Ordered");
        payment.setCustomerId(this.getCustomerId());
        // mappings goes here
        OrderApplication.applicationContext.getBean(bookmarket.external.PaymentService.class)
            .payReq(payment);


    }

    @PreRemove
    public void onPreRemove(){
        OrderCanceled orderCanceled = new OrderCanceled();
        BeanUtils.copyProperties(this, orderCanceled);
        orderCanceled.setStatus("OrderCanceled");
        orderCanceled.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }




}
