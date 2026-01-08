// import LoginPage from "@/pages/LoginPage";
// import Dashboard from "@/Dashboard";
// import { UseAuth } from "@/UseAuth";
//
// export default function App() {
//     const { user, loading } = UseAuth();
//
//     if (loading) {
//         return <p>Loading...</p>;
//     }
//
//     if (!user) {
//         return <LoginPage />;
//     }
//
//     return <Dashboard/>;
// }

import Dashboard from "./Dashboard.tsx";

export default function App() {
    return (
        <div>
            <Dashboard />
        </div>
    );
}