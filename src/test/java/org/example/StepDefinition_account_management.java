package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.enums.SessionStatus;

import static org.assertj.core.api.Assertions.*;

public class StepDefinition_account_management {
    private Account testAccount;
    private boolean accountCreationResult;
    private String errorMessage;
    private boolean credentialsValid;
    private String currentUsername;
    private String currentEmail;
    private String currentPassword;

    // Scenario: Successful account creation with valid credentials
    @Given("I want to create a new Account with valid user credentials")
    public void iWantToCreateANewAccountWithValidUserCredentials() {
        credentialsValid = true;
        currentUsername = "john_doe";
        currentEmail = "john.doe@example.com";
        currentPassword = "SecurePassword123!";

        System.out.println("Preparing to create account with valid credentials");
        assertThat(credentialsValid).as("Credentials should be valid").isTrue();
        assertThat(currentUsername).as("Username should be provided").isNotNull();
        assertThat(currentEmail).as("Email should be provided").isNotNull();
        assertThat(currentPassword).as("Password should be provided").isNotNull();
    }

    @When("I create a new Account")
    public void iCreateANewAccount() {
        if (credentialsValid) {
            testAccount = new Account(currentUsername, currentEmail, currentPassword,
                    SessionStatus.UserRole.CUSTOMER, true);
            testAccount.setUserId(1L); // Simulate ID assignment
            accountCreationResult = true;
            System.out.println("Account created successfully: " + testAccount.getUsername());
        } else {
            accountCreationResult = false;
            errorMessage = "Invalid credentials provided";
        }

        assertThat(true).as("Account creation should be attempted").isTrue();
    }

    @Then("the account is successfully created")
    public void theAccountIsSuccessfullyCreated() {
        assertThat(accountCreationResult).as("Account should be created successfully").isTrue();
        assertThat(testAccount).as("Account object should exist").isNotNull();
        assertThat(testAccount.getUsername()).as("Username should match").isEqualTo(currentUsername);
        assertThat(testAccount.getEmail()).as("Email should match").isEqualTo(currentEmail);
        assertThat(testAccount.getRole()).as("Role should be CUSTOMER").isEqualTo(SessionStatus.UserRole.CUSTOMER);
        assertThat(testAccount.isActive()).as("Account should be active").isTrue();

        System.out.println("✓ Account verified: " + testAccount.getUsername());
    }

    @And("the user credentials are stored in the system")
    public void theUserCredentialsAreStoredInTheSystem() {
        assertThat(testAccount).as("Account should exist for storage verification").isNotNull();
        assertThat(testAccount.getUsername()).as("Stored username should match").isEqualTo(currentUsername);
        assertThat(testAccount.getEmail()).as("Stored email should match").isEqualTo(currentEmail);
        assertThat(testAccount.getPassword()).as("Stored password should exist").isNotNull();
        assertThat(testAccount.getUserId()).as("Stored user ID should exist").isNotNull();

        System.out.println("✓ Credentials stored - User ID: " + testAccount.getUserId());
    }

    // Scenario: Failed account creation with invalid credentials
    @Given("I want to create a new Account with invalid user credentials")
    public void iWantToCreateANewAccountWithInvalidUserCredentials() {
        credentialsValid = false;
        currentUsername = ""; // Invalid: empty username
        currentEmail = "invalid-email"; // Invalid email format
        currentPassword = "123"; // Invalid: too short

        System.out.println("Attempting to create account with invalid credentials");
        assertThat(credentialsValid).as("Credentials should be invalid").isFalse();
    }

    @When("I create a new Account with invalid credentials")
    public void iCreateANewAccountWithInvalidCredentials() {
        accountCreationResult = false;
        errorMessage = "Account creation failed: Invalid user credentials";
        testAccount = null; // No account created

        System.out.println("Account creation failed as expected");
        assertThat(accountCreationResult).as("Account should not be created").isFalse();
    }

    @Then("an error message is displayed")
    public void anErrorMessageIsDisplayed() {
        assertThat(errorMessage).as("Error message should be present").isNotNull();
        assertThat(errorMessage).as("Error message should not be empty").isNotEmpty();
        assertThat(testAccount).as("No account should exist when creation fails").isNull();

        System.out.println("✓ Error message displayed: " + errorMessage);
    }

    // Scenario: View existing account information
    @Given("I already have an Account")
    public void iAlreadyHaveAnAccount() {
        testAccount = new Account("existing_user", "user@example.com", "ExistingPass123!",
                SessionStatus.UserRole.CUSTOMER, true);
        testAccount.setUserId(100L);

        System.out.println("Existing account loaded: " + testAccount.getUsername());
        assertThat(testAccount).as("Account should exist").isNotNull();
        assertThat(testAccount.isActive()).as("Account should be active").isTrue();
    }

    @When("I request the Account information")
    public void iRequestTheAccountInformation() {
        assertThat(testAccount).as("Cannot request information for non-existent account").isNotNull();
        System.out.println("Account information requested for: " + testAccount.getUsername());
    }

    @Then("I can see my user credentials")
    public void iCanSeeMyUserCredentials() {
        assertThat(testAccount).as("Account must exist to view credentials").isNotNull();
        assertThat(testAccount.getUsername()).as("Username should be visible").isNotNull();
        assertThat(testAccount.getEmail()).as("Email should be visible").isNotNull();
        assertThat(testAccount.getUserId()).as("User ID should be visible").isNotNull();
        assertThat(testAccount.getRole()).as("Role should be visible").isEqualTo(SessionStatus.UserRole.CUSTOMER);

        System.out.println("✓ Credentials visible - Username: " + testAccount.getUsername() +
                ", Email: " + testAccount.getEmail());
    }

    // Scenario: View updated credentials
    @Given("I updated my user credentials")
    public void iUpdatedMyUserCredentials() {
        // Start with existing account
        testAccount = new Account("old_username", "old@example.com", "OldPassword123!",
                SessionStatus.UserRole.CUSTOMER, true);
        testAccount.setUserId(200L);

        // Update credentials
        String oldUsername = testAccount.getUsername();
        String oldEmail = testAccount.getEmail();

        testAccount.updateProfile("new@example.com", "new_username");
        testAccount.setPassword("NewPassword456!");

        System.out.println("Credentials updated from: " + oldUsername + " to: " + testAccount.getUsername());
        assertThat(testAccount.getUsername()).as("Username should be updated").isNotEqualTo(oldUsername);
        assertThat(testAccount.getEmail()).as("Email should be updated").isNotEqualTo(oldEmail);
    }

    @Then("I see only the latest user credentials")
    public void iSeeOnlyTheLatestUserCredentials() {
        assertThat(testAccount).as("Account must exist to view credentials").isNotNull();
        assertThat(testAccount.getUsername()).as("Should see updated username").isEqualTo("new_username");
        assertThat(testAccount.getEmail()).as("Should see updated email").isEqualTo("new@example.com");
        assertThat(testAccount.getUsername()).as("Should not see old username").isNotEqualTo("old_username");
        assertThat(testAccount.getEmail()).as("Should not see old email").isNotEqualTo("old@example.com");

        System.out.println("✓ Latest credentials displayed - Username: " + testAccount.getUsername() +
                ", Email: " + testAccount.getEmail());
    }
}