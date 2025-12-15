package de.thm.se.backend.controller;

import de.thm.se.backend.service.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/import")
public class ImportController {

    @Autowired
    private DataImportService dataImportService;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return dataImportService.importData(file.getInputStream());
        } catch (IOException e) {
            return "Fehler beim Lesen der Datei: " + e.getMessage();
        }
    }
}