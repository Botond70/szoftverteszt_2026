Feature: Communities page language filter order

  Scenario: Language filter shows English first when UI language is English
    Given the Communities page is opened
    When the user opens the language filter dropdown
    Then English is the first option in the language filter list
