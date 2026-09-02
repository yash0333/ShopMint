# Order Management

ShopMint orders follow a defined lifecycle from order creation through payment, shipping, delivery, or cancellation.

The current implementation introduces explicit order states and operations. State transition rules are currently handled directly inside `OrderService`.

Orders also interact with inventory during their lifecycle. When an order is placed, the required stock is reserved while the order is in `PAYMENT_PENDING`. The reservation is either confirmed when payment succeeds or released when the pending order is cancelled.

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

```text
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

## Inventory Reservation

Inventory is reserved when an order is placed.

The reservation is created while the order is in `PAYMENT_PENDING`. Reserved stock is not available for another order.

For example:

```text
Before Order:

Available Stock = 10
Reserved Stock  = 0
```

When an order for 3 units is placed:

```text
After Order:

Available Stock = 7
Reserved Stock  = 3
Order Status    = PAYMENT_PENDING
```

The cart itself does not reserve inventory. Stock is reserved only when the order is successfully created.

### Successful Payment

When payment is completed successfully, the order moves from `PAYMENT_PENDING` to `CONFIRMED`.

The inventory reservation is then confirmed and the reserved quantity is removed:

```text
Before Payment:

Available Stock = 7
Reserved Stock  = 3
Order Status    = PAYMENT_PENDING

        ↓ Payment

After Payment:

Available Stock = 7
Reserved Stock  = 0
Order Status    = CONFIRMED
```

The reserved quantity is now considered committed to the confirmed order.

### Cancellation of a Pending Order

If an order is cancelled while it is in `PAYMENT_PENDING`, the inventory reservation is released.

```text
Available Stock = 7
Reserved Stock  = 3

        ↓ Cancel

Available Stock = 10
Reserved Stock  = 0
Order Status    = CANCELLED
```

### Cancellation of a Confirmed Order

A confirmed order no longer has an active reservation because the reservation was already confirmed after successful payment.

If a confirmed order is subsequently cancelled, the inventory is restored directly to available stock.

```text
Available Stock = 7
Reserved Stock  = 0
Order Status    = CONFIRMED

        ↓ Cancel

Available Stock = 10
Reserved Stock  = 0
Order Status    = CANCELLED
```

Therefore, cancellation has different inventory behavior depending on the current order state:

| Current State     | Cancellation Behavior                           |
| ----------------- | ----------------------------------------------- |
| `PAYMENT_PENDING` | Release reservation and restore available stock |
| `CONFIRMED`       | Restore committed inventory to available stock  |

The cart is not restored when an order is cancelled.

## Inventory Flow

The overall inventory behavior during the order lifecycle is:

```text
                     Place Order
                          ↓
                  Reserve Inventory
                          ↓
                 PAYMENT_PENDING
                    ↙           ↘
                Pay              Cancel
                 ↓                 ↓
             CONFIRMED         CANCELLED
                 │                 │
       Reservation Confirmed    Reservation Released
                 │
              Cancel
                 ↓
             CANCELLED
                 │
          Inventory Restored
```

## Current Implementation

State transition logic and inventory-related behavior are currently implemented directly inside `OrderService`.

Each order operation checks the current status before performing the requested action. For example, payment is allowed only when the order is in `PAYMENT_PENDING`, while shipping is allowed only when the order is `CONFIRMED`.

`OrderService` also currently coordinates inventory operations:

* Reserve inventory when an order is placed.
* Confirm the inventory reservation after successful payment.
* Release the reservation when a pending order is cancelled.
* Restore inventory when a confirmed order is cancelled.

As the number of states, operations, and state-dependent behaviors grows, `OrderService` can accumulate more conditional logic. This makes the service responsible for knowing the behavior associated with every possible state.

This is the main design problem that the next refactoring will address.

## Future Evolution

The order workflow is a good candidate for the **State Pattern** because an order's behavior changes depending on its current state.

The intended design is to move state-specific behavior from `OrderService` into dedicated state classes:

```text
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

Instead of `OrderService` containing multiple state checks, the order will delegate operations to the appropriate state.

For example:

```text
order.pay()
    ↓
PaymentPendingState
    ↓
Confirm inventory reservation
    ↓
ConfirmedState
```

and:

```text
order.cancel()
    ↓
PaymentPendingState
    ↓
Release inventory reservation
    ↓
CancelledState
```

For a confirmed order:

```text
order.cancel()
    ↓
ConfirmedState
    ↓
Restore inventory
    ↓
CancelledState
```

The goal is to encapsulate state-specific behavior and transitions within the appropriate state classes, making the order lifecycle easier to understand, maintain, and extend.
