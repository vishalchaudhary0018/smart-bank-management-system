package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bill_groups")
@Data
public class BillGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Customer createdBy;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "bill_group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id"))
    private List<Customer> members = new ArrayList<>();
}
