import { Component, OnInit } from '@angular/core';
import { ProductService, Product } from 'src/app/services/product.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  products: Product[] = [];

  constructor(private productService: ProductService) { }

  ngOnInit() {
    this.productService.getAll()
      .subscribe((products: Product[]) => {
        this.products = products;
      });
  }
}