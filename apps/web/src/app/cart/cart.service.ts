import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cart } from './cart.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiUrl = 'http://localhost:8080/carts';

  constructor(private http: HttpClient) {
  }

  getCart(customerId: number): Observable<Cart> {
    return this.http.get<Cart>(
      `${this.apiUrl}/${customerId}`
    );
  }

  addToCart(
    customerId: number,
    productId: number): Observable<string> {

    return this.http.post(
      `${this.apiUrl}/${customerId}/items?productId=${productId}`,
      {},
      { responseType: 'text' }
    );
  }

  removeFromCart(
    customerId: number,
    productId: number
  ): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}/${customerId}/items/${productId}`,
      { responseType: 'text' }
    );
  }

  clearCart(customerId: number): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}/${customerId}`,
      { responseType: 'text' }
    );
  }
}