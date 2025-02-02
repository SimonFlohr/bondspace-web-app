import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchMemoriesComponent } from './search-memories.component';

describe('SearchMemoriesComponent', () => {
  let component: SearchMemoriesComponent;
  let fixture: ComponentFixture<SearchMemoriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchMemoriesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SearchMemoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
