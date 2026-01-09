Feature: Prepaid Balance
  As a customer
  I want to read and update my prepaid balance
  so that I make sure to have enough prepaid funds to charge my car.

  Background:
    Given an account exists with the user Id 1001

  #User Story 2.1: Update Prepaid Balance
  #As a customer
  #I want to deposit money into my Prepaid Account
  #so that I can use it to pay for charging my car.
  Scenario Outline: display of deposit amount
    Given an account exists with the user Id 1001
    When the prepaid amount <amount> is selected
    And the payment method <payment method> is selected
    Then <amount> and <payment method> are displayed
    Examples:
      | amount    | payment method   |
      | "20"      | "credit card"    |
      | "500"     | "debit card"     |
      | "344.34"  | "paypal"         |
      | "200"     | "bank transfer"  |
      | "100"     | "apple pay"      |
      | "20.23"   | "google pay"     |

  Scenario Outline: invalid input of deposit amount
    Given the invalid deposit amount <amount> is selected
    When  I confirm the amount
    Then the error message <amount error> is displayed
    Examples:
      | amount   | amount error                       |
      | ""       | "Amount must not be empty"         |
      | "600"    | "Amount must not exceed 500"       |
      | "10"     | "Amount must be at least 20"       |
      | "50abc"  | "Amount must contain only numbers" |
      | "50!"    | "Amount must contain only numbers" |
      | "xyz"    | "Amount must contain only numbers" |

  Scenario Outline: invalid input of payment method
    Given the invalid payment method <method> is selected
    When I confirm the method
    Then the error message <method error> is displayed
    Examples:
      | method          | method error                                 |
      | ""              | "Payment Method must not be empty"           |
      | "paypal1"       | "Payment Method must contain only letters"   |
      | "google_pay"    | "Payment Method must contain only letters"   |
      | "cash"          | "Select valid payment method"                |

  #User Story 2.2: Read Prepaid Balance
  #As a customer
  #I want to view my balance
  #so that I can see if I have enough prepaid funds to charge my car.

  Scenario Outline: display of updated balance
    Given My current balance is <balance>
    When I want to deposit <amount>
    Then <amount> is added to <balance>
    And the updated balance is displayed as <sum>
    Examples:
      | balance | amount   | sum    |
      | 0       | "20"     | 20     |
      | 20      | "500"    | 520    |
      | 520     | "344.34" | 864    |

  Scenario: insufficient prepaid balance
    Given a charging station exists with the location Id 1001
    When the charging price is 100
    And  my current balance is lower than 100
    Then an error message is displayed saying "Charging terminated: Insufficient balance"