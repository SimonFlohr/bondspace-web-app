// edit-memory.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { MemoryService } from '../../service/memory.service';

@Component({
  selector: 'app-edit-memory',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './edit-memory.component.html',
  styleUrls: ['./edit-memory.component.css']
})
export class EditMemoryComponent implements OnInit {
  spaceId: number = 0;
  memoryId: number = 0;
  name: string = '';
  tag: string = '';
  textContent: string = '';
  wordCount: number = 0;
  existingTags: string[] = [];

  constructor(
    private memoryService: MemoryService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.route.params.subscribe(params => {
      this.spaceId = +params['id'];
      this.memoryId = +params['memoryId'];
    });
  }

  ngOnInit() {
    // Load the memory details
    this.memoryService.getMemoryById(this.memoryId).subscribe({
      next: (memory) => {
        this.name = memory.name;
        this.tag = memory.tags[0]; // Assuming single tag for now
        this.textContent = memory.textContent;
        this.checkWordCount();
      },
      error: (error) => {
        console.error('Error loading memory:', error);
        alert(error.error?.message || 'Failed to load memory');
        this.router.navigate(['/space', this.spaceId]);
      }
    });

    // Load existing tags
    this.loadTags();
  }

  cancelEdit() {
    this.router.navigate(['/space', this.spaceId]);
  }

  checkWordCount() {
    // Split by whitespace and filter out empty strings
    const words = this.textContent.trim().split(/\s+/).filter(word => word.length > 0);
    this.wordCount = words.length;
  }

  loadTags() {
    this.memoryService.getSpaceTags(this.spaceId).subscribe({
      next: (tags) => {
        this.existingTags = tags;
      },
      error: (error) => {
        console.error('Failed to load tags:', error);
      }
    });
  }

  // Rest of the methods similar to CreateMemoryComponent
  // Add updateMemory method:
  onSubmit() {
    if (this.name && this.tag && this.textContent && this.wordCount <= 70) {
      this.memoryService.updateMemory(
        this.memoryId,
        this.name,
        [this.tag],
        this.textContent
      ).subscribe({
        next: () => {
          this.router.navigate(['/space', this.spaceId]);
        },
        error: (error) => {
          console.error('Failed to update memory:', error);
          alert(error.error?.message || 'Failed to update memory');
        }
      });
    }
  }
}