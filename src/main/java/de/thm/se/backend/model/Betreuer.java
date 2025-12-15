package de.thm.se.backend.model;

/**
 * Model-Klasse für Betreuer
 * Entspricht der Tabelle Betreuer
 */
public class Betreuer {
    private int betreuerId;
    private String vorname;
    private String nachname;
    private String titel; // z.B. "Prof. Dr."
    private String email;
    private String rolle; // z.B. "Professor", "Wissenschaftlicher Mitarbeiter"

    // Konstruktoren
    public Betreuer() {
    }

    public Betreuer(String vorname, String nachname, String email) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
    }

    // Getter und Setter
    public int getBetreuerId() {
        return betreuerId;
    }

    public void setBetreuerId(int betreuerId) {
        this.betreuerId = betreuerId;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    // Hilfsmethoden
    public String getVollstaendigerName() {
        if (titel != null && !titel.isEmpty()) {
            return titel + " " + vorname + " " + nachname;
        }
        return vorname + " " + nachname;
    }

    @Override
    public String toString() {
        return String.format("Betreuer[ID=%d, %s]", betreuerId, getVollstaendigerName());
    }

}
