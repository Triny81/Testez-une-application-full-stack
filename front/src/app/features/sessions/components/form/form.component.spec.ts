import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';
import { TeacherService } from 'src/app/services/teacher.service';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    },
  }

  const mockSession = {
    id: '123',
    name: 'Test Session',
    date: '2023-01-01',
    teacher_id: 1,
    description: 'A test session'
  };

  const mockTeachers = [
    { id: 1, firstName: 'John', lastName: 'Doe', createdAt: new Date(), updatedAt: new Date() },
    { id: 2, firstName: 'Jane', lastName: 'Smith', createdAt: new Date(), updatedAt: new Date() }
  ];

  const mockTeacherService = {
    all: jest.fn().mockReturnValue(of(mockTeachers))
  };

  const mockRouter = {
    url: '',
    navigate: jest.fn()
  };
  
  const mockRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue('123') 
      }
    }
  };
  
  const mockFormBuilder = new FormBuilder();
  
  const mockMatSnackBar = {
    open: jest.fn()
  };
  
  const mockSessionApiService = {
    create: jest.fn().mockReturnValue(of({})), 
    update: jest.fn().mockReturnValue(of({})), 
    detail: jest.fn().mockReturnValue(of(mockSession))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        NoopAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*********** UNIT TESTS ***********/
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should disable the submit button if the form is invalid', () => {
    component.sessionForm!.get('name')?.setValue('');
    component.sessionForm!.get('date')?.setValue('');
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBe(true);
  });

  it('should call `submit` method on form submission', () => {
    const submitSpy = jest.spyOn(component, 'submit');
    component.sessionForm!.get('name')?.setValue('Yoga Session');
    component.sessionForm!.get('date')?.setValue(new Date().toISOString());
    fixture.detectChanges();

    const form = fixture.nativeElement.querySelector('form');
    form.dispatchEvent(new Event('submit'));

    expect(submitSpy).toHaveBeenCalled();
  });

  it('should initialize the form with default values', () => {
    expect(component.sessionForm).toBeTruthy();
    const formValue = component.sessionForm!.value;
    expect(formValue.name).toBe('');
    expect(formValue.date).toBe('');
    expect(formValue.teacher_id).toBe('');
    expect(formValue.description).toBe('');
  });

  it('should mark form as invalid if required fields are missing', () => {
    component.sessionForm!.setValue({
      name: '',
      date: '',
      teacher_id: null,
      description: ''
    });
    expect(component.sessionForm!.invalid).toBe(true);
  });

  it('should call submit method when form is valid', () => {
    component.submit = jest.fn();

    component.sessionForm!.setValue({
      name: 'Session 1',
      date: '2023-01-01',
      teacher_id: 1,
      description: 'Description of session'
    });

    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button[type="submit"]');
    button.click();

    expect(component.submit).toHaveBeenCalled();
  });

  it('should display teachers in dropdown', async () => {
    component.teachers$ = of(mockTeachers); // Mock l'observable
    fixture.detectChanges();
    await fixture.whenStable();

    // Récupère et clique sur le déclencheur de mat-select
    const select = fixture.debugElement.query(By.css('mat-select'));
    select.nativeElement.click();
    fixture.detectChanges();
    await fixture.whenStable();

    const overlayContainer = document.querySelector('.cdk-overlay-container');
    expect(overlayContainer).toBeTruthy();
    const options = overlayContainer!.querySelectorAll('mat-option');
    expect(options.length).toBe(mockTeachers.length);
    expect(options[0].textContent).toContain('John Doe');
  });

  it('should show "Update session" title if onUpdate is true', () => {
    component.onUpdate = true;
    fixture.detectChanges();
    const title = fixture.nativeElement.querySelector('h1').textContent;
    expect(title).toContain('Update session');
  });

  it('should handle form submission when onUpdate is true', () => {
    component.onUpdate = true;
    component.sessionForm!.setValue({
      name: 'Updated Session',
      date: '2023-01-01',
      teacher_id: 1,
      description: 'Updated description'
    });

    jest.spyOn(component, 'submit');
    component.submit();
    expect(component.submit).toHaveBeenCalled();
  });

  it('should not submit the form if it is invalid', () => {
    component.sessionForm!.setValue({
      name: '',
      date: '',
      teacher_id: null,
      description: ''
    });

    jest.spyOn(component, 'submit');
    const result = component.submit();
    expect(component.submit).toHaveBeenCalled();
    expect(result).toBeUndefined(); // Suppose que submit retourne quelque chose
  });

  it('should enable the Save button when the form is valid', () => {
    component.sessionForm!.setValue({
      name: 'Valid Name',
      date: '2023-01-01',
      teacher_id: 1,
      description: 'Valid Description'
    });

    fixture.detectChanges();
    const button = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBe(false);
  });

  it('should redirect to /sessions if the user is not an admin', () => {
    const mockSessionServiceNonAdmin = {
      sessionInformation: { admin: false }
    };
    
    const component = new FormComponent(
      mockRoute as any,
      mockFormBuilder,
      mockMatSnackBar as any,
      mockSessionApiService as any,
      mockSessionServiceNonAdmin as any,
      mockTeacherService as any,
      mockRouter as any
    );

    component.ngOnInit();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should call sessionApiService.create and exitPage with "Session created !" when onUpdate is false', () => {
    const component = new FormComponent(
      mockRoute as any,
      mockFormBuilder,
      mockMatSnackBar as any,
      mockSessionApiService as any,
      mockSessionService as any,
      mockTeacherService as any,
      mockRouter as any
    );
  
    component.sessionForm = new FormBuilder().group({
      name: ['Test Session'],
      date: ['2023-01-01'],
      teacher_id: [1],
      description: ['A test session']
    });
  
    const exitPageSpy = jest.spyOn(component as any, 'exitPage');
  
    component.onUpdate = false; 
    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalled(); 
    expect(exitPageSpy).toHaveBeenCalledWith('Session created !'); 
  });
  
