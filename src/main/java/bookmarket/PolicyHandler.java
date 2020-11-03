package bookmarket;

import bookmarket.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPaid_UpdateStatus(@Payload Paid paid){

        if(paid.isMe()){
            System.out.println("##### listener UpdateStatus : " + paid.toJson());

            updateStatus(paid.getOrderId(), paid.getStatus());

        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverShipped_UpdateStatus(@Payload Shipped shipped){

        if(shipped.isMe()){
            System.out.println("##### listener UpdateStatus : " + shipped.toJson());

            updateStatus(shipped.getOrderId(), shipped.getStatus());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDeliveryCanceled_UpdateStatus(@Payload DeliveryCanceled deliveryCanceled){

        if(deliveryCanceled.isMe()){
            System.out.println("##### listener UpdateStatus : " + deliveryCanceled.toJson());

            updateStatus(deliveryCanceled.getOrderId(), deliveryCanceled.getStatus());
        }
    }

    private void updateStatus(Long orderId, String status){
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        Order order = orderOptional.get();
        order.setStatus(status);

        orderRepository.save(order);
    }
}
