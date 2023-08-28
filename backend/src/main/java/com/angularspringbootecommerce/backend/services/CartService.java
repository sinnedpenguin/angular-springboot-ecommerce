package com.angularspringbootecommerce.backend.services;

import com.angularspringbootecommerce.backend.dtos.CartDto;
import com.angularspringbootecommerce.backend.dtos.CartItemDto;
import com.angularspringbootecommerce.backend.dtos.ProductDto;
import com.angularspringbootecommerce.backend.exceptions.AppException;
import com.angularspringbootecommerce.backend.mappers.CartMapper;
import com.angularspringbootecommerce.backend.models.Cart;
import com.angularspringbootecommerce.backend.models.CartItem;
import com.angularspringbootecommerce.backend.models.Product;
import com.angularspringbootecommerce.backend.models.User;
import com.angularspringbootecommerce.backend.repository.CartItemRepository;
import com.angularspringbootecommerce.backend.repository.CartRepository;
import com.angularspringbootecommerce.backend.repository.ProductRepository;
import com.angularspringbootecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartDto getCartByUserId(Long userId) {
        Optional<Cart> userCart = cartRepository.findByUserId(userId);
        if (userCart.isPresent()) {
            Cart cart = userCart.get();

            CartDto cartDto = new CartDto();
            cartDto.setId(cart.getId());
            cartDto.setUserId(cart.getUserId());

            List<CartItemDto> cartItemDtos = getCartItemDto(cart);

            Map<Long, CartItemDto> cartItemMap = new HashMap<>();

            for (CartItemDto cartItemDto : cartItemDtos) {
                Long productId = cartItemDto.getProductId();
                if (cartItemMap.containsKey(productId)) {
                    CartItemDto existingItem = cartItemMap.get(productId);
                    existingItem.setQuantity(existingItem.getQuantity() + cartItemDto.getQuantity());
                    existingItem.setSubTotal(existingItem.getSubTotal().add(cartItemDto.getSubTotal()));
                } else {
                    cartItemMap.put(productId, cartItemDto);
                }
            }

            List<CartItemDto> consolidatedCartItems = new ArrayList<>(cartItemMap.values());
            cartDto.setCartItems(consolidatedCartItems);

            for (CartItemDto consolidatedCartItem : consolidatedCartItems) {
                Long productId = consolidatedCartItem.getProductId();
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

                ProductDto productDto = new ProductDto();
                productDto.setImgUrl(product.getImgUrl());
                consolidatedCartItem.setProduct(productDto);
            }

            BigDecimal totalPrice = consolidatedCartItems.stream()
                    .map(CartItemDto::getSubTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            cartDto.setTotalPrice(totalPrice);

            return cartDto;
        } else {
            return null;
        }
    }

    public int getNumberOfItemsInCart(Long userId) {
        Optional<Cart> userCart = cartRepository.findByUserId(userId);
        if (userCart.isPresent()) {
            Cart cart = userCart.get();
            return cart.getCartItems().stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();
        } else {
            return 0;
        }
    }

    private static List<CartItemDto> getCartItemDto(Cart cart) {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setProductId(cartItem.getProductId());
            cartItemDto.setProductName(cartItem.getProductName());
            cartItemDto.setQuantity(cartItem.getQuantity());
            cartItemDto.setPrice(cartItem.getPrice());

            cartItemDto.setSubTotal(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            cartItemDtos.add(cartItemDto);
        }
        return cartItemDtos;
    }

    public CartDto addItemToCart(Long userId, Long productId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        BigDecimal itemPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));

        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        Cart userCart = optionalCart.orElse(new Cart());
        if (optionalCart.isEmpty()) {
            userCart.setUserId(userId);
            cartRepository.save(userCart);
        }

        CartItem newItem = new CartItem();
        newItem.setProductId(product.getId());
        newItem.setProductName(product.getName());
        newItem.setQuantity(quantity);
        newItem.setPrice(itemPrice);
        newItem.setCart(userCart);
        newItem.setSubTotal(itemPrice);

        cartItemRepository.save(newItem);

        userCart.getCartItems().add(newItem);

        BigDecimal totalPrice = userCart.getCartItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        userCart.setTotalPrice(totalPrice);
        cartRepository.save(userCart);

        List<CartItemDto> cartItemDtos = userCart.getCartItems().stream()
                .map(item -> {
                    CartItemDto itemDto = new CartItemDto();
                    itemDto.setProductId(item.getProductId());
                    itemDto.setProductName(item.getProductName());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());
                    itemDto.setSubTotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                            .setScale(2, RoundingMode.HALF_UP));
                    return itemDto;
                }).collect(Collectors.toList());

        return CartMapper.INSTANCE.cartToCartDto(userCart, totalPrice, cartItemDtos);
    }

    public CartDto removeItemFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        Cart userCart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException("Cart not found", HttpStatus.NOT_FOUND));

        CartItem cartItemToRemove = userCart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new AppException("Cart item not found", HttpStatus.NOT_FOUND));

        userCart.getCartItems().remove(cartItemToRemove);
        cartItemRepository.delete(cartItemToRemove);

        BigDecimal totalPrice = userCart.getCartItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        userCart.setTotalPrice(totalPrice);
        cartRepository.save(userCart);

        List<CartItemDto> cartItemDtos = getCartItemDto(userCart);
        return CartMapper.INSTANCE.cartToCartDto(userCart, totalPrice, cartItemDtos);
    }

    public Cart getCartEntityByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() ->
                new AppException("Cart not found for user id: " + userId, HttpStatus.NOT_FOUND));
    }

    public void clearCart(Long userId) {
        Cart cart = getCartEntityByUserId(userId);
        if (cart != null) {
            cart.getCartItems().clear();
            cart.setTotalPrice(BigDecimal.ZERO);
            cartRepository.save(cart);
        }
    }
}
