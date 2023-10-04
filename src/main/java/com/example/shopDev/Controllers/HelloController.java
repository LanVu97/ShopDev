package com.example.shopDev.Controllers;

import com.example.shopDev.Models.GroceryItem;
import com.example.shopDev.Repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/")
    public List<GroceryItem> index() {
        String x =  "Greetings from Spring Boot!";

        int count = 1000;

        return itemRepository.findAll();
    }

    @PostMapping("/addItem")
    public void addUser()
    {
        itemRepository.save(new GroceryItem("Whole Wheat Biscuit", "Whole Wheat Biscuit", 5, "snacks"));
    }

}
