import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ChargentPaymentProcessor {

    private static final String CHARGENT_API_URL = "CHARGENT_API_URL";

    public static void processPayment(String paymentToken, String amount) {
        try {
            // Create the HTTP client
            CloseableHttpClient httpClient = HttpClients.createDefault();

            // Create the HTTP POST request to the Chargent API
            HttpPost httpPost = new HttpPost(CHARGENT_API_URL);
            httpPost.setHeader("Content-Type", "application/json");

            // Construct the request body
            String requestBody = buildPaymentRequestBody(paymentToken, amount);
            StringEntity entity = new StringEntity(requestBody);
            httpPost.setEntity(entity);

            // Execute the request
            HttpResponse response = httpClient.execute(httpPost);

            // Process the response
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String responseBody = getResponseBody(response.getEntity());
                // Process the response from Chargent API
                System.out.println("Payment processed successfully: " + responseBody);
            } else {
                System.out.println("Payment processing failed. Status Code: " + statusCode);
            }

            // Close the HTTP client
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String buildPaymentRequestBody(String paymentToken, String amount) {
        // Build the request body JSON object with payment token and amount
        // Customize this method based on your specific requirements
        return "{\"paymentToken\": \"" + paymentToken + "\", \"amount\": \"" + amount + "\"}";
    }

    private static String getResponseBody(HttpEntity entity) throws IOException {
        InputStream inputStream = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
