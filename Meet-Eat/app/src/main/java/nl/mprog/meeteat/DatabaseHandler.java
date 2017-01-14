package nl.mprog.meeteat;

import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by tirza on 11-1-17.
 */

class DatabaseHandler {
    private DatabaseReference database;
    private ArrayList<Dinner> dinners;

    void writeToDatabase(Dinner dinner) {
        database = FirebaseDatabase.getInstance().getReference();
        database.push().setValue(dinner);
    }

    /** Reads the database based on the user's input of his area and dinner preference (cuisine) */
    ArrayList<Dinner> readDatabase(String area, String cuisine, final ArrayList<Dinner> dinners,
                                   final DinnerAdapter adapter, final ResultFragment fragment) {
        database = FirebaseDatabase.getInstance().getReference();
        Query areaQuery = database.orderByChild("area").equalTo(area);

        if (cuisine.equals("")) {
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

        } else { //TODO: Can't combine multiple orderBy calls
            Query cuisineQuery = areaQuery.orderByChild("cuisine").equalTo(cuisine);

            cuisineQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Dinner dinner = dataSnapshot.getValue(Dinner.class);
                    dinners.add(dinner);
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
//                    Toast.makeText(this, "Failed to read database", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return dinners;
    }
}
