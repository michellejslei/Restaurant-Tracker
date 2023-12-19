# Restaurant Recording Application

## About
A recording application for restaurants. It lets the user enter information about 
each restaurant they visit, including the price range, cuisine, what they ordered, 
and their rating. Possible features include listing all restaurants of a certain 
cuisine or within a certain price range. (X could be a restaurant, a dish that was 
ordered, etc. Y could be someone's collection of restaurants, a restaurant and the 
list of dishes ordered, etc.)

**Users**

- People who would like to keep track of the restaurants they have visited in Vancouver

**Why am I interested?**

I am a foodie and I like to be able to record the places I have eaten in the past.
Being able to look back on a list of options help me choose a place to eat when
I am not feeling inspired, and it also allows me to easily give recommendations
on restaurants for others.

##

## User Stories
- As a user, I want to be able to add a restaurant to my collection
- As a user, I want to be able to view a list of restaurant names in my collection
- As a user, I want to be able to select a restaurant in my collection and view the restaurant in detail
- As a user, I want to be able to rate a restaurant in my collection a scale of one to five stars
- As a user, I want to be able to modify the type of cuisine for a given restaurant in my collection
- As a user, I want to be able to modify the meal type for a given restaurant in my collection
- As a user, I want to be able to save my restaurant tracker from the application menu (if I so choose)
- As a user, when I start my application, I want to be given the *option* to load my tracker from file

## Instructions for Grader
- You can generate the first required action related to the user story "adding a restaurant to my collection" by clicking on the "add new restaurant" button after loading or creating a new tracker
- You can generate the second required action related to the user story "select a restaurant in my collection and view restaurant details" by selecting a restaurant name from the *My Restaurants!* page and selecting the "view" button
- You can locate the visual component by viewing a restaurant in detail and changing the rating. The rating icon next to the restaurant name header will update based on the rating from 1-5 stars
- You can save the state of my application by selecting the "save" button on the *My Restaurants!* page on the bottom left hand corner
- You can reload the state of my application by selecting the "load tracker" button when application is first run

## Phase 4: Task 2
Mon Nov 27 15:10:45 PST 2023
Event log cleared.

Mon Nov 27 15:10:47 PST 2023
Added restaurant: test

Mon Nov 27 15:10:50 PST 2023
Set test's rating to: 2

Mon Nov 27 15:10:59 PST 2023
Set test's cuisine to: American

Mon Nov 27 15:11:06 PST 2023
Added dish: test1 to test

## Phase 4: Task 3
To improve the design of this project, I would refactor the ui classes so that
RestaurantListPage, RestaurantInfoPage and DishListPage all extend
LaunchPage. By doing so, the three pages would inherit LaunchPage's fields
and don't all need individual fields of RestaurantTracker and JsonWriter. Additionally,
the shared functionality across pages (i.e. quit button) can also be inherited
so that there is less room for error - if one field/method is modified, the other classes
adopt these changes as well without having to manually update.