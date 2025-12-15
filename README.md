# Software-Engineering
Software Engineering profIS für WiSe 2025/26
# 📚 Datenbank-Dokumentation: Abschlussarbeiten-Verwaltung

Diese Dokumentation beschreibt das Datenmodell, die Struktur der Datenbanktabellen, die Beziehungen zwischen den Entitäten sowie die grundlegenden CRUD-Operationen (Create, Read, Update, Delete) für die Anwendung.

---

## 1. 📊 Entity-Relationship-Diagramm (ERD)



Das Diagramm visualisiert die Struktur der Datenbank. Die **WISSENSCHAFTLICHE\_ARBEIT** ist die zentrale Entität, die die Verknüpfungen zu Studierenden, Studiengängen, Prüfungsordnungen und Betreuern über Assoziationstabellen herstellt.

### 1.1. Zentrale Entitäten (Tabellen)

| Entität | Primärschlüssel (PK) | Fremdschlüssel (FK) | Kurzbeschreibung |
| :--- | :--- | :--- | :--- |
| **WISSENSCHAFTLICHE\_ARBEIT** | `arbeit_id` | `studierende_id`, `studiengang_id`, `pruefungsordnung_id`, `semester_id` | Zentrale Entität für die Abschlussarbeiten. |
| **STUDIERENDE** | `studierenden_id` | - | Studierendenstammdaten (mit `matrikelnummer` als Unique Key). |
| **BETREUER** | `betreuer_id` | - | Daten der betreuenden Personen. |
| **STUDIENGANG** | `studiengang_id` | `fachbereich_id` | Details zu den angebotenen Studiengängen. |
| **FACHBEREICH** | `fachbereich_id` | - | Organisatorische Einheit (z.B. Fakultät). |
| **PRUEFUNGSORDNUNG** | `pruefungsordnung_id` | - | Regeln und Gültigkeitszeiträume für Prüfungen und Arbeiten. |
| **SWS\_BERECHNUNG** | *(Komplex)* | `arbeit_id`, `betreuer_id`, `pruefungsordnung_id` | Assoziationstabelle: Speichert die SWS-Berechnung pro Betreuer und Arbeit. |
| **NOTENBESTANDTEIL** | `noten_id` | `arbeit_id`, `betreuer_id` | Assoziationstabelle: Speichert die Notendetails (Arbeitsnote, Kolloquiumsnote) pro Betreuer. |

### 1.2. Beziehungen und Kardinalitäten

| Entitäten (Beziehung) | Kardinalität | Fremdschlüssel (FK) | Erklärung |
| :--- | :--- | :--- | :--- |
| **STUDIENGANG** $\leftrightarrow$ **FACHBEREICH** | N : 1 | `STUDIENGANG.fachbereich_id` | Jeder Studiengang gehört genau zu **einem** Fachbereich. |
| **WISSENSCHAFTLICHE\_ARBEIT** $\leftrightarrow$ **STUDIERENDE** | 1 : 1 | `WISSENSCHAFTLICHE_ARBEIT.studierenden_id` | Jede Arbeit ist **einem** Studierenden zugeordnet. |
| **WISSENSCHAFTLICHE\_ARBEIT** $\leftrightarrow$ **PRUEFUNGSORDNUNG** | N : 1 | `WISSENSCHAFTLICHE_ARBEIT.pruefungsordnung_id` | **Viele** Arbeiten richten sich nach **einer** Prüfungsordnung. |
| **WISSENSCHAFTLICHE\_ARBEIT** $\leftrightarrow$ **BETREUER** | N : M | `NOTENBESTANDTEIL.arbeit_id`, `NOTENBESTANDTEIL.betreuer_id` | **Viele** Arbeiten haben **mehrere** Betreuer und umgekehrt. |
| **SEMESTER** $\leftrightarrow$ **SEMESTERZEIT** | N : 1 | `SEMESTER.semesterzeit_id` | **Viele** Semester teilen sich **eine** Semesterzeit-Definition (z.B. "Sommersemester"). |

---

## 2. 📋 Tabellendetails (Schema)

