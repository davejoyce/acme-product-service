package com.github.davejoyce.acme;

import com.github.davejoyce.acme.model.ProductForm;
import com.github.davejoyce.acme.model.ProductType;
import com.github.davejoyce.acme.model.validator.*;
import jdk.nashorn.internal.runtime.options.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@Slf4j
public class WebController implements WebMvcConfigurer {

    private final Set<AbstractProductFormValidator> productFormValidators = new HashSet<>();

    public WebController() {
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
    public ModelAndView addProduct(@Valid ProductForm productForm, Errors errors, Model model) {
        validateProductFormByType(productForm, errors);
        if (null != errors && 0 < errors.getErrorCount()) {
            ModelAndView mav = new ModelAndView("addform", model.asMap());
            mav.addObject("productForm", productForm);
            mav.addObject("productTypes", ProductType.values());
            return mav;
        } else {
            log.info("Posted new customer product: {}", productForm);
            model.addAttribute("successMsg", "Added product");
            return new ModelAndView("menu", model.asMap());
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
