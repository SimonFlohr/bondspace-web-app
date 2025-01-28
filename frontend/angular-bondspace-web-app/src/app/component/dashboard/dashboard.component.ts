import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { Subscription } from 'rxjs';
import { SpaceService } from '../../service/space.service';

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
  userSpaces: any[] = [];

  constructor(
    private authService: AuthService,
    private spaceService: SpaceService,
    private router: Router
  ) {}

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

      // Fetch user's spaces
      this.spaceService.getUserSpaces().subscribe({
        next: (spaces) => {
          this.userSpaces = spaces;
        },
        error: (error) => {
          console.error('Failed to fetch spaces:', error);
        }
      });
  }

}
