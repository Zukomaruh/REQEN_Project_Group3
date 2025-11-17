Feature: Prepaid Service Management
  Customers can deposit money and view their prepaid balance for charging.

  # User Story 2.1 – Deposit Money
  Scenario: Successful deposit
    Given I am logged in
    When I choose to deposit money
    And I select an amount and payment method
    Then the deposited amount is reflected immediately in my balance

  Scenario: Invalid deposit amount
    Given I enter an invalid number
    When I check my balance
    Then no changes are made
    And an error message is displayed

  # User Story 2.2 – View Balance
  Scenario: Show prepaid balance
    Given I am a customer
    When I open the balance page
    Then I see my current prepaid balance

  Scenario: Balance updates after deposit
    Given I recently made a deposit
    When I refresh the balance page
    Then the updated balance is displayed

  Scenario: Low balance warning
    Given I have insufficient funds
    When I attempt to start charging
    Then I am notified about the low balance
