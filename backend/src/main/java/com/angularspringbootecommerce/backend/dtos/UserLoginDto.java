package com.angularspringbootecommerce.backend.dtos;

import com.angularspringbootecommerce.backend.models.User;
import lombok.Getter;

@Getter
public class UserLoginDto {

    private User user;
    private String jwt;

    public UserLoginDto() {
        super();
    }

    public UserLoginDto(User user, String jwt) {
        this.user = user;
        this.jwt = jwt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
