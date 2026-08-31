# Notifications

ShopMint generates order notifications when important order lifecycle operations are completed.

The current implementation keeps notification processing simple and creates notification information as part of the corresponding order operation.

This provides the baseline for identifying coupling between the order workflow and individual notification mechanisms.

## Supported Notification Channels

ShopMint currently supports:

* Email
* SMS

## Notification Processing

Notifications are generated for different order lifecycle events.

Currently, notifications are generated when:

* Payment is completed and the order is confirmed
* The order is shipped
* The order is delivered
* The order is cancelled

The notifications are currently simulated. The application does not connect to any real email or SMS provider.

Each notification contains information such as:

* Notification type
* Recipient
* Notification message

The Angular application displays the generated notification information for the current order.

## Notification Flow

```text
PAYMENT_PENDING
      ↓
   Pay Order
      ↓
  CONFIRMED
      ↓
Confirmation Notification
      ↓
   ┌───────────────┐
   ↓               ↓
  Email            SMS
```

The same approach is used for other order lifecycle events:

```text
Ship Order       → Shipping Notification
Deliver Order    → Delivery Notification
Cancel Order     → Cancellation Notification
```

## Current Implementation

Notification creation is currently handled directly inside the order placement workflow.

The order service knows about the individual notification mechanisms and creates notification information for Email and SMS.

This is intentionally kept simple for the baseline implementation.

As additional notification channels are introduced, the order workflow would need to be modified to support each new channel. This creates increasing coupling between order processing and notification mechanisms.

## Future Evolution

Additional notification channels may be introduced in the future, such as:

* Push notifications
* WhatsApp
* In-app notifications

The notification workflow will be used to explore how the order process can be decoupled from individual notification mechanisms.

The goal is to allow new notification channels to be introduced without continuously modifying the core order placement workflow.
