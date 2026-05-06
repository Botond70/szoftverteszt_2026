Feature: Communities page Join buttons

  Scenario: User can see the communities page
    Given the Home page is opened
    When there are multiple communities shown in the grid
    Then in each grid cell the join button height is consistent