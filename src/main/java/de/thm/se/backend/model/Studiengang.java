package de.thm.se.backend.model;

/**
 * Model-Klasse von Studiengang
 * Entspricht der Tabelle STUDIENGANG
 */
public class Studiengang {
    private int studiengangId;
    private int fachbereichId;
    private String bezeichnung;
    private String kuerzel;
    private String abschlusstitel; // z.B. "Master of Science Software Engineering"
    private String abschluss; // z.B. "M.Sc"
    private boolean aktiv;

    // Konstruktor
    public Studiengang() {
    }

    public Studiengang(String bezeichnung, int fachbereichId, String kuerzel, String abschlusstitel, String abschluss,
            boolean aktiv) {
        this.bezeichnung = bezeichnung;
        this.fachbereichId = fachbereichId;
        this.kuerzel = kuerzel;
        this.abschlusstitel = abschlusstitel;
        this.abschluss = abschluss;
        this.aktiv = aktiv;
    }

    // Getter und Setter
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

    public int getFachbereichId() {
        return fachbereichId;
    }

    public void setFachbereichId(int fachbereichId) {
        this.fachbereichId = fachbereichId;
    }

    public String getKuerzel() {
        return kuerzel;
    }

    public void setKuerzel(String kuerzel) {
        this.kuerzel = kuerzel;
    }

    public String getAbschlusstitel() {
        return abschlusstitel;
    }

    public void setAbschlusstitel(String abschlusstitel) {
        this.abschlusstitel = abschlusstitel;
    }

    public String getAbschluss() {
        return abschluss;
    }

    public void setAbschluss(String abschluss) {
        this.abschluss = abschluss;
    }

    public boolean isAktiv() {
        return aktiv;
    }

    public void setAktiv(boolean aktiv) {
        this.aktiv = aktiv;
    }

    // Hilfsmethoden
    public String toString() {
        return String.format("Studiengang[ID=%d, Bezeichnung=%s, Abschlusstitel=%s, Aktiv=%b]", studiengangId,
                bezeichnung, abschlusstitel, aktiv);
    }
}
