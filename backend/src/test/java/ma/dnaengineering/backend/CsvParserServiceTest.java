package ma.dnaengineering.backend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import ma.dnaengineering.backend.service.CsvParserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ResourceLoader;

import ma.dnaengineering.backend.entity.Employee;
import ma.dnaengineering.backend.repository.EmployeeRepo;

class CsvParserServiceTest {

    @Mock
    private EmployeeRepo employeeRepository;

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private CsvParserService csvParserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testParseCsvAndSave() throws IOException {
        String csvData = "id,name,jobTitle,salary\n1,amine,Developer,99000.0\n2,yasser,Manager,90000.0";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        when(employeeRepository.saveAll(any())).thenReturn(null);
        csvParserService.parseCsvAndSave(inputStream);
        verify(employeeRepository, times(1)).saveAll(any());
    }

    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(new Employee(), new Employee()));
        List<Employee> employees = csvParserService.getAllEmployees();
        verify(employeeRepository, times(1)).findAll();
        assertEquals(2, employees.size());
    }

    @Test
    void testGetAverageSalaryByJobTitle() {
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "amine", "Developer", 1000.0),
                new Employee(2L, "aymane", "Developer", 1000.0),
                new Employee(3L, "amani", "Developer", 1000.0),
                new Employee(4L, "imane", "Manager", 1020.0)
        );
        var result = csvParserService.getAverageSalaryByJobTitle(employees);
        assertEquals(1000.0, result.get("Developer"));
        assertEquals(1020.0, result.get("Manager"));
    }

    @Test
    void testParseCsv() throws IOException {
        String csvData = "id,name,jobTitle,salary\n1,amine,Developer,99000.0\n2,yasser,Manager,90000.0";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        List<Employee> employees = csvParserService.parseCsv(inputStream);
        assertEquals(2, employees.size());
        assertEquals("amine", employees.get(0).getName());
        assertEquals("Developer", employees.get(0).getJobTitle());
        assertEquals(99000.0, employees.get(0).getSalary());
        assertEquals("yasser", employees.get(1).getName());
        assertEquals("Manager", employees.get(1).getJobTitle());
        assertEquals(90000.0, employees.get(1).getSalary());
    }
}
