Feature: Invoice Management
  As owner and customer
  I want to create, read, update, and delete invoice information
  so that I can track charging sessions, payments, and account balances reliably

  #User Story 9.1 - Create Invoices
  Scenario: Automatically create an invoice entry when a charging session ends
    Given a charging session has ended
    When the session result is received with:
      | kWh      |
      | duration |
      | mode     |
      | station  |
    Then an invoice record is created automatically

  Scenario: Apply the price valid at the start of the charging session
    Given prices may have changed during the day
    When the invoice is created
    Then the system applies the price valid at the start time of the charging session

  Scenario: Deduct charged amount from customer's prepaid balance
    Given the customer uses a prepaid system
    When the invoice is generated
    Then the charged amount is deducted from the customer’s available balance

  #User Story 9.2 - Read Invoices
  Scenario: View itemized invoice details
  As customer
  I want to view detailed invoice information
  so that I can verify charging sessions and resolve disputes
    Given I open an invoice
    When I expand the invoice details
    Then I see itemized rows containing:
      | Start time        |
      | End time          |
      | kWh charged       |
      | Price per kWh     |
      | Total cost        |
      | Applied pricing rule |

  Scenario: Download invoice PDF
    Given I click "Download PDF"
    When the request is made
    Then a correctly formatted PDF invoice is generated instantly
    And the PDF is available for immediate download

  Scenario: Flag invoice for correction
    Given I detect a billing error
    When I click "Flag for correction"
    Then the invoice is marked as "Needs Review"
    And the action is logged with reporter and timestamp
    And the invoice appears in the "Needs Review" queue

  #User Story 9.3 - Update Invoices
  Scenario: Correct and reissue an incorrect invoice
  As owner
  I want to correct and reissue an incorrect invoice
  so that billing errors can be fixed transparently.

    Given an invoice is flagged or selected for correction
    When I open the correction tool
    Then I can adjust the following fields:
      | kWh     |
      | price   |
      | duration |
    And I can generate a credit note and a corrected invoice

  Scenario: Send corrected invoice and credit note to customer
    Given a correction is saved
    When the process is completed
    Then the customer automatically receives the credit note and the corrected invoice via email

  Scenario: Mark original invoice as corrected
    Given the correction is processed
    When I view the customer’s invoice history
    Then the original invoice is shown as "Cancelled/Corrected"
    And a link to the new invoice version is displayed