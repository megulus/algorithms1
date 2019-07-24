/* *****************************************************************************
 *  Name:    Alan Turing
 *  NetID:   aturing
 *  Precept: P00
 *
 *  Description:  test program from www.techiedelight.com/
 *                why-override-equals-and-hashcode-methods-java/
 *                to demonstrate the above
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class Employee {

    private String name;
    private int salary;

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;
    //
    //     Employee employee = (Employee) o;
    //     if (salary != employee.salary) return false;
    //     if (name != null ? !name.equals(employee.name) : employee.name != null) return false;
    //
    //     return true;
    // }

    public boolean equals(Employee that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        if (salary != that.salary) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + salary;
        return result;
    }

    // Demonstrate the need for overriding equals and hashcode method in java
    public static void main(String[] args) {
        Employee e1 = new Employee("John", 80000);
        Employee e2 = new Employee("John", 80000);

        Set<Employee> employees = new HashSet<>();

        employees.add(e1);
        employees.add(e2);

        StdOut.println(employees);
        StdOut.println("employee e1 == employee e2 " + e1.equals(e2));
        StdOut.println(
                "employee e1 hashcode == employee e2 hashcode " + (e1.hashCode() == e2.hashCode()));
    }
}
