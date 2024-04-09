package com.aerologix.server;

import java.util.HashMap;

import com.aerologix.serialization.User;
import com.aerologix.serialization.User.UserType;

public class Main {
    
    public static void main(String[] args) {
        User u1 = new User("admin", "admin", UserType.ADMIN, "God");
        User u2 = new User("pepeprueba", "1234", UserType.COUNTER_CLERK, "Pepe Prueba");

        HashMap<String, User> users = new HashMap<String, User>();

        users.put(u1.getUserId(), u1);
        users.put(u2.getUserId(), u2);

        System.out.println(u1);
        System.out.println(u2);
    }
}
