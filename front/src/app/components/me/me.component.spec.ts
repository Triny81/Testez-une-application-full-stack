import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule } from '@angular/router/testing';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn(),
  }

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of({ firstName: 'John', lastName: 'Doe', email: 'john.doe@example.com', admin: false })),
    delete: jest.fn(),
  };

  const mockRouter = {
    navigate: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule,
        NoopAnimationsModule 
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }, 
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user data on init', () => {
    const mockUser = { firstName: 'John', lastName: 'Doe', email: 'john.doe@example.com', admin: false };
    mockUserService.getById.mockReturnValue(of(mockUser));

    component.ngOnInit();
    fixture.detectChanges();

    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(mockUser);
  });

  it('should call back method when back button is clicked', () => {
    const backSpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(backSpy).toHaveBeenCalled();
  });

  it('should call delete and logOut methods on account deletion', fakeAsync(() => {
    const snackBar = TestBed.inject(MatSnackBar);
    const snackBarSpy = jest.spyOn(snackBar, 'open');
    
    const router = TestBed.inject(Router);
    const routerNavigateSpy = jest.spyOn(router, 'navigate');
    
    mockUserService.delete.mockReturnValue(of({}));
  
    component.delete();
    tick(); // Résout les tâches asynchrones liées à l'Observable
    tick(3000); // Avance le temps pour les animations de MatSnackBar
  
    expect(mockUserService.delete).toHaveBeenCalledWith('1');
    expect(snackBarSpy).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(routerNavigateSpy).toHaveBeenCalledWith(['/']);
  }));
});
