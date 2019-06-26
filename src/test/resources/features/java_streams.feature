Feature: Java Streams Test
  Background:
    Given a stream of integers [4,1,13,90,44,2,0]
    Given a list of employees
      | name | salary| isActive | departmentId |
      |Jack | 10000 | true      | 100 |
      | James | 25000 | true    | 100 |
      | Ram | 40000 | true | 100      |
      | Julia | 15000 | true | 200    |
      | Ramesh | 50309 | true | 200   |
      | Tom    | 30000 | false | 105  |
      | Alex | 14500 | true      | 100 |
      | Luke | 250000 | false    | 100 |
      | Novak | 56000 | true      | 400 |
      | Kevin | 45000 | true    | 100 |

  Scenario: test java streams for Integer
    Then the minimum is 0
  Scenario: test java streams for 3 distinct numbers
    Then the sorted three distinct numbers are [0,1,2]
  Scenario: get highest earning employees
    Then the top three earning employees are [Luke,Novak,Ramesh]
  Scenario: get highest earning active employees
    Then the top three active earning employees are [Novak,Ramesh,Kevin]
  Scenario: get highest earning active employees for a department
    Then the top active earning employees per department
    | 100 | Kevin |
    | 200 | Ramesh |
    | 400 | Novak |
  Scenario: get the count of active employees per department
    Then the active employee count per department
    | 100 | 5 |
    | 200 | 2 |
    | 400 | 1 |