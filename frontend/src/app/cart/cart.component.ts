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
  console.log('User ID:', userId);

  if (userId) {
    const userIdNumber = +userId;
    console.log('User ID number:', userIdNumber);

    if (!isNaN(userIdNumber)) {
      const jwt = localStorage.getItem('access_token');
      console.log('JWT:', jwt);

      if (jwt) {
        this.cartService.getCartByUserId(userIdNumber, jwt).subscribe(
          (cart) => {
            console.log('Cart data:', cart);
            console.log('Cart keys:', Object.keys(cart));
            if (cart && cart.length > 0) {
              this.numberOfItemsInCart = cart.length;
              console.log('Number of items in cart:', this.numberOfItemsInCart);
            } else {
              console.log('Cart is empty.');
            }
          },
          (error) => {
            console.error('Error fetching cart data:', error);
          }
        );
      } else {
        console.error('JWT token not found in local storage.');
      }
    } else {
      console.error('Invalid user ID:', userId);
    }
  } else {
    console.error('User ID not found in local storage.');
  }
}
}