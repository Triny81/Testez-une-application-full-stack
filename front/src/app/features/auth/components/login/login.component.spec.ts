import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const authServiceMock = {
    login: jest.fn().mockReturnValue(of({ token: 'fake-token' })),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService, { provide: AuthService, useValue: authServiceMock }],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*********** UNIT TESTS ***********/ 
  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should have a form with email and password controls', () => {
    expect(component.form.contains('email')).toBe(true);
    expect(component.form.contains('password')).toBe(true);
  });

  it('should make email control required', () => {
    const emailControl = component.form.get('email');
    emailControl?.setValue('');
    expect(emailControl?.valid).toBeFalsy();
    expect(emailControl?.errors?.['required']).toBeTruthy();
  });

  it('should validate email format', () => {
    const emailControl = component.form.get('email');
    emailControl?.setValue('invalid-email');
    expect(emailControl?.valid).toBeFalsy();
    expect(emailControl?.errors?.['email']).toBeTruthy();
  });

  it('should make password control required', () => {
    const passwordControl = component.form.get('password');
    passwordControl?.setValue('');
    expect(passwordControl?.valid).toBeFalsy();
    expect(passwordControl?.errors?.['required']).toBeTruthy();
  });

  it('should toggle password visibility', () => {
    const initialHideValue = component.hide;
    component.hide = !initialHideValue;
    expect(component.hide).toBe(!initialHideValue);
  });

  it('should disable the submit button if the form is invalid', () => {
    component.form.get('email')?.setValue('');
    component.form.get('password')?.setValue('');
    fixture.detectChanges();
  
    const button = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(button.disabled).toBe(true); // Le bouton devrait être désactivé
  });

  it('should call submit method when form is submitted', () => {
    const spySubmit = jest.spyOn(component, 'submit');
    component.form.get('email')?.setValue('test@example.com');
    component.form.get('password')?.setValue('123456');
  
    fixture.detectChanges();
  
    const form = fixture.nativeElement.querySelector('form');
    form.dispatchEvent(new Event('submit'));
  
    expect(spySubmit).toHaveBeenCalled(); // Vérifie que `submit` a été appelé
    expect(authServiceMock.login).toHaveBeenCalledWith({
      email: 'test@example.com',
      password: '123456',
    }); // Vérifie que le service a été appelé avec les bonnes données
  });

  it('should display error message when onError is true', () => {
    component.onError = true;
    fixture.detectChanges();
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).toBeTruthy();
    expect(errorElement.textContent).toContain('An error occurred');
  });

  /*********** INTEGRATION TESTS ***********/ 
  it('should complete the login flow successfully', () => {
    // Simulate user input
    component.form.get('email')?.setValue('user@example.com');
    component.form.get('password')?.setValue('password123');

    // Trigger form submission
    const form = fixture.nativeElement.querySelector('form');
    form.dispatchEvent(new Event('submit'));

    // Assert the service is called with correct data
    expect(authServiceMock.login).toHaveBeenCalledWith({
      email: 'user@example.com',
      password: 'password123',
    });

    // Simulate response processing
    fixture.detectChanges();

    // Ensure no error message is displayed
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).toBeNull();
  });

  it('should display an error message if login fails', () => {
    // Mock service to return an error
    authServiceMock.login.mockReturnValue(throwError(() => new Error('Invalid credentials')));

    // Simulate user input
    component.form.get('email')?.setValue('user@example.com');
    component.form.get('password')?.setValue('wrongpassword');

    // Trigger form submission
    const form = fixture.nativeElement.querySelector('form');
    form.dispatchEvent(new Event('submit'));

    // Simulate response processing
    fixture.detectChanges();

    // Assert error message is displayed
    const errorElement = fixture.nativeElement.querySelector('.error');
    expect(errorElement).toBeTruthy();
    expect(errorElement.textContent).toContain('An error occurred');
  });
});
