import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';

const ReferentenMaske = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const isNew = id === 'neu';
    const [mode, setMode] = useState(isNew ? 'edit' : 'view');
    const [isLoading, setIsLoading] = useState(!isNew);

    const [formData, setFormData] = useState({
        titel: '',
        vorname: '',
        nachname: '',
        email: '',
        rolle: ''
    });

    useEffect(() => {
        if (!isNew) {
            api.get(`/betreuer/${id}`)
                .then(res => {
                    setFormData(res.data);
                    setIsLoading(false);
                })
                .catch(err => {
                    console.error("Fehler:", err);
                    setIsLoading(false);
                });
        }
    }, [id, isNew]);

    const handleChange = (e) => setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            if(isNew) {
                await api.post('/betreuer', formData);
                alert("Betreuer angelegt");
                navigate('/referenten');
            } else {
                await api.put(`/betreuer/${id}`, formData);
                alert("Betreuer aktualisiert");
                setMode('view');
            }
        } catch (e) {
            console.error(e);
            alert("Fehler beim Speichern");
        }
    };

    const handleDelete = async () => {
        if(!window.confirm("Wirklich löschen?")) return;
        try {
            await api.delete(`/betreuer/${id}`);
            navigate('/referenten');
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
                <h1 className="text-2xl font-bold text-gray-900">{isNew ? 'Neuen Referenten anlegen' : 'Referentendaten'}</h1>
                <div className="flex space-x-3">
                    <button onClick={() => navigate('/referenten')} className="px-4 py-2 border rounded-md text-sm bg-white hover:bg-gray-50">Zurück</button>
                    {mode === 'view' && <button onClick={() => setMode('edit')} className="px-4 py-2 bg-indigo-600 text-white rounded-md text-sm hover:bg-indigo-700">Bearbeiten</button>}
                    {mode === 'edit' && <button onClick={handleSave} className="px-4 py-2 bg-green-600 text-white rounded-md text-sm hover:bg-green-700">Speichern</button>}
                    {!isNew && <button onClick={handleDelete} className="px-4 py-2 bg-red-50 text-red-600 border border-red-200 rounded-md text-sm hover:bg-red-100">Löschen</button>}
                </div>
            </div>

            <form className="bg-white shadow sm:rounded-lg p-6 space-y-6">
                <div className="grid grid-cols-1 gap-y-6 gap-x-4 sm:grid-cols-6">
                    <div className="sm:col-span-2">
                        <label className="block text-sm font-medium text-gray-700">Titel</label>
                        <input type="text" name="titel" value={formData.titel} onChange={handleChange} readOnly={mode === 'view'} className={inputClass(mode === 'view')} placeholder="z.B. Prof. Dr." />
                    </div>
                    <div className="sm:col-span-2">
                        <label className="block text-sm font-medium text-gray-700">Vorname</label>
                        <input type="text" name="vorname" value={formData.vorname} onChange={handleChange} readOnly={mode === 'view'} className={inputClass(mode === 'view')} />
                    </div>
                    <div className="sm:col-span-2">
                        <label className="block text-sm font-medium text-gray-700">Nachname</label>
                        <input type="text" name="nachname" value={formData.nachname} onChange={handleChange} readOnly={mode === 'view'} className={inputClass(mode === 'view')} />
                    </div>
                    <div className="sm:col-span-4">
                        <label className="block text-sm font-medium text-gray-700">E-Mail</label>
                        <input type="email" name="email" value={formData.email} onChange={handleChange} readOnly={mode === 'view'} className={inputClass(mode === 'view')} />
                    </div>
                    <div className="sm:col-span-2">
                        <label className="block text-sm font-medium text-gray-700">Rolle</label>
                        <input type="text" name="rolle" value={formData.rolle} onChange={handleChange} readOnly={mode === 'view'} className={inputClass(mode === 'view')} placeholder="z.B. Professor" />
                    </div>
                </div>
            </form>
        </div>
    );
};

export default ReferentenMaske;