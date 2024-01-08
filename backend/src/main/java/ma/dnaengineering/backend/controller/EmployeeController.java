package ma.dnaengineering.backend.controller;

import io.micrometer.common.util.StringUtils;
import ma.dnaengineering.backend.entity.Employee;
import ma.dnaengineering.backend.service.CsvParserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {

    private final CsvParserService csvParserService;

    @Autowired
    public EmployeeController(CsvParserService csvParserService) {
        this.csvParserService = csvParserService;
    }

    @GetMapping("/employee")
    public List<Employee> getAllEmployees() {
        return csvParserService.getAllEmployees();
    }

    @GetMapping("/average-salary-by-job-title")
    public ResponseEntity<Map<String, Double>> getAverageSalaryByJobTitle() throws IOException {
        List<Employee> employees = csvParserService.getAllEmployees();
        Map<String, Double> averageSalaryByJobTitle = csvParserService.getAverageSalaryByJobTitle(employees);
        return ResponseEntity.ok(averageSalaryByJobTitle);
    }

    @PostMapping("/parse-csv")
    public ResponseEntity<String> parseCsvFile(@RequestParam("file") MultipartFile file,
                                               @RequestParam(value = "filePath", required = false) String filePath) throws IOException {
        if (file.isEmpty() && StringUtils.isBlank(filePath)) {
            return ResponseEntity.badRequest().body("Please upload a CSV file or provide a file path.");
        }

        try (InputStream inputStream = (StringUtils.isNotBlank(filePath))
                ? new FileInputStream(filePath)
                : file.getInputStream()) {
            csvParserService.parseCsvAndSave(inputStream);
        }

        return ResponseEntity.ok("CSV file parsed and data saved successfully.");
    }


    @PostMapping("/import-csv")
    public ResponseEntity<String> importCsvData(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a CSV file.");
        }

        try (InputStream inputStream = file.getInputStream()) {
            csvParserService.parseCsvAndSave(inputStream);
        }

        return ResponseEntity.ok("CSV data imported successfully.");
    }
}
