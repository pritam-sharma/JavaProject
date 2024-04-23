import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// Employee class
class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int id;
    private String department;
    private String position;
    private double salary;

    // Constructor
    public Employee(String name, int id, String department, String position, double salary) {
        this.name = name;
        this.id = id;
        this.department = department;
        this.position = position;
        this.salary = salary;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    // Override toString() method
    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Department: " + department + ", Position: " + position + ", Salary: $" + salary;
    }
}

// Employee Management System class
class EmployeeManagementSystem {
    private java.util.List<Employee> employees;
    private static final String FILE_NAME = "employees.txt";

    // Constructor
    public EmployeeManagementSystem() {
        employees = new ArrayList<>();
        loadDataFromFile();
    }

    // Add new employee
    public void addEmployee(Employee employee) {
        employees.add(employee);
        saveDataToFile();
    }

    // Remove employee
    public void removeEmployee(int id) {
        employees.removeIf(employee -> employee.getId() == id);
        saveDataToFile();
    }

    // Update employee information
    public void updateEmployee(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == employee.getId()) {
                employees.set(i, employee);
                saveDataToFile();
                return;
            }
        }
        System.out.println("Employee not found.");
    }

    // Search employee by ID
    public Employee findEmployeeById(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    // Display all employees
    public java.util.List<Employee> getAllEmployees() {
        return employees;
    }

    // Save employee data to a file
    private void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(employees);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load employee data from a file
    private void loadDataFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            employees = (java.util.List<Employee>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File does not exist or empty
        }
    }
}

public class EmployeeManagementSystemGUI extends JFrame {
    private EmployeeManagementSystem ems;
    private JTextArea employeeTextArea;

    public EmployeeManagementSystemGUI() {
        ems = new EmployeeManagementSystem();

        setTitle("Employee Management System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });
        buttonPanel.add(addEmployeeButton);

        JButton removeEmployeeButton = new JButton("Remove Employee");
        removeEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEmployee();
            }
        });
        buttonPanel.add(removeEmployeeButton);

        JButton updateEmployeeButton = new JButton("Update Employee");
        updateEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployee();
            }
        });
        buttonPanel.add(updateEmployeeButton);

        JButton displayAllButton = new JButton("Display All Employees");
        displayAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAllEmployees();
            }
        });
        buttonPanel.add(displayAllButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Text area to display employees
        employeeTextArea = new JTextArea();
        add(new JScrollPane(employeeTextArea), BorderLayout.CENTER);

        setVisible(true);
    }

    private void addEmployee() {
        // Create dialog for adding employee
        JDialog addEmployeeDialog = new JDialog(this, "Add Employee", true);
        addEmployeeDialog.setSize(300, 200);
        addEmployeeDialog.setLayout(new GridLayout(5, 2));

        addEmployeeDialog.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        addEmployeeDialog.add(nameField);

        addEmployeeDialog.add(new JLabel("ID:"));
        JTextField idField = new JTextField();
        addEmployeeDialog.add(idField);

        addEmployeeDialog.add(new JLabel("Department:"));
        JTextField departmentField = new JTextField();
        addEmployeeDialog.add(departmentField);

        addEmployeeDialog.add(new JLabel("Position:"));
        JTextField positionField = new JTextField();
        addEmployeeDialog.add(positionField);

        addEmployeeDialog.add(new JLabel("Salary:"));
        JTextField salaryField = new JTextField();
        addEmployeeDialog.add(salaryField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    int id = Integer.parseInt(idField.getText());
                    String department = departmentField.getText();
                    String position = positionField.getText();
                    double salary = Double.parseDouble(salaryField.getText());

                    ems.addEmployee(new Employee(name, id, department, position, salary));
                    addEmployeeDialog.dispose();
                    displayAllEmployees();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addEmployeeDialog, "Invalid input. Please enter numeric values for ID and Salary.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        addEmployeeDialog.add(addButton);

        addEmployeeDialog.setVisible(true);
    }

    private void removeEmployee() {
        String idString = JOptionPane.showInputDialog(this, "Enter Employee ID to remove:");
        if (idString != null) {
            try {
                int id = Integer.parseInt(idString);
                ems.removeEmployee(id);
                displayAllEmployees();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a numeric value for ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateEmployee() {
        String idString = JOptionPane.showInputDialog(this, "Enter Employee ID to update:");
        if (idString != null) {
            try {
                int id = Integer.parseInt(idString);
                Employee employeeToUpdate = ems.findEmployeeById(id);
                if (employeeToUpdate != null) {
                    JDialog updateEmployeeDialog = new JDialog(this, "Update Employee", true);
                    updateEmployeeDialog.setSize(300, 200);
                    updateEmployeeDialog.setLayout(new GridLayout(5, 2));

                    updateEmployeeDialog.add(new JLabel("Name:"));
                    JTextField nameField = new JTextField(employeeToUpdate.getName());
                    updateEmployeeDialog.add(nameField);

                    updateEmployeeDialog.add(new JLabel("ID:"));
                    JTextField idField = new JTextField(String.valueOf(employeeToUpdate.getId()));
                    idField.setEditable(false);
                    updateEmployeeDialog.add(idField);

                    updateEmployeeDialog.add(new JLabel("Department:"));
                    JTextField departmentField = new JTextField(employeeToUpdate.getDepartment());
                    updateEmployeeDialog.add(departmentField);

                    updateEmployeeDialog.add(new JLabel("Position:"));
                    JTextField positionField = new JTextField(employeeToUpdate.getPosition());
                    updateEmployeeDialog.add(positionField);

                    updateEmployeeDialog.add(new JLabel("Salary:"));
                    JTextField salaryField = new JTextField(String.valueOf(employeeToUpdate.getSalary()));
                    updateEmployeeDialog.add(salaryField);

                    JButton updateButton = new JButton("Update");
                    updateButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                String name = nameField.getText();
                                String department = departmentField.getText();
                                String position = positionField.getText();
                                double salary = Double.parseDouble(salaryField.getText());

                                Employee updatedEmployee = new Employee(name, id, department, position, salary);
                                ems.updateEmployee(updatedEmployee);
                                updateEmployeeDialog.dispose();
                                displayAllEmployees();
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(updateEmployeeDialog, "Invalid input. Please enter a numeric value for Salary.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    updateEmployeeDialog.add(updateButton);

                    updateEmployeeDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a numeric value for ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayAllEmployees() {
        java.util.List<Employee> employees = ems.getAllEmployees();
        StringBuilder sb = new StringBuilder();
        for (Employee employee : employees) {
            sb.append(employee).append("\n");
        }
        employeeTextArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        new EmployeeManagementSystemGUI();
    }
}
