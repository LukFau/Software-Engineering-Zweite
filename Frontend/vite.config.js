import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
    plugins: [react()],
    // Konfiguriert den Proxyserver für die Entwicklungsphase.
    // Alle Anfragen, die nicht statische Assets sind, werden zum Backend weitergeleitet.
    server: {
        port: 5173, // Standard-Vite-Port
        proxy: {
            // Wenn Sie /api/betreuer aufrufen, leitet Vite es an http://localhost:8080/api/betreuer weiter
            '/api': {
                target: 'http://localhost:8080', // Das ist der Port Ihres Spring Boot Backends
                changeOrigin: true,
                // rewrite: (path) => path.replace(/^\/api/, ''), // Hier nicht nötig, da wir /api im Target haben
            },
        },
    },
});