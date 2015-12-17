package de.codematch.naoray.media_player_app;

/**
 * Created on 15.12.2015.
 */
public class Card {
    private String titel;
    private String description;
    private int image;
    private int background;

    public Card(String titel, String description, int image, int background) {
        this.titel = titel;
        this.description = description;
        this.image = image;
        this.background = background;
    }

    public Card(String titel, String description, int background) {
        this.titel = titel;
        this.description = description;
        this.background = background;
    }

    public String getTitel() {
        return titel;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    public int getBackground() {
        return background;
    }
}
