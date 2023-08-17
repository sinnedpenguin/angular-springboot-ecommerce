package com.angularspringbootecommerce.backend.services;

import com.angularspringbootecommerce.backend.exceptions.AppException;
import com.angularspringbootecommerce.backend.models.Cart;
import com.angularspringbootecommerce.backend.models.CartItem;
import com.angularspringbootecommerce.backend.models.Product;
import com.angularspringbootecommerce.backend.models.User;
import com.angularspringbootecommerce.backend.repository.CartRepository;
import com.angularspringbootecommerce.backend.repository.ProductRepository;
import com.angularspringbootecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<Cart> getAllItems() {
        return cartRepository.findAll();
    }

    public Cart addToCart(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            Cart newCart = new Cart();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
            newCart.setUser(user);
            return cartRepository.save(newCart);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(product.getPrice() * quantity);
        cart.getItems().add(cartItem);

        return cart;
    }
}
