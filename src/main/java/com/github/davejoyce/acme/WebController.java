package com.github.davejoyce.acme;

import com.github.davejoyce.acme.model.Product;
import com.github.davejoyce.acme.model.ProductForm;
import com.github.davejoyce.acme.model.ProductType;
import com.github.davejoyce.acme.model.ProductsForm;
import com.github.davejoyce.acme.model.service.ProductService;
import com.github.davejoyce.acme.model.validator.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@Controller
@Slf4j
public class WebController implements WebMvcConfigurer {

    private final ProductService productService;
    private final Set<AbstractProductFormValidator> productFormValidators = new HashSet<>();

    public WebController(final ProductService productService) {
        this.productService = productService;
        productFormValidators.add(new DomainProductFormValidator());
        productFormValidators.add(new EDomainProductFormValidator());
        productFormValidators.add(new PDomainProductFormValidator());
        productFormValidators.add(new OtherProductFormValidator());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("menu");
    }

    @GetMapping("/add")
    public ModelAndView showAddForm() {
        ModelAndView mav = new ModelAndView("addform");
        mav.addObject("productForm", new ProductForm());
        mav.addObject("productTypes", ProductType.values());
        return mav;
    }

    @PostMapping("/add")
    public String addProduct(@Valid ProductForm productForm,
                             Errors errors,
                             RedirectAttributes redirectAttributes) {
        validateProductFormByType(productForm, errors);
        if (null != errors && 0 < errors.getErrorCount()) {
            return "redirect:/add";
        } else {
            Product p = productService.addNewProduct(productForm);
            log.info("Posted new customer product: {}", p);
            redirectAttributes.addFlashAttribute("successMsg", "Added product (" + p.getId() + ")");
            return "redirect:/";
        }
    }

    @GetMapping("/load")
    public ModelAndView showLoadForm() {
        ModelAndView mav = new ModelAndView("loadform");
        mav.addObject("productsForm", new ProductsForm());
        return mav;
    }

    @PostMapping("/load")
    public String loadProducts(@Valid ProductsForm productsForm,
                               Errors errors,
                               RedirectAttributes redirectAttributes) {
        List<ProductForm> productsToLoad = new ArrayList<>();
        try {
            productsToLoad.addAll(productsForm.asList());
            productsToLoad.forEach(productForm -> validateProductFormByType(productForm, errors));
        } catch (Exception e) {
            errors.rejectValue("csvProductEntries", "csvProductEntries.invalid", "Invalid product CSV records");
        }
        if (null != errors && 0 < errors.getErrorCount()) {
            return "redirect:/load";
        } else {
            List<Product> p = productService.loadNewProducts(productsToLoad);
            log.info("Posted {} new customer products", p.size());
            redirectAttributes.addFlashAttribute("successMsg", "Loaded " + p.size() + " products");
            return "redirect:/";
        }
    }

    void validateProductFormByType(ProductForm productForm, Errors errors) {
        Optional<ProductType> productType = Optional.ofNullable(productForm.getProductType());
        productType.ifPresent(type -> {
            Optional<AbstractProductFormValidator> validator = productFormValidators.stream().filter(v -> v.supportsProductType(productForm)).findFirst();
            validator.ifPresent(v -> {
                log.info("Validating {} submission...", type);
                v.validate(productForm, errors);
            });
        });
    }

}
