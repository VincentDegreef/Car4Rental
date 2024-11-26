Feature: Cars

    Scenario Outline: Get all cars when there are no cars
        Given I have no cars
        When I get all cars
        Then I should get an empty list

    Scenario Outline: Get all cars when there are cars
        Given I have cars
        When I get all cars2
        Then I should get a list of cars

    Scenario Outline: Add Car when car is added
        Given No cars
        When I add a car
        Then The car should be added

    Scenario Outline: Delete Car when car is deleted
        Given I have a car
        When I delete a car
        Then The car should be deleted
