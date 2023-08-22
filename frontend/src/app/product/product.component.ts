import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService, Product } from 'src/app/services/product.service';
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

  constructor(private route: ActivatedRoute, private productService: ProductService) {}

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
}
