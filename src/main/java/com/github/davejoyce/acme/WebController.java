package com.github.davejoyce.acme;

import com.github.davejoyce.acme.model.ProductForm;
import com.github.davejoyce.acme.model.ProductType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
public class WebController implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("menu");
    }

    @GetMapping("/add")
    public ModelAndView showAddForm(ProductForm productForm) {
        ModelAndView mav = new ModelAndView("addform");
        mav.addObject("productTypes", ProductType.values());
        return mav;
    }

    @PostMapping("/add")
    public String addProduct(ProductForm productForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addform";
        }

        return "redirect:/";
    }

}
