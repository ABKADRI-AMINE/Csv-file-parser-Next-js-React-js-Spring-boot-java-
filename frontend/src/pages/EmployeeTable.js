import React, { useState, useEffect } from 'react';
import axios from 'axios';

const EmployeeTable = () => {
  const [employees, setEmployees] = useState([]);
  const [averageSalaries, setAverageSalaries] = useState([]);
  const [currentPageEmployees, setCurrentPageEmployees] = useState(1);
  const [currentPageSalaries, setCurrentPageSalaries] = useState(1);
  const [employeesPerPage] = useState(5);
  const [salariesPerPage] = useState(5);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const employeesResponse = await axios.get('http://localhost:8080/employees/employee');
        setEmployees(employeesResponse.data);

        const averageSalariesResponse = await axios.get('http://localhost:8080/employees/average-salary-by-job-title');
        setAverageSalaries(averageSalariesResponse.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  const indexOfLastEmployee = currentPageEmployees * employeesPerPage;
  const indexOfFirstEmployee = indexOfLastEmployee - employeesPerPage;
  const currentEmployees = employees.slice(indexOfFirstEmployee, indexOfLastEmployee);

  const pageNumbersSalaries = Object.keys(averageSalaries);

  const indexOfLastSalary = currentPageSalaries * salariesPerPage;
  const indexOfFirstSalary = indexOfLastSalary - salariesPerPage;
  const currentSalaries = pageNumbersSalaries
    .slice(indexOfFirstSalary, indexOfLastSalary)
    .map((jobTitle) => ({
      jobTitle,
      averageSalary: averageSalaries[jobTitle],
    }));

  const paginateEmployees = (pageNumber) => setCurrentPageEmployees(pageNumber);
  const paginateSalaries = (pageNumber) => setCurrentPageSalaries(pageNumber);

  return (
    <div className="container mt-4">
      <div>
        <h2>Employee Information</h2>
        <table className="table table-bordered table-employees">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Job Title</th>
              <th>Salary</th>
            </tr>
          </thead>
          <tbody>
            {currentEmployees.map((employee) => (
              <tr key={employee.id}>
                <td>{employee.id}</td>
                <td>{employee.name}</td>
                <td>{employee.jobTitle}</td>
                <td>{employee.salary}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <Pagination
          itemsPerPage={employeesPerPage}
          totalItems={employees.length}
          currentPage={currentPageEmployees}
          paginate={paginateEmployees}
        />
      </div>

      <div className="mt-4">
        <h2>Jobs summary</h2>
        <table className="table table-bordered table-salaries">
          <thead>
            <tr>
              <th>Job Title</th>
              <th>Average Salary</th>
            </tr>
          </thead>
          <tbody>
            {currentSalaries.map((item) => (
              <tr key={item.jobTitle}>
                <td>{item.jobTitle}</td>
                <td>{item.averageSalary}</td>
              </tr>
            ))}
          </tbody>
        </table>
        <Pagination
          itemsPerPage={salariesPerPage}
          totalItems={pageNumbersSalaries.length}
          currentPage={currentPageSalaries}
          paginate={paginateSalaries}
        />
      </div>
    </div>
  );
};

const Pagination = ({ itemsPerPage, totalItems, currentPage, paginate }) => {
  const totalPages = Math.ceil(totalItems / itemsPerPage);

  const handlePrevClick = () => {
    if (currentPage > 1) {
      paginate(currentPage - 1);
    }
  };

  const handleNextClick = () => {
    if (currentPage < totalPages) {
      paginate(currentPage + 1);
    }
  };

  return (
    <nav>
      <ul className="pagination">
        <li className="page-item">
          <button onClick={handlePrevClick} className="page-link">
            &lt;
          </button>
        </li>
        <li className="page-item">
          <button onClick={handleNextClick} className="page-link">
            &gt;
          </button>
        </li>
      </ul>
    </nav>
  );
};

export default EmployeeTable;
