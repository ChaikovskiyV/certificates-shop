package com.epam.esm.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * <p>
 * The type User dto.
 */
public class UserDto {
    @NotEmpty(message = "A user's firstName shouldn't be empty.")
    @Pattern(regexp = "\\w[\\w\\s-]+", message = "A user's firstName should contain only alphabetic symbols, space or dash.")
    @Length(min = 2, max = 100, message = "The length of the user's firstName should be between 2 and 100 symbols.")
    private String firstName;

    @NotEmpty(message = "A user's lastName shouldn't be empty.")
    @Pattern(regexp = "\\w[\\w\\s-]+", message = "A user's lastName should contain only alphabetic symbols, space or dash.")
    @Length(min = 2, max = 100, message = "The length of the user's lastName should be between 2 and 100 symbols.")
    private String lastName;

    @Email
    private String email;

    @Length(min = 3, max = 10, message = "A password should contain between two amd ten symbols.")
    private String password;

    /**
     * Instantiates a new User dto.
     */
    public UserDto() {
        //This default constructor without parameters.
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new StringBuilder("User{")
                .append("firstName='")
                .append(firstName)
                .append("', lastName='")
                .append(lastName)
                .append("', email='")
                .append(email)
                .append(", password='")
                .append(password)
                .append("'}")
                .toString();
    }
}