package com.neetesh.Problem2;

public class Employee extends Person {
    private final String role;

    public Employee(String name, int age, String role) {
        super(name, age);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        if (!super.equals(obj)) return false;

        Employee other = (Employee) obj;
        return role.equals(other.role);
    }
}
