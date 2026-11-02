
package com.farmket.controller;

import com.farmket.model.Product;
import com.farmket.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

@GetMapping("/farmer-dashboard")
public String showFarmerDashboard(
        Model model,
        HttpSession session) {

    String farmerEmail =
            (String) session.getAttribute("loggedInEmail");

    String loggedInRole =
            (String) session.getAttribute("loggedInRole");

    if (farmerEmail == null || !"FARMER".equals(loggedInRole)) {
        return "redirect:/login";
    }

    List<Product> products =
            productRepository.findByFarmerEmail(farmerEmail);

    model.addAttribute("products", products);
    model.addAttribute("loggedInEmail", farmerEmail);
model.addAttribute("loggedInRole", loggedInRole);

    return "farmer-dashboard";
}


    @PostMapping("/products/add")
    public String addProduct(
            @RequestParam("productName") String productName,
            @RequestParam("quantity") double quantity,
            @RequestParam("pricePerKg") double pricePerKg,
            @RequestParam("district") String district,
            Model model,HttpSession session) {

        if (productName.trim().isEmpty()
                || district.trim().isEmpty()
                || quantity <= 0
                || pricePerKg <= 0) {

            model.addAttribute("error",
                    "Please enter valid product details.");
String farmerEmail =
        (String) session.getAttribute("loggedInEmail");

String loggedInRole =
        (String) session.getAttribute("loggedInRole");

if (farmerEmail == null || !"FARMER".equals(loggedInRole)) {
    return "redirect:/login";
}

model.addAttribute("products",
        productRepository.findByFarmerEmail(farmerEmail));

return "farmer-dashboard";
        }

       
String farmerEmail =
        (String) session.getAttribute("loggedInEmail");

String loggedInRole =
        (String) session.getAttribute("loggedInRole");

if (farmerEmail == null || !"FARMER".equals(loggedInRole)) {
    return "redirect:/login";
}

Product product = new Product();
product.setFarmerEmail(farmerEmail);
product.setProductName(productName.trim());
product.setQuantity(quantity);
product.setPricePerKg(pricePerKg);
product.setDistrict(district.trim());

productRepository.save(product);

return "redirect:/farmer-dashboard?success";

    }
}
