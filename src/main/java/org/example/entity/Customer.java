package org.example.entity;
import jakarta.persistence.*; // ye data base se sari file (annotation ) apni file me use krne ke liye import kiya
import lombok.Data;   // lambok se @Data annotation import ho rha h

@Entity  // ye spring ko btati h ki ye keval normal class nhi h database ko represent krti h
@Table(name="customer")// ye specify krta h ki database me jo table h bo "customer " name ki h
@Data // ye automatic getter setter tostring etc method generate krta h


public class Customer {

    @Id// ye batata hai ki ye field primary key hai, matlab har customer ki unique

    @GeneratedValue(strategy = GenerationType.IDENTITY)//iska matlab hai ye id automatically generate hogi (1, 2, 3...), tumhe khud number dene ki zarurat nahi
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)// ye role ke liye h
    private Role role;

    private Long guardianId;
}
