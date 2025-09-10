package csc1035.project2.DatabaseTables;

import csc1035.project2.DatabaseIO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Stores a user 'account' so multiple users can access and use the database.
 */
@Entity
@Table(name = "tblUser")
public class User {
    @Id
    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) { this.username = username; }
}