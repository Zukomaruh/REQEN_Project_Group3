package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.ChargingStation;
import org.example.enums.AccountType;
import org.example.enums.PaymentMethod;
import org.example.enums.StationType;
import org.example.managementClasses.AccountManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class StepDefinition_epic2_prepaid_balance {
    Account account;
    private final AccountManager accountManager = AccountManager.getInstance();
    String prepaidAmount;
    String paymentMethod;
    String input;
    ChargingStation chargingStation;
    private String capturedOutput;
    private boolean success;

    @Given("an account exists with the user Id {int}")
    public void anAccountExistsWithTheUserId(int arg0) {
        account = new Account("Max", "max.123@gmail.com", "Max123456789", AccountType.CUSTOMER);
        account.setUserId(arg0);
        accountManager.addAccount(account);
    }

    @When("the prepaid amount {string} is selected")
    public void thePrepaidAmountPaymentMethodIsSelected(String arg0) {
        prepaidAmount = arg0;
        account.setPrepaidAmount(prepaidAmount);
    }

    @And("the payment method {string} is selected")
    public void thePaymentMethodAmountIsSelected(String arg0) {
        paymentMethod = arg0;
        account.setPaymentMethod(paymentMethod);
    }

    @Then("{string} and {string} are displayed")
    public void amountAndPaymentMethodAreDisplayed(String arg0, String arg1) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        account.getPaymentConfirmationMessage();
        System.setOut(originalOut);
        String output = outputStream.toString();
        assertThat(output).contains(arg0, arg1);
    }

    @Given("the invalid deposit amount {string} is selected")
    public void theInvalidDepositAmountAmountIsSelected(String arg0) {
        input = arg0;
    }

    @When("I confirm the amount")
    public void iConfirmTheAmount() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        success = account.setPrepaidAmount(input);
        System.setOut(originalOut);
        capturedOutput = outputStream.toString().trim();
    }

    @Given("the invalid payment method {string} is selected")
    public void theInvalidPaymentMethodIsSelected(String arg0) {
        input = arg0;
    }

    @When("I confirm the method")
    public void iConfirmTheMethod() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        success = account.setPaymentMethod(input);
        System.setOut(originalOut);
        capturedOutput = outputStream.toString().trim();
    }

    @Then("the error message {string} is displayed")
    public void theErrorMessageIsDisplayed(String arg0) {
        assertThat(success).isFalse();
        assertEquals(arg0, capturedOutput);
    }

    @Given("My current balance is {float}")
    public void myCurrentBalanceIs(float arg0) {
        account.setPrepaidBalance(arg0);
    }

    @When("I want to deposit {string}")
    public void iWantToDeposit(String arg0) {
        account.setPrepaidAmount(arg0);
    }

    @Then("{string} is added to {float}")
    public void isAddedTo(String arg0, float arg1) {
        accountManager.updatePrepaidBalance(account);
    }

    @And("the updated balance is displayed as {int}")
    public void theUpdatedBalanceIsDisplayedAs(int arg0) {
        assertEquals((float) arg0, account.getPrepaidBalance());
    }

    @Given("a charging station exists with the location Id {long}")
    public void aChargingStationExistsWithTheLocationId(long arg0) {
        chargingStation = new ChargingStation(arg0, "Station A", StationType.AC, 50, 0.30f);
    }

    @When("the charging price is {float}")
    public void theChargingPriceIs(float arg0) {
        chargingStation.setPricing(arg0);
    }

    @And("my current balance is lower than {int}")
    public void myCurrentBalanceIsLowerThan(int arg0) {
        int balance = 20 + (int) (Math.random() * 80);
        account.setPrepaidBalance(balance);
    }

    @Then("an error message is displayed saying {string}")
    public void anErrorMessageIsDisplayedSaying(String arg0) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        boolean result = account.canStartCharging(chargingStation);
        System.setOut(originalOut);
        String output = outputStream.toString().trim();
        assertThat(result).isFalse();
        assertTrue(output.contains(arg0));
    }
}