package com.monentreprise.gestion_employes.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.monentreprise.gestion_employes.model.User;
import com.monentreprise.gestion_employes.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(Model model, Principal principal) {
        // Récupérer les informations de l'utilisateur connecté
        Optional<User> user = userService.findByUsername(principal.getName());
        user.ifPresent(u -> model.addAttribute("username", u.getUsername()));
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "profile"; // Retourner le formulaire avec les erreurs
        }
        userService.updateUser(user); // Logique pour mettre à jour les informations utilisateur
        return "redirect:/profile?success";
    }
}
