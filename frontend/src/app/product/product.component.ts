import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService, Product } from 'src/app/services/product.service';
import { CartService } from '../services/cart.service';
import { catchError } from 'rxjs/operators';
import { EMPTY } from 'rxjs';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {
  product: Product | null = null;
  selectedQuantity: number = 1;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private cdRef: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    const productId = this.route.snapshot.params['id'];
    this.productService.getProductById(productId)
      .pipe(
        catchError(err => {
          console.error('Error fetching product details:', err);
          return EMPTY;
        })
      )
      .subscribe(
        (product: Product) => {
          this.product = product;
        }
      );
  }

  addToCart(productId: number, quantity: number) {
    this.productService.addToCart(productId, quantity).subscribe(
      response => {
        console.log('Product added to cart:', response);
  
        const userId = localStorage.getItem('user_id');
  
        if (userId) {
          this.cartService.getCartItemsCount(+userId).subscribe(
            (count: number) => {
              alert("Product added to cart.");
              this.cartService.updateCartItemCount(count);
              this.cdRef.detectChanges(); 
              window.location.reload();
            },
            (error: any) => {
              console.error('Error fetching cart count:', error);
            }
          );
        }
      },
      (error: any) => {
        console.error('Error adding product to cart:', error);
      }
    );
  }
}
