Feature: Charging Process Management
  As customer
  I want to start and monitor a charging process
  so that I can charge my car and know when it has finished charging.

  # User Story 4.1 – Start Charging Process
  Scenario: start charging process and charge battery to target percentage
  As customer
  I want to start a charging process
  so that my battery is charged to the target percentage.
    Given the battery level is 40 percent
    And the target battery level is 80 percent
    And the car is connected to a charging station with the status AVAILABLE
    When the customer starts a charging process
    Then the battery level is 80 percent
    And the charging status is "COMPLETED"

  Scenario: car starts immediately when battery is at least 20 percent
  As customer
  I want to start my car
  so that I can drive when the battery is not empty.
    Given the battery level is 25 percent
    And the battery is not empty
    When the customer starts the car
    Then the car starts immediately

  Scenario: driven distance matches expected range with 5 percent tolerance
  As customer
  I want the driven distance to match the expected range
  so that I can rely on the range information of my car.
    Given the target battery level is 80 percent
    And the expected range at this level is 320 kilometers
    When the charging process has finished
    And the driven distance is calculated
    Then the driven distance matches the expected range with a tolerance of 5 percent

  # User Story 4.2 – Read Charging Status
  Scenario: read charging status returns percentage, power and time to full
  As customer
  I want to read the charging information
  so that I know the current status of my charging process.
    Given an active charging process exists for the customer with the userID 2001 at the station with the id 1010 and these values:
      | batteryPercentage | powerKW | timeToFullMinutes |
      | 40                | 11      | 30                |
    When the customer requests the charging information
    Then the charging information looks like this:
    """
    ---
    customerID: 2001
    stationID: 1010
    batteryPercentage: 40
    powerKW: 11
    timeToFullMinutes: 30
    status: CHARGING
    ---
    """

  Scenario: charging percentage only increases while charging
  As customer
  I want the charging percentage to increase over time
  so that the charging progress is consistent.
    Given an active charging process for the station with the id 1010 with this percentage history:
      | percentage |
      | 20         |
      | 40         |
      | 60         |
    When the system validates the charging percentage updates
    Then each new percentage is greater than or equal to the previous percentage

  Scenario: charging completes at 100 percent and stops
  As customer
  I want to know when the charging has finished
  so that I can disconnect my car.
    Given an active charging process for the station with the id 1010 and a battery level of 99 percent
    When the charging percentage is updated to 100 percent
    Then a completion message is returned that says "Charging completed"
    And the charging status is "COMPLETED"

  # User Story 4.3 – Update Charging Station Status
  Scenario: station status changes from AVAILABLE to CHARGING when process starts
  As the system
  I want to update the status of a charging station when a process starts
  so that the station network reflects the correct availability.
    Given a charging station exists with the id 1010 and the status AVAILABLE
    When a charging process starts at the station with the id 1010
    Then the system updates the station status to CHARGING

  Scenario: station status changes from CHARGING to AVAILABLE when process finishes
  As the system
  I want to update the status of a charging station when a process finishes
  so that the station network reflects the correct availability.
    Given a charging station exists with the id 1010 and the status CHARGING
    When the charging process at the station with the id 1010 finishes
    Then the system updates the station status to AVAILABLE

  Scenario Outline: charging request is rejected when station is not available
  As the system
  I want to reject charging requests on unavailable stations
  so that the stored station status remains correct.
    Given a charging station exists with the id 1011 and the status <status>
    When a charging process is requested at the station with the id 1011
    Then the system rejects the charging request
    And the station status remains <status>

    Examples:
      | status      |
      | MAINTENANCE |
      | OFFLINE     |
