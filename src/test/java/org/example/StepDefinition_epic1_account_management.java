package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.enums.AccountType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefinition_epic1_account_management {
    String username;
    String email;
    String password;
    AccountType role;
    Account account;
    String testOutput;

    @Given("The customer is on the systems main class")
    public void theCustomerIsOnTheSystemsMainClass() {
    }

    @When("the customer registers a new account with the username {string}")
    public void theCustomerRegistersANewAccountWithTheUsername(String arg0) {
        username = arg0;
    }

    @And("enters the email {string}")
    public void entersTheEmail(String arg0) {
        email = arg0;
    }

    @And("enters the password {string}")
    public void entersThePassword(String arg0) {
        password = arg0;
    }

    @And("the role is set to {string}")
    public void theRoleIsSetTo(String arg0) {
        if(Objects.equals(arg0, "OWNER")){
            role = AccountType.OWNER;
        }else{
            role = AccountType.CUSTOMER;
        }
    }

    @Then("an Account with the username {string} is successfully created.")
    public void anAccountWithTheUsernameIsSuccessfullyCreated(String arg0) {
        account = new Account(username, email, password, role);
        assertEquals(arg0, account.getUsername());
    }

    @And("a confirmation message is printed that says {string}")
    public void aConfirmationMessageIsPrintedThatSays(String arg0) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new Account(username, email, password, role);
        System.setOut(originalOut);
        String output = outContent.toString();
        assertTrue(output.contains(arg0));
    }

    @Given("the customer is on the system main class")
    public void theCustomerIsOnTheSystemMainClass() {
    }

    @Then("the Account is not created")
    public void theAccountIsNotCreated() {
        Account account = new Account(username, email, password, role);
        assertEquals(0, account.getUserId());
    }

    @And("an error message is printed that says {string}")
    public void anErrorMessageIsPrintedThatSays(String arg0) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        new Account(username, email, password, role);
        System.setOut(originalOut);
        String output = outContent.toString();
        assertTrue(output.contains(arg0));
    }

    @Given("An Account exists with the following user credentials:")
    public void anAccountExistsWithTheFollowingUserCredentials(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        username = data.get("username");
        email = data.get("email");
        role = AccountType.valueOf(data.get("role"));

        account = new Account(username, email, "123456789012", role);
    }

    @When("the Account information is requested")
    public void theAccountInformationIsRequested() {
        testOutput = account.toString();
    }

    @Then("the output is:")
    public void theOutputIs(String expected) {
        expected = expected.replace("<generated_ID>", String.valueOf(account.getUserId()));
        assertEquals(expected,testOutput);
    }

    @Given("There exists an Account with the following user credentials:")
    public void thereExistsAnAccountWithTheFollowingUserCredentials(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        username = data.get("username");
        email = data.get("email");
        role = AccountType.valueOf(data.get("role"));

        account = new Account(username, email, "123456789012", role);
    }

    @When("the email is updated to {string}")
    public void theEmailIsUpdatedTo(String arg0) {
        account.setEmail(arg0);
    }

    @Then("the updated output is:")
    public void theUpdatedOutputIs(String expected) {
        expected = expected.replace("<generated_ID>", String.valueOf(account.getUserId()));
        assertEquals(expected, account.toString());
    }
}
