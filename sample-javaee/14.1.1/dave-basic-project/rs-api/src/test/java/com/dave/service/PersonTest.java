package com.dave.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PersonTest {

    @Test
    public void defaultConstructorInitializesEmptyState() {
        Person person = new Person();

        assertNull(person.getName());
        assertEquals(0, person.getAge());
    }

    @Test
    public void constructorAndAccessorsWork() {
        Person person = new Person("alice", 42);

        assertEquals("alice", person.getName());
        assertEquals(42, person.getAge());
    }

    @Test
    public void settersUpdateStateAndToStringReflectsValues() {
        Person person = new Person();

        person.setName("bob");
        person.setAge(27);

        assertEquals("bob", person.getName());
        assertEquals(27, person.getAge());
        assertEquals("bob(27)", person.toString());
    }
}
