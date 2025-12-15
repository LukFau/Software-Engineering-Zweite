import React, { useState } from 'react';
import api from '../services/api';

const DateiImport = () => {
    const [file, setFile] = useState(null);
    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
    };

    const handleUpload = async (e) => {
        e.preventDefault();
        if (!file) {
            setMessage('Bitte wählen Sie eine Datei aus.');
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        setLoading(true);
        setMessage('');

        try {
            const response = await api.post('/import/upload', formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });
            setMessage(response.data);
        } catch (error) {
            console.error(error);
            setMessage('Fehler beim Upload: ' + (error.response?.data || error.message));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-2xl mx-auto mt-10 bg-white p-8 rounded-lg shadow-md">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">Excel Import</h2>
            <p className="mb-4 text-gray-600">Laden Sie hier die "Wissenschaftliche Arbeiten" Excel-Datei hoch.</p>

            <form onSubmit={handleUpload} className="space-y-4">
                <div className="border-2 border-dashed border-gray-300 rounded-md p-6 text-center">
                    <input
                        type="file"
                        accept=".xlsx"
                        onChange={handleFileChange}
                        className="block w-full text-sm text-gray-500
                        file:mr-4 file:py-2 file:px-4
                        file:rounded-full file:border-0
                        file:text-sm file:font-semibold
                        file:bg-indigo-50 file:text-indigo-700
                        hover:file:bg-indigo-100"
                    />
                </div>
                <button
                    type="submit"
                    disabled={loading}
                    className={`w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white 
                    ${loading ? 'bg-gray-400' : 'bg-indigo-600 hover:bg-indigo-700'}`}
                >
                    {loading ? 'Importiere...' : 'Hochladen & Importieren'}
                </button>
            </form>
            {message && (
                <div className={`mt-4 p-4 rounded ${message.includes('Fehler') ? 'bg-red-50 text-red-700' : 'bg-green-50 text-green-700'}`}>
                    {message}
                </div>
            )}
        </div>
    );
};

export default DateiImport;