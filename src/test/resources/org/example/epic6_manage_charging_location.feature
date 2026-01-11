Feature: Station Network Management
  As Owner
  I want to manage the Station Network
  so charging locations and charging stations, can be stored, updated retrieved and removed.

  # User Story 6.1 – create charging locations
  Scenario: create charging location with valid input
  As an owner
  I want to create a charging location
  so that the system stores a new place where stations can be installed.
    Given owner is on the system main class.
    When the owner creates a charging location with the name "Simmering"
    And enters the address "Industriezeile 45, 1110 Wien"
    Then a charging Location with the name "Simmering" with an empty Charging Station List is created successfully
    And a confirmation is printed that says "Charging Location created successfully"

  Scenario: create Charging Location with invalid input fails
    Given owner is on the system main class.
    When the owner creates a charging location with the name ""
    And enters the address ""
    Then no charging location is created
    And an error is printed that says "Invalid Input, Location not created"

  # User Story 6.2 – read Charging Locations
  Scenario: read charging locations
  As owner
  I want to read all charging locations
  so that I can access the stored network information
    Given these charging locations exist:
      | name                  | address                            | stations              |
      | "St Pölten"           | "Landstraße 42, 3100 St Pölten"    | [station1, station2]  |
      | "Wien Hauptbahnhof"   | "Favoritenstraße 51, 1100 Wien"    | []                    |
    When the system retrieves all charging locations
    Then the console shows this:
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


    # User Story 6.3 Update Locations
  Scenario: Update existing Charging Location with valid value
  As an owner
  I want to update a charging station
  so that its information remains correct and up to date.
    Given a location exists with the following values:
      | name                  | address                            | stations              |
      | "Südbahnhof"          | "Landstraße 42, 3100 St Pölten"    | station01, station02  |
    When the name is updated to "Hauptbahnhof"
    And the Locations information is requested
    Then it contains "Hauptbahnhof"
    And does not contain "Südbahnhof"



    # User Story 6.4 – delete Charging Locations

  Scenario Outline: Delete charging location with no stations succeeds
    Given owner is on the system main class.
    And a charging location exists with id <id>, name "<name>" and address "<address>" and has no stations
    When the owner requests deletion of the charging location with id <id>
    Then the system deletes the charging location
    And when all charging locations are retrieved, the location with id <id> is not included

    Examples:
      | id   | name        | address                    |
      | 1001 | Linz Center | Hauptplatz 1, 4020 Linz     |
      | 1002 | Wien West   | Europaplatz 2, 1150 Wien   |

  Scenario Outline: Delete charging location with stations is rejected
    Given owner is on the system main class.
    And a charging location exists with id <id>, name "<name>" and address "<address>" and has stations
    When the owner requests deletion of the charging location with id <id>
    Then the system rejects the deletion and returns a system notification "Deletion rejected: location contains charging stations"
    And when all charging locations are retrieved, the location with id <id> is included

    Examples:
      | id   | name       | address                          |
      | 2001 | Graz Süd   | Münzgrabenstraße 90, 8010 Graz   |
      | 2002 | Salzburg   | Alpenstraße 99, 5020 Salzburg   |

  Scenario Outline: Delete non-existing charging location is rejected
    Given owner is on the system main class.
    When the owner requests deletion of the charging location with id <id>
    Then the system rejects the deletion and returns a system notification "Deletion rejected: location not found"
    And when all charging locations are retrieved, the location with id <id> is not included

    Examples:
      | id   |
      | 9999 |
      | 8888 |

