# Meet & Eat
UvA Programmeerproject: Android Application<br>
January 2016<br>
Tirza Soute

This application will help reduce the amount of food that is thrown away each year and allow users to meet new people over dinner.

### Problem 
On average, each person in The Netherlands throws away 135 kilograms of food each year, which is a waste. Therefore, I will create an application that prevents there from being so much wasted food.

### Available features to solve the problem 
Users will be able to indicate whether they will be cooking or whether they would like to join someone else's dinner. This will results in less food being thrown away or, ideally, no  food being thrown away at all.

### Sketch of application
<img src="/doc/loginSketch.png" height="400">
<img src="/doc/indicationSketch.png" height="400">
<img src="/doc/cookingSketch.png" height="400"><br>
<img src="/doc/joiningSketch.png" height="400">
<img src="/doc/resultsSketch.png" height="400">
<img src="/doc/dinnerSketch.png" height="400">

### Separate parts of the application and how these should work together
The home screen will be a login screen. After that, there will be a screen where the user can indicate whether they will be cooking or joining. 
If the user is cooking, they have to specify what they will be cooking, for how many people they will provide dinner and in what area they live. 
If the user is joining, they have to specify in which area they live and will then be provided with a list of dinners people are making in that area. I might implement an option to search their area based on some dinner preference, such as pasta or fish. I might also implement the option to read information about the dinner host so that the user will not be joining a complete stranger for dinner.

### External components needed 
Firebase to keep track of the food users will be making and to keep track of the amount of people that have already indicated that they want to join someone's dinner. Optionally, also to find some information about a dinner host or guest.

### Technical problems or limitations that could arise and how to overcome these
I want to integrate Firebase's Google login, which might be difficult since I have not done this before. I can use online tutorials to figure out how this works exactly.

### Review of similar applications or visualisations in terms of features and technical aspects
