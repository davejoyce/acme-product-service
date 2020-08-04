package com.github.davejoyce.acme.model.repository;

import com.github.davejoyce.acme.model.Customer;
import com.github.davejoyce.acme.model.Product;
import com.github.davejoyce.acme.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductsByCustomerAndDomain(Customer customer, String domain);

    Product findProductByCustomerAndTypeAndDomain(Customer customer, ProductType type, String domain);

}
