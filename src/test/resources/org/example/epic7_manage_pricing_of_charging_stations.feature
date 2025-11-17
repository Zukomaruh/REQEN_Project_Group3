Feature: Pricing Management
  Owners can set and review pricing for charging stations.

  # User Story 7.1 – Set Pricing
  Scenario: Set price per station
    Given I am logged in as an owner
    When I open the pricing management page
    Then I can set the price per kWh for each station

  Scenario: Save updated price
    Given I have updated a price
    When I save the changes
    Then the new price is stored and displayed

  Scenario: Customers see updated prices
    Given a pricing update occurs
    When customers view charging station prices
    Then they see the latest pricing immediately

  # User Story 7.2 – Read Pricing
  Scenario: Display price of a station
    Given I open the pricing overview
    When I select a charging station
    Then its current price per kWh is displayed

  Scenario: Sort by price
    Given multiple charging stations are available
    When I sort by price
    Then the list is shown in ascending or descending order

  Scenario: Latest price displayed on refresh
    Given pricing data is outdated
    When I refresh the page
    Then the latest information is retrieved
