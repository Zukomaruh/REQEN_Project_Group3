Feature: Owner Invoice Management
  Owners can review, filter, and inspect all customer invoices.

  # User Story 9.1 – Read All Invoices
  Scenario: Display invoice list
    Given I am logged in as the owner
    When I open the invoice management view
    Then I can see a list of all customer invoices with date customer ID and total amount

  Scenario: Apply invoice filters
    Given I apply filters
    When I select parameters such as date range or payment status
    Then only matching invoices are shown

  Scenario: Access archived invoices
    Given invoices are archived
    When I scroll or load more entries
    Then older invoices remain accessible

  # User Story 9.2 – Detailed Invoice Information
  Scenario: Display invoice details
    Given I open an invoice
    When I expand it
    Then I see itemized details including session duration energy charged price per kWh and total cost

  Scenario: Download invoice PDF
    Given I am reviewing an invoice
    When I click "download"
    Then a detailed PDF version is generated

  Scenario: Flag invoice for correction
    Given a billing issue is detected
    When I click "flag for correction"
    Then the system logs the invoice for further investigation
