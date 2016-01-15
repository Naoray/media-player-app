package de.codematch.naoray.media_player_app;

/**
 * Created on 15.12.2015.
 */
public class Card {
    String background;
    private String titel;
    private String description;
    private int image;
    private String largeDescritption;

    public Card(String titel, String description, int image, String background, String largeDescritption) {
        this.titel = titel;
        this.description = description;
        this.image = image;
        this.background = background;
        this.largeDescritption = largeDescritption;
    }

    public Card(String titel, String description, String background, String largeDescritption) {
        this.titel = titel;
        this.description = description;
        this.background = background;
        this.largeDescritption = largeDescritption;
    }

    public String getTitel() {
        return titel;
    }

    public String getDescription() {
        return description;
    }

    public String getLargeDescritption() {
        return largeDescritption;
    }

    public int getImage() {
        return image;
    }

    public String getBackground() {
        return background;
    }
}
