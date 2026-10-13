package org.example.repository;

import org.example.entity.BillGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillGroupRepository extends JpaRepository<BillGroup, Long> {

    List<BillGroup> findByMembersId(Long customerId);
}
