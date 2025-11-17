Feature: Station Network Management
  Owners can manage locations and stations so customers can find charging places.

  # User Story 6.1 – Add Charging Locations
  Scenario: Add valid location
    Given the location has a valid form
    When the location on map is less than 30s away
    Then it is navigable

  Scenario: Invalid location input
    Given the input is invalid
    When I want to add the location
    Then an error message is displayed

  Scenario: Location details match input
    Given there is an input field
    When a location is added
    Then the location details match the input

  # User Story 6.2 – Add Charging Stations
  Scenario: Valid station setup
    Given a charging station was added
    When the setup is valid
    Then the station is listed active and bookable

  Scenario: Serial number is unique
    Given a charging station was added
    When a serial number is added
    Then that serial number cannot be used for another station

  Scenario: Confirmation for active station
    Given a charging station was added
    When the station is active
    Then a confirmation message is displayed

  # User Story 6.3 – View Charging Locations
  Scenario: Closest station color-coded
    Given there are multiple charging locations with different distances
    When one station is closer than another
    Then it is color coded accordingly

  Scenario: Apply distance filter
    Given there are multiple charging stations with different distances
    When distance sorting filter with target distance is applied
    Then stations within the target distance are displayed

  Scenario: View station details
    Given the charging stations contain information
    When chosen
    Then address distance and prices are displayed
