## Description
<img src="/doc/searchScreenshot.png" width="350">

Meet & Eat is an Android application that allows users to join other users' dinners. The idea behind this is that there will be less food that goes to waste. Users can also add new dinners that can be joined by searching for recipes given a food. These recipes are retrieved using the Spoonacular API.

## Technical design

### High-level overview

<img src="/doc/graph.png">

- MainFragment
 * CookFragment (DateSelector, TimeSelector) -> HttpRequestHandler -> DinnerAsyncTask (Dinner) -> InfoAsyncTask (Dinner) -> RecipeResultFragment (RecipeAdapter) -> DatabaseHandler
 * JoinFragment -> DatabaseHandler -> DinnerResultFragment (DinnerAdapter) -> DatabaseHandler
 * SignUpFragment -> DatabaseHandler (User) or SignInFragment -> DatabaseHandler (User)
- AccountFragment -> SignUpFragment -> DatabaseHandler (User) or SignInFragment -> DatabaseHandler (User)
- HostListFragment (SavedAdapter) -> DatabaseHandler -> EditDinnerFragment (DateSelector, TimeSelector) -> DatabaseHandler
- JoinListFragment (SavedAdapter) -> DatabaseHandler

- MainActivity -> DrawerAdapter (DrawerItem)


<br><br><br>
- CookFragment 
- DatabaseHandler
- DateSelector
- Dinner
- DinnerAdapter
- DinnerAsyncTask
- DrawerAdapter
- DrawerItem
- EditDinnerFragment
- HostListFragment 
- HttpRequestHandler
- InfoAsyncTask
- JoinFragment
- JoinListFragment
- MainActivity
- MainFragment
- RecipeAdapter
- RecipeResultFragment
- DinnerResultFragment
- SavedAdapter
- SignInFragment
- SignUpFragment
- TimeSelector
- User

### High-level overview: to navigate and help understand total of code
### Detailed: describe modules/classes and how they relate

## Challenges during development: important changes from DESIGN document

## Defend decisions
### Why was it good to do it differently
### Are there tradeoffs for the current solution
### Would you have chosen a differnt decision if you'd had more time
