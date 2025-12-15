package de.thm.se.backend.model;

/**
 * Model-Klasse für wissenschaftliche Arbeit
 * Entspricht der Tabelle WISSENSCHAFTLICHE_ARBEIT
 */
public class WissenschaftlicheArbeit {
    private int arbeitId;
    private int studierendenId;
    private int studiengangId;
    private int pruefungsordnungId;
    private int semesterId;
    private String titel;
    private String typ; // z.B. "Bachelor", "Master"
    private String status; // z.B. "In Bearbeitung", "Abgeschlossen"

    // Konstruktoren
    public WissenschaftlicheArbeit() {
    }

    public WissenschaftlicheArbeit(int studierendenId, String titel, String typ) {
        this.studierendenId = studierendenId;
        this.titel = titel;
        this.typ = typ;
        this.status = "Geplant";
    }

    // Getter und Setter
    public int getArbeitId() {
        return arbeitId;
    }

    public void setArbeitId(int arbeitId) {
        this.arbeitId = arbeitId;
    }

    public int getStudierendenId() {
        return studierendenId;
    }

    public void setStudierendenId(int studierendenId) {
        this.studierendenId = studierendenId;
    }

    public int getStudiengangId() {
        return studiengangId;
    }

    public void setStudiengangId(int studiengangId) {
        this.studiengangId = studiengangId;
    }

    public int getPruefungsordnungId() {
        return pruefungsordnungId;
    }

    public void setPruefungsordnungId(int pruefungsordnungId) {
        this.pruefungsordnungId = pruefungsordnungId;
    }

    public int getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(int semseterId) {
        this.semesterId = semseterId;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Hilfsmethoden
    @Override
    public String toString() {
        return String.format("Arbeit[ID=%d, Title='%s', Typ=%s, Status=%s]", arbeitId, titel, typ, status);
    }
}
