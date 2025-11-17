Feature: Account Management
  The application allows customers to register, view, and update their account credentials.

  # User Story 1.1 - Create Account
  Scenario: Account is created with valid credentials
    Given I am on the registration page
    When I enter valid credentials
    And I submit the registration form
    Then my account is created

  Scenario: User can log in after registration
    Given I have registered successfully
    When I log in with my user credentials
    Then I gain access to the system

  Scenario: Registration fails with invalid credentials
    Given I am on the registration page
    When I enter invalid user credentials
    And I submit the registration form
    Then an error message is displayed

  # User Story 1.2 - View Credentials
  Scenario: Credentials are visible on customer view
    Given I am on the customers view
    Then I can see my credentials

  Scenario: User can view profile
    Given I am logged in
    When I open my profile
    Then my credentials are displayed

  Scenario: Only latest credentials are shown
    Given I am on the customer page
    Then I see only the latest credentials

  # User Story 1.3 - Update Credentials
  Scenario: Edit user credentials
    Given I am on my profile page
    When I edit my user credentials
    Then the updated credentials are saved

  Scenario: Store only the latest credentials
    Given I changed my user credentials
    Then only the latest credentials are stored

  Scenario: Edit credentials again
    Given I changed my user credentials
    When I edit them again
    Then the new changes are saved
