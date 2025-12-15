import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import api from '../services/api'; // Dein Axios Service

const StatusBadge = ({ status }) => { /* ... Code bleibt gleich ... */ };

const ArbeitenListe = () => {
    const [arbeiten, setArbeiten] = useState([]);
    const [filteredArbeiten, setFilteredArbeiten] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const filterStatus = searchParams.get('status');

    useEffect(() => {
        // ECHTER API CALL
        const fetchArbeiten = async () => {
            try {
                // Endpoint der 'ArbeitMitDetails' zurückgibt
                const response = await api.get('/wissenschaftliche-arbeiten/details');
                setArbeiten(response.data);
                setIsLoading(false);
            } catch (error) {
                console.error("Fehler beim Laden der Arbeiten:", error);
                setIsLoading(false);
            }
        };
        fetchArbeiten();
    }, []);

    // Filter-Logik (muss angepasst werden, da Feldnamen vom Backend evtl. anders sind)
    useEffect(() => {
        if (filterStatus) {
            setFilteredArbeiten(arbeiten.filter(a => a.status?.toLowerCase() === filterStatus.toLowerCase()));
        } else {
            setFilteredArbeiten(arbeiten);
        }
    }, [filterStatus, arbeiten]);

    if (isLoading) return <div className="text-center mt-10">Lade Arbeiten aus Datenbank...</div>;

    return (
        <div className="space-y-6">
            {/* ... Header Code bleibt gleich ... */}

            <div className="bg-white shadow-sm border border-gray-200 rounded-lg overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    {/* ... Table Header bleibt gleich ... */}
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {filteredArbeiten.length > 0 ? (
                        filteredArbeiten.map((arbeit) => (
                            <tr key={arbeit.arbeitId} onClick={() => navigate(`/arbeiten/${arbeit.arbeitId}`)} className="hover:bg-indigo-50 cursor-pointer transition-colors">
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-600">{arbeit.titel}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {/* Backend liefert 'studierenderName' im ArbeitMitDetails Objekt */}
                                    {arbeit.studierenderName}
                                    <span className="text-gray-400 text-xs block">{arbeit.studiengang}</span>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap"><StatusBadge status={arbeit.status} /></td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-bold text-gray-700"> - </td>
                            </tr>
                        ))
                    ) : (
                        <tr><td colSpan="4" className="px-6 py-10 text-center">Keine Daten.</td></tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
export default ArbeitenListe;