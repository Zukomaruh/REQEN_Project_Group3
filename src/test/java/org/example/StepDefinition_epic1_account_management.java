package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.enums.AccountType;
import org.example.enums.StationType;
import org.example.managementClasses.AccountManager;
import org.example.managementClasses.ChargingLocationManager;
import org.example.managementClasses.StationManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class StepDefinition_epic1_account_management {
    String username;
    String email;
    String password;
    AccountType role;
    Account account;
    String testOutput;
    long userId;
    byte valueToUpdateAccount;
    String errorMessage;

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
    public void anAccountExistsWithTheFollowingUserCredentials(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        username = data.get("username");
        email = data.get("email");
        role = AccountType.valueOf(data.get("role"));
        password = data.get("password");

        account = new Account(username, email, password, role);
        userId = account.getUserId();
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
    public void thereExistsAnAccountWithTheFollowingUserCredentials(DataTable dataTable) {
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

    @Given("there exists an Account with the userID {int} and the following values:")
    public void thereExistsAnAccountWithTheUserIDAndTheFollowingValues(int arg0, DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        username = data.get("username");
        email = data.get("email");
        role = AccountType.valueOf(data.get("role"));
        password = data.get("password");
        account = new Account(username, email, password, role);
    }

    @When("the {string} is updated to {string}")
    public void theIsUpdatedTo(String arg0, String arg1) {
        switch (arg0.trim().toLowerCase()) {
            case "username":
                username = arg1;
                valueToUpdateAccount = 0;
                AccountManager.getInstance().updateAccount(username, account.getUserId());
                break;
            case "email":
                email = arg1;
                valueToUpdateAccount = 1;
                AccountManager.getInstance().updateAccount(email, account.getUserId());
                break;
            case "password":
                password = arg1;
                valueToUpdateAccount = 2;
                AccountManager.getInstance().updateAccount(password, account.getUserId());
                break;
            case "role":
                role = AccountType.valueOf(arg1);
                valueToUpdateAccount = 3;
                AccountManager.getInstance().updateAccount(role, account.getUserId());
                break;
            case "active":
                boolean active = Boolean.parseBoolean(arg1);
                valueToUpdateAccount = 4;
                AccountManager.getInstance().updateAccount(active, account.getUserId());
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @And("the updated value is requested")
    public void theIsRequested() {
        testOutput = AccountManager.getInstance().readAccount(account.getUserId()).toString();
    }

    @Then("the output contains the value {string}")
    public void theOutputContainsTheValue(String arg0) {
        assertTrue(testOutput.contains(arg0));
    }

    @And("the Accounts balance is zero")
    public void theAccountsBalanceIsZero() {
        if(AccountManager.getInstance().readAccount(account.getUserId()).getBalance() != 0){
            AccountManager.getInstance().readAccount(account.getUserId()).setRole(AccountType.OWNER);
            AccountManager.getInstance().readAccount(account.getUserId()).updateBalance(
                    -AccountManager.getInstance().readAccount(account.getUserId()).getBalance()
            );
            AccountManager.getInstance().readAccount(account.getUserId()).setRole(AccountType.CUSTOMER);
        }
    }

    @When("the user wants to delete the Account with the correct password")
    public void theUserWantsToDeleteTheAccountWithTheCorrectPassword() {
        try{AccountManager.getInstance().deleteAccount(userId, password);}
        catch (Exception e){errorMessage = e.getMessage();}
    }

    @Then("there is no Account with the username {string}")
    public void thereIsNoAccountWithTheUsername(String arg0) {
        assertNull(AccountManager.getInstance().readAccount(userId));
    }

    @And("the Account balance is {float}")
    public void theAccountBalanceIs(float arg0) {
        AccountManager.getInstance().readAccount(userId).updateBalance(arg0);
    }

    @Then("an error indicates that it is not allowed to delete Accounts with a balance not equal to zero")
    public void anErrorIndicatesThatItIsNotAllowedToDeleteAccountsWithABalanceNotEqualToZero() {
        assertEquals("You cannot delete an Account with a balance not zero!", errorMessage);
    }

    @And("the Account has an active charging status")
    public void theAccountHasAnActiveChargingStatus() {
        ChargingLocation location = ChargingLocationManager.getInstance().createLocation("Westbahnhof", "Kaisergassde13, 1150, Wien");
        long id = ChargingLocationManager.getInstance().getLocation("Westbahnhof").getLocationId();
        StationManager.getInstance().createStation(id, "station01", StationType.AC, 50, 0.4);
        ChargingStation station = StationManager.getInstance().createStation(id, "station01", StationType.AC, 50, 0.4);
        ChargingLocationManager.getInstance().getLocation("Westbahnhof").addStation(station);
        //AccountManager.getInstance().readAccount(userId).startChargingProcess(location.getLocationId(), station.getStationId());
    }

    @Then("an error indicates that it is not allowed to delete Accounts with an active charging status")
    public void anErrorIndicatesThatItIsNotAllowedToDeleteAccountsWithAnActiveChargingStatus() {
        assertEquals("You cannot delete an Account with an active charging process!", errorMessage);
    }
}
