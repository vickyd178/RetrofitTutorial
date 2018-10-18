package com.vickyd178.retrofittutorial.models;

import java.util.List;

public class UsersResponse {
    private boolean error;
    List<User> users;

    public UsersResponse(boolean error, List<User> users) {
        this.error = error;
        this.users = users;
    }

    public boolean isError() {
        return error;
    }

    public List<User> getUsers() {
        return users;
    }
}
