import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout'; // Hier das neue Layout importieren

// Echte Seiten
import Dashboard from './pages/Dashboard';
import ArbeitenListe from './pages/ArbeitenListe';
import ArbeitenMaske from './pages/ArbeitenMaske';
import StudierendenListe from './pages/StudierendenListe';
import StudierendenMaske from './pages/StudierendenMaske';
import ReferentenListe from './pages/ReferentenListe';
import ReferentenMaske from './pages/ReferentenMaske';
import DateiImport from './pages/DateiImport';

function App() {
    return (
        <Router>
            {/* Das Layout umschließt nun alle Routen */}
            <Layout>
                <Routes>
                    <Route path="/" element={<Dashboard />} />

                    {/* Arbeiten */}
                    <Route path="/arbeiten" element={<ArbeitenListe />} />
                    <Route path="/arbeiten/neu" element={<ArbeitenMaske />} />
                    <Route path="/arbeiten/:id" element={<ArbeitenMaske />} />

                    {/* Studierende */}
                    <Route path="/studierende" element={<StudierendenListe />} />
                    <Route path="/studierende/neu" element={<StudierendenMaske />} />
                    <Route path="/studierende/:id" element={<StudierendenMaske />} />

                    {/* Referenten */}
                    <Route path="/referenten" element={<ReferentenListe />} />
                    <Route path="/referenten/neu" element={<ReferentenMaske />} />
                    <Route path="/referenten/:id" element={<ReferentenMaske />} />

                    {/* Import */}
                    <Route path="/import" element={<DateiImport />} />
                </Routes>
            </Layout>
        </Router>
    );
}

export default App;