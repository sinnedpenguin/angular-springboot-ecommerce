import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';

interface CartItem {
  id: number;
  productId: number;
  quantity: number;
  price: number;
}

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private apiUrl = 'http://localhost:8080/api/v1/cart';

  constructor(private http: HttpClient) {}

  getCartByUserId(userId: number, jwt: string): Observable<CartItem[]> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${jwt}`,
    });
  
    const url = `${this.apiUrl}/${userId}`;
  
    return this.http.get<CartItem[]>(url, { headers }).pipe(
      map((result: any) => {
        return result.cartItems;
      })
    );
  }
}