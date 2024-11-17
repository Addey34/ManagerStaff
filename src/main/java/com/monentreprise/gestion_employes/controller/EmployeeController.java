package com.monentreprise.gestion_employes.controller;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.monentreprise.gestion_employes.model.Employee;
import com.monentreprise.gestion_employes.model.User;
import com.monentreprise.gestion_employes.service.EmployeeService;
import com.monentreprise.gestion_employes.service.UserService;

@Controller
@RequestMapping("/")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @GetMapping("/view")
    public String viewEmployees(Model model, Authentication authentication,
    @RequestParam(name = "sort", required = false, defaultValue = "nom,asc") String sort) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username).orElse(null);

        if (user != null) {
            List<Employee> employees = employeeService.getEmployeesForUser(user.getId());

            // Extraire les paramètres de tri (exemple : "nom,asc")
            String[] sortParams = sort.split(",");
            String field = sortParams[0];
            String order = sortParams[1];

            // Tri des employés
            Comparator<Employee> comparator = getComparatorForField(field);
            if ("desc".equals(order)) {
                comparator = comparator.reversed();
            }

            // Appliquer le tri
            List<Employee> sortedEmployees = employees.stream().sorted(comparator).collect(Collectors.toList());

            model.addAttribute("employees", sortedEmployees);
            model.addAttribute("currentSortField", field);
            model.addAttribute("currentSortOrder", order);
            model.addAttribute("username", username); // Ajouter le nom d'utilisateur au modèle
        }

        return "view";
    }

    private Comparator<Employee> getComparatorForField(String field) {
        switch (field) {
            case "prenom":
                return Comparator.comparing(Employee::getPrenom);
            case "email":
                return Comparator.comparing(Employee::getEmail);
            case "poste":
                return Comparator.comparing(Employee::getPoste);
            case "salaire":
                return Comparator.comparing(Employee::getSalaire);
            case "nom":
            default:
                return Comparator.comparing(Employee::getNom);
        }
    }

    @GetMapping("/add-employee")
    public String showFormAddEmployee(Model model, Principal principal) {
        model.addAttribute("employee", new Employee());
        Optional<User> user = userService.findByUsername(principal.getName());
        user.ifPresent(u -> model.addAttribute("username", u.getUsername()));
        return "add-employee";
    }

    @PostMapping("/add-employee")
    public String createEmployee(@ModelAttribute Employee employee, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username).orElse(null);

        if (user != null) {
            // Formatage des champs
            employee.setNom(employee.getNom().toUpperCase());
            employee.setPrenom(employee.getPrenom().toUpperCase());
            employee.setPoste(employee.getPoste().toUpperCase());
            employee.setEmail(employee.getEmail().toLowerCase());

            // Associer l'utilisateur à l'employé
            employee.setUser(user);
            employeeService.saveEmployee(employee);
        }

        return "redirect:/view";
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return "redirect:/view";
    }
}
