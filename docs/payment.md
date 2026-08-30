# Payment

ShopMint supports multiple payment methods during checkout.

The current implementation intentionally keeps payment processing simple and uses straightforward conditional logic. This provides a realistic baseline for identifying design problems and later refactoring the payment flow.

## Supported Payment Methods

The following payment methods are currently supported:

* UPI
* Credit Card
* Debit Card
* Cash on Delivery

## Payment Processing

During checkout, the customer selects a payment method.

The selected payment method is then processed during order placement.

The payment processing is currently simulated. ShopMint does not connect to any real payment gateway or external payment service.

The application generates a payment result containing information such as:

* Payment status
* Payment message
* Selected payment method

## Payment Flow

```text
Checkout
   ↓
Select Payment Method
   ↓
Place Order
   ↓
Process Payment
   ↓
Payment Result
   ↓
Order Confirmation
```

## Current Implementation

Payment processing is currently handled using conditional logic based on the selected payment type.

For example, the order workflow determines which payment logic should be executed based on the selected payment method.

This approach is intentionally simple at the baseline stage.

As more payment methods are introduced, the conditional logic can grow and make the payment processing code increasingly difficult to maintain and extend.

## Future Evolution

Additional payment methods may be introduced in the future, such as:

* PayPal
* Bank Transfer
* Digital Wallets
* Buy Now, Pay Later

These requirements may be used to demonstrate the problems caused by tightly coupled payment logic and explore an appropriate design pattern for improving the implementation.

The goal is not to introduce a pattern simply because multiple payment types exist, but to first understand the design problem and then evaluate whether a pattern provides a meaningful solution.
