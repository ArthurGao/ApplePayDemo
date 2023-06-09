1. Apple Pay Integration:
Set up the necessary HTML and JavaScript code to integrate Apple Pay into your web application.
Configure the Apple Pay session by specifying the supported payment networks, merchant capabilities, and other relevant details.
Implement the necessary event handlers, such as *onpaymentauthorized*, to capture the payment details and initiate the payment processing.
HTML:
```html
<script src="https://applepay.cdn.apple.com/jsapi/v3/apple-pay.js" type="module"></script>
```


2. Payment Authorization and Tokenization:
Use the *ApplePaySession* object to handle the *onpaymentauthorized* event and retrieve the payment token, billing contact, and shipping contact.
After calling ```ApplePaySession.begin()```, The onvalidatemerchant event handler is triggered, and the Apple Pay session needs to validate the merchant. It is responsible for sending the validation URL received from Apple Pay to the backend server for merchant validation. 
Send an HTTP request (e.g., a POST request) to your backend server, passing the payment token and other relevant details.

3. Validate merchant
On the server side, handle the request and validate the payment token with Apple's server by making a request to Apple's */validate-merchant* endpoint.

```javascript
session.onvalidatemerchant = async (event) => {
    const validationURL = event.validationURL; // The validation URL received from Apple Pay
}
```

Implement the merchant validation logic on your backend server by sending a request to Apple's /validate-merchant endpoint with the received validation URL.
Verify the response from Apple's server to ensure the validation was successful.
Extract the merchant session from the response and complete the validation process.

```java
// Create the HTTP entity with the request payload and headers
HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
// Make the HTTP POST request
RestTemplate restTemplate = new RestTemplate();
ResponseEntity<String> response = restTemplate.postForEntity("VALIDATION_URL_RECEIVED_FROM_APPLE_PAY", entity, String.class);
```

VALIDATION_URL_RECEIVED_FROM_APPLE_PAY is gotten from 
```javascript
const validationURL = event.validationURL;
```

4. Complete the validation process and present the payment sheet
After receiving the merchant session from the server, the handler calls session.completeMerchantValidation(merchantSession) to complete the merchant validation process.

```javascript 
// Complete the validation with the merchant session
session.completeMerchantValidation(merchantSession);
```

the Apple Pay payment sheet will be presented to the user. The user will see the payment options and can select their preferred payment method. They may need to authenticate using Face ID, Touch ID, or their device passcode, depending on the device and payment method chosen.

Once the user has selected a payment method and completed the authentication process, the onpaymentauthorized event will be triggered. This event provides you with the payment details, including the payment token, billing contact, shipping contact, and other relevant information

```javascript
session.onpaymentauthorized = async (event) => {}
```

If user cancel the payment, the oncancel event will be triggered.

```javascript
session.oncancel = (event) => {
    // The payment was cancelled by the user
}
```

5. Payment Processing with Chargent:
In ```onpaymentauthorized```, you can invoke the Chargent API to initiate the payment transaction.
Construct the necessary parameters (e.g., payment token, amount) and make an HTTP request to the Chargent API endpoint.
To process a payment using Chargent, you typically utilize the Chargent objects and methods provided by the library, such as creating a Charge__c record or invoking the ChargentOrder functionality. 

6. eWay Payment Processing:
    - Set up the eWAY gateway configuration in Chargent:
       - Configure and connect the eWAY gateway with Chargent by providing the necessary credentials, API endpoint URLs, and other configuration details.
       - This configuration step is typically done within the Salesforce environment using Chargent's configuration settings.
    - Prepare the payment transaction data:
       - Collect and prepare the required payment transaction data, such as the payment amount, currency, customer details, and any other relevant information.
       - Ensure that you have the payment token received from Apple Pay and any additional parameters required by eWAY for the transaction.
    - Invoke the Chargent methods to initiate the payment transaction:
       - Utilize the Chargent Apex methods or classes provided by the Chargent package to initiate the payment transaction using the configured eWAY gateway.
       - The specific methods and classes to use may depend on your Chargent version and configuration. You can consult the Chargent documentation and resources for the appropriate methods to invoke.
    - Handle the payment response:
       - Receive the payment response from eWAY via Chargent.
       - Parse and process the response to determine the status of the payment transaction, transaction ID, error messages, or any other relevant information returned by eWAY.

Process the response from the Chargent API to determine the status of the payment transaction.
Handle the success or failure of the payment processing and provide appropriate feedback to the user.

7. Apple Pay Session summarize:
- ```session.begin()``` initializes the Apple Pay session and presents the payment sheet.
- ```onvalidatemerchant``` event is triggered after ```session.begin()``` and before the payment sheet is presented.
- User interacts with the payment sheet and authorizes the payment.
- ```onpaymentauthorized``` event is triggered after the user authorizes the payment.

**The order of events is:**
``` session.begin() -> onvalidatemerchant -> Payment sheet presented -> User authorization -> onpaymentauthorized -> Chargent -> eWay ```