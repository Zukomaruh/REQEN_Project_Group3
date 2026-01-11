Feature: Invoice Management
  As a customer
  I want to read and sort my invoice records
  so I know how much I spent.

  # User Story 5.1 - Read Invoices
  Scenario: Customer reads their invoice list
    Given a "Customer" with ID 1001 exists
    And the customer has the following "Invoices":
      | stationName | chargingMode | kWh | duration | pricePerKWh | totalCost | startTime           | status |
      | Station A   | AC           | 10  | 60       | 0.50        | 5.00      | 2024-05-01T10:00:00 | PAID   |
      | Station B   | DC           | 50  | 30       | 0.80        | 40.00     | 2024-05-05T14:00:00 | PAID   |
    When the customer requests the invoice list
    Then the list contains 2 invoices
    And the first invoice is from "Station A" with total cost 5.00

  # User Story 5.2 - Sort Invoices
  # Edge Case: Sortierung prüfen
  Scenario: Invoices are displayed sorted by date (oldest first)
    Given a "Customer" with ID 1001 exists
    And the customer has the following "Invoices":
      | stationName | chargingMode | kWh | duration | pricePerKWh | totalCost | startTime           | status |
      | Station New | DC           | 20  | 15       | 0.90        | 18.00     | 2024-06-01T12:00:00 | UNPAID |
      | Station Old | AC           | 10  | 60       | 0.50        | 5.00      | 2024-01-01T08:00:00 | PAID   |
    When the customer requests the invoice list
    Then the list contains 2 invoices
    And the invoices are sorted by "Start Time" (oldest first)
    And the first invoice is from "Station Old" starting at "2024-01-01T08:00:00"

  # Edge Case: Leere Liste (US 5.1)
  Scenario: Customer has no invoices
    Given a "Customer" with ID 9999 exists
    And the customer has no invoices
    When the customer requests the invoice list
    Then the system returns an empty list
    And a message "No invoices found" is displayed

  Scenario: Owner deletes an invoice older than 7 years
    Given a "Customer" with ID 1001 exists
    And the customer has the following "Invoices":
      | stationName | chargingMode | kWh | duration | pricePerKWh | totalCost | startTime            | status |
      | Station Old | AC           | 10  | 60       | 0.50        | 5.00      | 2017-01-01T08:00:00  | PAID   |
    When the owner deletes the invoice
    Then It gets deleted

  Scenario: Owner cannot delete an invoice that is only 6 years old
    Given a "Customer" with ID 1001 exists
    And the customer has the following "Invoices":
      | stationName | chargingMode | kWh | duration | pricePerKWh | totalCost | startTime            | status |
      | Station New | DC           | 20  | 30       | 0.80        | 16.00     | 2020-01-01T08:00:00  | PAID   |
    When the owner deletes it.
    Then the Invoice does not get deleted and an error is printed.