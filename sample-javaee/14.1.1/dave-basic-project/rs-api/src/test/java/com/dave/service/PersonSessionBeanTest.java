package com.dave.service;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

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
    public void getPersonsReturnsInternalListReference() {
        PersonSessionBean bean = new PersonSessionBean();

        List<Person> persons = bean.getPersons();
        persons.add(new Person("new", 99));

        assertSame(persons, bean.getPersons());
        assertEquals(3, bean.getPersons().size());
    }

    @Test
    public void findPersonByNameReturnsMatchOrNull() throws Exception {
        PersonSessionBean bean = new PersonSessionBean();
        Method method = PersonSessionBean.class.getDeclaredMethod("findPersonByName", String.class);
        method.setAccessible(true);

        Object match = method.invoke(bean, "dave");
        Object miss = method.invoke(bean, "missing");

        assertNotNull(match);
        assertEquals("dave", ((Person) match).getName());
        assertNull(miss);
    }
}