| Entität | Schlüssel | Fremdschlüssel | Attribute |
| :--- | :--- | :--- | :--- |
| **Studenten** | `studierenden_id` (PK) | - | `matrikelnummer` (UK), `vorname`, `nachname`, `email`, `geburtsdatum` (Date), `adresse` |
| **Studiengang** | `studiengang_id` (PK) | - | `bezeichnung` (Unique Not Null), `kuerzel`, `abschluss`, `aktiv` (Integer, Default 1) |
| **Prüfungsordnung** | `pruefungsordnung_id` (PK) | `studiengang_id` (FK) | `bezeichnung` (Not Null), `gueltig_ab` (Date), `gueltig_bis` (Date), `sws_referent` (Integer), `sws_koreferent` (Integer) |
| **Semesterzeit** | `semesterzeit_id` (PK) | - | `beginn` (Date Not Null), `ende` (Date Not Null), `bezeichnung` |
| **Semester** | `semester_id` (PK) | `semesterzeit_id` (FK) | `bezeichnung` (Not Null), `typ`, `jahr` (Integer) |
| **Wiss. Arbeit** | `arbeit_id` (PK) | `studierenden_id` (FK), `studiengagn_id` (FK), `pruefungsordnung_id` (FK), `semester_id` (FK) | `title` (Not Null), `typ`, `status` |
| **Zeitdaten** | `zeit_id` (PK) | `arbeit_id` (FK) | `anfangsdatum` (Date), `abgabedatum` (Date), `kolloquiumsdatum` (Date) |
| **Betreuer** | `betreuer_id` (PK) | - | `vorname` (Not Null), `nachname` (Not Null), `title`, `email`, `rolle` |
| **Notenbestandteil** | `note_id` (PK) | `arbeit_id` (FK), `betreuer_id` (FK) | `rolle`, `note_arbeit` (Real), `note_kolloquium` (Real), `gewichtung` (Real) |
| **SWS\_Berechnung** | `sws_id` (PK) | `arbeit_id` (FK), `betreuer_id` (FK), `semester_id` (FK), `pruefungsordnung_id` (FK) | `sws_wert` (Real), `rolle`, `berechnet_am` (Date) |

---

## 3. ⚙️ Datenbank-Operationen (CRUD)

Die nachfolgenden CRUD-Befehle beschreiben die Standard-Datenbank-Interaktionen, die für jede Entität implementiert werden.

| SQL-Befehl | Funktion (im Code) | Beschreibung |
| :--- | :--- | :--- |
| **CREATE:** `INSERT INTO Tabellenname (attribut 1, attribut 2 …) VALUES (?, ?, …)` | `create(Entität enti)` | Fügt einen neuen Datensatz basierend auf einem übergebenen Java-Objekt ein. |
| **READ (by ID):** `SELECT * FROM Tabellenname WHERE attribut_id = ?` | `findById(int id)` | Gibt einen einzelnen Datensatz als Java-Objekt zurück, basierend auf der ID. |
| **READ (all):** `SELECT * FROM Tabellenname ORDER BY attribut` | `findAll()` | Gibt eine `ArrayList` aller Objekte der Entität, sortiert nach einem Attribut, zurück. |
| **UPDATE:** `UPDATE Tabellenname SET attribut 1 = ?, attribut 2 = ? … WHERE attribut_id = ?` | `update(Entität enti)` | Aktualisiert einen bestehenden Datensatz basierend auf dem übergebenen Java-Objekt und gibt den Erfolgsstatus zurück. |
| **DELETE:** `DELETE FROM Tabellenname WHERE attribut_id = ?` | `delete(int attribut_id)` | Löscht einen Datensatz anhand der ID und gibt den Erfolgsstatus zurück. |

---

## 4. 📝 SQLite Besonderheiten

Diese Anwendung nutzt SQLite als Datenbank-Engine, was folgende wichtige Merkmale und Einschränkungen mit sich bringt:

### 4.1. Kernmerkmale

* **Serverlos:** SQLite läuft als Teil der Anwendung (als Bibliothek), was Einrichtung und Wartung vereinfacht.
* **Dateibasiert & Portabel:** Die gesamte Datenbank ist eine einzige Datei (`.sqlite` oder `.db`), die leicht kopiert, gesichert und geteilt werden kann.
* **Ressourcenschonend:** Geringer Speicher- und Bandbreitenbedarf, ideal für eingebettete Systeme und Desktop-Anwendungen.
* **Transaktional:** Vollständig **ACID**-konform (Atomicity, Consistency, Isolation, Durability).

### 4.2. Wichtige Unterschiede & Einschränkungen

* **Kein Multi-User-Server:** Ausgelegt für den Einzelanwender-Zugriff (unterstützt jedoch gleichzeitige Leser).
* **Datentypen-Flexibilität:** SQLite nutzt nur fünf interne Typen (`NULL`, `INTEGER`, `REAL`, `TEXT`, `BLOB`). Es speichert Daten flexibel; der deklarierte Typ (z.B. `VARCHAR(10)`) wird nicht streng erzwungen (z.B. keine automatische Längenprüfung). `CHECK`-Constraints müssen zur Erzwingung von Regeln manuell hinzugefügt werden.
