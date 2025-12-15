import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';

const ArbeitenListe = () => {
    const [arbeiten, setArbeiten] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        // Nutzt den Endpoint, der JOINs macht für Namen statt IDs
        api.get('/wissenschaftliche-arbeiten/details')
            .then(res => setArbeiten(res.data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div className="space-y-6 mt-6">
            <div className="flex justify-between items-center">
                <h1 className="text-3xl font-bold text-gray-900">Wissenschaftliche Arbeiten</h1>
                <Link to="/arbeiten/neu" className="px-4 py-2 bg-indigo-600 text-white rounded-md font-medium">+ Arbeit anlegen</Link>
            </div>
            <div className="bg-white shadow overflow-hidden sm:rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Titel</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Studierende/r</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {arbeiten.map((a) => (
                        <tr key={a.arbeitId} onClick={() => navigate(`/arbeiten/${a.arbeitId}`)} className="hover:bg-indigo-50 cursor-pointer">
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-600">{a.titel}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                {a.studierenderName} <span className="text-gray-400 text-xs block">{a.studiengang}</span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm"><span className="px-2 py-1 bg-gray-100 rounded-full">{a.status}</span></td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
export default ArbeitenListe;