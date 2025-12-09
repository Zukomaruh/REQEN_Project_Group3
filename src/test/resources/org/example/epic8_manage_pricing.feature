Feature: Manage Pricing of Charging Stations
  As an owner
  I want to manage the pricing of charging stations
  so that I can set and update the costs for customers effectively.

  # User Story 8.1 - Create Pricing
  Scenario: create pricing rules
  As owner
  I want to create new pricing rules for charging stations
  so that new tariffs can be introduced.
    Given a pricing rule exists with the pricingId 1001
    When I create a pricing rule with the name "pricingRule01"
    And the pricingId 1001
    And and the content "rule content"
    And <label selected>
    Then a new pricing rule with the name "pricingRule01", an unique pricingId and the status ACTIVE is created.

    #the label "kWh", "AC", "kwh DC" or "Minuten pro Lademodus"
  Scenario Outline:
    Examples:
      | label selected                      |
      | "kWh" selected                      |
      | "AC" selected                       |
      | "kwh DC" selected                   |
      | "Minuten pro Lademodus" selected    |


  Scenario: displaying confirmation message for valid pricing rule
    Given I save a new valid pricing rule
    When the save is successful
    Then a confirmation message is displayed and the rule is immediately active.

  Scenario: displaying error message for invalid pricing rule
    Given the pricing rule contains invalid values (e.g. negative price)
    When I try to save
    Then an error message is shown and the rule is not created.

  # User Story 8.2 - Read Pricing
  Scenario: view current pricing of selected charging station
  As owner
  I want to read the current pricing of charging stations
  so that I can review or compare station rates.
    Given I open the pricing overview
    When I select a charging station
    Then its current price per kWh and any active special rules are displayed.

  Scenario: sort or filter pricing list
    Given multiple charging stations are available
    When I sort or filter by price
    Then the list of stations is displayed in the chosen order.

  Scenario: refresh pricing data
    Given pricing data might be outdated
    When I refresh the page
    Then the latest pricing information is retrieved from the database.


# User Story 8.3 - Update Pricing
  Scenario: update pricing rule with immediate effect
  As owner
  I want to update existing pricing rules
  so that price changes take effect immediately or at a scheduled time.
    Given I am editing a pricing rule
    When I change the price and save
    Then the updated price is stored and visible to customers within ≤ 10 seconds.

  Scenario: schedule future pricing rule activation
    Given I schedule a price change for a future date
    When that date is reached
    Then the new price automatically becomes active without manual intervention.

  Scenario: handle active sessions during pricing update
    Given the pricing rule is in use by active sessions
    When I update it
    Then ongoing sessions continue with the old price and new sessions use the new price.


