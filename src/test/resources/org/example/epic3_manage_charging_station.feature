Feature: Charging Station
  As customer
  I want to read information about the charging stations
  so that I can make an informed decision
  # User Story X.X - create Charging Station
  Scenario: create charging station with location ID
  As an owner
  I want to create a charging station
  so that I can add them to my charging location.
    Given a charging location exists with locationId 1001
    When the owner creates a charging station with the name "station01"
    And the locationID 1001
    And the type "AC"
    And the capacity 145
    And the pricing of 0,30
    Then a new charging station with the name "station01", an unique stationID and the status AVAILABLE is created.
    And the station is added to the List of the location with locationId 1001
    And a confirmation message is printed that says "Charging station created successfully"

  Scenario: create charging station without location ID
    Given the owner is on the system
    When the owner creates a charging station with the name "station02"
    And the type AC
    And the capacity 145
    And the pricing of 0,30
    Then a new charging station with the name "station01", an unique stationID and the status AVAILABLE is created.
    And a confirmation message is printed that says "Charging station created successfully, it was not added to a location"

  Scenario: create charging station with invalid input fails
    Given a charging location exists with locationId 1002
    When the owner creates a charging station with the name ""
    And the locationID 1002
    And the type AC
    And the capacity 30000
    And the pricing of 4000
    Then no charging station is created
    And an error massage is printed that says "Creation faild! Please enter valid Station settings"

  # User Story 3.1 – read Charging Station
  Scenario: read all charging Station information for one location
  As a customer
  I want to read information about the charging stations
  so that I can make an informed decision.
    Given these charging location exist:
      | name                  | address                            | stations                    |
      | "Westbahnhof"         | "Europaplatz 3, 1150 Wien"         | [station1001, station1002]  |
    And these stations exist:
      | stationName        | type | capacity | status       | pricing   |
      | "station1001"      | AC   | 150      | AVAILABLE    | 0,55      |
      | "station1002"      | DC   | 50       | CHARGING     | 0,35      |
    When the customer requests the charging stations information
    Then the output looks like this:
    """
    ---
    name: station1001
    location: Westbahnhof
    type: AC
    capacity: 150 kWh
    status: AVAILABLE
    price 0,55 EUR/kWh
    ---
    name: station1002
    location: Westbahnhof
    type: DC
    capacity: 50 kWh
    status: CHARGING
    price 0,35 EUR/kWh
    ---
    """

  Scenario: Only latest charging station information is shown
    Given these charging location exist:
      | name                  | address                            | stations      |
      | "Döbling"             | "Armbrustergasse 35, 1190 Wien"    | [station101]  |
    And these stations exist:
      | stationName       | type | capacity | status       | pricing   |
      | "station101"      | DC   | 100      | AVAILABLE    | 0,45      |
    When the pricing of the charging station "station101" is updated to 0,50
    And the information is requested
    Then the output looks like this:
    """
    ---
    name: station101
    location: Döbling
    type: DC
    capacity: 100 kWh
    status: AVAILABLE
    price 0,50 EUR/kWh
    ---
    """
