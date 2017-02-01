## Description
<img src="/doc/searchScreenshot.png" width="350">

Meet & Eat is an Android application that allows users to join other users' dinners. The idea behind this is that less food will go to waste. Users can also register what they're going to cook so that other users can join their dinner. If users add their own dinner, they automatically search for recipes given a food using the Spoonacular API.

## Technical design

### High-level overview

The following graph shows how all the parts of my application are connected. Since I used fragments, <i>MainActivity</i> is the only activity. All the other 'activities' are fragments. <i>MainActivity</i> creates the Navigation Drawer and replaces the current fragment when an item in the Navigation Drawer is clicked. This activity uses <i>DrawerAdapter</i> to show the navigation drawer items with an icon, which is why <i>DrawerItem</i> is necessary.

<i>MainFragment</i> is the home screen of the application, which is where the user indicates whether they want to cook or join a dinner tonight.

If the user wants to join a dinner, they are sent to <i>JoinFragment</i>, where they write down the area in which they want to have dinner tonight. Once they click the search button, they are sent to <i>DinnerResultFragment</i>. This fragment uses <i>DatabaseHandler</i> to show their search results and the results are displayed in a ListView using <i>DinnerAdapter</i>. The user can join a dinner if they are logged in, in which case the guest list of a dinner is updated using <i>DatabaseHandler</i>.

If the user wants to cook a dinner, they are sent to <i>CookFragment</i>, where they indicate what they want to cook, how many people can join their dinner, what area they're in and the date of their dinner. The date of their dinner is picked using <i>DateSelector</i> and the time of their dinner is picked using <i>TimeSelector</i>. Once they hit the 'get recipe' button, they are sent to <i>RecipeResultFragment</i>. This fragment shows recipes that were retrieved using <i>HttpRequestHandler</i>, which reads a web page (from the Spoonacular API). An ArrayList of <i>Dinner</i> objects is created using <i>DinnerAsyncTask</i> and <i>InfoAsyncTask</i> from this web page. The results are displayed in a ListView using <i>RecipeAdapter</i>. The user can add the recipe they are going to cook using <i>DatabaseHandler</i>. 

Using the Navigation Drawer, the user can also navigate to <i>SignUpFragment<i>, which is where the user can create an account using <i>DatabaseHandler</i>. The user can also indicate that they already have an account, in which case they are sent to <i>SignInFragment</i>. In this fragment, the user can log in using <i>DatabaseHandler</i>.

The user can also navigate to <i>HostListFragment</i>, where they can view the dinners that they are hosting. These dinners are retrieved using <i>DatabaseHandler</i>. In this fragment, the user can also remove the dinner they're hosting, which is done using <i>DatabaseHandler</i>. Furthermore, the user can edit a dinner they're hosting. If this is the case, they are sent to <i>EditDinnerFragment</i>. In this fragment, they can edit the title, ingredients and date and time of their dinner using <i>DatabaseHandler</i>.

Finally, the user can navigate to <i>JoinListFragment</i>, where the dinners that they've joined are displayed. These dinners are retrieved using <i>DatabaseHandler</i>. This fragment also allows the users to remove themselves from a dinner, which is done using <i>DatabaseHandler</i> as well.

<img src="/doc/graph.png">

- MainActivity -> DrawerAdapter (DrawerItem)

- MainFragment
  - CookFragment (DateSelector, TimeSelector) -> HttpRequestHandler -> DinnerAsyncTask (Dinner) -> InfoAsyncTask (Dinner) -> RecipeResultFragment (RecipeAdapter) -> DatabaseHandler
  - JoinFragment -> DatabaseHandler -> DinnerResultFragment (DinnerAdapter) -> DatabaseHandler
- SignUpFragment -> DatabaseHandler (User) or SignInFragment -> DatabaseHandler (User)
- HostListFragment (SavedAdapter) -> DatabaseHandler -> EditDinnerFragment (DateSelector, TimeSelector) -> DatabaseHandler
- JoinListFragment (SavedAdapter) -> DatabaseHandler

### High-level overview: to navigate and help understand total of code
### Detailed: describe modules/classes and how they relate

## Challenges during development: important changes from DESIGN document
Originally, I was going to use Google authentication to sign in and sign up to Firebase. However, after I started using fragments, this was no longer possible, since Google authentication requires an activity for certain methods that have to be used. Therefore, I decided to use e-mail verification instead. 

I was also going to use the type of cuisine as a parameter to search for dinners, but since I am using Firebase to retrieve dinners in an area, using <i>orderByChild(area)</i>, this was not possible. This is because you cannot use two search parameters in Firebase. Originally, I was not going to use an API either, but since I did and the type of cuisine was not available in the API data, I decided to leave this out completely.  

## Defend decisions
### Why was it good to do it differently
### Are there tradeoffs for the current solution
### Would you have chosen a differnt decision if you'd had more time
