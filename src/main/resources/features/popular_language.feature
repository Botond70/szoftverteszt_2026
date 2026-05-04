Feature: Home page Popular events language

  Scenario: Popular events list show English language events when UI language is English
    Given the Home page is opened
    When the language selector shows "Eng"
    Then all Popular events are marked as English
