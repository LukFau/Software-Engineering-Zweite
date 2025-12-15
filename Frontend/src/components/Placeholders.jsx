import React from 'react';

export const PlaceholderPage = ({ title }) => (
    <div className="bg-white shadow overflow-hidden sm:rounded-lg p-6 mt-6">
        <h1 className="text-2xl font-bold text-gray-900 mb-4">{title}</h1>
        <div className="p-4 bg-yellow-50 border-l-4 border-yellow-400">
            <p className="text-yellow-700">Diese Maske ist noch in Arbeit.</p>
        </div>
    </div>
);