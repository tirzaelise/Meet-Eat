# Process book

#### Day 1
Wrote my application proposal.

#### Day 2
Wrote my design document and started creating the layouts of the different activities.

#### Day 3
Started writing code: Google authentication using Firebase.

#### Day 4
Can now read from Firebase database and write to Firebase database.

#### Day 5
Gave presentation and started working on navigation drawer. Will have to figure out how Fragments work next week. (Worked on Fragments during the weekend to improve app design, might have to drop Google login and work with email verification instead because it does not work well with fragments)

##### Day 6
Worked on integrating the Spoonacular API. I need two AsyncTasks: one to retrieve the id, the title and the image url (might want to use this later) and another one to retrieve the ingredients and whether the recipe is vegan or vegetarian (might want to use this later to show in the list view where users can see which dinners are cooked in their area). Tried to call the second AsyncTask in the first one to be able to create a Dinner object with all information at once, but this did not work because the first AsyncTask took too long to finish. Will need to come up with a new idea tomorrow.

#### Day 7
Called both AsyncTasks in the same Fragment, which works. However, the information now needs to be sent to a new Fragment to show the retrieved information about the recipes. New idea: call AsyncTasks in fragment where the results should be displayed instead of sending the results to the fragment where the results should be displayed. Users can now update the amount of free spaces by clicking the plus button when they want to join a dinner. Worked out the new AsyncTasks idea, which works. However, the adapter should be changed so that different (relevant) info is displayed.

#### Day 8
Created a custom adapter to show recipe search results, which displays relevant information. The new recipes can be saved to the database. Also worked on logging in, but it was not possible to use Google login because of certain necessary methods that require AppCompatActivity instead of Fragment. Therefore, I chose e-mail verification instead, which works now.

#### Day 9
Wanted to save the user's name using Firebase's setDisplayName, but apparently this does not work if you use e-mail authentication. Now the user's name is saved in SharedPreferences instead. Found a bug where it turned out that the recipe that was saved to the database was random instead of the recipe that was clicked. Fixed this using the adapter. Added images to dinners for that day. Also started working on activities that show the user's dinners (either the ones he's going to join or host).

#### Day 10
Rewrote code so that int freeSpaces is now an ArrayList\<String\> that holds all the users that have joined the dinner. Also created a User object which consists of a Firebase-generated user ID and the username that the user fills in when signing up. Will also have to change Dinner object to hold both the hostId and hostName so that I can make a list which shows the user's hosting dinners. Changed the database to \{dinners, users\}. And added the hostId and hostName. Will have to do the same thing for guests. (Did this during the weekend and it works. Also worked on a DrawerAdapter, but this is empty/crashses the app, so left it for what it is for now.)

#### Day 11
Went back to the standard ArrayAdapter so that I can work on things that are more important right now. I made another custom adapter that has ideal info for joined/hosting dinners and fragments for these lists. This now works, so the basic functionality of my application is done. Tweaked some other little things, such as the custom Navigation Drawer adapter which works now, and some little design things. 

#### Day 12
Fragments seemed to overlap each other when the back button is clicked so I fixed this. I also made sure the amount of free spaces goes down when the join button is clicked in a dinner. I thought it would make sense if the drawer closed after an item is clicked, so I implemented this. Implemented that the user who's hosting a dinner can edit the title, ingredients and date. I am currently working on giving the user the option to remove themselves from a dinner, but I can't seem to get this to work yet. There is a loop, even though there is no loop anywhere and the app crashes because it tries to remove something that isn't there and has already been removed. I found out that this was because I used addValueEventListener when searching for the Dinner that had to be changed instead of addListenerForSingleValueEvent. This caused the code to run multiple times. Tomorrow, I will have to work on the double entries in the Joined Dinners list when an entry is deleted.

#### Day 13
The double entries in the Joined Dinners list was also caused because I used addValueEventListener instead of addListenerForSingleValueEvent. It still took a while to realise this, but there are now no longer duplicate entries when a dinner is edited. I worked on a DatePickerDialog and a TimePickerDialog for CookFragment, which now both work. This allows the user to also hold future dinners (instead of just today) and makes filling in all the necessary fields in this fragment more user friendly. I also worked on cleaning a lot of my code using BetterCodeHub and I am working on setting a 'No results found' TextView for several fragments, but this does not work yet.

#### Day 14
I refactored a lot of code today, because I realised I was doing unnecessary extra work. This was mainly in DatabaseHandler and DinnerAdapter. I also added a feature where the user can send an e-mail to the host of a dinner once they join it so that they can be informed of the host's address etc. I mostly spent today refactoring code. Lastly, I added a 'loading' text to all fragments where data has to be loaded into a listview. If no data is found, then the 'loading' text is replaced by a 'no results' text. I found out that during refactoring, I created a global variable 'position' in SavedAdapter to minimise the amount of parameters in methods (since I was using it in almost every method), which messed the entire app up. It took a while to find where in the code it went wrong, but I fixed this.

#### Day 15
I worked on some things that were given to me as feedback during my presentation/demo today. There is now a pop up dialog when the user wants to join a dinner where the user has to specify with how many people they'd like to join a dinner. I also made a check when the user wants to join a dinner to see whether the user trying to join the dinner is not the host of the dinner. I made the ingredients EditText in EditDinnerFragment multiline so that the ingredients would be easier to read and I made some other layout changes. I also worked on sending e-mails when the host deletes a dinner or when a guest unjoins a dinner, but this does not work yet. Furthermore, I created a launcher icon. I managed to fix sending the e-mails by the end of the day as well.

#### Day 16
I spent today cleaning a lot of code using BetterCodeHub. I mostly made methods shorter and minimised the amount of duplicated code. I cleaned MainActivity, AccountFragment, RecipeAdapter and created DateSelector and TimeSelector to avoid duplicated code.

#### Day 17 
Finished creating headers and commenting methods for all Java files. I also cleaned all the XML files. I also created landscape layouts for fragments that did not fit in the screen when they were rotated. I tried to work on state restoration: currently, when the user searches for a recipe, his hosted/joined dinners or for dinners in a specified area, the search restarts when the screen rotates. The retrieved data is not saved, but since this is done using Firebase, which is asynchronous, and the ListView is populated in onDataChange it is not possible to simply save this data once everything is retrieved. I talked to a TA and she told me that it would be okay if the search starts again on rotation. I did decide to separate signing in and signing up into two different fragments. At first, this was done in the same fragment and the visibility of certain views/buttons was decided according to the user's input of whether he wanted to sign in or sign up. However, if the screen was rotated, the visibilities were reset to their standard values. I tried to save the visibilities in a bundle and restore them when the screen was rotated, but the visibility would not change. I even tried to simply set all visibilities to invisibile to see if this would work, but nothing changed. I think this is because I use fragments instead of activities. Therefore, I decided to put signing in in a different fragment so that the state would be restored correctly. I have now cleaned all of my code and I don't think I have to spend any more time of this. I will have to start working on my report tomorrow. 

#### Day 18
I spent the entire day working on my report. I let someone else test my app and they found a couple bugs, which I fixed. You can now no longer put in that 0 people can join your dinner and the area that is put in must be at least two characters.
