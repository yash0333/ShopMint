import { Component, OnInit, signal } from '@angular/core';
import { ProductService } from './products/product.service';
import { Product } from './products/product.model';
import { CartService } from './cart/cart.service';
import { Cart } from './cart/cart.model';
import { OrderService } from './orders/order.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  styleUrl: './app.css',
  imports: [
    FormsModule
  ]
})
export class App implements OnInit {

  paymentType = 'UPI';
  couponCode = '';
  shippingType = 'EXPRESS';
  customerId = 1;

  order = signal<any>(null);
  products = signal<Product[]>([]);
  cart = signal<Cart | null>(null);

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private orderService: OrderService
  ) {
  }

  ngOnInit(): void {

    this.loadProducts();

    this.loadCart();
  }

  loadProducts(): void {

    this.productService.getProducts().subscribe({
      next: (products) => {
        this.products.set(products);
      },
      error: (error) => {
        alert(error.error);
      }
    });
  }

  loadCart(): void {

    this.cartService.getCart(this.customerId).subscribe({
      next: (cart) => {

        this.cart.set(cart);

      },
      error: (error) => {
        alert(error.error);
      }
    });
  }

  addToCart(product: Product): void {
    console.log('Before add:', this.cart);

    this.cartService
      .addToCart(this.customerId, product.id)
      .subscribe({
        next: () => {
          this.loadCart();
        },
        error: (error) => {
          alert(error.error);
        }
      });
  }

  calculateCartTotal(): number {

    const cart = this.cart();

    if (!cart) {
      return 0;
    }

    return cart.items.reduce(
      (total, item) =>
        total + item.product.price * item.quantity,
      0
    );
  }

  placeOrder(): void {

    this.orderService
      .placeOrder(
        this.customerId,
        this.couponCode,
        this.shippingType)
      .subscribe({
        next: (order) => {
          this.order.set(order);
          this.loadCart();
          this.loadProducts();
        },

        error: (error) => {
          alert(error.error);
        }
      });
  }

  payOrder(): void {

    const currentOrder = this.order();

    if (!currentOrder) {
      return;
    }

    this.orderService
      .payOrder(currentOrder.id, this.paymentType)
      .subscribe({
        next: (order) => {
          this.order.set(order);
          this.loadProducts();
        },

        error: (error) => {
          alert(error.error);
        }
      });
  }

  shipOrder(): void {

    const currentOrder = this.order();

    if (!currentOrder) {
      return;
    }

    this.orderService
      .shipOrder(currentOrder.id)
      .subscribe({
        next: (order) => {
          this.order.set(order);
        },

        error: (error) => {
          alert(error.error);
        }
      });
  }

  deliverOrder(): void {

    const currentOrder = this.order();

    if (!currentOrder) {
      return;
    }

    this.orderService
      .deliverOrder(currentOrder.id)
      .subscribe({
        next: (order) => {
          this.order.set(order);
        },

        error: (error) => {
          alert(error.error);
        }
      });
  }

  cancelOrder(): void {

    const currentOrder = this.order();

    if (!currentOrder) {
      return;
    }

    this.orderService
      .cancelOrder(currentOrder.id)
      .subscribe({
        next: (order) => {
          this.order.set(order);
          this.loadProducts();
        },

        error: (error) => {
          alert(error.error);
        }
      });
  }
}