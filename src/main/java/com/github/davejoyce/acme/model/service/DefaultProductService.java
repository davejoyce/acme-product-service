package com.github.davejoyce.acme.model.service;

import com.github.davejoyce.acme.model.Customer;
import com.github.davejoyce.acme.model.Product;
import com.github.davejoyce.acme.model.ProductForm;
import com.github.davejoyce.acme.model.ProductType;
import com.github.davejoyce.acme.model.repository.CustomerRepository;
import com.github.davejoyce.acme.model.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class DefaultProductService implements ProductService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = false)
    public Product addNewProduct(ProductForm productToAdd) {
        Customer c = customerRepository.findByCustomerName(productToAdd.getCustomerName());
        if (null == c) {
            c = new Customer();
            c.setCustomerName(productToAdd.getCustomerName());
            c = customerRepository.save(c);
        }
        List<Product> existingProducts = productRepository.findProductsByCustomerAndDomain(c, productToAdd.getDomain().toLowerCase());
        Optional<Product> domainRegistration = findDomainRegistration(existingProducts.stream());
        Optional<Product> emailRegistration = findEmailRegistration(existingProducts.stream());
        Optional<Product> hostingRegistration = findHostingRegistration(existingProducts.stream());

        Product added;
        switch (productToAdd.getProductType()) {
            case DOMAIN:
            case E_DOMAIN:
            case P_DOMAIN:
                added = addDomainProduct(domainRegistration, c, productToAdd);
                break;
            case EMAIL:
                added = addOtherProduct(emailRegistration, domainRegistration, c, productToAdd);
                break;
            case HOSTING:
                added = addOtherProduct(hostingRegistration, domainRegistration, c, productToAdd);
                break;
            default:
                added = null;
                break;
        }

        return added;
    }

    @Override
    @Transactional(readOnly = false)
    public List<Product> loadNewProducts(List<ProductForm> productsToLoad) {
        List<Product> added = new ArrayList<>();
        for (ProductForm productToAdd : productsToLoad) {
            added.add(this.addNewProduct(productToAdd));
        }
        return added;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> listProducts() {
        return productRepository.findAll(Sort.by("customer"));
    }

    Product addDomainProduct(Optional<Product> existingRegistration,
                             Customer customer,
                             ProductForm domainToAdd) {
        if (existingRegistration.isPresent()) return existingRegistration.get();
        final Product p = new Product();
        p.setCustomer(customer);
        p.setType(domainToAdd.getProductType());
        p.setDomain(domainToAdd.getDomain().toLowerCase());
        p.setStartDate(domainToAdd.getStartDate());
        p.setDurationMonths(domainToAdd.getDuration() * 12); // Domain duration in years

        return productRepository.save(p);
    }

    Product addOtherProduct(Optional<Product> existingRegistration,
                            Optional<Product> domainRegistration,
                            Customer customer,
                            ProductForm productToAdd) {
        if (existingRegistration.isPresent()) return existingRegistration.get();
        // Register domain first
        if (!domainRegistration.isPresent()) {
            Product dr = new Product();
            dr.setCustomer(customer);
            dr.setType(ProductType.DOMAIN);
            dr.setDomain(productToAdd.getDomain());
            dr.setStartDate(productToAdd.getStartDate());
            dr.setDurationMonths(12);
            dr = productRepository.save(dr);
        }
        final Product p = new Product();
        p.setCustomer(customer);
        p.setType(productToAdd.getProductType());
        p.setDomain(productToAdd.getDomain().toLowerCase());
        p.setStartDate(productToAdd.getStartDate());
        p.setDurationMonths(productToAdd.getDuration());

        return productRepository.save(p);
    }

    Optional<Product> findDomainRegistration(Stream<Product> domainProducts) {
        return domainProducts.filter(product -> {
            switch (product.getType()) {
                case DOMAIN:
                case E_DOMAIN:
                case P_DOMAIN:
                    return true;
                default:
                    return false;
            }
        }).findFirst();
    }

    Optional<Product> findEmailRegistration(Stream<Product> domainProducts) {
        return domainProducts.filter(product -> (ProductType.EMAIL == product.getType())).findFirst();
    }

    Optional<Product> findHostingRegistration(Stream<Product> domainProducts) {
        return domainProducts.filter(product -> (ProductType.HOSTING == product.getType())).findFirst();
    }

}
