
/*
@RestController — ye batata hai Spring ko "ye class API endpoints handle karegi, aur jo bhi return karegi wo automatically JSON mein convert ho jayega"
@RequestMapping("/api/customers") — ye is poore controller ka base URL set kar raha hai. Matlab is class ke andar jitne bhi methods honge, sabka URL /api/customers se start hoga
@PostMapping("/register") — ye batata hai "jab koi POST request /api/customers/register pe aaye, to ye method chalao". (POST matlab naya data bhejna/create karna — jaise form submit karna)
@RequestBody Customer customer — iska matlab hai jo bhi JSON data client bhejega (naam, email, password), wo automatically ek Customer object mein convert ho jayega

Poora URL banega: http://localhost:8081/api/customers/register

*/
package org.example.controller;

import org.example.entity.Customer;
import org.example.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public Customer register(@RequestBody Customer customer) {
        return customerService.registerCustomer(customer);
    }

    // add GET endpoints


    @GetMapping
    public java.util.List<Customer> getAll() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }
}