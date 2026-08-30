# Discount Rules

ShopMint supports multiple discount rules that are evaluated during checkout.

The current implementation intentionally keeps the discount logic simple and centralized in `OrderService`. This provides a realistic starting point for identifying design smells and later refactoring the code using the Chain of Responsibility pattern.

## Current Discount Rules

### 1. Order Value Discount

Orders with a subtotal of ₹5,000 or more receive a 10% discount.

### 2. Large Order Discount

Orders with a subtotal of ₹10,000 or more receive an additional ₹500 discount.

### 3. Coupon Discount

Customers can enter the coupon code `WELCOME1000` to receive an additional ₹1,000 discount.

### 4. New Customer Discount

New customers receive an additional ₹250 discount on their first order.

### 5. Festival Discount

When the festival sale is active, orders with a subtotal of ₹20,000 or more receive an additional 15% discount.

The festival sale is currently controlled by a simple flag in `OrderService`.

### 6. Maximum Discount

The total discount applied to an order cannot exceed ₹5,000, regardless of how many discount rules are applicable.

## Discount Evaluation Order

The rules are currently evaluated in the following order:

```text
Order Value Discount
        ↓
Large Order Discount
        ↓
Coupon Discount
        ↓
New Customer Discount
        ↓
Festival Discount
        ↓
Maximum Discount
```

Multiple discounts can be applied to the same order.

## Example

For an order with a subtotal of ₹25,000 and coupon `WELCOME1000`:

```text
Order Value Discount   = ₹2,500
Large Order Discount   = ₹500
Coupon Discount        = ₹1,000
New Customer Discount  = ₹250
Festival Discount      = ₹3,750
--------------------------------
Calculated Discount    = ₹8,000
Maximum Discount       = ₹5,000
```

Therefore:

```text
Subtotal                ₹25,000
Discount                -₹5,000
Shipping                  ₹50
--------------------------------
Final Amount             ₹20,050
```

## Current Implementation

The discount rules are currently implemented using conditional logic inside `OrderService`.

This is intentional. As more discount rules are introduced, the number of conditions and responsibilities in the service can grow, making the code harder to maintain and extend.

This implementation will later serve as the baseline for refactoring the discount calculation using the **Chain of Responsibility Pattern**.

## Future Evolution

The discount system may evolve to support additional rules such as:

* Category-specific discounts
* Product-specific discounts
* Seasonal promotions
* Membership discounts
* Customer-specific offers

These requirements can be used to demonstrate how the existing implementation becomes difficult to maintain and how an appropriate design pattern can address the resulting design problems.
