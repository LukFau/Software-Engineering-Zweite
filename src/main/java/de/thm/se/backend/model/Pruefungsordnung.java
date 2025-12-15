package de.thm.se.backend.model;

import java.time.LocalDate;

/**
 * Model-Klasse für Prüfungsordnung
 * Entspricht der Tabelle PRUEFUNGSORDNUNG
 */
public class Pruefungsordnung {
    private int pruefungsordnungId;
    private int studiengangId;
    private String bezeichnung;
    private LocalDate gueltigAb;
    private LocalDate gueltigBis;
    private int swsReferent;
    private int swsKoreferent;

    // Konstruktor
    public Pruefungsordnung() {
    }

    public Pruefungsordnung(int studiengangId, String bezeichnung, LocalDate gueltigAb, LocalDate gueltigBis,
            int swsReferent, int swsKoreferent) {
        this.studiengangId = studiengangId;
        this.bezeichnung = bezeichnung;
        this.gueltigAb = gueltigAb;
        this.gueltigBis = gueltigBis;
        this.swsReferent = swsReferent;
        this.swsKoreferent = swsKoreferent;
    }

    // Getter und Setter
    public int getPruefungsordnungId() {
        return pruefungsordnungId;
    }

    public void setPruefungsordnungId(int pruefungsordnungId) {
        this.pruefungsordnungId = pruefungsordnungId;
    }

    public int getStudiengangId() {
        return studiengangId;
    }

    public void setStudiengangId(int studiengangId) {
        this.studiengangId = studiengangId;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public LocalDate getGueltigAb() {
        return gueltigAb;
    }

    public void setGueltigAb(LocalDate gueltigAb) {
        this.gueltigAb = gueltigAb;
    }

    public LocalDate getGueltigBis() {
        return gueltigBis;
    }

    public void setGueltigBis(LocalDate gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public int getSwsReferent() {
        return swsReferent;
    }

    public void setSwsReferent(int swsReferent) {
        this.swsReferent = swsReferent;
    }

    public int getSwsKoreferent() {
        return swsKoreferent;
    }

    public void setSwsKoreferent(int swsKoreferent) {
        this.swsKoreferent = swsKoreferent;
    }

    // Hilfsmethoden
    @Override
    public String toString() {
        return String.format("Prüfungsordnung[ID=%d, Bezeichnung=%s, GültigAb=&d, GültigBis=%d]", pruefungsordnungId,
                bezeichnung, gueltigAb, gueltigBis);
    }
}
