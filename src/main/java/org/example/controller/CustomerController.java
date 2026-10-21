
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