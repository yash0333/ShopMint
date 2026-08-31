# Order Management

ShopMint orders follow a defined lifecycle from order creation through payment, shipping, delivery, or cancellation.

The current implementation introduces explicit order states and operations. State transition rules are currently handled directly inside `OrderService`.

This provides a realistic baseline for identifying state-dependent logic and later refactoring the order workflow using the **State Pattern**.

## Order States

An order can be in one of the following states:

* `PAYMENT_PENDING`
* `CONFIRMED`
* `SHIPPED`
* `DELIVERED`
* `CANCELLED`

## Order Lifecycle

The current order lifecycle is:

```text id="r5u2xk"
                  Place Order
                       ↓
              PAYMENT_PENDING
                 │           │
              Pay()       Cancel()
                 │           │
                 ↓           ↓
             CONFIRMED    CANCELLED
              │      │
           Ship()  Cancel()
              │      │
              ↓      ↓
           SHIPPED  CANCELLED
              │
           Deliver()
              │
              ↓
          DELIVERED
```

## State Transitions

| Current State     | Operation | Next State  |
| ----------------- | --------- | ----------- |
| `PAYMENT_PENDING` | Pay       | `CONFIRMED` |
| `PAYMENT_PENDING` | Cancel    | `CANCELLED` |
| `CONFIRMED`       | Ship      | `SHIPPED`   |
| `CONFIRMED`       | Cancel    | `CANCELLED` |
| `SHIPPED`         | Deliver   | `DELIVERED` |

Operations that are not defined for the current state are rejected by the application.

When an order is cancelled, the inventory reserved for the order is restored. The cart is not restored.

## Current Implementation

State transition logic is currently implemented directly inside `OrderService`.

Each order operation checks the current status before performing the requested action. For example, payment is allowed only when the order is in `PAYMENT_PENDING`, while shipping is allowed only when the order is `CONFIRMED`.

As the number of states and operations grows, `OrderService` can accumulate more state-dependent conditions. This makes the service responsible for knowing the behavior associated with every possible state.

This is the main design problem that the next refactoring will address.

## Future Evolution

The order workflow is a good candidate for the **State Pattern** because an order's behavior changes depending on its current state.

The intended design is to move state-specific behavior from `OrderService` into dedicated state classes:

```text id="7z4wqf"
Order
  │
  └── OrderState
        │
        ├── PaymentPendingState
        ├── ConfirmedState
        ├── ShippedState
        ├── DeliveredState
        └── CancelledState
```

Instead of `OrderService` containing multiple state checks, the order will delegate operations to its current state.

For example:

```text id="x5y8na"
order.pay()
    ↓
PaymentPendingState
    ↓
ConfirmedState
```

and:

```text id="3z7mqa"
order.ship()
    ↓
ConfirmedState
    ↓
ShippedState
```

The goal is to encapsulate state-specific behavior and transitions within the appropriate state classes, making the order lifecycle easier to understand, maintain, and extend.
