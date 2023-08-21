import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private baseApiUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  getCartItemsCount(userId: number): Observable<number> {
    const apiUrl = `${this.baseApiUrl}/cart/${userId}`;
    return this.http.get<number>(apiUrl);
  }

  getCartItems(userId: number): Observable<any[]> {
    const apiUrl = `${this.baseApiUrl}/cart/${userId}`;
    return this.http.get<any[]>(apiUrl);
  }
}
