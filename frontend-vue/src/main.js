import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import './index.css'
import App from './App.vue'
import Landing from './pages/Landing.vue'
import SignIn from './pages/SignIn.vue'
import CellarOverview from './pages/CellarOverview.vue'
import CellarView from './pages/CellarView.vue'
import { isAuthenticated } from './state/session'

const router = createRouter({
	history: createWebHistory(),
	routes: [
		{ path: '/', component: Landing, meta: { guestOnly: true } },
		{ path: '/signin', component: SignIn, meta: { guestOnly: true } },
		{ path: '/cellars', component: CellarOverview, meta: { requiresAuth: true } },
		{ path: '/cellars/:cellarId', component: CellarView, meta: { requiresAuth: true } },
	],
})

router.beforeEach((to) => {
	if (to.meta.requiresAuth && !isAuthenticated()) {
		return '/'
	}

	if (to.meta.guestOnly && isAuthenticated()) {
		return '/cellars'
	}

	return true
})

createApp(App).use(router).mount('#app')
