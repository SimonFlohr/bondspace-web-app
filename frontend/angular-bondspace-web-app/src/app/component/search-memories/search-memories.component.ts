import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { SpaceService } from '../../service/space.service';

@Component({
  selector: 'app-search-memories',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './search-memories.component.html',
  styleUrl: './search-memories.component.css'
})
export class SearchMemoriesComponent implements OnInit {
  spaceId: number = 0;
  memories: any[] = [];
  filteredMemories: any[] = [];
  searchTerm: string = '';
  sortField: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private spaceService: SpaceService
  ) {
    this.route.params.subscribe(params => {
      this.spaceId = +params['id'];
      this.loadMemories();
    });
  }

  ngOnInit() {
    // Initial load handled in constructor
  }

  loadMemories() {
    this.spaceService.getSpaceMemories(this.spaceId).subscribe({
      next: (memories) => {
        this.memories = memories;
        this.filteredMemories = [...memories];
        this.onSearch(); // Apply initial search/sort
      },
      error: (error) => {
        console.error('Failed to load memories:', error);
        alert(error.error?.message || 'Failed to load memories');
      }
    });
  }

  onSearch() {
    if (!this.searchTerm.trim()) {
      this.filteredMemories = [...this.memories];
    } else {
      const term = this.searchTerm.toLowerCase().trim();
      this.filteredMemories = this.memories.filter(memory =>
        memory.name.toLowerCase().includes(term) ||
        memory.tags.some((tag: string) => tag.toLowerCase().includes(term)) ||
        memory.type.toLowerCase().includes(term) ||
        `${memory.uploadedBy.firstName} ${memory.uploadedBy.lastName}`.toLowerCase().includes(term)
      );
    }
    
    if (this.sortField) {
      this.applySorting();
    }
  }

  sort(field: string) {
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.applySorting();
  }

  private applySorting() {
    this.filteredMemories.sort((a, b) => {
      let valueA, valueB;

      switch(this.sortField) {
        case 'uploadedBy':
          valueA = `${a.uploadedBy.firstName} ${a.uploadedBy.lastName}`.toLowerCase();
          valueB = `${b.uploadedBy.firstName} ${b.uploadedBy.lastName}`.toLowerCase();
          break;
        case 'tags':
          valueA = a.tags[0]?.toLowerCase() || '';
          valueB = b.tags[0]?.toLowerCase() || '';
          break;
        default:
          valueA = (a[this.sortField] || '').toString().toLowerCase();
          valueB = (b[this.sortField] || '').toString().toLowerCase();
      }

      if (valueA < valueB) return this.sortDirection === 'asc' ? -1 : 1;
      if (valueA > valueB) return this.sortDirection === 'asc' ? 1 : -1;
      return 0;
    });
  }

  viewMemory(memory: any) {
    // Use the same memory viewing logic as in space-details component
    this.router.navigate(['/space', this.spaceId], { 
      queryParams: { 
        openMemory: memory.id 
      }
    });
  }

  goBack() {
    this.router.navigate(['/space', this.spaceId]);
  }
}