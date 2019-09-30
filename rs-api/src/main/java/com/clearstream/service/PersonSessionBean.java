package com.clearstream.service;

import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PersonSessionBean {

    private final List<Person> list;

    public PersonSessionBean() {
        list = new ArrayList<>();
        Person p1 = new Person("dave", 50);
        list.add(p1);
        Person p2 = new Person("abc", 30);
        list.add(p2);
    }

    private Person findPersonByName(String name) {
        for (Person p : list) {
            if (name.equals(p.getName()))
                return p;
        }
        return null;
    }

    public List<Person> getPersons() {
        return list;
    }
}
