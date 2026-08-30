import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiUrl = 'http://localhost:8080/orders';

  constructor(private http: HttpClient) {
  }

  placeOrder(
    customerId: number,
    paymentType: string,
    couponCode: string,
    shippingType: string
  ): Observable<any> {

    const url =
      `${this.apiUrl}?customerId=${customerId}` +
      `&paymentType=${paymentType}` +
      `&couponCode=${couponCode}` +
      `&shippingType=${shippingType}`;

    return this.http.post(url, {});
  }

  getOrder(orderId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${orderId}`);
  }
}