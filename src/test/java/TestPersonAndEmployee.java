import com.neetesh.Problem2.Employee;
import com.neetesh.Problem2.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPersonAndEmployee {

    @Test
    void testPersonEqualsAndHashCode() {
        Person person1 = new Person("Alice", 30);
        Person person2 = new Person("Bob", 25);
        Person person3 = new Person("Alice", 30);

        // Reflexive: An object must be equal to itself
        assertTrue(person1.equals(person1));

        // Symmetric: If obj1.equals(obj2) is true, then obj2.equals(obj1) must be true
        assertEquals(person1.equals(person2), person2.equals(person1));

        // Transitive: If obj1.equals(obj2) is true and obj2.equals(obj3) is true, then obj1.equals(obj3) must be true
        if (person1.equals(person2) && person2.equals(person3)) {
            assertTrue(person1.equals(person3));
        }

        // Consistent: Multiple invocations of equals() should consistently return the same result
        assertTrue(person1.equals(person3));
        assertTrue(person1.equals(person3));

        // Null comparison: A non-null object should not be equal to null
        assertFalse(person1.equals(null));

        // Hash code consistency: If objects are equal, their hash codes should be equal too
        assertTrue(person1.hashCode() == person3.hashCode());
    }

    @Test
    void testEmployeeEqualsAndHashCode() {
        Employee employee1 = new Employee("John", 35, "Manager");
        Employee employee2 = new Employee("Mary", 28, "Developer");
        Employee employee3 = new Employee("John", 35, "Manager");
        Employee employee4 = new Employee("John", 35, "QA Engineer");
        Employee employee5 = new Employee("John", 30, "Manager");

        // Reflexive
        assertTrue(employee1.equals(employee1));

        // Symmetric
        assertEquals(employee1.equals(employee2), employee2.equals(employee1));

        // Transitive
        if (employee1.equals(employee2) && employee2.equals(employee3)) {
            assertTrue(employee1.equals(employee3));
        }

        // Consistent
        assertTrue(employee1.equals(employee3));
        assertTrue(employee1.equals(employee3));

        // Null comparison
        assertFalse(employee1.equals(null));

        // Hash code consistency
        assertTrue(employee1.hashCode() == employee3.hashCode());

        // Employees should not be equal to different role, even if other fields are the same
        assertFalse(employee1.equals(employee4));
        // Employees should not be equal to different age, even if other fields are the same
        assertFalse(employee1.equals(employee5));
    }
}