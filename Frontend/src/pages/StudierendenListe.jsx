import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';

const DUMMY_STUDENTS = [
    { id: 101, vorname: "Max", nachname: "Mustermann", matrikelnr: "123456", email: "max.mustermann@stud.thm.de", studiengang: "M.Sc. Logistics" },
    { id: 102, vorname: "Anna", nachname: "Fischer", matrikelnr: "654321", email: "anna.fischer@stud.thm.de", studiengang: "B.Eng. Bau" },
    { id: 103, vorname: "Markus", nachname: "Wolf", matrikelnr: "987654", email: "markus.wolf@stud.thm.de", studiengang: "B.Sc. Informatik" },
];

const StudierendenListe = () => {
    const [students, setStudents] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        // Später: await api.get('/studierende');
        setTimeout(() => {
            setStudents(DUMMY_STUDENTS);
            setIsLoading(false);
        }, 500);
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
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Studiengang</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">E-Mail</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {students.map((s) => (
                        <tr key={s.id} onClick={() => navigate(`/studierende/${s.id}`)} className="hover:bg-indigo-50 cursor-pointer transition-colors">
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-mono">{s.matrikelnr}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-indigo-600">{s.nachname}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{s.vorname}</td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{s.studiengang}</td>
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