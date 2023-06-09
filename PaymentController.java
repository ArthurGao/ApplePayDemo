import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @PostMapping("/process-payment")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            String paymentToken = paymentRequest.getPaymentToken();
            String amount = paymentRequest.getAmount();

            ChargentPaymentProcessor.processPayment(paymentToken, amount);

            return ResponseEntity.ok("Payment processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment processing failed");
        }
    }

    // Define the PaymentRequest class based on your requirements
    private static class PaymentRequest {
        private String paymentToken;
        private String amount;

        // getters and setters

        public String getPaymentToken() {
            return paymentToken;
        }

        public void setPaymentToken(String paymentToken) {
            this.paymentToken = paymentToken;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
