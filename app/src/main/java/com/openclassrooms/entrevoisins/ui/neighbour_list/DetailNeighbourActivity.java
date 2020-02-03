package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.SELECTED_NEIGHBOUR;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.mApiService;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.sharedPreferences;


public class DetailNeighbourActivity extends AppCompatActivity
{

    @BindView(R.id.neighbourAvatar)    TextView neighbourAvatar;

    @BindView(R.id.cardname)    TextView cardName;

    @BindView(R.id.email)    TextView neighbourEmail;

    @BindView(R.id.starButton)    ImageButton starButton;

    //Voisin sur lequel l'utilisateur a cliqué dans ListNeighbourActivity

    Neighbour currentNeighbour;

    //Intent récupérant les messages transmis par ListNeighbourActivity

    static Intent intent = new Intent();

    boolean favoriteadded;
    public static final String [] FAVORITE_NAMES = {"Caroline","Jack","Chloé","Vincent","Elodie","Sylvain","Laetitia","Dan","Joseph","Emma","Patrick","Ludovic"};
    public static final String SAVED_FAVORITE_LIST = "FAVORITELIST";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_neighbour);
        ButterKnife.bind(this);

        //On récupère la position du voisin sélectionné
        intent = getIntent();
        int selectedNeighbour = intent.getIntExtra(SELECTED_NEIGHBOUR,0);

        sharedPreferences = this.getSharedPreferences(SAVED_FAVORITE_LIST, this.MODE_PRIVATE);
        //sharedPreferences.getAll().clear();

        //On récupère le voisin sélectionné grâce à son id, en le recherchant dans la liste que l'API conserve :

       currentNeighbour = mApiService.getNeighbour(selectedNeighbour);

        // On vérifie s'il est ou non dans les favoris

        favoriteadded = currentNeighbour.isFavorite();

        /*Récupère à l'aide de Glide l'image du voisin en récupérant l'Url correspondante,
        *   l'applique à un Textview situé en haut de l'écran
        *   attribue ensuite le nom du voisin à la CardView présentant ses informations
         */

        Glide.with(this)
                .load(currentNeighbour.getAvatarUrl())
                .apply(RequestOptions.noTransformation())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource,
                                                @Nullable Transition<? super Drawable> transition) {
                        /* Set a drawable to the left of textView */
                        neighbourAvatar.setBackground(resource);
                    }

                });
        neighbourAvatar.setText(currentNeighbour.getName());
        cardName.setText(currentNeighbour.getName());
        neighbourEmail.setText("www.facebook.fr/" + currentNeighbour.getName());

        if (favoriteadded) ImageViewCompat.setImageTintList(starButton, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorFavoriteSelected)));
        else                ImageViewCompat.setImageTintList(starButton, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorFavoriteUnselected)));


        starButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                            if(favoriteadded){
                                ImageViewCompat.setImageTintList(starButton, ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.colorFavoriteUnselected)));
                                favoriteadded = false;
                                mApiService.removeFromFavorite(currentNeighbour.getId());
                                sharedPreferences.edit().remove(FAVORITE_NAMES[currentNeighbour.getId()-1]).commit();
                            }
                            else {
                                ImageViewCompat.setImageTintList(starButton, ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.colorFavoriteSelected)));
                                favoriteadded = true;
                                mApiService.addToFavorite(currentNeighbour.getId());
                                sharedPreferences
                                        .edit()
                                        .putInt(FAVORITE_NAMES[currentNeighbour.getId()-1],currentNeighbour.getId() )
                                        .apply();
                            }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void backToListNeighbourActivity(View v){
        finish();
    }



}
