# Design Document

### Activity MainActivity.java
If the user is not logged in yet, there is a screen with 'Log in' and a Google image button. If the Google image is clicked,
the user can either sign in or sign up using <i>AccountActivity.java</i>. If the user is logged in, there is a screen where 
the user can indicate whether he wants to cook or join dinner. If the user wants to cook dinner, he is sent to
<i>CookActivity.java</i>. If the user wants to join dinner, he is sent to <i>JoinActivity.java</i>.

### Class Account.java
This class will handle the user's Google sign in. After the user has logged in, he is sent back to <i>MainActivity.java</i>.

### Activity CookActivity.java
In this activity, the user gives information about the dinner he will be cooking. He has to specify what he'll be making, how
many people can join, the start time of dinner, what area he's in, the type of cuisine of the food and the ingredients 
he'll be using. After clicking the submit button, a Dinner object is created, which is put into the Firebase database using 
<i>DatabaseHandler.java</i>. 

### Class DatabaseHandler.java (Firebase)
This class will handle all the Firebase actions. This includes writing to the database when a dinner is added, reading from
the database to display search results and deleting a dinner from the database.

### Activity JoinActivity.java
In this activity, the user that wants to join dinner has to specify what area he's in and what cuisine he's interested in.
After clicking the search button, the user is sent to <i>ResultsActivity.java</i> with his specified information in a bundle.

### Activity ResultsActivity.java
In this activity, the <i>DatabaseHandler.java</i> is used to get the user's search results using the bundle that has the
dinner-joining user's specified information. The search results are then displayed. This activity shows the food that
someone will be making and the time dinner will start. The user can click on a dinner to get more information and is then
sent to <i>DinnerActivity.java</i>.

### Activity DinnerActivity.java
This activity shows all the information about a dinner that the cook has specified. There is also a join button that the user
can click if he wants to join dinner. If it's clicked, <i>DatabaseHandler.java</i> is used to update the amount of people 
that can join dinner.

### Object Dinner.java
This object holds all the specified information about a dinner: food (string), start time (string), amountOfPeople (int), 
cuisine (string), area (string), ingredients (string). The start time should look like: hh:mm.

### Database (Firebase)
Each entry in the database will look as follows:

<table class="tg">
  <tr>
    <th class="tg-yw4l">dinner id</th>
    <th class="tg-yw4l"></th>
    <th class="tg-yw4l"></th>
  </tr>
  <tr>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"><b>food</b></td>
    <td class="tg-yw4l">string</td>
  </tr>
  <tr>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"><b>startTime</b></td>
    <td class="tg-yw4l">string</td>
  </tr>
  <tr>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"><b>amountOfPeople</b></td>
    <td class="tg-yw4l">int</td>
  </tr>
  <tr>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"><b>cuisine</b></td>
    <td class="tg-yw4l">string</td>
  </tr>
  <tr>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"><b>area</b></td>
    <td class="tg-yw4l">string</td>
  </tr>
  <tr>
    <td class="tg-yw4l"></td>
    <td class="tg-yw4l"><b>ingredients</b></td>
    <td class="tg-yw4l">string</td>
  </tr>
</table>

### Diagram
<img src="/doc/diagramSketch.png" height="400">
