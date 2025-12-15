package de.thm.se.backend.model;

import java.time.LocalDate;

/**
 * Model-Klasse für Zeitdaten
 * Entspricht der Tabelle ZEITDATEN
 */
public class Zeitdaten {
    private int zeitId;
    private int arbeitId;
    private LocalDate anfangsdatum;
    private LocalDate abgabedatum;
    private LocalDate kolloquiumsdatum;

    // Konstruktoren
    public Zeitdaten() {
    }

    public Zeitdaten(int arbeitId, LocalDate anfangsdatum, LocalDate abgabedatum) {
        this.arbeitId = arbeitId;
        this.anfangsdatum = anfangsdatum;
        this.abgabedatum = abgabedatum;
    }

    // Getter und Setter
    public int getZeitId() {
        return zeitId;
    }

    public void setZeitId(int zeitId) {
        this.zeitId = zeitId;
    }

    public int getArbeitId() {
        return arbeitId;
    }

    public void setArbeitId(int arbeitId) {
        this.arbeitId = arbeitId;
    }

    public LocalDate getAnfangsdatum() {
        return anfangsdatum;
    }

    public void setAnfangsdatum(LocalDate anfangsdatum) {
        this.anfangsdatum = anfangsdatum;
    }

    public LocalDate getAbgabedatum() {
        return abgabedatum;
    }

    public void setAbgabedatum(LocalDate abgabedatum) {
        this.abgabedatum = abgabedatum;
    }

    public LocalDate getKolloquiumsdatum() {
        return kolloquiumsdatum;
    }

    public void setKolloquiumsdatum(LocalDate kolloquiumsdatum) {
        this.kolloquiumsdatum = kolloquiumsdatum;
    }

    // Hilfsmethoden
    public long getBearbeitungstageInTagen() {
        if (anfangsdatum != null && abgabedatum != null) {
            return java.time.temporal.ChronoUnit.DAYS.between(anfangsdatum, abgabedatum);
        }
        return 0;
    }

    public boolean istAbgelaufen() {
        if (abgabedatum != null) {
            return LocalDate.now().isAfter(abgabedatum);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Zeitdaten[Start=%s, Abgabe=%s, Kolloquium=%s", anfangsdatum, abgabedatum,
                kolloquiumsdatum);
    }

}
