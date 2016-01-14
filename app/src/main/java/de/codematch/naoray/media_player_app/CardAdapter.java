package de.codematch.naoray.media_player_app;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created on 15.12.2015.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private ArrayList<Card> cardArrayList;
    private MainMenuActivity main;

    public CardAdapter(ArrayList<Card> cardArrayList, MainMenuActivity main) {
        this.cardArrayList = cardArrayList;
        this.main = main;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        CardViewHolder cards = new CardViewHolder(v);
        return cards;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.titel.setText(cardArrayList.get(position).getTitel());
        holder.description.setText(cardArrayList.get(position).getDescription());
        //Hintergrundbild nur dan setzen, wenn es eins gibt.
        if (cardArrayList.get(position).getImage() != 0)
            holder.image.setImageResource(cardArrayList.get(position).getImage());
        // Hintergrundfarbe setzen
        holder.background.setCardBackgroundColor(Color.parseColor(cardArrayList.get(position).getBackground()));

    }

    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titel, description;
        ImageView image;
        CardView background;
        ImageButton dots;

        public CardViewHolder(View itemView) {
            super(itemView);

            titel = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            image = (ImageView) itemView.findViewById(R.id.thumbnail);
            background = (CardView) itemView.findViewById(R.id.card_view);
            dots = (ImageButton) itemView.findViewById(R.id.threeDots);

            itemView.setOnClickListener(this);

            /**
             * ClickListener für die drei Punkte, oben rechts einer Karte
             * Diese ruft eine Methode der MainActvity auf, da dort der OnClick gehandelt werden soll
             */
            dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    main.cardDescription(getAdapterPosition());
                }
            });
        }

        /**
         * ClickListener für die Karte selbst.
         * Diese ruft eine Methode der MainActvity auf, da dort der OnClick gehandelt werden soll
         */
        @Override
        public void onClick(View v) {
            main.onClick(getAdapterPosition());
        }
    }
}
