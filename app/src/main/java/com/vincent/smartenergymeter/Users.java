package com.vincent.smartenergymeter;


public class Users {
    private String email;
    private String names;
    private String username;

    public Users() {
    }

    public Users(String email, String names, String username) {
        this.email = email;
        this.names = names;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