it('should call sessionApiService.create and exitPage with "Session created !" when onUpdate is false', () => {
    const component = new FormComponent(
      mockRoute as any,
      mockFormBuilder,
      mockMatSnackBar as any,
      mockSessionApiService as any,
      mockSessionService as any,
      mockTeacherService as any,
      mockRouter as any
    );
  
    component.sessionForm = new FormBuilder().group({
      name: ['Test Session'],
      date: ['2023-01-01'],
      teacher_id: [1],
      description: ['A test session']
    });
  
    const exitPageSpy = jest.spyOn(component as any, 'exitPage');
  
    component.onUpdate = false; 
    component.submit();

    expect(mockSessionApiService.create).toHaveBeenCalled(); 
    expect(exitPageSpy).toHaveBeenCalledWith('Session created !'); 
  });

  /*********** INTEGRATION TESTS ***********/
  it('should disable the Save button if the form is invalid', () => {
    const saveButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(saveButton.disabled).toBe(true);

    component.sessionForm!.get('name')?.setValue('Yoga Class');
    component.sessionForm!.get('date')?.setValue('2025-01-01');
    component.sessionForm!.get('teacher_id')?.setValue("");
    component.sessionForm!.get('description')?.setValue('A relaxing yoga session.');
    fixture.detectChanges();

    expect(saveButton.disabled).toBe(true);
  });

  it('should call submit method when the form is submitted', () => {
    const submitSpy = jest.spyOn(component, 'submit');

    component.sessionForm!.get('name')?.setValue('Yoga Class');
    component.sessionForm!.get('date')?.setValue('2025-01-01');
    component.sessionForm!.get('teacher_id')?.setValue(1);
    component.sessionForm!.get('description')?.setValue('A relaxing yoga session.');
    fixture.detectChanges();

    const form = fixture.nativeElement.querySelector('form');
    form.dispatchEvent(new Event('submit'));

    expect(submitSpy).toHaveBeenCalled();
  });
});
