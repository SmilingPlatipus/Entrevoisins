package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.openclassrooms.entrevoisins.events.AddFavoriteEvent;
import com.openclassrooms.entrevoisins.events.RemoveFavoriteEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.SELECTED_NEIGHBOUR;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.mApiService;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_neighbour);
        ButterKnife.bind(this);

        //On récupère la position du voisin sélectionné
        intent = getIntent();
        int selectedNeighbour = intent.getIntExtra(SELECTED_NEIGHBOUR,0);


        //On récupère le voisin sélectionné grâce à son id, en le recherchant dans la liste que l'API conserve :

       currentNeighbour = mApiService.getNeighbour(selectedNeighbour);

        // On vérifie s'il est ou non dans les favoris

        favoriteadded = mApiService.containsFavorite(currentNeighbour);

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


        starButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                /* Si le favori n'est pas dans la liste et qu'il n'a pas déjà été ajouté par l'intermédiaire de l'écran de détail
                *  ou chargé au démarrage, on émet un Sticky Event Add
                *   réceptionné plus tard par FavoriteFragment
                *   sinon on enlève le voisin des favoris et on supprime le AddFavoriteEvent
                 */

                AddFavoriteEvent addFavorite = EventBus.getDefault().getStickyEvent(AddFavoriteEvent.class);
                RemoveFavoriteEvent removeFavorite = EventBus.getDefault().getStickyEvent(RemoveFavoriteEvent.class);

                // Si le voisin n'est pas déjà dans les favoris on doit l'ajouter
                if (!favoriteadded) {
                   if (addFavorite == null) EventBus.getDefault().postSticky(new AddFavoriteEvent(currentNeighbour));
                   if (removeFavorite != null) EventBus.getDefault().removeStickyEvent(RemoveFavoriteEvent.class);
                   favoriteadded = true;

                }
                // Cas où le voisin est déjà dans les favoris : on l'enlève
                else
                {
                    if (addFavorite != null) EventBus.getDefault().removeStickyEvent(AddFavoriteEvent.class);
                    if (removeFavorite == null) EventBus.getDefault().postSticky(new RemoveFavoriteEvent(currentNeighbour));
                    favoriteadded = false;
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    // L'événement doit être traité au sein de FavoriteFragment et non ici

    @Subscribe
    public void onAddFavoriteEvent(AddFavoriteEvent event) {
    }

    // L'événement doit être traité au sein de FavoriteFragment et non ici

    @Subscribe
    public void onRemoveFavoriteEvent(RemoveFavoriteEvent event) {
    }

    public void backToListNeighbourActivity(View v){
        finish();
    }



}
