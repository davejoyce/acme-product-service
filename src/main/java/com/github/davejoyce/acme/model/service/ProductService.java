package com.github.davejoyce.acme.model.service;

import com.github.davejoyce.acme.model.Product;
import com.github.davejoyce.acme.model.ProductForm;

import java.util.List;

public interface ProductService {

    Product addNewProduct(ProductForm productToAdd);

    List<Product> loadNewProducts(List<ProductForm> productsToLoad);

    List<Product> listProducts();

}
