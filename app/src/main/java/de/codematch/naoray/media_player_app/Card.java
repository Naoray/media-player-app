package de.codematch.naoray.media_player_app;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Diese Klasse enthält alle Infomationen (Model) einer Karte,
 * die im Hauptmenü angezeigt werden.
 * Die Informationen können mit den Getter-Methoden abgefragt werden.
 *
 * Parcelable implementiert, um ein Objekt dieser Klasse von
 * einer Activity an eine andere weitergeben zu können.
 */
public class Card implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
    String background;
    private String titel;
    private String description;
    private int image;
    private String largeDescritption;
    private String url;

    /**
     * Konstruktur mit Bild
     *
     * @param titel             = Titel der Karte
     * @param description       = kurze Beschreibung der Karte
     * @param image             = Bild für den Hintergrund der Karte
     * @param background        = Farbe des Hintergrunds der Karte
     * @param largeDescritption = Langbeschreibung der Karte
     * @param url               = URL des Programms, welches die Karte repräsentiert
     */
    public Card(String titel, String description, int image, String background, String largeDescritption, String url) {
        this.titel = titel;
        this.description = description;
        this.image = image;
        this.background = background;
        this.largeDescritption = largeDescritption;
        this.url = url;
    }

    /**
     * Konstruktur ohne Bild
     *
     * @param titel             = Titel der Karte
     * @param description       = kurze Beschreibung der Karte
     * @param background        = Farbe des Hintergrunds der Karte
     * @param largeDescritption = Langbeschreibung der Karte
     * @param url               = URL des Programms, welches die Karte repräsentiert
     */
    public Card(String titel, String description, String background, String largeDescritption, String url) {
        this.titel = titel;
        this.description = description;
        this.background = background;
        this.largeDescritption = largeDescritption;
        this.url = url;
    }

    /**
     * Konstruktur, der nur aufgerufen wird, wenn ein Objekt der Klasse Card
     * an eine andere Activity weitergegeben wird.
     *
     * @param in = Informationen aus dem Parcel
     */
    public Card(Parcel in) {
        readFromParcel(in);
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

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Informationen in das Parcel legen.
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Alle Felder in parcel schreiben.
        // In der Reihenfolge, in der die Felder eingelesen werden,
        // werden sie auch wieder ausgelesen (siehe Methode "readFromParcel")
        dest.writeString(background);
        dest.writeString(titel);
        dest.writeString(description);
        dest.writeInt(image);
        dest.writeString(largeDescritption);
        dest.writeString(url);
    }

    /**
     * Informationen aus dem Parcel auslesen.
     *
     * @param in
     */
    private void readFromParcel(Parcel in) {
        // Jedes Feld wieder zurückschreiben
        // gemäß der in Methode "writeToParcel"
        // vorgegebene Reihenfolge
        this.background = in.readString();
        this.titel = in.readString();
        this.description = in.readString();
        this.image = in.readInt();
        this.largeDescritption = in.readString();
        this.url = in.readString();
    }
}
