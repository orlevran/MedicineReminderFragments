package Controllers;

import Models.User;

public class UserController {
    private static UserController instance = null;

    private User user;
    private UserDataBaseHelper usersDB;
    private MedicinesDataBaseHelper medicinesDB;

    public static UserController getInstance(){
        if(instance == null)
            instance = new UserController();
        return instance;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public UserDataBaseHelper getUsersDB() {
        return this.usersDB;
    }

    public void setUsersDB(UserDataBaseHelper usersDB) {
        this.usersDB = usersDB;
    }

    public MedicinesDataBaseHelper getMedicinesDB() {
        return this.medicinesDB;
    }

    public void setMedicinesDB(MedicinesDataBaseHelper medicinesDB) {
        this.medicinesDB = medicinesDB;
    }
}
