import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {

  private authSubscription?: Subscription;
  userName = '';
  userUserSpaces = [];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
      this.authSubscription = this.authService.isAuthenticated().subscribe({
        next: (auth) => {
          if (auth) {
            this.authService.getCurrentUser().subscribe(user => {
              this.userName = user.firstName;
            });
          }
        }
      });
  }

}
