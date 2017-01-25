package nl.mprog.meeteat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

class DatabaseHandler {

    void writeToDatabase(Dinner dinner) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("dinners").push().setValue(dinner);
    }

    /** Reads the database based on the user's input area. */
    void readDatabase(String area, final ArrayList<Dinner> dinners, final DinnerAdapter adapter,
                      final ResultFragment fragment) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query areaQuery = database.child("dinners").orderByChild("area").equalTo(area);

        areaQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Dinner dinner = dataSnapshot.getValue(Dinner.class);
                dinners.add(dinner);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(fragment.getActivity(), "Failed to read database",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** The list of guests is updated if there are more than 0 free spaces. */
    void updateFreeSpaces(String dinnerId, final Context context, final int position,
                          final DinnerAdapter adapter, final ArrayList<Dinner> dinners) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findId = database.child("dinners").orderByChild("id").equalTo(dinnerId);

        findId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String databaseKey = snapshot.getKey();
                    Dinner dinner = snapshot.getValue(Dinner.class);
                    String guestString = dinner.getGuestNames().toString();
                    int freeSpaces = StringUtils.countMatches(guestString, "null");

                    if (freeSpaces > 0) {
                        ArrayList<String> guestIds = dinner.getGuestIds();
                        ArrayList<String> guestNames = dinner.getGuestNames();

                        updateGuests(guestIds, guestNames, dinner, databaseKey, position, adapter,
                                dinners, context);
                        Toast.makeText(context, "Joined dinner", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "There are no more free spaces for this dinner",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to join dinner", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Finds the user's name to update the Dinner entry in the database when the join button is
     * clicked.
     */
    private void updateGuests(final ArrayList<String> guestIds, final ArrayList<String> guestNames,
                              final Dinner dinner, final String databaseKey, final int position,
                              final DinnerAdapter adapter, final ArrayList<Dinner> dinners,
                              final Context context) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            final String userId = user.getUid();
            final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query findName = database.child("users").orderByChild("userId").equalTo(userId);

            findName.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        String username = user.getUsername();
                        updateFirebaseGuests(guestIds, guestNames, userId, username, dinner,
                                databaseKey, position, adapter, dinners);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "Failed to retrieve username",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Searches for the first "null" occurrence in the guests array and replaces this with the
     * guest's name. After that, the Dinner entry in the database is updated.
     */
    private void updateFirebaseGuests(ArrayList<String> guestIds, ArrayList<String> guestNames,
                                      String id, String name, Dinner dinner, String databaseKey,
                                      int position, DinnerAdapter adapter,
                                      ArrayList<Dinner> dinners) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        for (int i = 0; i < guestNames.size(); i++) {
            if (guestIds.get(i).equals("null")) {
                guestIds.set(i, id);
                guestNames.set(i, name);
                break;
            }
        }
        dinner.setGuestIds(guestIds);
        dinner.setGuestNames(guestNames);
        dinners.set(position, dinner);
        adapter.notifyDataSetChanged();
        database.child("dinners").child(databaseKey).setValue(dinner);
    }

    /** Retrieves the dinners that the user is hosting from Firebase. */
    void getHostingDinners(final SavedAdapter adapter, final ArrayList<Dinner> dinners,
                           final Activity activity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            final String userId = user.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query findHosting = database.child("dinners").orderByChild("hostId").equalTo(userId);

            findHosting.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Dinner dinner = snapshot.getValue(Dinner.class);
                        dinners.add(dinner);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(activity, "Could not retrieve dinners",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Please log in to view this page",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /** Retrieves the dinners that the user has joined from Firebase. */
    void getJoinedDinners(final SavedAdapter adapter, final ArrayList<Dinner> dinners,
                          final Activity activity) {
        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            final String userId = user.getUid();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            Query findJoined = database.child("dinners").orderByChild("guestIds");

            findJoined.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        Dinner dinner = snapshot.getValue(Dinner.class);
                        ArrayList<String> guestIds = dinner.getGuestIds();

                        if (isInArray(userId, guestIds)) {
                            dinners.add(dinner);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(activity, "Could not retrieve joined dinners",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /** Checks if a certain value is in an array. */
    private boolean isInArray(String value, ArrayList<String> array) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).equals(value)) {
                return true;
            }
        }
        return false;
    }

    /** Saves a user in Firebase under his user ID. */
    void saveUser(User user, Context context) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(user.getUserId()).setValue(user);

        SharedPreferences.Editor editor = context.getSharedPreferences("userInfo",
                Context.MODE_PRIVATE).edit();
        editor.putString("userId", user.getUserId());
        editor.putString("username", user.getUsername()).apply();
    }

    /** Saves the user's name in SharedPreferences. */
    void getUsername(final String userId, final Context context) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findName = database.child("users").orderByChild("userId").equalTo(userId);

        findName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    String username = user.getUsername();
                    SharedPreferences.Editor editor = context.getSharedPreferences("userInfo",
                            Context.MODE_PRIVATE).edit();
                    editor.putString("username", username);
                    editor.putString("userId", userId).apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /** Updates a dinner in the database. */
    void updateDinner(final Dinner dinner, final Activity activity) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query updateDinner = database.child("dinners").orderByChild("id").equalTo(dinner.getId());

        updateDinner.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String databaseKey = snapshot.getKey();
                    database.child("dinners").child(databaseKey).setValue(dinner);
                    Toast.makeText(activity, "Updated dinner", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, "Failed to update dinner", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Finds a dinner in the database according to its ID and removes the current user from the
     * list of guests.
     */
    void removeGuest(final Dinner dinner, final Activity activity, final ArrayList<Dinner> dinners,
                    final SavedAdapter adapter, final int position) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findDinner = database.child("dinners").orderByChild("id").equalTo(dinner.getId());

        findDinner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String databaseKey = snapshot.getKey();
                    Dinner foundDinner = snapshot.getValue(Dinner.class);
                    removeUser(foundDinner, databaseKey, database, dinners, adapter, position,
                            activity);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, "Could not find dinner", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** Removes the current user from a dinner in the database. */
    private void removeUser(Dinner dinner, String key, DatabaseReference database,
                             ArrayList<Dinner> dinners, SavedAdapter adapter, int dinnerPosition,
                            Activity activity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            ArrayList<String> guestIds = dinner.getGuestIds();
            ArrayList<String> guestNames = dinner.getGuestNames();
            String userId = user.getUid();
            int lastOccurrence = guestIds.lastIndexOf(userId);

            if (lastOccurrence != -1) {
                guestIds.set(lastOccurrence, "null");
                guestNames.set(lastOccurrence, "null");
                dinner.setGuestIds(guestIds);
                dinner.setGuestNames(guestNames);

                dinners.set(dinnerPosition, dinner);
                adapter.notifyDataSetChanged();
                adapter.setData(dinners);

                database.child("dinners").child(key).setValue(dinner);
            } else {
                Toast.makeText(activity, "You have already been removed from this dinner",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /** Removes a dinner from the database. */
    void deleteDinner(final Dinner dinner, final Activity activity, final SavedAdapter adapter,
                              final ArrayList<Dinner> dinners) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findDinner = database.child("dinners").orderByChild("id").equalTo(dinner.getId());

        findDinner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String databaseKey = snapshot.getKey();
                    database.child("dinners").child(databaseKey).removeValue();
                    dinners.remove(dinner);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, "Could not remove dinner", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
