package com.shopmint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopMintController {

    @GetMapping("/")
    public String home() {
        return "Welcome to ShopMint!";
    }
}
