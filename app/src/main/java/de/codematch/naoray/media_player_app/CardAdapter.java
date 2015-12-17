package de.codematch.naoray.media_player_app;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created on 15.12.2015.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    TextView descriptionMain;
    private ArrayList<Card> cardArrayList;
    //    private ClickListener clickListener;
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
        //holder.image.setImageResource(cardArrayList.get(position).getImage());
        if (cardArrayList.get(position).getImage() != 0)
            holder.image.setImageResource(cardArrayList.get(position).getImage());
        else
            holder.test1.setCardBackgroundColor(cardArrayList.get(position).getBackground());

    }

    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }

    /*
        public void setClickListener(ClickListener clickListener){
            this.clickListener = clickListener;
        }
    */
    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titel, description;
        ImageView image;
        CardView test1;

        public CardViewHolder(View itemView) {
            super(itemView);

            titel = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            image = (ImageView) itemView.findViewById(R.id.thumbnail);
            test1 = (CardView) itemView.findViewById(R.id.card_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            main.onClick(getAdapterPosition(), cardArrayList.get(getAdapterPosition()).getTitel());
        }
    }
}
