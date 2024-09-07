package com.example.elearningplatform;

import static okhttp3.internal.Internal.instance;

public class GlobalVariables {
    private static GlobalVariables instance;

    String Email;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    private GlobalVariables() {
        // Private constructor to prevent instantiation
    }
    public static synchronized GlobalVariables getInstance() {
        if (instance == null) {
            instance = new GlobalVariables();
        }
        return instance;
    }
}
