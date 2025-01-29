// create-memory.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MemoryService } from '../../service/memory.service';

@Component({
  selector: 'app-create-memory',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-memory.component.html',
  styleUrls: ['./create-memory.component.css']
})
export class CreateMemoryComponent implements OnInit {
  name: string = '';
  tag: string = '';
  textContent: string = '';
  spaceId: number = 0;
  wordCount: number = 0;
  existingTags: string[] = [];
  showNewTagInput: boolean = false;
  newTag: string = '';

  constructor(
    private memoryService: MemoryService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Get the spaceId from the current URL
    this.route.params.subscribe(params => {
      this.spaceId = +params['id'];
    });
  }

  ngOnInit() {
    // Load existing tags when component initializes
    this.loadTags();
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

  checkWordCount() {
    // Split by whitespace and filter out empty strings
    const words = this.textContent.trim().split(/\s+/).filter(word => word.length > 0);
    this.wordCount = words.length;
  }

  toggleNewTagInput() {
    this.showNewTagInput = !this.showNewTagInput;
    if (!this.showNewTagInput) {
      this.newTag = '';
    }
  }

  selectTag(event: Event) {
    this.tag = (event.target as HTMLSelectElement).value;
  }

  addNewTag() {
    if (this.newTag.trim()) {
      this.tag = this.newTag.trim();
      if (!this.existingTags.includes(this.tag)) {
        this.existingTags.push(this.tag);
      }
      this.newTag = '';
      this.showNewTagInput = false;
    }
  }

  onSubmit() {
    if (this.name && this.tag && this.textContent && this.wordCount <= 70) {
      this.memoryService.createMemory(
        this.spaceId,
        this.name,
        [this.tag],
        this.textContent
      ).subscribe({
        next: () => {
          this.router.navigate(['/space', this.spaceId]);
        },
        error: (error) => {
          console.error('Failed to create memory:', error);
          alert(error.error?.message || 'Failed to create memory');
        }
      });
    }
  }
}