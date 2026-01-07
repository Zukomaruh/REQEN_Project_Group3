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
    When I log in
    Then the login is successful
    And I can choose between following payment methods:
      | CREDIT_CARD  |
      | DEBIT_CARD   |
      | PAYPAL       |
      | BANK_TRANSFER|
      | APPLE_PAY    |
      | GOOGLE_PAY   |
    And choose between following prepaid amounts:
      | 20  |
      | 50  |
      | 100 |
      | 150 |
      | 200 |

  Scenario: display of deposited money
    Given I selected the payment method "PAYPAL" with the prepaid amount "TWO_HUNDRED"
    When the payment was successful
    Then a message is displayed saying "payment successful"
    And the deposited amount is added to the current balance
    And the updated balance is displayed

  Scenario Outline: invalid input of deposit amount
    Given I selected a payment method and a prepaid amount
    When an invalid deposit amount of "<amount>" is selected
    Then an error message is displayed saying "<error message>"
    And nothing is added to the current balance
    And the balance is displayed as unchanged
    Examples:
      | amount | error message                    |
      | 600    | Amount must not exceed 500       |
      | 10     | Amount must be at least 20       |
      | 50abc  | Amount must contain only numbers |
      | 50!    | Amount must contain only numbers |
      | xyz    | Invalid input                    |

  #User Story 2.2
  #As a customer
  #I want to see my balance
  #so that I can see if I have enough prepaid funds to charge my car.
  Scenario: display of current prepaid balance
    Given I am logged in
    When I want to read my current prepaid balance
    Then the current prepaid balance is displayed

  Scenario: display of updated prepaid balance
    Given I made a deposit
    When I want to read my updated prepaid balance
    Then the updated prepaid balance is displayed

  Scenario: insufficient prepaid balance
    Given I want to charge my car
    When my current balance is lower than the charging price of 100
    Then an error message is displayed saying "Charging terminated: Insufficient balance"
    And the charging process is terminated