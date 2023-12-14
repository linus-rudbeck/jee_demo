package com.example.jee_demo;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        var timeNow = LocalDateTime.now();


        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        MyEntity entity = new MyEntity();
        entity.setName(timeNow.toString());
        em.persist(entity);
        em.getTransaction().commit();

        var results = em.createQuery("SELECT e FROM MyEntity e", MyEntity.class).getResultList();

        em.close();
        emf.close();


        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");

        for(var timeEntity : results){
            out.println("<p>" + timeEntity.getName() + "</p>");
        }

        out.println("</body></html>");
    }

    public void destroy() {
    }
}