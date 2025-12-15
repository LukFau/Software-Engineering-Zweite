import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';

const StudierendenMaske = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const isNew = id === 'neu';
    const [formData, setFormData] = useState({ vorname: '', nachname: '', matrikelnummer: '', email: '', adresse: '', geburtsdatum: '' });

    useEffect(() => {
        if (!isNew) {
            api.get(`/studierende/${id}`).then(res => setFormData(res.data));
        }
    }, [id, isNew]);

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            if (isNew) await api.post('/studierende', formData);
            else await api.put(`/studierende/${id}`, formData);
            navigate('/studierende');
        } catch (e) { alert("Fehler beim Speichern"); }
    };

    const handleChange = (e) => setFormData({...formData, [e.target.name]: e.target.value});

    return (
        <div className="max-w-3xl mx-auto mt-6 bg-white p-6 shadow rounded-lg">
            <h1 className="text-2xl font-bold mb-6">{isNew ? 'Neuer Student' : 'Student bearbeiten'}</h1>
            <form onSubmit={handleSave} className="space-y-4">
                <input name="vorname" placeholder="Vorname" value={formData.vorname} onChange={handleChange} className="border p-2 w-full rounded" required />
                <input name="nachname" placeholder="Nachname" value={formData.nachname} onChange={handleChange} className="border p-2 w-full rounded" required />
                <input name="matrikelnummer" placeholder="Matrikelnummer" value={formData.matrikelnummer} onChange={handleChange} className="border p-2 w-full rounded" required />
                <input name="email" type="email" placeholder="E-Mail" value={formData.email} onChange={handleChange} className="border p-2 w-full rounded" required />
                <div className="flex justify-end gap-2">
                    <button type="button" onClick={() => navigate('/studierende')} className="px-4 py-2 border rounded">Abbrechen</button>
                    <button type="submit" className="px-4 py-2 bg-green-600 text-white rounded">Speichern</button>
                </div>
            </form>
        </div>
    );
};
export default StudierendenMaske;