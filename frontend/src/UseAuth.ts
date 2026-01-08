import { useEffect, useState } from "react";
import axios from "axios";

export function UseAuth() {
    const [user, setUser] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        axios
            .get("/api/v1/auth/me")
            .then(res => setUser(res.data))
            .catch(() => setUser(null))
            .finally(() => setLoading(false));
    }, []);

    return { user, loading };
}
