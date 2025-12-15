import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';

const ReferentenMaske = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const isNew = id === 'neu';
    const [formData, setFormData] = useState({ titel: '', vorname: '', nachname: '', email: '', rolle: '' });

    useEffect(() => {
        if (!isNew) api.get(`/betreuer/${id}`).then(res => setFormData(res.data));
    }, [id, isNew]);

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            if (isNew) await api.post('/betreuer', formData);
            else await api.put(`/betreuer/${id}`, formData);
            navigate('/referenten');
        } catch (e) { alert("Fehler"); }
    };

    const handleChange = (e) => setFormData({...formData, [e.target.name]: e.target.value});

    return (
        <div className="max-w-3xl mx-auto mt-6 bg-white p-6 shadow rounded-lg">
            <h1 className="text-2xl font-bold mb-6">{isNew ? 'Neuer Referent' : 'Referent bearbeiten'}</h1>
            <form onSubmit={handleSave} className="grid grid-cols-2 gap-4">
                <input name="titel" placeholder="Titel" value={formData.titel} onChange={handleChange} className="border p-2 rounded" />
                <input name="rolle" placeholder="Rolle (z.B. Professor)" value={formData.rolle} onChange={handleChange} className="border p-2 rounded" />
                <input name="vorname" placeholder="Vorname" value={formData.vorname} onChange={handleChange} className="border p-2 rounded" required />
                <input name="nachname" placeholder="Nachname" value={formData.nachname} onChange={handleChange} className="border p-2 rounded" required />
                <input name="email" type="email" placeholder="E-Mail" value={formData.email} onChange={handleChange} className="border p-2 rounded col-span-2" />

                <div className="col-span-2 flex justify-end gap-2 mt-4">
                    <button type="button" onClick={() => navigate('/referenten')} className="px-4 py-2 border rounded">Abbrechen</button>
                    <button type="submit" className="px-4 py-2 bg-green-600 text-white rounded">Speichern</button>
                </div>
            </form>
        </div>
    );
};
export default ReferentenMaske;