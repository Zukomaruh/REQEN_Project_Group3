Feature: Customer Invoice Management
  Customers can review their invoices to track their expenses.

  # User Story 5.1 – View Invoice Balance
  Scenario: Display all invoices
    Given I am on the invoice page
    Then I can see all my invoices

  Scenario: No invoices available
    Given I have no invoices
    Then no invoices are depicted

  Scenario: Multiple invoices displayed
    Given there are multiple invoices
    Then I can see all of them

  # User Story 5.2 – Filter/Sort Invoices
  Scenario: Filter invoices by criteria
    Given the invoice is valid
    When a filter is applied
    Then only matching invoices are shown

  Scenario: Filter limit reached
    Given a limit to applying filters is set
    When the limit is reached
    Then I am blocked from selecting more filters

  Scenario: Show error when filter limit is reached
    Given a limit to applying filters is set
    When the limit is reached
    Then an error message is displayed
