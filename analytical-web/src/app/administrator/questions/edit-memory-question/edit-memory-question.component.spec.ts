import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditMemoryQuestionComponent } from './edit-memory-question.component';

describe('EditMemoryQuestionComponent', () => {
  let component: EditMemoryQuestionComponent;
  let fixture: ComponentFixture<EditMemoryQuestionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditMemoryQuestionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditMemoryQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
