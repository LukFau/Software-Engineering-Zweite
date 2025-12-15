import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';

const ArbeitenMaske = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const isNew = id === 'neu';
    const [mode, setMode] = useState(isNew ? 'edit' : 'view');
    const [isLoading, setIsLoading] = useState(!isNew);

    const [formData, setFormData] = useState({
        titel: '', studentId: '', studentName: '', studiengang: '', referent1Id: '', referent2Id: '',
        startDatum: '', abgabeDatum: '', korrekturEnde: '', status: 'in Planung',
        noteArbeit: '', noteKolloquium: '', swsDeputat: 0.0, istAbgebrochen: false
    });

    useEffect(() => {
        if (!isNew) {
            api.get(`/studierende/${id}`)
                .then(res => {
                    setFormData({
                        vorname: res.data.vorname,
                        nachname: res.data.nachname,
                        matrikelnr: res.data.matrikelnummer, // Mapping beachten!
                        email: res.data.email,
                        // ...
                    });
                    setIsLoading(false);
                })
                .catch(err => console.error(err));
        }
    }, [id, isNew]);

    const handleChange = (e) => setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
    const handleSave = async (e) => {
        e.preventDefault();

        // Daten für Backend mappen (Keys müssen zum Java-Model passen)
        const payload = {
            vorname: formData.vorname,
            nachname: formData.nachname,
            matrikelnummer: formData.matrikelnr,
            email: formData.email,
            // geburtsdatum etc...
        };

        try {
            if (isNew) {
                await api.post('/studierende', payload);
                alert("Student angelegt!");
                navigate('/studierende');
            } else {
                await api.put(`/studierende/${id}`, payload);
                alert("Student aktualisiert!");
                setMode('view');
            }
        } catch (error) {
            console.error("Fehler beim Speichern:", error);
            alert("Fehler beim Speichern.");
        }
    };

    if (isLoading) return <div className="p-8 text-center">Lade Datensatz...</div>;

    // Helper Styles
    const labelStyle = "block text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1";
    const inputStyle = (isView) => isView
        ? "block w-full text-gray-900 bg-transparent border-b border-gray-200 py-2 focus:outline-none cursor-default"
        : "block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm";

    // Section Component
    const Section = ({ title, children }) => (
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden mb-6">
            <div className="bg-gray-50 px-4 py-3 border-b border-gray-200">
                <h3 className="text-sm font-bold text-gray-700 uppercase">{title}</h3>
            </div>
            <div className="p-6 grid grid-cols-1 md:grid-cols-2 gap-6">
                {children}
            </div>
        </div>
    );

    return (
        <div className="max-w-5xl mx-auto pb-12">

            {/* Header Area */}
            <div className="flex justify-between items-start mb-8">
                <div>
                    <h1 className="text-2xl font-bold text-gray-900">
                        {isNew ? 'Neue Arbeit anlegen' : formData.titel || 'Arbeit bearbeiten'}
                    </h1>
                    <p className="text-sm text-gray-500 mt-1">
                        Status: <span className={`font-semibold ${formData.status === 'in Bearbeitung' ? 'text-yellow-600' : 'text-gray-700'}`}>{formData.status}</span>
                    </p>
                </div>
                <div className="flex space-x-3">
                    <button onClick={() => navigate('/arbeiten')} className="px-4 py-2 border bg-white rounded-md text-sm text-gray-600 hover:bg-gray-50">Abbrechen</button>
                    {mode === 'view' ? (
                        <button onClick={() => setMode('edit')} className="px-4 py-2 bg-indigo-600 text-white rounded-md text-sm hover:bg-indigo-700">Bearbeiten</button>
                    ) : (
                        <button onClick={handleSave} className="px-4 py-2 bg-green-600 text-white rounded-md text-sm hover:bg-green-700">Speichern</button>
                    )}
                </div>
            </div>

            <form>
                {/* 1. Basisdaten */}
                <Section title="Zuordnung & Thema">
                    <div className="col-span-1 md:col-span-2">
                        <label className={labelStyle}>Titel der Arbeit</label>
                        <input type="text" name="titel" value={formData.titel} onChange={handleChange} readOnly={mode === 'view'} className={inputStyle(mode === 'view')} />
                    </div>
                    <div>
                        <label className={labelStyle}>Studierende/r</label>
                        {mode === 'edit' && isNew ? (
                            <select name="studentId" onChange={handleChange} className={inputStyle(false)}>
                                <option value="">Bitte wählen...</option>
                                <option value="101">Max Mustermann (Logistics)</option>
                            </select>
                        ) : (
                            <input type="text" value={formData.studentName} readOnly className={inputStyle(true)} />
                        )}
                    </div>
                    <div>
                        <label className={labelStyle}>Studiengang</label>
                        <input type="text" value={formData.studiengang} readOnly className={inputStyle(true)} />
                    </div>
                </Section>

                {/* 2. Prüfer */}
                <Section title="Prüfer & Betreuung">
                    <div>
                        <label className={labelStyle}>Erstprüfer</label>
                        {mode === 'edit' ? (
                            <select name="referent1Id" value={formData.referent1Id} onChange={handleChange} className={inputStyle(false)}>
                                <option value="1">Prof. Dr. Müller</option>
                                <option value="2">Prof. Dr. Schmidt</option>
                            </select>
                        ) : (
                            <input type="text" value="Prof. Dr. Müller" readOnly className={inputStyle(true)} />
                        )}
                    </div>
                    <div>
                        <label className={labelStyle}>Zweitprüfer</label>
                        {mode === 'edit' ? (
                            <select name="referent2Id" value={formData.referent2Id} onChange={handleChange} className={inputStyle(false)}>
                                <option value="3">Prof. Dr. Weber</option>
                            </select>
                        ) : (
                            <input type="text" value="Prof. Dr. Schmidt" readOnly className={inputStyle(true)} />
                        )}
                    </div>
                </Section>

                {/* 3. Termine & Status */}
                <Section title="Fristen & Termine">
                    <div>
                        <label className={labelStyle}>Startdatum</label>
                        <input type="date" name="startDatum" value={formData.startDatum} onChange={handleChange} readOnly={mode === 'view'} className={inputStyle(mode === 'view')} />
                    </div>
                    <div>
                        <label className={labelStyle}>Abgabedatum</label>
                        <input type="date" name="abgabeDatum" value={formData.abgabeDatum} onChange={handleChange} readOnly={mode === 'view'} className={inputStyle(mode === 'view')} />
                    </div>
                    <div>
                        <label className={labelStyle}>Ende der Korrektur (Ziel)</label>
                        <input type="date" name="korrekturEnde" value={formData.korrekturEnde} onChange={handleChange} readOnly={mode === 'view'} className={inputStyle(mode === 'view')} />
                    </div>
                    <div>
                        <label className={labelStyle}>Aktueller Status</label>
                        {mode === 'edit' ? (
                            <select name="status" value={formData.status} onChange={handleChange} className={inputStyle(false)}>
                                <option>in Planung</option>
                                <option>in Bearbeitung</option>
                                <option>korrektur</option>
                                <option>abgeschlossen</option>
                                <option>abgebrochen</option>
                            </select>
                        ) : (
                            <div className="py-2"><span className="px-2 py-1 bg-gray-100 rounded text-sm">{formData.status}</span></div>
                        )}
                    </div>
                </Section>

                {/* 4. Benotung (Nur sichtbar, wenn nicht neu) */}
                {!isNew && (
                    <Section title="Ergebnisse & Deputat">
                        <div>
                            <label className={labelStyle}>Note Arbeit</label>
                            <input type="text" name="noteArbeit" value={formData.noteArbeit} onChange={handleChange} readOnly={mode === 'view'} className={inputStyle(mode === 'view')} placeholder="-" />
                        </div>
                        <div>
                            <label className={labelStyle}>Note Kolloquium</label>
                            <input type="text" name="noteKolloquium" value={formData.noteKolloquium} readOnly className={inputStyle(true)} placeholder="(wird über Protokoll erfasst)" />
                        </div>
                        <div>
                            <label className={labelStyle}>Angerechnetes Deputat (SWS)</label>
                            <input type="number" step="0.1" name="swsDeputat" value={formData.swsDeputat} readOnly className={inputStyle(true)} />
                        </div>
                    </Section>
                )}
            </form>
        </div>
    );
};

export default ArbeitenMaske;