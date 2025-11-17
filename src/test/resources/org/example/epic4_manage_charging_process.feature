Feature: Charging Process Management
  The system controls and displays the charging process for the customer.

  # User Story 4.1 – Charge Car
  Scenario: Charging starts when battery < 100%
    Given the battery is less than 100%
    When the car is connected
    Then it charges to 100% or target percentage

  Scenario: Charging starts immediately when battery ≥ 20%
    Given the battery is greater or equal to 20%
    When charging begins
    Then the car starts charging immediately

  Scenario: Driven distance matches expected range
    Given the car has finished charging
    When the target percentage is charged
    Then the driven distance matches expected range within 5% tolerance

  # User Story 4.2 – View Charging Status
  Scenario: Charging display is legible
    Given the display is on
    When charging details are shown (percentage, kW, time to full)
    Then the information is legible

  Scenario: Charging percentage only increases
    Given the car is charging
    When the charging percentage updates
    Then it only increases

  Scenario: Charging stops at 100%
    Given the car is charging
    When it reaches 100%
    Then a completion message is displayed
    And the charging stops
