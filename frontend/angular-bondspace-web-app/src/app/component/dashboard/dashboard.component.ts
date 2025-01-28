import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import { Subscription } from 'rxjs';
import { SpaceService } from '../../service/space.service';
import { UserNotificationService } from '../../service/user-notification.service';

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
  notifications: any[] = [];

  constructor(
    private authService: AuthService,
    private spaceService: SpaceService,
    private notificationService: UserNotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
      this.authSubscription = this.authService.isAuthenticated().subscribe({
        next: (auth) => {
          if (auth) {
            this.authService.getCurrentUser().subscribe(user => {
              this.userName = user.firstName;
            });
            this.loadNotifications();
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

  loadNotifications() {
    this.notificationService.getUserNotifications().subscribe({
      next: (notifications) => {
        this.notifications = notifications;
      },
      error: (error) => {
        console.error('Failed to load notifications:', error);
      }
    });
  }

  loadUserSpaces() {
    // Get user's spaces from the spaceService
    this.spaceService.getUserSpaces().subscribe({
      next: (spaces) => {
        this.userSpaces = spaces;
      },
      error: (error) => {
        console.error('Failed to fetch spaces:', error);
        alert(error.error?.message || 'Failed to load spaces');
      }
    });
  }

  acceptInvite(notificationId: number) {
    this.notificationService.acceptInvite(notificationId).subscribe({
      next: () => {
        this.loadNotifications(); // Refresh the notifications
        this.userSpaces = []; // Clear existing spaces
        this.loadUserSpaces(); // Reload spaces to show the newly accepted space
      },
      error: (error) => {
        console.error('Failed to accept invite:', error);
      }
    });
  }

  denyInvite(notificationId: number) {
    this.notificationService.denyInvite(notificationId).subscribe({
      next: () => {
        this.loadNotifications(); // Refresh the notifications
      },
      error: (error) => {
        console.error('Failed to deny invite:', error);
      }
    });
  }

}
