Feature: Account Management
  The application allows customers to register, view, and update their account credentials.

  # User Story 1.1 - Create Account
  Scenario: Account is created with valid credentials
    Given I want to create a new Account with valid user credentials
    When I create a new Account
    Then the account is successfully created
    And the user credentials are stored in the system

  Scenario: Creation fails with invalid user credentials
    Given I want to create a new Account with invalid user credentials
    When I create a new Account
    Then an error message is displayed

  # User Story 1.2 - View Credentials
  Scenario: Credentials are visible to customer
    Given I already have an Account
    When I request the Account information
    Then I can see my user credentials

  Scenario: Only latest credentials are shown
    Given I updated my user credentials
    When I request the Account information
    Then I see only the latest user credentials
