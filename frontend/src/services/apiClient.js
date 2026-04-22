const DEFAULT_BASE_URL = "http://localhost:8080";

const configuredBaseUrl = import.meta.env.VITE_API_BASE_URL;

export const API_BASE_URL = (configuredBaseUrl || DEFAULT_BASE_URL).replace(/\/$/, "");

export async function apiFetch(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      ...(options.headers || {}),
    },
  });

  const contentType = response.headers.get("content-type") || "";
  const body = contentType.includes("application/json")
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    const message = typeof body === "string" ? body : body?.message || "Request failed";
    throw new Error(message);
  }

  return body;
}
