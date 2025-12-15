import axios from 'axios';

// Erstellt eine Basis-Instanz für alle Anfragen
const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

export default api;