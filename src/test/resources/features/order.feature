Feature: Get order details
  Background:
    Given order service request is configured
  Scenario: client calls web service to get order by SKU
    When this client retrieves order by sku AB-15426
    Then the order service status code is 200
    And order response includes the following
      | sku | AB-15426 |
    And response body has a valid order schema

  Scenario Outline: order sku checks
    When this client retrieves order by sku <sku>
    Then the order service status code is <status>
    And response body has a valid <schema> schema

    Examples:
      | sku     | status  | schema |
      | AB-2123 | 200     | order  |
      | FF-8920 | 200     | order  |
      | FF-322s | 400     | error  |

  Scenario: client calls web service to get order with invalid SKU
    When this client retrieves order by sku Ac-15426
    Then the order service status code is 400
    And order response includes the following
      | code | 100 |
      | type | error |
    And response body has a valid error schema