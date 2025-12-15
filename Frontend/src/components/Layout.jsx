import React from 'react';
import { Link, useLocation } from 'react-router-dom';

// Icons ... (bleiben gleich wie zuvor, hier abgekürzt für Übersichtlichkeit)
const IconDashboard = () => <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z"></path></svg>;
const IconUsers = () => <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z"></path></svg>;
const IconAcademic = () => <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 14l9-5-9-5-9 5 9 5z"></path><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 14l6.16-3.422a12.083 12.083 0 01.665 6.479A11.952 11.952 0 0012 20.055a11.952 11.952 0 00-6.824-2.998 12.078 12.078 0 01.665-6.479L12 14z"></path></svg>;
const IconBook = () => <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253"></path></svg>;
const IconUpload = () => <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"></path></svg>;

const SidebarItem = ({ to, icon, label }) => {
    const location = useLocation();
    const isActive = location.pathname.startsWith(to) && to !== '/' || location.pathname === to;

    return (
        <Link
            to={to}
            className={`flex items-center space-x-3 px-6 py-3 transition-colors ${
                isActive ? 'bg-indigo-50 text-indigo-600 border-r-4 border-indigo-600' : 'text-gray-500 hover:bg-gray-50 hover:text-gray-900'
            }`}
        >
            <span className={`${isActive ? 'text-indigo-600' : 'text-gray-400'}`}>{icon}</span>
            <span className="font-medium">{label}</span>
        </Link>
    );
};

const Layout = ({ children }) => {
    return (
        <div className="flex h-screen bg-slate-50 font-sans">
            <div className="w-64 bg-white shadow-lg fixed h-full z-10 flex flex-col">
                {/* HIER: Klickbares Logo */}
                <Link to="/" className="h-16 flex items-center px-6 border-b border-gray-100 hover:bg-slate-50 transition-colors cursor-pointer">
                    <span className="text-2xl font-bold text-indigo-600 tracking-tight">TMS</span>
                    <span className="ml-2 text-xs font-semibold text-gray-400 uppercase tracking-wider mt-1">Manager</span>
                </Link>

                <div className="flex-1 overflow-y-auto py-4 space-y-1">
                    <p className="px-6 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2 mt-2">Übersicht</p>
                    <SidebarItem to="/" icon={<IconDashboard />} label="Dashboard" />
                    <p className="px-6 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2 mt-6">Verwaltung</p>
                    <SidebarItem to="/arbeiten" icon={<IconBook />} label="Arbeiten" />
                    <SidebarItem to="/studierende" icon={<IconAcademic />} label="Studierende" />
                    <SidebarItem to="/referenten" icon={<IconUsers />} label="Referenten" />
                    <p className="px-6 text-xs font-semibold text-gray-400 uppercase tracking-wider mb-2 mt-6">System</p>
                    <SidebarItem to="/import" icon={<IconUpload />} label="Datei Import" />
                </div>

                <div className="p-4 border-t border-gray-100">
                    <div className="flex items-center space-x-3">
                        <div className="h-8 w-8 rounded-full bg-indigo-100 flex items-center justify-center text-indigo-600 font-bold text-xs">AD</div>
                        <div><p className="text-sm font-medium text-gray-700">Admin User</p></div>
                    </div>
                </div>
            </div>
            <div className="flex-1 ml-64 flex flex-col min-w-0 overflow-hidden">
                <main className="flex-1 overflow-x-hidden overflow-y-auto bg-slate-50 p-8">{children}</main>
            </div>
        </div>
    );
};

export default Layout;