import { Component, OnInit } from '@angular/core';
import { CartService } from 'src/app/services/cart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  numberOfItemsInCart = 0;

  constructor(private cartService: CartService) {}
  
  ngOnInit() {
    console.log('Cart component initialized.');
  
    const userId = localStorage.getItem('user_id');
    const jwt = localStorage.getItem('access_token');
  
    if (userId && jwt) {
      const userIdNumber = +userId;
      console.log('User ID number:', userIdNumber);
  
      this.cartService.getCartItemsCount(userIdNumber).subscribe(
        (response: any) => {
          console.log('Response:', response);
          this.numberOfItemsInCart = response.numberOfItemsInCart;
        },
        (error: any) => {
          console.error('Error fetching cart count:', error);
        }
      );
    } else {
      console.error('User ID or JWT token not found in local storage.');
    }
  }
}
