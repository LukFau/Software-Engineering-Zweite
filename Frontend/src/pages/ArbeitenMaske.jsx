import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';

const ArbeitenMaske = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const isNew = id === 'neu';
    const [mode, setMode] = useState(isNew ? 'edit' : 'view');
    const [isLoading, setIsLoading] = useState(true);

    // Listen für Dropdowns
    const [students, setStudents] = useState([]);
    const [referenten, setReferenten] = useState([]);

    const [formData, setFormData] = useState({
        titel: '',
        studierendenId: '',
        studiengangId: 1, // Dummy Default
        pruefungsordnungId: 1, // Dummy Default
        semesterId: 1, // Dummy Default
        typ: 'Bachelor',
        status: 'in Planung',
        // Zeitdaten
        startDatum: '',
        abgabeDatum: '',
        kolloquiumsDatum: '',
        // Betreuer (IDs)
        referent1Id: '',
        referent2Id: ''
    });

    useEffect(() => {
        const loadBaseData = async () => {
            try {
                const [studRes, refRes] = await Promise.all([
                    api.get('/studierende'),
                    api.get('/betreuer')
                ]);
                setStudents(studRes.data);
                setReferenten(refRes.data);

                if (!isNew) {
                    // Arbeit laden
                    const arbeitRes = await api.get(`/wissenschaftliche-arbeiten/${id}`);
                    const arbeit = arbeitRes.data;

                    // Versuchen Zeitdaten zu laden (wenn vorhanden)
                    let zeiten = {};
                    try {
                        const zeitRes = await api.get(`/zeitdaten/arbeit/${id}`);
                        if (zeitRes.data) zeiten = zeitRes.data;
                    } catch (e) { console.log("Keine Zeitdaten gefunden"); }

                    setFormData({
                        titel: arbeit.titel,
                        studierendenId: arbeit.studierendenId,
                        studiengangId: arbeit.studiengangId,
                        pruefungsordnungId: arbeit.pruefungsordnungId,
                        semesterId: arbeit.semesterId,
                        typ: arbeit.typ,
                        status: arbeit.status,
                        startDatum: zeiten.anfangsdatum || '',
                        abgabeDatum: zeiten.abgabedatum || '',
                        kolloquiumsDatum: zeiten.kolloquiumsdatum || '',
                        referent1Id: '', // TODO: Müsste aus Notenbestandteil geladen werden
                        referent2Id: ''
                    });
                }
            } catch (error) {
                console.error("Fehler beim Laden:", error);
            } finally {
                setIsLoading(false);
            }
        };
        loadBaseData();
    }, [id, isNew]);

    const handleChange = (e) => setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            let arbeitId = id;

            // 1. Arbeit speichern
            const arbeitPayload = {
                titel: formData.titel,
                studierendenId: parseInt(formData.studierendenId),
                studiengangId: parseInt(formData.studiengangId),
                pruefungsordnungId: parseInt(formData.pruefungsordnungId),
                semesterId: parseInt(formData.semesterId),
                typ: formData.typ,
                status: formData.status
            };

            if (isNew) {
                const res = await api.post('/wissenschaftliche-arbeiten', arbeitPayload);
                arbeitId = res.data.arbeitId;
            } else {
                await api.put(`/wissenschaftliche-arbeiten/${id}`, arbeitPayload);
            }

            // 2. Zeitdaten speichern (Create or Update Logik vereinfacht: immer create wenn neu)
            if (formData.startDatum || formData.abgabeDatum) {
                const zeitPayload = {
                    arbeitId: parseInt(arbeitId),
                    anfangsdatum: formData.startDatum || null,
                    abgabedatum: formData.abgabeDatum || null,
                    kolloquiumsdatum: formData.kolloquiumsDatum || null
                };
                // Hinweis: Hier bräuchte man eigentlich eine ID Check Logik für Update
                // Wir senden einfach einen Post für neue Zeitdaten
                await api.post('/zeitdaten', zeitPayload);
            }

            // 3. Betreuer verknüpfen (Optional: Über Notenbestandteil erstellen)
            if (formData.referent1Id) {
                await api.post('/notenbestandteile', {
                    arbeitId: parseInt(arbeitId),
                    betreuerId: parseInt(formData.referent1Id),
                    rolle: 'Referent',
                    noteArbeit: 0, noteKolloquium: 0, gewichtung: 0
                });
            }

            alert("Speichern erfolgreich!");
            navigate('/arbeiten');

        } catch (error) {
            console.error(error);
            alert("Fehler beim Speichern: " + error.message);
        }
    };

    if (isLoading) return <div className="p-8 text-center">Lade Datensatz...</div>;

    // Helper Styles
    const labelStyle = "block text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1";
    const inputStyle = (isView) => isView
        ? "block w-full text-gray-900 bg-transparent border-b border-gray-200 py-2 focus:outline-none cursor-default"
        : "block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm";

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
            <div className="flex justify-between items-start mb-8">
                <div>
                    <h1 className="text-2xl font-bold text-gray-900">
                        {isNew ? 'Neue Arbeit anlegen' : formData.titel}
                    </h1>
                    <p className="text-sm text-gray-500 mt-1">Status: {formData.status}</p>
                </div>
                <div className="flex space-x-3">
                    <button onClick={() => navigate('/arbeiten')} className="px-4 py-2 border bg-white rounded-md text-sm text-gray-600">Abbrechen</button>
                    {mode === 'view' ? (
                        <button onClick={() => setMode('edit')} className="px-4 py-2 bg-indigo-600 text-white rounded-md text-sm">Bearbeiten</button>
                    ) : (
                        <button onClick={handleSave} className="px-4 py-2 bg-green-600 text-white rounded-md text-sm">Speichern</button>
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
                        {mode === 'edit' ? (
                            <select name="studierendenId" value={formData.studierendenId} onChange={handleChange} className={inputStyle(false)}>
                                <option value="">Bitte wählen...</option>
                                {students.map(s => (
                                    <option key={s.studierendenId} value={s.studierendenId}>
                                        {s.nachname}, {s.vorname} ({s.matrikelnummer})
                                    </option>
                                ))}
                            </select>
                        ) : (
                            <input type="text" value={students.find(s => s.studierendenId === formData.studierendenId)?.nachname || formData.studierendenId} readOnly className={inputStyle(true)} />
                        )}
                    </div>
                    <div>
                        <label className={labelStyle}>Typ</label>
                        <select name="typ" value={formData.typ} onChange={handleChange} disabled={mode === 'view'} className={inputStyle(mode === 'view')}>
                            <option>Bachelor</option>
                            <option>Master</option>
                        </select>
                    </div>
                </Section>

                {/* 2. Prüfer */}
                <Section title="Prüfer & Betreuung">
                    <div>
                        <label className={labelStyle}>Erstprüfer</label>
                        <select name="referent1Id" value={formData.referent1Id} onChange={handleChange} disabled={mode === 'view'} className={inputStyle(mode === 'view')}>
                            <option value="">Bitte wählen...</option>
                            {referenten.map(r => (
                                <option key={r.betreuerId} value={r.betreuerId}>{r.nachname}, {r.vorname}</option>
                            ))}
                        </select>
                    </div>
                    <div>
                        <label className={labelStyle}>Zweitprüfer</label>
                        <select name="referent2Id" value={formData.referent2Id} onChange={handleChange} disabled={mode === 'view'} className={inputStyle(mode === 'view')}>
                            <option value="">Bitte wählen...</option>
                            {referenten.map(r => (
                                <option key={r.betreuerId} value={r.betreuerId}>{r.nachname}, {r.vorname}</option>
                            ))}
                        </select>
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
                        <label className={labelStyle}>Aktueller Status</label>
                        <select name="status" value={formData.status} onChange={handleChange} disabled={mode === 'view'} className={inputStyle(mode === 'view')}>
                            <option>in Planung</option>
                            <option>in Bearbeitung</option>
                            <option>abgeschlossen</option>
                            <option>abgebrochen</option>
                        </select>
                    </div>
                </Section>
            </form>
        </div>
    );
};

export default ArbeitenMaske;