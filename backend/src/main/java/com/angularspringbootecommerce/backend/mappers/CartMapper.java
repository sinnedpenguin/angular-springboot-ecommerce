package com.angularspringbootecommerce.backend.mappers;

import com.angularspringbootecommerce.backend.dtos.CartDto;
import com.angularspringbootecommerce.backend.dtos.CartItemDto;
import com.angularspringbootecommerce.backend.models.Cart;
import com.angularspringbootecommerce.backend.models.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CartMapper {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    @Mapping(target = "id", source = "cart.id")
    @Mapping(target = "cartItems", source = "cartItems")
    CartDto cartToCartDto(Cart cart, BigDecimal totalPrice, List<CartItemDto> cartItems);

    @Mapping(target = "subTotal", expression = "java(cartItem.getSubTotal())")
    CartItemDto cartItemToCartItemDto(CartItem cartItem);
}