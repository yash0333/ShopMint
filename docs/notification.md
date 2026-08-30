# Notifications

ShopMint generates order notifications after an order is successfully placed.

The current implementation intentionally keeps notification processing simple and creates notification information directly as part of the order placement workflow.

This provides the baseline for identifying coupling between the order workflow and individual notification mechanisms.

## Supported Notification Channels

ShopMint currently supports:

* Email
* SMS

## Notification Processing

After an order is confirmed, ShopMint generates notification information for the supported notification channels.

The notifications are currently simulated. The application does not connect to any real email or SMS provider.

Each notification contains information such as:

* Notification type
* Recipient
* Notification message

The Angular application displays the generated notification information on the order confirmation screen.

## Notification Flow

```text
Order Confirmed
      ↓
Generate Notifications
      ↓
 ┌───────────────┐
 ↓               ↓
Email            SMS
 ↓               ↓
Notification    Notification
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
