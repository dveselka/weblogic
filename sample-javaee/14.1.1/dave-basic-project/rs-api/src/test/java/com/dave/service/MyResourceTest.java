package com.dave.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MyResourceTest {

    private MyResource resource;

    @Before
    public void setUp() {
        resource = new MyResource();
        resource.bean = new PersonSessionBean();
    }

    @Test
    public void personsReturnsAllBeanPersonsAsArray() {
        Person[] persons = resource.persons();

        assertEquals(2, persons.length);
        assertEquals("dave", persons[0].getName());
        assertEquals("abc", persons[1].getName());
    }

    @Test
    public void getPersonReturnsExpectedPersonForValidId() {
        Person person = resource.getPerson(1);

        assertNotNull(person);
        assertEquals("abc", person.getName());
        assertEquals(30, person.getAge());
    }

    @Test
    public void getPersonReturnsNullForOutOfRangeId() {
        assertNull(resource.getPerson(2));
    }

    @Test
    public void getPersonReturnsNullForNegativeId() {
        assertNull(resource.getPerson(-1));
    }
}
