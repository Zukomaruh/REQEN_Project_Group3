Feature: Account Management
  As a customer, I want to manage an account so that I can easily handle my payment and preferences.

  # User Story 1.1 - create user credentials
  Scenario: set Account with valid user credentials
  As a customer
  I want to set my user credentials when I create an account
  so that I can create an Account successfully.
    Given The customer is on the systems main class
    When the customer registers a new account with the username "Max Mustermann"
    And enters the email "max.musterman@email.com"
    And enters the password "testpassword123"
    And the role is set to "CUSTOMER"
    # role must be CUSTOMER or OWNER
    Then an Account with the username "Max Mustermann" is successfully created.
    And a confirmation message is printed that says "Your account has been created"

  Scenario: set Account with invalid user credentials
    Given the customer is on the system main class
    When the customer registers a new account with the username "123"
    And enters the email "foo"
    And enters the password "lessthan12"
    Then the Account is not created
    And an error message is printed that says "Please enter valid username, email or password"

  # User Story 1.2 - read user credentials
  Scenario: Credentials are visible to customer
  As a customer
  I want to read my user credentials
  so that I can check if they are still up to date.
    Given An Account exists with the following user credentials:
      | Field      | Value                    |
      | username   | Fiona Fantasie           |
      | email      | fiona.fantasie@email.com |
      | role       | CUSTOMER                 |
      | status     | active                   |
    When the Account information is requested
    Then the output is:
    """
    userID: <generated_ID>
    username: Fiona Fantasie
    email: fiona.fantasie@email.com
    role: CUSTOMER
    status: active
    """

  Scenario: Only latest credentials are shown
    Given There exists an Account with the following user credentials:
      | Field      | Value                      |
      | username   | Johannes Joghurt           |
      | email      | johannes.joghurt@email.com |
      | role       | CUSTOMER                   |
      | status     | active                     |
    When the email is updated to "jo.jo@email.com"
    And the Account information is requested
    Then the updated output is:
    """
    userID: <generated_ID>
    username: Johannes Joghurt
    email: jo.jo@email.com
    role: CUSTOMER
    status: active
    """

  # User Story 1.3 Update Account
  Scenario: Update Account with valid value
  As a customer
  I want to update my user credential
  so that I can keep them up to date.
    Given there exists an Account with the userID 1001 and the following values:
      | Field      | Value                      |
      | username   | Hans Hubert                |
      | email      | hans.hubi@email.com        |
      | role       | CUSTOMER                   |
      | password   | hansiHuberti2344           |
    When the "username" is updated to "Hans Hubertus"
    And the updated value is requested
    Then the output contains the value "Hans Hubertus"
