package com.clearstream.service;


import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("persons")
@RequestScoped
public class MyResource {

    @EJB
    PersonSessionBean bean;

    @GET
    @Produces({"application/json"})
    public Person[] persons() {
        return bean.getPersons().toArray(new Person[0]);
    }

    @GET
    @Produces({"application/json"})
    @Path("{id}")
    public Person getPerson(@PathParam("id") int id) {
        if (id < bean.getPersons().size())
            return bean.getPersons().get(id);
        else
            return null;
    }
}