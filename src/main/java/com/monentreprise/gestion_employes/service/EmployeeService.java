package com.monentreprise.gestion_employes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monentreprise.gestion_employes.model.Employee;
import com.monentreprise.gestion_employes.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Méthode pour obtenir tous les employés associés à un utilisateur spécifique.
    public List<Employee> getEmployeesForUser(Long userId) {
        return employeeRepository.findByUserId(userId);
    }

    // Méthode pour enregistrer un employé dans la base de données.
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Méthode pour supprimer un employé par son ID.
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // Méthode pour obtenir un employé en fonction de son ID.
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }
}
