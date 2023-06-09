const initiatePayment = () => {
    if (window.ApplePaySession) {
        const merchantIdentifier = 'YOUR_MERCHANT_IDENTIFIER'; // Replace with your Apple Merchant ID
        const paymentRequest = {
            countryCode: 'US',
            currencyCode: 'USD',
            supportedNetworks: ['visa', 'mastercard', 'amex'],
            merchantCapabilities: ['supports3DS'],
            total: {
                label: 'Total Amount',
                amount: '10.00'
            }
        };

        const session = new ApplePaySession(1, paymentRequest);
        session.begin();

        session.onvalidatemerchant = async (event) => {
            const validationURL = event.validationURL; // The validation URL received from Apple Pay

            // Send the validation URL to the backend server
            try {
                const response = await fetch('/validate-merchant', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ validationURL })
                });
                const { merchantSession } = await response.json();

                // Complete the validation with the merchant session
                session.completeMerchantValidation(merchantSession);
            } catch (error) {
                // Merchant validation failed
                session.abort();
            }
        };

        session.onpaymentauthorized = async (event) => {
            const payment = event.payment; // The payment details received from Apple Pay
            const paymentToken = payment.token.paymentData; // Retrieve the payment token

            // Send the payment details to the backend server
            try {
                const response = await fetch('/process-payment', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ payment })
                });

                if (response.ok) {
                    // Payment processed successfully
                    session.completePayment(ApplePaySession.STATUS_SUCCESS);
                } else {
                    // Payment processing failed
                    session.completePayment(ApplePaySession.STATUS_FAILURE);
                }
            } catch (error) {
                // Payment processing failed
                session.completePayment(ApplePaySession.STATUS_FAILURE);
            }
        };

        session.oncancel = (event) => {
            // Payment cancelled by the user
        };
    } else {
        console.log('Apple Pay is not supported on this device');
    }
};
