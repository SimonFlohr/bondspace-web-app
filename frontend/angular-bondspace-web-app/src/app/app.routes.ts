import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { AuthGuard } from './guard/auth-guard.service';
import { UnauthGuard } from './guard/unauth-guard.service';

export const routes: Routes = [
    {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'login',
        component: LoginComponent,
        canActivate: [UnauthGuard]
    },
    {
        path: 'register',
        component: RegisterComponent,
        canActivate: [UnauthGuard]
    }
];

export const AppRoutingModule = RouterModule.forRoot(routes);
