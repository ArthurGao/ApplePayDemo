import com.eway.payment.rapid.sdk.RapidClient;
import com.eway.payment.rapid.sdk.RapidSDK;
import com.eway.payment.rapid.sdk.beans.external.PaymentMethod;
import com.eway.payment.rapid.sdk.beans.external.Transaction;
import com.eway.payment.rapid.sdk.beans.external.TransactionType;
import com.eway.payment.rapid.sdk.beans.internal.Payment;

public class EwayDirectConnection {

    public static void main(String[] args) {
        // Configure the eWAY API credentials
        String apiKey = "YOUR_API_KEY";
        String apiPassword = "YOUR_API_PASSWORD";
        boolean isTestMode = true; // Set to true for testing, false for production

        // Create the RapidClient instance
        RapidClient client = RapidSDK.newRapidClient(apiKey, apiPassword, isTestMode);

        // Collect the payment token from the client-side (Apple Pay JS)
        String paymentToken = "APPLE_PAY_PAYMENT_TOKEN";

        // Prepare the payment request
        Transaction transaction = new Transaction();
        transaction.setPayment(paymentToken);
        transaction.setTransactionType(TransactionType.Purchase);
        transaction.setPaymentMethod(PaymentMethod.ApplePay);
        transaction.setAmount(10.0); // Set the payment amount

        // Make the payment request
        Payment payment = client.create(Payment.class);
        payment.setTransaction(transaction);
        payment.process();

        // Handle the payment response
        if (payment.isSuccess()) {
            // Payment successful
            String transactionId = payment.getTransactionStatus().getTransactionID();
            System.out.println("Payment successful. Transaction ID: " + transactionId);
        } else {
            // Payment failed
            String errorCode = payment.getErrors().get(0).getErrorCode();
            String errorMessage = payment.getErrors().get(0).getMessage();
            System.out.println("Payment failed. Error Code: " + errorCode + ", Error Message: " + errorMessage);
        }
    }
}
