package com.github.davejoyce.acme.model.repository;

import com.github.davejoyce.acme.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByCustomerName(String customerName);

}
