import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useSearchParams } from 'react-router-dom';

const DUMMY_ARBEITEN = [
    { id: 1, titel: "Einsatz von KI in der Logistik", status: "abgeschlossen", studiengang: "M.Sc. Global Logistics", erstpruefer: "Prof. Dr. Müller", student: "Max Mustermann", note: "1.3" },
    { id: 2, titel: "Nachhaltigkeit im Bauwesen", status: "in Bearbeitung", studiengang: "B.Eng. Bauingenieurwesen", erstpruefer: "Prof. Dr. Schmidt", student: "Anna Fischer", note: "-" },
    { id: 3, titel: "Sicherheit in Cloud-Systemen", status: "in Planung", studiengang: "B.Sc. Informatik dual", erstpruefer: "Prof. Dr. Weber", student: "Markus Wolf", note: "-" },
    { id: 4, titel: "Kulturelle Einflüsse auf Marketing", status: "korrektur", studiengang: "B.A. Int. Business", erstpruefer: "Prof. Dr. Wagner", student: "Lena Becker", note: "-" },
];

const StatusBadge = ({ status }) => {
    let style = 'bg-gray-100 text-gray-800';
    if (status === 'abgeschlossen') style = 'bg-green-100 text-green-800';
    if (status === 'in Bearbeitung') style = 'bg-yellow-100 text-yellow-800';
    if (status === 'korrektur') style = 'bg-orange-100 text-orange-800';
    if (status === 'in Planung') style = 'bg-blue-100 text-blue-800';
    if (status === 'abgebrochen') style = 'bg-red-100 text-red-800';

    return <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${style}`}>{status}</span>;
};

const ArbeitenListe = () => {
    const [arbeiten, setArbeiten] = useState([]);
    const [filteredArbeiten, setFilteredArbeiten] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    // Der Filter aus der URL (z.B. ?status=in Bearbeitung)
    const filterStatus = searchParams.get('status');

    useEffect(() => {
        // Daten laden
        setTimeout(() => {
            setArbeiten(DUMMY_ARBEITEN);
            setIsLoading(false);
        }, 300);
    }, []);

    // Filter anwenden, wenn Daten geladen sind oder sich der URL-Parameter ändert
    useEffect(() => {
        if (filterStatus) {
            setFilteredArbeiten(arbeiten.filter(a => a.status.toLowerCase() === filterStatus.toLowerCase()));
        } else {
            setFilteredArbeiten(arbeiten);
        }
    }, [filterStatus, arbeiten]);

    if (isLoading) return <div className="text-center mt-10">Arbeiten werden geladen...</div>;

    return (
        <div className="space-y-6">
            <div className="flex justify-between items-center">
                <div className="flex items-center space-x-4">
                    <h1 className="text-3xl font-bold text-gray-900">Wissenschaftliche Arbeiten</h1>
                    {filterStatus && (
                        <span className="px-3 py-1 rounded-full bg-indigo-100 text-indigo-700 text-sm font-medium flex items-center">
                            Filter: {filterStatus}
                            <Link to="/arbeiten" className="ml-2 text-indigo-900 hover:text-indigo-500 font-bold">×</Link>
                        </span>
                    )}
                </div>
                <Link to="/arbeiten/neu" className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700">
                    + Arbeit anlegen
                </Link>
            </div>

            <div className="bg-white shadow-sm border border-gray-200 rounded-lg overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Titel</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Studierende/r</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Note</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {filteredArbeiten.length > 0 ? (
                        filteredArbeiten.map((arbeit) => (
                            <tr key={arbeit.id} onClick={() => navigate(`/arbeiten/${arbeit.id}`)} className="hover:bg-indigo-50 cursor-pointer transition-colors">
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-600">{arbeit.titel}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {arbeit.student} <span className="text-gray-400 text-xs block">{arbeit.studiengang}</span>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap"><StatusBadge status={arbeit.status} /></td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-bold text-gray-700">{arbeit.note}</td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="4" className="px-6 py-10 text-center text-gray-500">Keine Arbeiten gefunden.</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default ArbeitenListe;