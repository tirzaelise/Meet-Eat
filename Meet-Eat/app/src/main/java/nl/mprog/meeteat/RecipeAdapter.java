/* Meet & Eat
 * Tirza Soute (10761977)
 * Programmeerproject
 *
 * This class implements the adapter for recipes that the user has searched for. It displays the
 * title, the ingredients, whether the recipe is vegetarian and/or vegan and an image of the
 * prepared recipe. The user can add a recipe to the database if it's what they're going to cook.
 */

package nl.mprog.meeteat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class RecipeAdapter extends BaseAdapter {
    private ArrayList<Dinner> recipes;
    private Context context;
    private LayoutInflater inflater;

    RecipeAdapter(Context context, ArrayList<Dinner> recipes) {
        this.context = context;
        this.recipes = recipes;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            view = inflater.inflate(R.layout.layout_recipe_row, parent, false);
        }

        setInfo(view, position);
        setVisibility(view, position);
        setImage(view, position);

        Button makeDinner = ((Button) view.findViewById(R.id.makeButton));
        setClickListener(makeDinner, position);

        return view;
    }

    /** Sets the info about a recipe in the corresponding TextViews. */
    private void setInfo(View view, int position) {
        String title = this.recipes.get(position).getTitle();
        String ingredients = "Ingredients: " + this.recipes.get(position).getIngredients();

        ((TextView) view.findViewById(R.id.recipeTitle)).setText(title);
        ((TextView) view.findViewById(R.id.recipeIngredients)).setText(ingredients);
    }

    /** Sets the visibility of a TextView according to a boolean value. */
    private void setVisibility(View view, int position) {
        TextView vegetarianView = (TextView) view.findViewById(R.id.recipeVegetarian);
        TextView veganView = (TextView) view.findViewById(R.id.recipeVegan);
        boolean isVegetarian = this.recipes.get(position).isVegetarian();
        boolean isVegan = this.recipes.get(position).isVegan();

        booleanToVisibility(isVegetarian, vegetarianView);
        booleanToVisibility(isVegan, veganView);
    }

    /** Determines the visibility of a TextView according to a boolean. */
    private void booleanToVisibility(boolean isTrue, TextView textView){
        if (isTrue) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    /** Sets the image of a recipe in the corresponding ImageView. */
    private void setImage(View view, int position) {
        ImageView imageView = (ImageView) view.findViewById(R.id.recipeImage);
        String url = "https://spoonacular.com/recipeImages/" + this.recipes.get(position).getId() +
                "-312x231.jpg";
        Picasso.with(context).load(url).into(imageView);
    }

    /** Adds a dinner to the Firebase database if the user is logged in. */
    private void setClickListener(Button button, final int position) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    Dinner clickedRecipe = recipes.get(position);
                    DatabaseHandler databaseHandler = new DatabaseHandler();
                    databaseHandler.writeToDatabase(clickedRecipe);
                    Toast.makeText(context, "Added dinner", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Please log in to add a dinner",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
