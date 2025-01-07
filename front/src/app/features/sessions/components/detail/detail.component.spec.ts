import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { DetailComponent } from './detail.component';
import { of } from 'rxjs';



describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionUsers = [1];

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
  }

  const mockSessionApiService = {
    delete: jest.fn().mockReturnValue(of({})), // Mock renvoyant un Observable
    detail: jest.fn().mockImplementation(() => {
      return of({
        id: 1,
        name: 'Yoga Session',
        description: 'A relaxing yoga session.',
        users: sessionUsers, // variable dynamique
        createdAt: new Date(),
        updatedAt: new Date(),
      });
    }),
    participate: jest.fn().mockImplementation(() => {
      sessionUsers.push(1); // Ajoute un utilisateur
      return of({});
    }),
    unParticipate: jest.fn().mockImplementation(() => {
      sessionUsers = []; // Supprime un utilisateur
      return of({});
    }),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatButtonModule,
        MatIconModule,
      ],
      declarations: [DetailComponent],
      providers: [{ provide: SessionService, useValue: mockSessionService },
      { provide: SessionApiService, useValue: mockSessionApiService },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    component.session = { name: 'Yoga Session', users: [], date: new Date(), createdAt: new Date(), updatedAt: new Date(), description: '', teacher_id: 1 };
    fixture.detectChanges();
  });

  /*********** UNIT TESTS ***********/
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display session name in title', () => {
    fixture.detectChanges();

    const title = fixture.nativeElement.querySelector('h1');
    expect(title.textContent).toContain('Yoga Session');
  });

  it('should call `back` method on button click', () => {
    fixture.detectChanges();

    const backSpy = jest.spyOn(component, 'back');
    const button = fixture.nativeElement.querySelector('button[mat-icon-button]');
    button.click();

    expect(backSpy).toHaveBeenCalled();
  });

  /*********** INTEGRATION TESTS ***********/
  it('should allow admin to delete a session', () => {
    component.isAdmin = true;
    fixture.detectChanges();

    const deleteButton = fixture.debugElement.nativeElement.querySelector('button[color="warn"]');
    deleteButton.click();

    expect(mockSessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
  });

  it('should allow user to participate in a session', () => {
    component.isAdmin = false;
    component.isParticipate = false;
    fixture.detectChanges();

    const participateButton = fixture.nativeElement.querySelector('button[color="primary"]');
    participateButton.click();

    expect(mockSessionApiService.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(component.isParticipate).toBe(true);
  });

  it('should allow user to un-participate from a session', () => {
    component.isAdmin = false;
    component.isParticipate = true; 
    fixture.detectChanges();

    const unParticipateButton = fixture.nativeElement.querySelector('button[color="warn"]');
    unParticipateButton.click();

    expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
    expect(component.isParticipate).toBe(false);
  });

  it('should display correct session details', () => {
    const title = fixture.nativeElement.querySelector('h1');
    const description = fixture.nativeElement.querySelector('.description');

    expect(title.textContent).toContain('Yoga Session');
    expect(description.textContent).toContain('A relaxing yoga session.');
  });
});

