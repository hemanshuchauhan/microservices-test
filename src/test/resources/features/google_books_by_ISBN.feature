Feature: Get book by ISBN
  Background:
    Given service request timeout is set
  Scenario: client calls web service to get a book by its ISBN
    Given a book exists with an isbn of 9781451648546
    Given the client sets headers to:
      | UUID | AB123-FDSS |
      | token | my-mock-token |
      | Accept | application/json |
      | Content-Type | application/json |
    And the client has the following cookies set:
      | Cookie1 | my-cookie-one |
      | Cookie2 | my-cookie-two |
    When the client retrieves the book by isbn
    Then the book service status code is 200
    And book response includes the following
      | totalItems 	 		| 1 					|
      | kind                | books#volumes			|
    And book response includes the following in any order
      | items.volumeInfo.title 					| Steve Jobs			|
      | items.volumeInfo.publisher 				| Simon and Schuster	|
      | items.volumeInfo.pageCount 				| 630					|
