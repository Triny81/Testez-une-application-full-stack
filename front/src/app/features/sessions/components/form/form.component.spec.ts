import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { of } from 'rxjs';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    },
  }

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
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
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

  /*********** INTEGRATION TESTS ***********/
  it('should disable the Save button if the form is invalid', () => {
    const saveButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(saveButton.disabled).toBe(true);

    component.sessionForm!.get('name')?.setValue('Yoga Class');
    component.sessionForm!.get('date')?.setValue('2025-01-01');
    component.sessionForm!.get('teacher_id')?.setValue(1);
    component.sessionForm!.get('description')?.setValue('A relaxing yoga session.');
    fixture.detectChanges();

    expect(saveButton.disabled).toBe(false);
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
