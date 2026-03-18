import { useEffect, useState } from 'react';
import { CellarSummary, getCellars } from '../auth/auth-api';

export function useCellarsQuery() {
  const [data, setData] = useState<CellarSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;
    void getCellars()
      .then((response) => {
        if (!cancelled) {
          setData(response.items);
          setError(null);
        }
      })
      .catch((queryError) => {
        if (!cancelled) {
          setError(queryError instanceof Error ? queryError.message : 'Unable to load cellars.');
        }
      })
      .finally(() => {
        if (!cancelled) {
          setLoading(false);
        }
      });

    return () => {
      cancelled = true;
    };
  }, []);

  return { data, loading, error };
}
