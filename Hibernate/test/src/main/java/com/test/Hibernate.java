package com.test;

import com.test.models.ClassEmployee;
import com.test.models.Employee;
import com.test.models.Rating;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Hibernate {
    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration config = new Configuration();
            config.addAnnotatedClass(Employee.class);
            config.addAnnotatedClass(ClassEmployee.class);
            config.addAnnotatedClass(Rating.class);
            sessionFactory = config.buildSessionFactory();
        } catch (Throwable ex){
            System.out.println(ex.getMessage());
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public static void shutdown(){
        sessionFactory.close();
    }
}
