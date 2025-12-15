import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';

const DashboardCard = ({ title, value, type, onClick }) => {
    let colors = {
        geplant: { border: 'border-blue-500', text: 'text-blue-600', bgIcon: 'bg-blue-50' },
        bearbeitung: { border: 'border-yellow-500', text: 'text-yellow-600', bgIcon: 'bg-yellow-50' },
        korrektur: { border: 'border-orange-500', text: 'text-orange-600', bgIcon: 'bg-orange-50' },
        abgeschlossen: { border: 'border-green-500', text: 'text-green-600', bgIcon: 'bg-green-50' }
    };
    const style = colors[type] || colors.geplant;

    return (
        <div
            onClick={onClick}
            className="bg-white rounded-xl shadow-sm border border-gray-100 p-6 flex items-center transition-transform hover:scale-105 duration-200 cursor-pointer hover:shadow-md"
        >
            <div className={`p-4 rounded-full ${style.bgIcon} mr-4`}>
                <span className={`text-xl font-bold ${style.text}`}>#</span>
            </div>
            <div>
                <p className="text-sm font-medium text-gray-500 uppercase tracking-wide">{title}</p>
                <p className="text-3xl font-bold text-gray-800 mt-1">{value}</p>
            </div>
        </div>
    );
};

const Dashboard = () => {
    const [stats, setStats] = useState({ inBearbeitung: 0, abgeschlossen: 0, korrektur: 0, geplant: 0 });
    const [professorenLoad, setProfessorenLoad] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Parallel fetching für Performance
                const [arbeitenRes, betreuerRes, notenRes] = await Promise.all([
                    api.get('/wissenschaftliche-arbeiten'),
                    api.get('/betreuer'),
                    api.get('/notenbestandteile')
                ]);

                const arbeiten = arbeitenRes.data;
                const betreuer = betreuerRes.data;
                const noten = notenRes.data;

                // 1. Statistiken berechnen
                const newStats = {
                    inBearbeitung: arbeiten.filter(a => a.status === 'in Bearbeitung').length,
                    abgeschlossen: arbeiten.filter(a => a.status === 'abgeschlossen').length,
                    korrektur: arbeiten.filter(a => a.status === 'in Korrektur').length, // Status muss exakt matchen
                    geplant: arbeiten.filter(a => a.status === 'in Planung' || a.status === 'angemeldet').length
                };
                setStats(newStats);

                // 2. Auslastung berechnen (Anzahl Arbeiten pro Betreuer)
                // Da wir keine SWS API haben, zählen wir die Einträge in Notenbestandteil als Proxy
                const loadMap = {};
                noten.forEach(n => {
                    if (loadMap[n.betreuerId]) {
                        loadMap[n.betreuerId]++;
                    } else {
                        loadMap[n.betreuerId] = 1;
                    }
                });

                const profsWithLoad = betreuer.map(b => ({
                    id: b.betreuerId,
                    name: `${b.nachname}, ${b.vorname}`,
                    titel: b.titel,
                    count: loadMap[b.betreuerId] || 0,
                    limit: 20 // Fiktives Limit für die Anzeige
                })).sort((a, b) => b.count - a.count).slice(0, 5); // Top 5 anzeigen

                setProfessorenLoad(profsWithLoad);

            } catch (error) {
                console.error("Fehler beim Laden des Dashboards:", error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, []);

    const goToStatus = (status) => {
        navigate(`/arbeiten?status=${encodeURIComponent(status)}`);
    };

    if (isLoading) return <div className="flex justify-center items-center h-64"><div className="text-gray-400">Lade Live-Daten...</div></div>;

    return (
        <div className="space-y-8">
            <div>
                <h3 className="text-lg font-bold text-gray-700 mb-4">Übersicht Arbeiten</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                    <DashboardCard title="In Planung" value={stats.geplant} type="geplant" onClick={() => goToStatus('in Planung')} />
                    <DashboardCard title="In Bearbeitung" value={stats.inBearbeitung} type="bearbeitung" onClick={() => goToStatus('in Bearbeitung')} />
                    <DashboardCard title="In Korrektur" value={stats.korrektur} type="korrektur" onClick={() => goToStatus('korrektur')} />
                    <DashboardCard title="Abgeschlossen" value={stats.abgeschlossen} type="abgeschlossen" onClick={() => goToStatus('abgeschlossen')} />
                </div>
            </div>

            <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                <div className="px-6 py-5 border-b border-gray-100 flex justify-between items-center bg-white">
                    <h3 className="text-lg font-bold text-gray-800">Aktive Betreuungen (Top 5)</h3>
                    <Link to="/referenten" className="text-sm font-medium text-indigo-600 hover:text-indigo-800 bg-indigo-50 px-3 py-1 rounded-full transition-colors">Alle anzeigen &rarr;</Link>
                </div>
                <div className="overflow-x-auto">
                    <table className="min-w-full divide-y divide-gray-100">
                        <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Referent</th>
                            <th className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Anzahl Arbeiten</th>
                            <th className="px-6 py-4 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Status</th>
                        </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-100">
                        {professorenLoad.map((prof) => {
                            const isOverload = prof.count > prof.limit;
                            const percentage = Math.min((prof.count / prof.limit) * 100, 100);
                            return (
                                <tr key={prof.id} className="hover:bg-slate-50 transition-colors">
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <div className="flex items-center">
                                            <div className="h-10 w-10 rounded-full bg-slate-200 flex items-center justify-center text-slate-600 font-bold text-sm mr-3">
                                                {prof.name.charAt(0)}
                                            </div>
                                            <div>
                                                <div className="text-sm font-medium text-gray-900">{prof.name}</div>
                                                <div className="text-xs text-gray-500">{prof.titel}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap align-middle">
                                        <div className="w-full max-w-xs">
                                            <div className="flex justify-between text-xs mb-1">
                                                <span className="font-medium text-gray-700">{prof.count} Arbeiten</span>
                                                <span className="text-gray-400">Limit: {prof.limit}</span>
                                            </div>
                                            <div className="w-full bg-gray-200 rounded-full h-2">
                                                <div className={`h-2 rounded-full ${isOverload ? 'bg-red-500' : 'bg-green-500'}`} style={{ width: `${percentage}%` }}></div>
                                            </div>
                                        </div>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <span className={`px-3 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${isOverload ? 'bg-red-50 text-red-700 border border-red-100' : 'bg-green-50 text-green-700 border border-green-100'}`}>
                                            {isOverload ? 'Viele Betreuungen' : 'Verfügbar'}
                                        </span>
                                    </td>
                                </tr>
                            );
                        })}
                        {professorenLoad.length === 0 && (
                            <tr>
                                <td colSpan="3" className="px-6 py-4 text-center text-gray-500 text-sm">Keine Daten verfügbar</td>
                            </tr>
                        )}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;