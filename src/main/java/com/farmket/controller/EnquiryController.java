
package com.farmket.controller;

import com.farmket.model.Enquiry;
import com.farmket.model.Product;
import com.farmket.repository.EnquiryRepository;
import com.farmket.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class EnquiryController {

    private final EnquiryRepository enquiryRepository;
    private final ProductRepository productRepository;

    public EnquiryController(
            EnquiryRepository enquiryRepository,
            ProductRepository productRepository) {

        this.enquiryRepository = enquiryRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/enquiries/send")
    public String sendEnquiry(
            @RequestParam String productId,
            @RequestParam double requestedQuantity,
            @RequestParam double offeredPricePerKg,
            @RequestParam String message,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Check whether the user is logged in as a buyer
        String buyerEmail =
                (String) session.getAttribute("loggedInEmail");

        String role =
                (String) session.getAttribute("loggedInRole");

        if (buyerEmail == null || !"BUYER".equals(role)) {
            return "redirect:/login";
        }

        // Find the selected product
        Optional<Product> result =
                productRepository.findById(productId);

        if (result.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "enquiryError",
                    "Product not found.");

            return "redirect:/buyer-dashboard";
        }

        Product product = result.get();

        // Validate the enquiry details
        if (requestedQuantity <= 0
                || requestedQuantity > product.getQuantity()
                || offeredPricePerKg <= 0
                || message.trim().isEmpty()
                || buyerEmail.equalsIgnoreCase(
                        product.getFarmerEmail())) {

            redirectAttributes.addFlashAttribute(
                    "enquiryError",
                    "Please enter a valid quantity, offer price, and message.");

            return "redirect:/buyer-dashboard";
        }

        // Create a new enquiry
        Enquiry enquiry = new Enquiry();

        enquiry.setProductId(product.getId());
        enquiry.setProductName(product.getProductName());
        enquiry.setFarmerEmail(product.getFarmerEmail());
        enquiry.setBuyerEmail(buyerEmail);
        enquiry.setRequestedQuantity(requestedQuantity);
        enquiry.setOfferedPricePerKg(offeredPricePerKg);
        enquiry.setMessage(message.trim());
        enquiry.setStatus("PENDING");

        // Save the enquiry in MongoDB
        enquiryRepository.save(enquiry);

        redirectAttributes.addFlashAttribute(
                "enquirySuccess",
                "Your enquiry was sent successfully!");

        return "redirect:/buyer-dashboard";
    }
}
