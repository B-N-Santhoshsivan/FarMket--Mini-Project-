
package com.farmket.controller;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;
import com.farmket.model.Enquiry;
import com.farmket.model.Product;
import com.farmket.repository.EnquiryRepository;
import com.farmket.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class FarmerEnquiryController {

    private final EnquiryRepository enquiryRepository;
    private final ProductRepository productRepository;

    public FarmerEnquiryController(
            EnquiryRepository enquiryRepository,
            ProductRepository productRepository) {

        this.enquiryRepository = enquiryRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/farmer-enquiries")
    public String showFarmerEnquiries(
            HttpSession session, Model model) {

        String farmerEmail =
                (String) session.getAttribute("loggedInEmail");

        String role =
                (String) session.getAttribute("loggedInRole");

        if (farmerEmail == null || !"FARMER".equals(role)) {
            return "redirect:/login";
        }

        List<Enquiry> allEnquiries =
        enquiryRepository.findByFarmerEmail(farmerEmail);

// Highest offered price per kg gets the highest priority.
PriorityQueue<Enquiry> priorityQueue = new PriorityQueue<>(
        Comparator.comparingDouble(Enquiry::getOfferedPricePerKg)
                  .reversed()
);

// Add all enquiries to the Priority Queue.
priorityQueue.addAll(allEnquiries);

// Remove enquiries in priority order.
List<Enquiry> enquiries = new ArrayList<>();

while (!priorityQueue.isEmpty()) {
    enquiries.add(priorityQueue.poll());
}

model.addAttribute("enquiries", enquiries);

        model.addAttribute("enquiries", enquiries);

        return "farmer-enquiries";
    }

    @PostMapping("/farmer-enquiries/{id}/respond")
    public String respondToEnquiry(
            @PathVariable String id,
            @RequestParam String status,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Check the logged-in farmer
        String farmerEmail =
                (String) session.getAttribute("loggedInEmail");

        String role =
                (String) session.getAttribute("loggedInRole");

        if (farmerEmail == null || !"FARMER".equals(role)) {
            return "redirect:/login";
        }

        // Accept only valid responses
        if (!"ACCEPTED".equals(status)
                && !"REJECTED".equals(status)) {

            redirectAttributes.addFlashAttribute(
                    "error", "Invalid enquiry response.");

            return "redirect:/farmer-enquiries";
        }

        // Find the enquiry
        Optional<Enquiry> optionalEnquiry =
                enquiryRepository.findById(id);

        if (optionalEnquiry.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error", "Enquiry not found.");

            return "redirect:/farmer-enquiries";
        }

        Enquiry enquiry = optionalEnquiry.get();

        // Ensure this enquiry belongs to the logged-in farmer
        if (!farmerEmail.equalsIgnoreCase(enquiry.getFarmerEmail())) {
            redirectAttributes.addFlashAttribute(
                    "error", "You cannot respond to this enquiry.");

            return "redirect:/farmer-enquiries";
        }

        // Prevent responding to an enquiry more than once
        if (!"PENDING".equals(enquiry.getStatus())) {
            redirectAttributes.addFlashAttribute(
                    "error", "This enquiry has already been answered.");

            return "redirect:/farmer-enquiries";
        }

        // If accepting, update the available product quantity
        if ("ACCEPTED".equals(status)) {

            Optional<Product> optionalProduct =
                    productRepository.findById(enquiry.getProductId());

            if (optionalProduct.isEmpty()) {
                redirectAttributes.addFlashAttribute(
                        "error", "Product not found. Stock was not changed.");

                return "redirect:/farmer-enquiries";
            }

            Product product = optionalProduct.get();

            // Check available stock before deducting
            if (enquiry.getRequestedQuantity() <= 0
                    || product.getQuantity()
                    < enquiry.getRequestedQuantity()) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Insufficient stock. The enquiry was not accepted.");

                return "redirect:/farmer-enquiries";
            }

            // Deduct the accepted quantity
            double remainingQuantity =
                    product.getQuantity()
                    - enquiry.getRequestedQuantity();

            product.setQuantity(remainingQuantity);

            // Save the updated product
            productRepository.save(product);
        }

        // Update the enquiry status
        enquiry.setStatus(status);
        enquiryRepository.save(enquiry);

        String message = "ACCEPTED".equals(status)
                ? "Enquiry accepted and product stock updated successfully."
                : "Enquiry rejected successfully.";

        redirectAttributes.addFlashAttribute("success", message);

        return "redirect:/farmer-enquiries";
    }
}
