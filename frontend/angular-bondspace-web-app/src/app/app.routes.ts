import { Router, RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { AuthGuard } from './guard/auth-guard.service';
import { UnauthGuard } from './guard/unauth-guard.service';
import { inject } from '@angular/core';
import { AuthService } from './service/auth.service';
import { map, take } from 'rxjs/operators';
import { AppComponent } from './app.component';
import { CreateSpaceComponent } from './component/create-space/create-space.component';
import { SpaceDetailsComponent } from './component/space-details/space-details.component';
import { CreateMemoryComponent } from './component/create-memory/create-memory.component';
import { EditMemoryComponent } from './component/edit-memory/edit-memory.component';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },
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
    },
    {
        path: 'create-space',
        component: CreateSpaceComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'space/:id',
        component: SpaceDetailsComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'space/:id/create-memory',
        component: CreateMemoryComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'space/:id/edit-memory/:memoryId',
        component: EditMemoryComponent,
        canActivate: [AuthGuard]
    }
];

export const AppRoutingModule = RouterModule.forRoot(routes);
