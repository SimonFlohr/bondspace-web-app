import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-create-space',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule
  ],
  templateUrl: './create-space.component.html',
  styleUrl: './create-space.component.css'
})
export class CreateSpaceComponent {
  spaceName: string = '';
  spaceDescription: string = '';

  onSubmit() {
    if (this.spaceName && this.spaceDescription) {
      // Trim the values to ensure we don't have extra whitespace
      const name = this.spaceName.trim();
      const description = this.spaceDescription.trim();
      
      // Additional validation (although maxlength in the template should prevent this)
      if (name.length <= 25 && description.length <= 140) {
        console.log('Space details:', {
          name,
          description
        });
        // We'll add the actual submission logic later
      }
    }
  }

}
