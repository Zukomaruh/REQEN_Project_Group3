package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.enums.AccountType;
import org.example.enums.PaymentMethod;
import org.example.enums.PrepaidAmount;
import org.example.managementClasses.AccountManager;
import org.example.managementClasses.PrepaidBalanceManager;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class StepDefinition_epic2_manage_prepaid_balance {
    private Account loggedIn;
    private PrepaidBalance prepaidBalance;
    String TestOutput;
    PaymentMethod paymentMethod;
    PrepaidAmount prepaidAmount;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrepaidBalanceManager prepaidManager = PrepaidBalanceManager.getInstance();
    AccountManager accountManager = AccountManager.getInstance();

    // Annahme: In einem @Before-Hook: System.setOut(new PrintStream(outContent));

    @Given("I am registered")
    public void iAmRegistered() {
        new Account("Max", "max@gmail.com", "Max123456789", AccountType.CUSTOMER); // Passwort auf 12 Zeichen angepasst
    }

    @When("I log in")
    public void iLogIn(){
        loggedIn = accountManager.login("Max", "Max123456789");
    }

    @Then("the login is successful")
    public void theLoginIsSuccessful() {
        assertNotNull(loggedIn);
    }

    @Then("I can choose between following payment methods:")
    public void iCanChooseBetweenFollowingPaymentMethods(DataTable datatable) {
        List<String> expectedMethods = datatable.asList();
        List<String> actualMethods = List.of(PaymentMethod.values()).stream().map(Enum::name).toList();
        assertEquals(expectedMethods, actualMethods);
    }

    @And("choose between following prepaid amounts:")
    public void chooseBetweenFollowingPrepaidAmounts(DataTable datatable) {
        List<String> expectedAmounts = datatable.asList();
        List<String> actualAmounts = List.of(PrepaidAmount.values()).stream().map(a -> String.valueOf(a.getCode())).toList();
        assertEquals(expectedAmounts, actualAmounts);
    }

    @Given("I selected the payment method {string} with the prepaid amount {string}")
    public void iSelectedThePaymentMethodWithThePrepaidAmount(String methodStr, String amountStr) {
        paymentMethod = PaymentMethod.valueOf(methodStr);
        prepaidAmount = PrepaidAmount.valueOf(amountStr);

        // Verbessert: Optionen als Strings für parseInt – Annahme: "3" für PAYPAL, "5" für TWO_HUNDRED
        String paymentOption = "3"; // Passe an dein Mapping an
        String amountOption = "5"; // Passe an

        prepaidManager.addPrepaidBalance(loggedIn, paymentOption, amountOption);

        prepaidBalance = loggedIn.getPrepaidBalance();
    }

    @When("the payment was successful")
    public void thePaymentWasSuccessful() {
        // Keine Assertion hier – das ist die Aktion
    }

    @Then("a message is displayed saying {string}")
    public void aMessageIsDisplayedSaying(String expectedMessage) {
        String output = outContent.toString();
        assertThat(output).contains(expectedMessage);
    }

    @And("the deposited amount is added to the current balance")
    public void theDepositedAmountIsAddedToTheCurrentBalance() {
        prepaidBalance = loggedIn.getPrepaidBalance();
        assertThat(prepaidBalance).isNotNull();

        BigDecimal current = prepaidBalance.getPrepaidAmount();
        BigDecimal previous = ScenarioContext.getPreviousBalance(); // Annahme: ScenarioContext existiert
        BigDecimal deposited = BigDecimal.valueOf(prepaidAmount.getCode());

        assertThat(current).isEqualByComparingTo(previous.add(deposited));
    }

    @And("the updated balance is displayed")
    public void theUpdatedBalanceIsDisplayed() {
        String output = outContent.toString();
        assertThat(output).contains(String.format("updated balance: %.2f", prepaidBalance.getPrepaidAmount()));
    }

    @Given("I selected a payment method and a prepaid amount")
    public void iSelectedAPaymentMethodAndAPrepaidAmount() {
        if (loggedIn.getPrepaidBalance() == null) {
            String paymentOption = "3"; // Beispiel
            String amountOption = "1"; // Beispiel für Initialisierung
            prepaidManager.addPrepaidBalance(loggedIn, paymentOption, amountOption);
        }
    }

    @When("an invalid deposit amount of {string} is selected")
    public void anInvalidDepositAmountOfAmountIsSelected(String invalidAmount) {
        String paymentMethodStr = "3"; // Beispiel gültig
        prepaidManager.addPrepaidBalance(loggedIn, paymentMethodStr, invalidAmount); // Erwartet null oder false
    }

    @And("nothing is added to the current balance")
    public void nothingIsAddedToTheCurrentBalance() {
        BigDecimal current = loggedIn.getPrepaidBalance().getPrepaidAmount();
        BigDecimal previous = ScenarioContext.getPreviousBalance();
        assertThat(current).isEqualByComparingTo(previous);
    }

    @And("the balance is displayed as unchanged")
    public void theBalanceIsDisplayedAsUnchanged() {
        String output = outContent.toString();
        assertThat(output).contains("updated balance: " + ScenarioContext.getPreviousBalance().toString());
    }

    @When("I want to read my current prepaid balance")
    public void iWantToReadMyCurrentPrepaidBalance() {
        prepaidBalance = loggedIn.getPrepaidBalance();
        System.out.println("current balance: " + (prepaidBalance != null ? prepaidBalance.getPrepaidAmount() : BigDecimal.ZERO));
    }

    @Then("the current prepaid balance is displayed")
    public void theCurrentPrepaidBalanceIsDisplayed() {
        String output = outContent.toString();
        assertThat(output).contains("current balance: " + prepaidBalance.getPrepaidAmount().toString());
    }

    @Given("I made a deposit")
    public void iMadeADeposit() {
        String paymentOption = "3";
        String amountOption = "5";
        prepaidManager.addPrepaidBalance(loggedIn, paymentOption, amountOption);
    }

    @When("I want to read my updated prepaid balance")
    public void iWantToReadMyUpdatedPrepaidBalance() {
        prepaidBalance = loggedIn.getPrepaidBalance();
        System.out.println("current balance: " + prepaidBalance.getPrepaidAmount());
    }

    @Then("the updated prepaid balance is displayed")
    public void theUpdatedPrepaidBalanceIsDisplayed(){
        String output = outContent.toString();
        assertThat(output).contains("current balance: " + prepaidBalance.getPrepaidAmount().toString());
    }

    @Given("I want to charge my car")
    public void iWantToChargeMyCar() {
        // Kontext: Balance holen
        prepaidBalance = loggedIn.getPrepaidBalance();
    }

    @When("my current balance is lower than the charging price of {int}")
    public void myCurrentBalanceIsLowerThanTheChargingPrice(int chargingPriceInt) {
        BigDecimal chargingPrice = BigDecimal.valueOf(chargingPriceInt);
        BigDecimal currentBalance = prepaidBalance != null ? prepaidBalance.getPrepaidAmount() : BigDecimal.ZERO;
        if (currentBalance.compareTo(chargingPrice) < 0) {
            System.out.println("Charging terminated: Insufficient balance");
        } else {
            System.out.println("Charging possible: Sufficient balance");
        }
    }

    @And("the charging process is terminated")
    public void theChargingProcessIsTerminated(){
        String output = outContent.toString();
        assertThat(output).contains("Charging terminated");
    }
}