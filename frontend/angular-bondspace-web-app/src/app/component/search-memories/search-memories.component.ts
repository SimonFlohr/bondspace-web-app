import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { SpaceService } from '../../service/space.service';
import { MemoryService } from '../../service/memory.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-search-memories',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './search-memories.component.html',
  styleUrl: './search-memories.component.css'
})
export class SearchMemoriesComponent implements OnInit {
  spaceId: number = 0;
  space: any = {};
  memories: any[] = [];
  filteredMemories: any[] = [];
  searchTerm: string = '';
  sortField: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';
  selectedMemory: any = null;
  currentUserName: string = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private spaceService: SpaceService,
    private memoryService: MemoryService,
    private authService: AuthService
  ) {
    this.route.params.subscribe(params => {
      this.spaceId = +params['id'];
      this.loadMemories();
    });
  }

  ngOnInit() {
    this.authService.getCurrentUser().subscribe({
      next: (user) => {
        this.currentUserName = user.firstName;
      },
      error: (error) => console.error('Error getting current user:', error)
    });
    this.spaceService.getSpaceDetails(this.spaceId).subscribe({
      next: (data) => {
        this.space = data;
      },
      error: (error) => console.error('Error loading space details:', error)
    });
  }

  isMemoryOwner(memory: any): boolean {
    return memory.uploadedBy.firstName === this.currentUserName;
  }

  editMemory(memory: any) {
    // Close the current modal
    const modalElement = document.getElementById('memoryModal');
    const closeButton = modalElement?.querySelector('[data-bs-dismiss="modal"]') as HTMLElement;
    closeButton?.click();

    // Navigate to edit route
    this.router.navigate(['/space', this.spaceId, 'edit-memory', memory.id]);
  }

  deleteMemory(memoryId: number) {
    if (confirm('Are you sure you want to delete this memory?')) {
      this.memoryService.deleteMemory(memoryId).subscribe({
        next: () => {
          // Close the modal
          const modalElement = document.getElementById('memoryModal');
          const closeButton = modalElement?.querySelector('[data-bs-dismiss="modal"]') as HTMLElement;
          closeButton?.click();

          // Reload memories to update the list
          this.loadMemories();
        },
        error: (error) => {
          console.error('Failed to delete memory:', error);
          alert(error.error?.message || 'Failed to delete memory');
        }
      });
    }
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

      switch (this.sortField) {
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
    this.selectedMemory = memory;
  }

  goBack() {
    this.router.navigate(['/space', this.spaceId]);
  }

  exportToCSV() {
    // Only export the filtered/searched results
    const memories = this.filteredMemories;

    // Define the headers
    const headers = [
      'Name',
      'Type',
      'Tags',
      'Created By',
      'Created At',
      'Updated At'
    ];

    // Transform the data into CSV format
    const rows = memories.map(memory => {
      // Format dates properly
      const createdAt = memory.createdAt ?
        new Date(memory.createdAt).toLocaleString('en-US', {
          year: 'numeric',
          month: 'numeric',
          day: 'numeric',
          hour: 'numeric',
          minute: 'numeric',
          second: 'numeric',
          hour12: true
        }) : '';

      const updatedAt = memory.updatedAt ?
        new Date(memory.updatedAt).toLocaleString('en-US', {
          year: 'numeric',
          month: 'numeric',
          day: 'numeric',
          hour: 'numeric',
          minute: 'numeric',
          second: 'numeric',
          hour12: true
        }) : '';

      return [
        memory.name,
        memory.type,
        (memory.tags || []).join('; '), // Join multiple tags with semicolon
        `${memory.uploadedBy.firstName} ${memory.uploadedBy.lastName}`,
        createdAt,
        updatedAt
      ];
    });

    // Combine headers and rows
    const csvContent = [
      headers.join(','),
      ...rows.map(row =>
        // Properly escape fields that might contain commas
        row.map(field => `"${field}"`).join(',')
      )
    ].join('\n');

    // Create a Blob with the CSV data
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });

    // Create a download link and trigger download
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);

    // Set the file name
    const fileName = `memories_export_${new Date().toISOString().slice(0, 10)}.csv`;

    link.setAttribute('href', url);
    link.setAttribute('download', fileName);
    link.style.visibility = 'hidden';

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }


}