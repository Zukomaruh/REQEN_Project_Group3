Feature: Realtime Charging Station Monitoring
  Owners can monitor station status and capacity in real-time.

  # User Story 8.1 – Read Station Status
  Scenario: Realtime status update
    Given I am viewing the monitoring dashboard
    When a charging station's status changes
    Then the update is shown in real-time

  Scenario: Warning when station is broken
    Given I am viewing the monitoring dashboard
    When a station is broken
    Then I get a warning about which station needs to be fixed

  Scenario: Operational state visible
    Given I open a charging station’s details
    When I view its info card
    Then I can see its operational state

  # User Story 8.2 – Read Station Capacity
  Scenario: Display station capacity
    Given I am on the monitoring dashboard
    When I select a charging station
    Then its maximum and current capacity are displayed

  Scenario: Realtime capacity update
    Given a charging station is in use
    When its power output changes
    Then the displayed capacity updates in real-time

  Scenario: Export capacity report
    Given I export data
    When I choose a time range
    Then the system provides the historical capacity report
