package com.dave.service;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PersonSessionBeanTest {

    @Test
    public void constructorSeedsExpectedPersons() {
        PersonSessionBean bean = new PersonSessionBean();

        List<Person> persons = bean.getPersons();
        assertEquals(2, persons.size());
        assertEquals("dave", persons.get(0).getName());
        assertEquals(50, persons.get(0).getAge());
        assertEquals("abc", persons.get(1).getName());
        assertEquals(30, persons.get(1).getAge());
    }

    @Test
    public void getPersonsConsistentlyReturnsSeededData() {
        PersonSessionBean bean = new PersonSessionBean();

        List<Person> firstRead = bean.getPersons();
        List<Person> secondRead = bean.getPersons();

        assertEquals(2, firstRead.size());
        assertEquals(2, secondRead.size());
        assertEquals("dave", secondRead.get(0).getName());
        assertEquals("abc", secondRead.get(1).getName());
    }
}
