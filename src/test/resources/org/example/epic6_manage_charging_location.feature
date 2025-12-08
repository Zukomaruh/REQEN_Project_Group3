Feature: Station Network Management
  As Owner
  I want to manage the Station Network
  so charging locations and charging stations, can be stored, updated retrieved and removed.

  # User Story 6.1 – create charging locations
  Scenario: create charging location with valid input
  As an owner
  I want to create a charging location
  so that the system stores a new place where stations can be installed.
    Given the owner is on the system main class.
    When the owner creates a charging location with the name "Simmering"
    And enters the address "Industriezeile 45, 1110 Wien"
    Then a charging Location with the name "Simmering" with an empty Charging Station List is created successfully
    And a confirmation message is printed that says "Charging Location created successfully"

  Scenario: create Charging Location with invalid input fails
    Given the owner is on the system main class.
    When the owner creates a charging location with the name ""
    And enters the address ""
    Then no charging location is created
    And an error message is printed that says "Invalid Input, Location not created"

  # User Story 6.2 – read Charging Locations
  Scenario: read charging locations
  As owner
  I want to read all charging locations
  so that I can access the stored network information
    Given these charging locations exist:
      | name                  | address                            | stations              |
      | "St Pölten"           | "Landstaße 42, 3100 St Pölten"     | [station1, station2]  |
      | "Wien Hauptbahnhof"   | "Favoritenstraße 51, 1100 Wien"    | []                    |
    When the system retrieves all charging locations
    Then the output looks like this:
    """
    ---
    locationID: <generated_ID>
    name: St Pölten
    address: Landstraße 42, 3100 St Pölten
    stations:
      station1
      station2
    ---
    locationID: <generated_ID>
    name: Wien Hauptbahnhof
    address: Favoritenstraße 51, 1100 Wien
    stations:
    ---
    """