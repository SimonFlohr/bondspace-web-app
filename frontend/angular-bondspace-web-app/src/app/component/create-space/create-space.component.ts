import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { SpaceService } from '../../service/space.service';

@Component({
  selector: 'app-create-space',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './create-space.component.html',
  styleUrls: ['./create-space.component.css']
})
export class CreateSpaceComponent {
  spaceName: string = '';
  spaceDescription: string = '';

  constructor(
    private spaceService: SpaceService,
    private router: Router
  ) {}

  onSubmit() {
    if (this.spaceName && this.spaceDescription) {
      // Trim the values to ensure we don't have extra whitespace
      const name = this.spaceName.trim();
      const description = this.spaceDescription.trim();
      
      // Additional validation
      if (name.length <= 25 && description.length <= 140) {
        this.spaceService.createSpace(name, description).subscribe({
          next: (response) => {
            console.log('Space created successfully:', response);
            this.router.navigate(['/dashboard']);
          },
          error: (error) => {
            console.error('Failed to create space:', error);
            alert(error.error?.message || 'Failed to create space');
          }
        });
      }
    }
  }
}