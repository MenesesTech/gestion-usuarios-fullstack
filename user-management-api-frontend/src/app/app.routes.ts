import { Routes } from '@angular/router';
import { MenuSidebarComponent } from './shared/ui/menuSidebar/menu-sidebar.component';
import { LoginComponent } from './authentication/login/login.component';
import { authGuard } from './authentication/guards/auth.guard';
import { DashboardComponent } from './features/users/components/dashboard.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard],
  },
];
