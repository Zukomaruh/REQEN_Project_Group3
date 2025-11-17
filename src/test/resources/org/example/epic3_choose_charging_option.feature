Feature: Charging Station Information
  Customers can read detailed information about charging stations to choose the best option.

  # User Story 3.1 – View Station Information
  Scenario: Show stations on charging map
    Given I open the charging map
    When I search for charging stations
    Then I can see all relevant information for each available charging station

  Scenario: Station information updates
    Given charging station data changes
    When I refresh or reload the view
    Then the displayed information is updated

  Scenario: View station details
    Given I view a charging station’s details
    When I open its info card
    Then all current details are displayed
