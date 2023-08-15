package com.angularspringbootecommerce.backend.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Getter
@Component
public class RSAKeyProperties {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSAKeyProperties(){
        KeyPair pair = KeyGeneratorUtility.generateRsaKey();
        this.publicKey = (RSAPublicKey) pair.getPublic();
        this.privateKey = (RSAPrivateKey) pair.getPrivate();
    }

    public void setPublicKey(RSAPublicKey publicKey){
        this.publicKey = publicKey;
    }

    public void setPrivateKey(RSAPrivateKey privateKey){
        this.privateKey = privateKey;
    }

}
