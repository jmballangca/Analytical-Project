import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateMemoryQuestionComponent } from './create-memory-question.component';

describe('CreateMemoryQuestionComponent', () => {
  let component: CreateMemoryQuestionComponent;
  let fixture: ComponentFixture<CreateMemoryQuestionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreateMemoryQuestionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CreateMemoryQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
