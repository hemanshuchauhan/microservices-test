Feature: Get people by ID
  Scenario: client calls web service to get people by ID
    When this client retrieves people by id 1
    Then the people service status code is 200
    And the people response body has a valid people schema
    And people response contains the following
      | name 	 		| Luke Skywalker |
      | birth_year      | 19BBY			|
