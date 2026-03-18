import { createContext, createElement, PropsWithChildren, useContext, useEffect, useSyncExternalStore } from 'react';
import { getCurrentSession, signIn as signInRequest, signOut as signOutRequest, SessionResponse } from '../../features/auth/auth-api';

type AuthSnapshot = {
  session: SessionResponse | null;
  bootstrapped: boolean;
};

class AuthStore {
  private snapshot: AuthSnapshot = { session: null, bootstrapped: false };
  private listeners = new Set<() => void>();

  subscribe = (listener: () => void) => {
    this.listeners.add(listener);
    return () => this.listeners.delete(listener);
  };

  getSnapshot = () => this.snapshot;

  private setSnapshot(next: Partial<AuthSnapshot>) {
    this.snapshot = { ...this.snapshot, ...next };
    this.listeners.forEach((listener) => listener());
  }

  reset = () => {
    this.setSnapshot({ session: null, bootstrapped: false });
  };

  bootstrap = async () => {
    try {
      const session = await getCurrentSession();
      this.setSnapshot({ session, bootstrapped: true });
    } catch {
      this.setSnapshot({ session: null, bootstrapped: true });
    }
  };

  signIn = async (email: string, password: string) => {
    const session = await signInRequest(email, password);
    this.setSnapshot({ session, bootstrapped: true });
  };

  signOut = async () => {
    await signOutRequest();
    this.setSnapshot({ session: null, bootstrapped: true });
  };
}

const authStore = new AuthStore();
const AuthContext = createContext(authStore);

export function AuthProvider({ children }: PropsWithChildren) {
  useEffect(() => {
    authStore.reset();
    void authStore.bootstrap();
  }, []);

  return createElement(AuthContext.Provider, { value: authStore }, children);
}

export function useAuth() {
  const store = useContext(AuthContext);
  const snapshot = useSyncExternalStore(store.subscribe, store.getSnapshot, store.getSnapshot);

  return {
    ...snapshot,
    bootstrap: store.bootstrap,
    signIn: store.signIn,
    signOut: store.signOut,
  };
}

export function resetAuthStoreForTests() {
  authStore.reset();
}
