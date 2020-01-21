package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.DummyNeighbourGenerator;
import butterknife.BindView;
import butterknife.ButterKnife;


import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.MESSAGE_TO_DETAIL_ACTIVITY;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity.mApiService;

public class DetailNeighbourActivity extends AppCompatActivity
{

    @BindView(R.id.neighbourAvatar)
    TextView neighbourAvatar;

    @BindView(R.id.cardname)
    TextView cardName;

    @BindView(R.id.email)
    TextView neighbourEmail;

    Neighbour currentNeighbour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_neighbour);
        ButterKnife.bind(this);

        //On récupère l'id du voisin sélectionné
        Intent intent = getIntent();
        String message = intent.getStringExtra(MESSAGE_TO_DETAIL_ACTIVITY);

        /*On récupère le voisin sélectionné grâce à son id, dans la liste générée :
            (les ID commencent à 1, alors que l'index de la liste, à 0, on retranche donc 1 à l'ID transmis)
         */

        currentNeighbour = mApiService.getNeighbour(Integer.parseInt(message)-1);

        //Récupère à l'aide de Glide l'image du voisin en récupérant l'Url correspondante, l'applique à un Textview

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
    }

    public void backToListNeighbourActivity(View v){
        finish();
    }



}
