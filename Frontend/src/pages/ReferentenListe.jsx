import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';

const DUMMY_REFS = [
    { id: 1, vorname: "Hans", nachname: "Müller", titel: "Prof. Dr.", email: "hans.mueller@thm.de", sws: 14.5 },
    { id: 2, vorname: "Sarah", nachname: "Schmidt", titel: "Prof. Dr.", email: "sarah.schmidt@thm.de", sws: 19.0 },
];

const ReferentenListe = () => {
    const [refs, setRefs] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        setTimeout(() => {
            setRefs(DUMMY_REFS);
            setIsLoading(false);
        }, 500);
    }, []);

    if (isLoading) return <div className="text-center mt-10">Lade Referenten...</div>;

    return (
        <div className="space-y-6 mt-6">
            <div className="flex justify-between items-center">
                <h1 className="text-3xl font-bold text-gray-900">Referenten</h1>
                <Link to="/referenten/neu" className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 shadow-sm text-sm font-medium">
                    + Referent anlegen
                </Link>
            </div>
            <div className="bg-white shadow overflow-hidden sm:rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Titel</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">E-Mail</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Aktuelles Deputat</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {refs.map((r) => (
                        <tr key={r.id} onClick={() => navigate(`/referenten/${r.id}`)} className="hover:bg-indigo-50 cursor-pointer transition-colors">
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{r.titel}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-600">{r.nachname}, {r.vorname}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{r.email}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-bold">{r.sws} SWS</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default ReferentenListe;