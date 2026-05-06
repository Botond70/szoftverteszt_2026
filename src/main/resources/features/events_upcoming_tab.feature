Feature: Events page default tab selection

  Scenario: Upcoming events tab is active by default on the Events page
    Given the Events page is opened
    Then the Upcoming events tab is active
    And the Upcoming events tab shows a non-negative event count
