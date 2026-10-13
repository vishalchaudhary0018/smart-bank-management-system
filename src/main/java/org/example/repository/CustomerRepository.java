/*
interface — ye Java ka concept hai jo Core Java mein tumne padha hoga — ek "contract" jisme sirf method signatures hote hain, implementation nahi

extends JpaRepository<Customer, Long> — yahi asli jaadu hai. Isका matlab hai "ye repository Customer entity ke liye hai, aur uski id ka type Long hai". Bas itna likhte hi, Spring automatically ye sab methods de deta hai:

save(customer) — naya customer save karna
findById(id) — id se dhundhna
findAll() — sab customers laana
deleteById(id) — delete karna
*/

package org.example.repository;
import java.util.Optional;
import org.example.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
}