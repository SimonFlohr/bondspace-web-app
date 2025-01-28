import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { SpaceService } from '../../service/space.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-space-details',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './space-details.component.html',
  styleUrls: ['./space-details.component.css']
})
export class SpaceDetailsComponent implements OnInit {
  spaceId: number = 0;
  space: any = {};
  members: any[] = [];
  memories: any[] = [];
  
  constructor(
    private route: ActivatedRoute,
    private spaceService: SpaceService
  ) {}

  ngOnInit() {
    // Get the space ID from the route
    this.route.params.subscribe(params => {
      this.spaceId = +params['id']; // Convert string to number
      this.loadSpaceDetails();
    });
  }

  loadSpaceDetails() {
    this.spaceService.getSpaceDetails(this.spaceId).subscribe({
      next: (data) => {
        this.space = data;
      },
      error: (error) => console.error('Error loading space details:', error)
    });

    this.spaceService.getSpaceMembers(this.spaceId).subscribe({
      next: (data) => {
        this.members = data;
      },
      error: (error) => console.error('Error loading members:', error)
    });

    this.spaceService.getSpaceMemories(this.spaceId).subscribe({
      next: (data) => {
        this.memories = data;
      },
      error: (error) => console.error('Error loading memories:', error)
    });
  }
}