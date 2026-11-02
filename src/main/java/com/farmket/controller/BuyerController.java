
package com.farmket.controller;

import com.farmket.model.Product;
import com.farmket.model.User;
import com.farmket.repository.ProductRepository;
import com.farmket.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@Controller
public class BuyerController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public BuyerController(
            ProductRepository productRepository,
            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/buyer-dashboard")
    public String showBuyerDashboard(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String district,
            Model model,
            HttpSession session) {

        String loggedInEmail =
                (String) session.getAttribute("loggedInEmail");

        String loggedInRole =
                (String) session.getAttribute("loggedInRole");

        // Only logged-in buyers can access this dashboard.
        if (loggedInEmail == null
                || !"BUYER".equals(loggedInRole)) {
            return "redirect:/login";
        }

        // Retrieve all products from MongoDB.
        List<Product> products = productRepository.findAll();

        // Apply the product-name search.
        if (!search.trim().isEmpty()) {
            String searchText = search.trim().toLowerCase();

            products = products.stream()
                    .filter(product ->
                            product.getProductName() != null
                            && product.getProductName()
                                    .toLowerCase()
                                    .contains(searchText))
                    .toList();
        }

        // Apply the district filter.
        if (!district.trim().isEmpty()) {
            String selectedDistrict = district.trim();

            products = products.stream()
                    .filter(product ->
                            product.getDistrict() != null
                            && product.getDistrict()
                                    .equalsIgnoreCase(selectedDistrict))
                    .toList();
        }

        // Create a Priority Queue:
        // Lower price first; higher quantity breaks price ties.
        PriorityQueue<Product> priorityQueue =
                new PriorityQueue<>(
                        Comparator
                                .comparingDouble(
                                        Product::getPricePerKg)
                                .thenComparing(
                                        Comparator.comparingDouble(
                                                Product::getQuantity)
                                                .reversed())
                );

        // Recommend only products with stock available.
        for (Product product : products) {
            if (product.getQuantity() > 0
                    && product.getPricePerKg() > 0) {
                priorityQueue.offer(product);
            }
        }

        // Extract products in priority order.
        List<Product> recommendedProducts = new ArrayList<>();

        while (!priorityQueue.isEmpty()) {
            recommendedProducts.add(priorityQueue.poll());
        }

        // Find farmer names using their email addresses.
        Map<String, String> farmerNames =
                userRepository.findAll().stream()
                        .filter(user ->
                                user.getEmail() != null
                                && user.getFullName() != null)
                        .collect(Collectors.toMap(
                                user -> user.getEmail().toLowerCase(),
                                User::getFullName,
                                (firstName, secondName) -> firstName
                        ));

        // Send data to the HTML page.
        model.addAttribute("products", recommendedProducts);
        model.addAttribute("farmerNames", farmerNames);
        model.addAttribute("search", search);
        model.addAttribute("district", district);
        model.addAttribute("loggedInEmail", loggedInEmail);
        model.addAttribute("loggedInRole", loggedInRole);

        return "buyer-dashboard";
    }
}
