package de.thm.se.backend.model;

public class ArbeitMitDetails {
    private int arbeitId;
    private String titel;
    private String typ;
    private String status;
    private String studierenderName;
    private String studiengang;
    private String semester;

    // Getter und Setter
    public int getArbeitId() {
        return arbeitId;
    }

    public void setArbeitId(int arbeitId) {
        this.arbeitId = arbeitId;
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

    public String getStudierenderName() {
        return studierenderName;
    }

    public void setStudierenderName(String studierenderName) {
        this.studierenderName = studierenderName;
    }

    public String getStudiengang() {
        return studiengang;
    }

    public void setStudiengang(String studiengang) {
        this.studiengang = studiengang;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
