import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';

const StudierendenMaske = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const isNew = id === 'neu';
    const [mode, setMode] = useState(isNew ? 'edit' : 'view');
    const [isLoading, setIsLoading] = useState(!isNew);

    const [formData, setFormData] = useState({
        vorname: '',
        nachname: '',
        matrikelnummer: '',
        email: '',
        adresse: '',
        geburtsdatum: ''
    });

    useEffect(() => {
        if (!isNew) {
            api.get(`/studierende/${id}`)
                .then(response => {
                    setFormData(response.data);
                    setIsLoading(false);
                })
                .catch(error => {
                    console.error("Fehler beim Laden:", error);
                    setIsLoading(false);
                });
        }
    }, [id, isNew]);

    const handleChange = (e) => setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            if (isNew) {
                await api.post('/studierende', formData);
                alert('Studierender erfolgreich angelegt!');
                navigate('/studierende');
            } else {
                await api.put(`/studierende/${id}`, formData);
                alert('Daten aktualisiert!');
                setMode('view');
            }
        } catch (error) {
            console.error("Speicherfehler:", error);
            alert("Fehler beim Speichern: " + error.message);
        }
    };

    const handleDelete = async () => {
        if(!window.confirm("Wirklich löschen?")) return;
        try {
            await api.delete(`/studierende/${id}`);
            navigate('/studierende');
        } catch (error) {
            console.error(error);
            alert("Fehler beim Löschen");
        }
    };

    if (isLoading) return <div className="p-8 text-center">Lade Daten...</div>;

    const inputClass = (isView) => isView
        ? "mt-1 block w-full bg-gray-50 border-gray-200 rounded-md shadow-sm sm:text-sm text-gray-600 cursor-default"
        : "mt-1 block w-full border border-gray-300 rounded-md shadow-sm sm:text-sm focus:ring-indigo-500 focus:border-indigo-500 p-2";

    return (
        <div className="max-w-3xl mx-auto mt-6">
            <div className="flex justify-between items-center border-b pb-4 mb-6">
                <h1 className="text-2xl font-bold text-gray-900">{isNew ? 'Neuen Studierenden anlegen' : `${formData.vorname} ${formData.nachname}`}</h1>
                <div className="flex space-x-3">
                    <button onClick={() => navigate('/studierende')} className="px-4 py-2 border rounded-md text-sm bg-white hover:bg-gray-50">Zurück</button>
                    {mode === 'view' && <button onClick={() => setMode('edit')} className="px-4 py-2 bg-indigo-600 text-white rounded-md text-sm hover:bg-indigo-700">Bearbeiten</button>}
                    {mode === 'edit' && <button onClick={handleSave} className="px-4 py-2 bg-green-600 text-white rounded-md text-sm hover:bg-green-700">Speichern</button>}
                    {!isNew && <button onClick={handleDelete} className="px-4 py-2 bg-red-50 text-red-600 border border-red-200 rounded-md text-sm hover:bg-red-100">Löschen</button>}
                </div>
            </div>

            <form className="bg-white shadow sm:rounded-lg p-6 space-y-6">
                <div className="grid grid-cols-1 gap-y-6 gap-x-4 sm:grid-cols-6">
                    <div className="sm:col-span-3">
                        <label className="block text-sm font-medium text-gray-700">Vorname</label>
                        <input type="text" name="vorname" value={formData.vorname} onChange={handleChange} readOnly={mode === 'view'} required className={inputClass(mode === 'view')} />
                    </div>

                    <div className="sm:col-span-3">
                        <label className="block text-sm font-medium text-gray-700">Nachname</label>
                        <input type="text" name="nachname" value={formData.nachname} onChange={handleChange} readOnly={mode === 'view'} required className={inputClass(mode === 'view')} />
                    </div>

                    <div className="sm:col-span-3">
                        <label className="block text-sm font-medium text-gray-700">Matrikelnummer</label>
                        <input type="text" name="matrikelnummer" value={formData.matrikelnummer} onChange={handleChange} readOnly={mode === 'view'} required className={inputClass(mode === 'view')} />
                    </div>

                    <div className="sm:col-span-3">
                        <label className="block text-sm font-medium text-gray-700">Geburtsdatum</label>
                        <input type="date" name="geburtsdatum" value={formData.geburtsdatum} onChange={handleChange} readOnly={mode === 'view'} className={inputClass(mode === 'view')} />
                    </div>

                    <div className="sm:col-span-6">
                        <label className="block text-sm font-medium text-gray-700">E-Mail</label>
                        <input type="email" name="email" value={formData.email} onChange={handleChange} readOnly={mode === 'view'} required className={inputClass(mode === 'view')} />
                    </div>

                    <div className="sm:col-span-6">
                        <label className="block text-sm font-medium text-gray-700">Adresse</label>
                        <input type="text" name="adresse" value={formData.adresse} onChange={handleChange} readOnly={mode === 'view'} className={inputClass(mode === 'view')} />
                    </div>
                </div>
            </form>
        </div>
    );
};

export default StudierendenMaske;