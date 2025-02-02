import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { SpaceService } from '../../service/space.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../service/auth.service';
import { MemoryService } from '../../service/memory.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-space-details',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './space-details.component.html',
  styleUrls: ['./space-details.component.css']
})
export class SpaceDetailsComponent implements OnInit {
  spaceId: number = 0;
  space: any = {};
  members: any[] = [];
  memories: any[] = [];
  emailAddress: string = '';
  isOwner: boolean = false;
  currentUserName: string = '';
  selectedMemory: any = null;
  spaceNotifications: any[] = [];
  
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private spaceService: SpaceService,
    private authService: AuthService,
    private memoryService: MemoryService
  ) {}

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.spaceId = +params['id'];
      // Get current user's name first
      this.authService.getCurrentUser().subscribe({
        next: (user) => {
          this.currentUserName = user.firstName;
          this.loadSpaceDetails();
        },
        error: (error) => console.error('Error getting current user:', error)
      });
    });
  }

  loadSpaceDetails() {
    // Load space details
    this.spaceService.getSpaceDetails(this.spaceId).subscribe({
      next: (data) => {
        this.space = data;
      },
      error: (error) => console.error('Error loading space details:', error)
    });

    this.spaceService.getSpaceNotifications(this.spaceId).subscribe({
      next: (data) => {
        // Take only the last 3 notifications
        this.spaceNotifications = data.slice(-3);
      },
      error: (error) => console.error('Error loading notifications:', error)
    });

    // Load members and check if current user is owner
    this.spaceService.getSpaceMembers(this.spaceId).subscribe({
      next: (data) => {
        this.members = data;
        // Check if the current user is the owner by matching the firstName
        this.isOwner = this.members.some(member => 
          member.role === 'OWNER' && member.firstName === this.currentUserName
        );
        console.log('Current user:', this.currentUserName);
        console.log('Members:', this.members);
        console.log('Is owner:', this.isOwner);
      },
      error: (error) => console.error('Error loading members:', error)
    });

    // Load memories
    this.spaceService.getSpaceMemories(this.spaceId).subscribe({
      next: (data) => {
        this.memories = data;
      },
      error: (error) => console.error('Error loading memories:', error)
    });
  }

  deleteSpace() {
    if (confirm('Are you sure you want to delete this space? This action cannot be undone.')) {
      this.spaceService.deleteSpace(this.spaceId).subscribe({
        next: () => {
          alert('Space deleted successfully');
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          console.error('Error deleting space:', error);
          alert(error.error?.message || 'Failed to delete space');
        }
      });
    }
  }

  inviteUser() {
    if (this.emailAddress) {
      this.spaceService.inviteUser(this.spaceId, this.emailAddress).subscribe({
        next: (response) => {
          alert('Invitation sent successfully');
          this.emailAddress = ''; // Reset form
          // Optionally refresh the members list
          this.loadSpaceDetails();
        },
        error: (error) => {
          if (error.error?.message === 'User has already been invited to this space') {
            alert('This user has already been invited to the space');
          } else {
            alert(error.error?.message || 'Failed to send invitation');
          }
        }
      });
    }
  }

  selectMemory(memory: any) {
    this.selectedMemory = memory;
  }

  isMemoryOwner(memory: any): boolean {
    return memory.uploadedBy.firstName === this.currentUserName;
  }

  deleteMemory(memoryId: number) {
    if (confirm('Are you sure you want to delete this memory?')) {
      this.memoryService.deleteMemory(memoryId).subscribe({
        next: () => {
          this.loadSpaceDetails();
          const modalElement = document.getElementById('memoryModal');
          const closeButton = modalElement?.querySelector('[data-bs-dismiss="modal"]') as HTMLElement;
          closeButton?.click();
        },
        error: (error) => {
          console.error('Failed to delete memory:', error);
          alert(error.error?.message || 'Failed to delete memory');
        }
      });
    }
  }

  editMemory(memory: any) {
    // Close the current modal
    const modalElement = document.getElementById('memoryModal');
    const closeButton = modalElement?.querySelector('[data-bs-dismiss="modal"]') as HTMLElement;
    closeButton?.click();
    
    // Navigate to edit route with memory ID
    this.router.navigate(['/space', this.spaceId, 'edit-memory', memory.id]);
  }

  deleteNotification(notificationId: number) {
    this.spaceService.deleteNotification(this.spaceId, notificationId).subscribe({
      next: () => {
        this.spaceNotifications = this.spaceNotifications
          .filter(n => n.id !== notificationId);
      },
      error: (error) => {
        console.error('Failed to delete notification:', error);
        alert(error.error?.message || 'Failed to delete notification');
      }
    });
  }
}