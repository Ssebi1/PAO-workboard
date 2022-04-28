package data;

import auth.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Data {
    private List<User> users;
    private Integer currentUserLogged;

    public Data() {
        this.users = new ArrayList<>();
        this.currentUserLogged = -1;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Integer getCurrentUserLogged() {
        return currentUserLogged;
    }

    public void setCurrentUserLogged(Integer currentUserLogged) {
        this.currentUserLogged = currentUserLogged;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User getCurrentUserLoggedObject() {
        for(User user:users) {
            if(Objects.equals(user.getId(), currentUserLogged)) {
                return user;
            }
        }
        System.out.println("User not found.");
        return null;
    }

    public void updateCurrentLoggedUser(User newUser) {
        for(User user:users) {
            if(Objects.equals(user.getId(), currentUserLogged)) {
                user = newUser;
            }
        }
    }
}
