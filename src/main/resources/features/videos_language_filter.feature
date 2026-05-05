Feature: Videos page language filters

  Scenario: Language filter defaults to English when UI language is English
    Given the Home page is opened
    When the language selector shows "Eng"
    And the Videos page is opened
    Then the Language filter shows "English"

  Scenario: Popular Talks show only English when UI language is English
    Given the Home page is opened
    When the language selector shows "Eng"
    And the Videos page is opened
    Then all Popular Talks are marked as English

