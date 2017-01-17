package nl.mprog.meeteat;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

    /** Reads the database based on the user's input of his area and dinner preference */
    ArrayList<Dinner> readDatabase(String area, final ArrayList<Dinner> dinners,
                                   final DinnerAdapter adapter, final ResultFragment fragment) {
        database = FirebaseDatabase.getInstance().getReference();
        Query areaQuery = database.orderByChild("area").equalTo(area);

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
        return dinners;
    }

    void updateFreeSpaces(String dinnerId, final Context context) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query findId = database.orderByChild("id").equalTo(dinnerId);

        findId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String databaseKey = snapshot.getKey();
                    Dinner dinner = snapshot.getValue(Dinner.class);
                    int newFreeSpaces = dinner.getFreeSpaces() - 1;

                    if (newFreeSpaces < 0) {
                        Toast.makeText(context, "There are no more free spaces for this dinner",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        dinner.setFreeSpaces(newFreeSpaces);
                        database.child(databaseKey).setValue(dinner);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to join dinner", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
