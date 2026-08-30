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
  order = signal<any>(null);
  products: Product[] = [];

  cart = signal<Cart | null>(null);

  customerId = 1;

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
        this.products = products;
      },
      error: (error) => {
        console.error('Failed to load products:', error);
      }
    });
  }

  loadCart(): void {

    this.cartService.getCart(this.customerId).subscribe({
      next: (cart) => {
        console.log(
    'Cart items:',
    cart.items.map(item => item.product.name)
  );

this.cart.set(cart);

  console.log('Angular cart updated:', this.cart);
      },
      error: (error) => {
        console.error('Failed to load cart:', error);
      }
    });
  }

  addToCart(product: Product): void {
    console.log('Before add:', this.cart);

    this.cartService
      .addToCart(this.customerId, product.id, 1)
      .subscribe({
        next: () => {
          console.log('Product added:', product.name);

          this.loadCart();

          console.log('After load cart called: ', this.cart);

        },
        error: (error) => {
          console.error('Failed to add product:', error);
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
        this.paymentType,
        this.couponCode,
        this.shippingType)
      .subscribe({
        next: (order) => {

          console.log('Order placed:', order);

          this.order.set(order);

          this.loadCart();
        },

        error: (error) => {
          console.error('Failed to place order:', error);
        }
      });
  }
}