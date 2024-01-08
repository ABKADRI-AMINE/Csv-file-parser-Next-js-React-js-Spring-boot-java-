package ma.dnaengineering.backend.service;

import ma.dnaengineering.backend.entity.Employee;
import ma.dnaengineering.backend.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CsvParserService {

    private final EmployeeRepo employeeRepository;
    private final ResourceLoader resourceLoader;

    private final Set<String> uniqueNames = new HashSet<>();

    @Autowired
    public CsvParserService(EmployeeRepo employeeRepository, ResourceLoader resourceLoader) {
        this.employeeRepository = employeeRepository;
        this.resourceLoader = resourceLoader;
    }

    public void parseCsvAndSave(InputStream inputStream) {
        List<Employee> employees = parseCsv(inputStream);
        employeeRepository.saveAll(employees);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Map<String, Double> getAverageSalaryByJobTitle(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getJobTitle, Collectors.averagingDouble(Employee::getSalary)));
    }

    public List<Employee> parseCsv(InputStream inputStream) {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String name = data[1];
                    if (!uniqueNames.contains(name)) {
                        Employee employee = new Employee();
                        employee.setName(name);
                        employee.setJobTitle(data[2]);
                        employee.setSalary(Double.parseDouble(data[3]));
                        employees.add(employee);
                        uniqueNames.add(name);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return employees;
    }
}
