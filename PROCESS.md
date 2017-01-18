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
