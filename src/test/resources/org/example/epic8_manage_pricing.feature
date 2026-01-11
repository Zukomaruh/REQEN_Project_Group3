Feature: Manage Pricing of Charging Stations
  As an owner
  I want to manage the pricing of charging stations
  so that I can set and update the costs for customers effectively.

  # User Story 8.1 - Create Pricing
  Scenario: create pricing rules
  As owner
  I want to create new pricing rules for charging stations
  so that new tariffs can be introduced.
    Given a charging station exists with the stationId 1001 and other values
    When I create a pricing rule with the pricingId 1001
    And with the locationId 1001
    And the validFrom "DDMMYY"
    And the validTo "DDMMYY"
    And the priceComponents
    Then a new pricing rule is created

  Scenario: save a valid pricing rule
    Given a pricing rule is created with the pricingId 1001
    When the input data is valid
    Then the pricing rule is added to the List of the pricing for the station
    And  isActive is true
    And a pricing confirmation message is printed that says "Pricing rule created successfully"

  Scenario: discard an invalid pricing rule
    Given a pricing rule is created with the pricingId 1001
    When the input data is invalid
    Then an pricing error message is printed that says "Creation failed! Please enter valid pricing settings."
    And the invalid input is deleted

  # User Story 8.2 - Read Pricing
  Scenario: view current pricing of selected charging station
  As Owner
  I want to read prices of locations
  so that they can vary.
    Given a charging station exists with the stationId 1001
    When the pricingId 1001 being the current pricing is true
    Then its current price 0,30 is displayed

  Scenario: display latest pricing
    Given a pricing exists with the pricingId 1001
    When I update the pricing 0,3
    Then the new pricing 0,4 is displayed

  Scenario: delete pricing
    Given delete the pricing with the pricingId 1001
    When the the new pricingId is null
    Then an error message is displayed that say "Deletion failed! Please enter valid Station settings."


# User Story 8.3 - Update Pricing
  Scenario: update pricing rule with immediate effect
  As owner
  I want to update existing pricing rules
  so that price changes take effect immediately or at a scheduled time.
    Given the pricing rule with the pricingId 1001 is in use by active sessions
    When I update the pricingId 1001 to 0,4
    Then ongoing sessions continue with 0,3
    And new sessions use 0,4

  Scenario: schedule future pricing rule activation
    Given chargingPoints is 2
    When I update the pricing to 0,4
    Then all chargingPoints should be updated to 0,4

  Scenario: updating pricing multiple times a day
    Given I have updated the pricing to 0,4
    When I want to update it again to 0,5
    Then a pricing confirmation message is printed that says "Pricing updates successfully."
    And the new pricing 0,5 is displayed

    # User Story 8.4 - Delete / Deactivate Pricing

  Scenario: delete pricing rule successfully
    Given a pricing exists with the pricingId 1001
    And the pricing rule is not in use by active sessions
    When I request deletion of the pricing rule with the pricingId 1001
    Then the pricing rule with pricingId 1001 is removed from the pricing list
    And a pricing confirmation message is printed that says "Pricing rule removed successfully"

  Scenario: reject deletion when pricing rule is in use (active sessions exist)
    Given the pricing rule with the pricingId 1001 is in use by active sessions
    When I request deletion of the pricing rule with the pricingId 1001
    Then the system rejects the deletion
    And a pricing error message is printed that says "Deletion failed! Pricing rule is currently in use."

  Scenario: reject deletion when pricing rule does not exist
    Given no pricing rule exists with the pricingId 9999
    When I request deletion of the pricing rule with the pricingId 9999
    Then the system rejects the deletion
    And a pricing error message is printed that says "Deletion failed! Pricing rule not found."
