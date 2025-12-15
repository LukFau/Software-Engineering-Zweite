import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

const StudierendenMaske = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const isNew = id === 'neu';
    const [mode, setMode] = useState(isNew ? 'edit' : 'view');
    const [isLoading, setIsLoading] = useState(!isNew);

    const [formData, setFormData] = useState({
        vorname: '', nachname: '', matrikelnr: '', email: '', studiengang: ''
    });

    useEffect(() => {
        if (!isNew) {
            setTimeout(() => {
                setFormData({
                    vorname: "Max", nachname: "Mustermann", matrikelnr: "123456",
                    email: "max.mustermann@stud.thm.de", studiengang: "M.Sc. Logistics"
                });
                setIsLoading(false);
            }, 500);
        }
    }, [id, isNew]);

    const handleChange = (e) => setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));

    const handleSave = (e) => {
        e.preventDefault();
        console.log("Speichere Student:", formData);
        setMode('view');
    };

    if (isLoading) return <div className="p-8 text-center">Lade Daten...</div>;

    const inputClass = (isView) => isView
        ? "mt-1 block w-full bg-gray-50 border-gray-200 rounded-md shadow-sm sm:text-sm text-gray-600 cursor-default"
        : "mt-1 block w-full border border-gray-300 rounded-md shadow-sm sm:text-sm focus:ring-indigo-500 focus:border-indigo-500 p-2";

    return (
        <div className="max-w-3xl mx-auto mt-6">
            <div className="flex justify-between items-center border-b pb-4 mb-6">
                <h1 className="text-2xl font-bold text-gray-900">{isNew ? 'Neuen Studierenden anlegen' : 'Studierendendaten'}</h1>
                <div className="flex space-x-3">
                    <button onClick={() => navigate('/studierende')} className="px-4 py-2 border rounded-md text-sm bg-white hover:bg-gray-50">Zurück</button>
                    {mode === 'view' && <button onClick={() => setMode('edit')} className="px-4 py-2 bg-indigo-600 text-white rounded-md text-sm hover:bg-indigo-700">Bearbeiten</button>}
                    {mode === 'edit' && <button onClick={handleSave} className="px-4 py-2 bg-green-600 text-white rounded-md text-sm hover:bg-green-700">Speichern</button>}
                </div>
            </div>

            <form className="bg-white shadow sm:rounded-lg p-6 space-y-6">
                <div className="grid grid-cols-1 gap-y-6 gap-x-4 sm:grid-cols-6">
                    <div className="sm:col-span-3">
                        <label className="block text-sm font-medium text-gray-700">Vorname</label>
                        <input type="text" name="vorname" value={formData.vorname} onChange={handleChange} readOnly={mode === 'view'} className={inputClass(mode === 'view')} />
                    </div>

                    <div className="sm:col-span-3">
                        <label className="block text-sm font-medium text-gray-700">Nachname</label>
                        <input type="text" name="nachname" value={formData.nachname} onChange={handleChange} readOnly={mode === 'view'} className={inputClass(mode === 'view')} />
                    </div>

                    <div className="sm:col-span-3">
                        <label className="block text-sm font-medium text-gray-700">Matrikelnummer</label>
                        <input type="text" name="matrikelnr" value={formData.matrikelnr} onChange={handleChange} readOnly={mode === 'view'} className={inputClass(mode === 'view')} />
                    </div>

                    <div className="sm:col-span-3">
                        <label className="block text-sm font-medium text-gray-700">Studiengang (Pflicht)</label>
                        {/* Hier könnte später ein Dropdown mit Studiengängen aus dem Backend hin */}
                        <input type="text" name="studiengang" value={formData.studiengang} onChange={handleChange} readOnly={mode === 'view'} required className={inputClass(mode === 'view')} placeholder="z.B. B.Sc. Informatik" />
                    </div>

                    <div className="sm:col-span-6">
                        <label className="block text-sm font-medium text-gray-700">E-Mail (Pflicht)</label>
                        <input type="email" name="email" value={formData.email} onChange={handleChange} readOnly={mode === 'view'} required className={inputClass(mode === 'view')} />
                    </div>
                </div>
            </form>
        </div>
    );
};

export default StudierendenMaske;