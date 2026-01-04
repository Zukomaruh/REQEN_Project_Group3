Feature: Prepaid Balance
  As a customer
  I want to read and update my prepaid balance
  so that I make sure to have enough prepaid funds to charge my car.

  #User Story 2.1: Update Prepaid Balance
  #As a customer
  #I want to deposit money into my Prepaid Account
  #so that I can use it to pay for charging my car.
  Scenario: select an amount and payment method
    Given I am registered
    When I log in with the username "Carmen"
    And the password "carmen123"
    And I want to deposit money
    Then I can choose between following payment methods:
      |credit card  |
      |debit card   |
      |PAYPAL       |
      |BANK TRANSFER|
      |APPLE PAY    |
      |GOOGLE PAY   |
    And choose between following prepaid amounts:
      |20                 |
      |50                 |
      |100                |
      |150                |
      |200                |
      |individual amount  |

  Scenario: display of deposited money
    Given I selected a payment method and a prepaid amount
    When the payment was successful
    Then a message is displayed saying "payment successful"
    And the deposited amount is added to the current balance
    And the updated balance is displayed

  Scenario Outline: invalid input of deposit amount
    Given I selected a payment method and a prepaid amount
    When <invalid deposit amount selected>
    Then an error message is displayed saying "error: <invalid deposit amount selected>"
    And nothing is added to the current balance
    And the balance is displayed as unchanged
  Examples:
    | invalid deposit amount selected                   |
    | the individual amount exceeds 500                 |
    | the individual amount subceeds 20                 |
    | the individual amount contains letters            |
    | the individual amount contains special characters |

  #User Story 2.2
  #As a customer
  #I want to see my balance
  #so that I can see if I have enough prepaid funds to charge my car.
  Scenario: display of current prepaid balance
    Given I am registered
    When I log in with the username "Carmen"
    And the password "carmen123"
    And I want to read my prepaid balance
    Then the current prepaid balance is displayed

  Scenario: display of updated prepaid balance
    Given I made a deposit
    When I want to read my prepaid balance
    Then the updated balance is displayed

  Scenario: insufficient prepaid balance
    Given I am registered
    When I log in with the username "Carmen"
    And the password "carmen123"
    And I want to charge my car
    And my current balance is lower than the charging price
    Then an error message is displayed saying "error: insufficient prepaid balance"
    And a message is displayed saying "terminate charging process ..."
    And the charging process is terminated

