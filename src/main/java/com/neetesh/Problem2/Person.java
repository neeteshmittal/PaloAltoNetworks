package com.neetesh.Problem2;

public class Person {
    private final int age;
    private final String name;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + age;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Person other = (Person) obj;
        return age == other.age && name.equals(other.name);
    }
}

