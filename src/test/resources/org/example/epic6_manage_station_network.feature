Feature: Station Network Management
  Owners can manage locations and stations so customers can find charging places.

  # User Story 6.1 – Add Charging Locations
  Scenario: Add valid charging location
    Given the charging location has a valid form
    When I add the Charging Location to the System
    Then it is added successfully

  Scenario: add invalid Charging Location fails
    Given the charging location input is invalid
    When I want to add the charging location
    Then an Error message is displayed

  # User Story 6.2 – Add Charging Stations
  Scenario: Valid station setup
    Given I want to add an Charging Station to an location
    When the input is valid
    Then the charging station is listed active and bookable to the charging location

  Scenario: Serial number is unique
    Given a charging station was added
    When a serial number is added
    Then that serial number cannot be used for another charging station

  Scenario: Confirmation for active station
    Given a charging station was added
    When the station is active
    Then a confirmation message is displayed

  # User Story 6.3 – View Charging Locations
  Scenario: Closest station color-coded
    Given there are multiple charging locations with different distances
    When I request the charging location information
    Then the locations are sorted with the closest ones on the top

  Scenario: Apply distance filter
    Given there are multiple charging locations with different distances
    When distance sorting filter with target distance is applied
    Then locations within the target distance are displayed

  Scenario: View station details
    Given I choose a Charging location
    When I request the charging stations information
    Then the information is displayed
