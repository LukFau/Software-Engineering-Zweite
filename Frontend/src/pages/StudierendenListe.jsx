import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../services/api';

const StudierendenListe = () => {
    const [students, setStudents] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchStudents = async () => {
            try {
                const response = await api.get('/studierende');
                setStudents(response.data);
            } catch (error) {
                console.error("Fehler beim Laden der Studierenden:", error);
            } finally {
                setIsLoading(false);
            }
        };
        fetchStudents();
    }, []);

    if (isLoading) return <div className="text-center mt-10">Lade Studierende...</div>;

    return (
        <div className="space-y-6 mt-6">
            <div className="flex justify-between items-center">
                <h1 className="text-3xl font-bold text-gray-900">Studierende</h1>
                <Link to="/studierende/neu" className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 shadow-sm text-sm font-medium">
                    + Studierenden anlegen
                </Link>
            </div>

            <div className="bg-white shadow overflow-hidden sm:rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Matrikelnr.</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Nachname</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Vorname</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">E-Mail</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {students.map((s) => (
                        <tr key={s.studierendenId} onClick={() => navigate(`/studierende/${s.studierendenId}`)} className="hover:bg-indigo-50 cursor-pointer transition-colors">
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-mono">{s.matrikelnummer}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-600">{s.nachname}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{s.vorname}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{s.email}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default StudierendenListe;