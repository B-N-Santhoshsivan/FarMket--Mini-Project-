
package com.farmket.controller;

import com.farmket.model.Enquiry;
import com.farmket.repository.EnquiryRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BuyerEnquiryController {

    private final EnquiryRepository enquiryRepository;

    public BuyerEnquiryController(EnquiryRepository enquiryRepository) {
        this.enquiryRepository = enquiryRepository;
    }

    @GetMapping("/buyer-enquiries")
    public String showBuyerEnquiries(
            HttpSession session, Model model) {

        String buyerEmail =
                (String) session.getAttribute("loggedInEmail");

        String role =
                (String) session.getAttribute("loggedInRole");

        // Only logged-in buyers can access this page.
        if (buyerEmail == null || !"BUYER".equals(role)) {
            return "redirect:/login";
        }

        // Get enquiries belonging to this buyer.
        List<Enquiry> enquiries =
                enquiryRepository.findByBuyerEmail(buyerEmail);

        model.addAttribute("enquiries", enquiries);

        return "buyer-enquiries";
    }
}
