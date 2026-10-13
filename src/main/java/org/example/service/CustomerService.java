
/*
@Service — ye batata hai Spring ko "ye class business logic wali hai", aur Spring isko automatically manage karta hai (memory mein ek object bana ke rakh leta hai jise use kar sako)
@Autowired — ye Spring ka Dependency Injection concept hai. Normally tumhe khud new CustomerRepository() likh ke object banana padta, lekin @Autowired likhne se Spring khud automatically ye object provide kar deta hai tumhare liye — bas maang lo, mil jayega
registerCustomer(Customer customer) — ye method ek Customer object leta hai (jo humne pehle banaya tha) aur usko customerRepository.save() ke through database mein save kar deta hai, aur saved customer wapas return karta hai (jisme ab id bhi generate ho chuki hogi)
 */
package org.example.service;


import org.example.entity.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public Customer registerCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    // add get method

    public java.util.List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }


}