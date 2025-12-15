import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';

const ReferentenListe = () => {
    const [refs, setRefs] = useState([]);
    useEffect(() => {
        api.get('/betreuer').then(res => setRefs(res.data));
    }, []);

    const navigate = useNavigate();

    return (
        <div className="space-y-6 mt-6">
            <div className="flex justify-between items-center">
                <h1 className="text-3xl font-bold text-gray-900">Referenten</h1>
                <Link to="/referenten/neu" className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 shadow-sm text-sm font-medium">+ Referent anlegen</Link>
            </div>
            <div className="bg-white shadow overflow-hidden sm:rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">E-Mail</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Rolle</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {refs.map((r) => (
                        <tr key={r.betreuerId} onClick={() => navigate(`/referenten/${r.betreuerId}`)} className="hover:bg-indigo-50 cursor-pointer">
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-600">{r.titel} {r.vorname} {r.nachname}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{r.email}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{r.rolle}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
export default ReferentenListe;