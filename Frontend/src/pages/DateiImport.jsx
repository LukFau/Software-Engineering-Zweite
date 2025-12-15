import React, { useState } from 'react';
import api from '../services/api';

const DateiImport = () => {
    const [file, setFile] = useState(null);
    const [status, setStatus] = useState('');
    const [isUploading, setIsUploading] = useState(false);

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleUpload = async (e) => {
        e.preventDefault();
        if (!file) {
            setStatus('Bitte eine Datei auswählen.');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        setIsUploading(true);
        setStatus('');

        try {
            // POST Request an dein Backend
            const response = await api.post('/import/upload', formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });
            setStatus('Erfolg: ' + response.data);
        } catch (error) {
            console.error(error);
            setStatus('Fehler beim Hochladen: ' + (error.response?.data || error.message));
        } finally {
            setIsUploading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto mt-10 p-6 bg-white rounded-xl shadow-md">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">Datenimport (Excel)</h2>

            <div className="mb-6 p-4 bg-blue-50 text-blue-700 rounded-md border border-blue-200">
                <p className="text-sm">Bitte laden Sie hier die Excel-Datei "Wissenschaftliche Arbeiten" hoch. Die Datei wird verarbeitet und die Datenbank aktualisiert.</p>
            </div>

            <form onSubmit={handleUpload} className="space-y-6">
                <div className="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center hover:border-indigo-500 transition-colors">
                    <input
                        type="file"
                        accept=".xlsx, .xls"
                        onChange={handleFileChange}
                        className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-indigo-50 file:text-indigo-700 hover:file:bg-indigo-100"
                    />
                </div>

                <div className="flex justify-end">
                    <button
                        type="submit"
                        disabled={isUploading}
                        className={`px-6 py-2 rounded-md text-white font-medium ${isUploading ? 'bg-gray-400' : 'bg-indigo-600 hover:bg-indigo-700'}`}
                    >
                        {isUploading ? 'Wird verarbeitet...' : 'Import starten'}
                    </button>
                </div>
            </form>

            {status && (
                <div className={`mt-4 p-4 rounded-md ${status.includes('Erfolg') ? 'bg-green-50 text-green-700' : 'bg-red-50 text-red-700'}`}>
                    {status}
                </div>
            )}
        </div>
    );
};

export default DateiImport;