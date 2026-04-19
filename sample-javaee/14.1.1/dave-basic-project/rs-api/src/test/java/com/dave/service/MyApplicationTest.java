package com.dave.service;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyApplicationTest {

    @Test
    public void getClassesRegistersOnlyMyResource() {
        MyApplication application = new MyApplication();

        Set<Class<?>> classes = application.getClasses();

        assertEquals(1, classes.size());
        assertTrue(classes.contains(MyResource.class));
    }
}
