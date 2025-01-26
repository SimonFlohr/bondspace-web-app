import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit, OnDestroy {

  isAuthenticated = false;
  private authSubscription?: Subscription;
  userName: string = '';

  constructor(private authService: AuthService,  private router: Router) {}

  ngOnInit(): void {
    // Store the subscription so we can clean it up later
    this.authSubscription = this.authService.isAuthenticated().subscribe({
      next: (auth) => {
        console.log('Auth state changed:', auth);
        this.isAuthenticated = auth;
        if (auth) {
          this.authService.getCurrentUser().subscribe(user => {
            this.userName = user.firstName;
          });
        }
      },
      error: (error) => {
        console.error('Auth subscription error:', error);
        this.isAuthenticated = false;
      }
    });
  }

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        console.log('Logged out successfully');
        this.router.navigate(['/login']);
      },
      error: (error) => console.error('Logout failed:', error)
    });
  }

  ngOnDestroy(): void {
    // Clean up subscription when component is destroyed
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

}
