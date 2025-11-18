Feature: Charging Station Information
  Customers can read detailed information about charging stations to choose the best option.

  # User Story 3.1 – View Station Information
  Scenario: Show Charging Locations
    Given I have an account
    When I request the information for Charging Locations
    Then I can see all relevant information for each Charging Location

  Scenario: Station information updates
    Given charging station data changes
    When I request the information for Charging Locations again
    Then the displayed information is updated

  Scenario: View station details
    Given I have chosen a Charging Location
    When I request information for the Locations Charging Stations
    Then all current Charging Station details are displayed
