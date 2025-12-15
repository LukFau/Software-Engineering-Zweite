package de.thm.se.backend.util;

import de.thm.se.backend.DataAcessLayer.WissenschaftlicheArbeitDAO;
import de.thm.se.backend.service.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class StartupDataInitializer implements CommandLineRunner {

    @Autowired private DatabaseCreator databaseCreator;
    @Autowired private DataImportService dataImportService;
    @Autowired private WissenschaftlicheArbeitDAO arbeitDAO;

    @Override
    public void run(String... args) throws Exception {
        // 1. Sicherstellen, dass DB existiert
        databaseCreator.createDatabase();

        // 2. Prüfen, ob schon Daten da sind (einfacher Check)
        if (arbeitDAO.findAll().isEmpty()) {
            System.out.println("Datenbank leer. Starte automatischen Import...");

            // Datei muss in src/main/resources liegen
            ClassPathResource resource = new ClassPathResource("Mockup Wissenschaftliche Arbeiten.xlsx");

            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    String result = dataImportService.importData(is);
                    System.out.println(result);
                }
            } else {
                System.out.println("Keine Import-Datei (Mockup Wissenschaftliche Arbeiten.xlsx) in resources gefunden.");
            }
        } else {
            System.out.println("Datenbank enthält bereits Daten. Import übersprungen.");
        }
    }
}