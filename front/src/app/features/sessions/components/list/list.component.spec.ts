import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { of } from 'rxjs';
import { RouterTestingModule } from '@angular/router/testing';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule],
      providers: [{ provide: SessionService, useValue: mockSessionService }]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*********** UNIT TESTS ***********/
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have a create button if user is admin', () => {
    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
    expect(createButton).toBeTruthy();
  });

  /*********** INTEGRATION TESTS ***********/ 
  it('should display a list of sessions', () => {
    // Fournir des données simulées à sessions$
    component.sessions$ = of([
      { id: 1, name: 'Yoga Session', date: new Date(), description: 'A relaxing session.', teacher_id: 1, users: [] },
      { id: 2, name: 'Meditation Session', date: new Date(), description: 'A calming session.', teacher_id: 1, users: [] }
    ]);

    fixture.detectChanges();

    const sessionCards = fixture.nativeElement.querySelectorAll('.item');
    expect(sessionCards.length).toBe(2); // Vérifie qu'il y a 2 éléments
    expect(sessionCards[0].querySelector('mat-card-title').textContent).toContain('Yoga Session');
    expect(sessionCards[1].querySelector('mat-card-title').textContent).toContain('Meditation Session');
  });

  it('should navigate to detail page on clicking Detail button', () => {
    const editSpan = fixture.nativeElement.querySelector('span.ml1'); // Rechercher le bouton contenant <span class="ml1">Edit</span>
    expect(editSpan).toBeTruthy(); // Vérifie que l'élément existe

    const editButton = editSpan.closest('button'); // Trouver le bouton parent
    expect(editButton).toBeTruthy(); // Vérifie que le bouton parent existe

    editButton!.click(); // Simuler un clic sur le bouton
  });

  it('should display Edit button for admin users', () => {
    jest.spyOn(component, 'user', 'get').mockReturnValue({
      token: 'fake-token',
      type: 'type',
      id: 1,
      username: "Username",
      firstName: "John",
      lastName: "Doe",
      admin: true
    });
    fixture.detectChanges();

    const editSpan = fixture.nativeElement.querySelector('span.ml1');
    expect(editSpan).toBeTruthy(); 

    const editButton = editSpan.closest('button'); 
    expect(editButton).toBeTruthy(); 
  });

  it('should not display Edit button for non-admin users', () => {
    jest.spyOn(component, 'user', 'get').mockReturnValue({
      token: 'fake-token',
      type: 'type',
      id: 1,
      username: "Username",
      firstName: "John",
      lastName: "Doe",
      admin: false
    });
    fixture.detectChanges();

    const editButtons = fixture.nativeElement.querySelectorAll('button[routerLink^="update"]');
    expect(editButtons.length).toBe(0);
  });

  it('should display Create button for admin users', () => {
    jest.spyOn(component, 'user', 'get').mockReturnValue({
      token: 'fake-token',
      type: 'type',
      id: 1,
      username: "Username",
      firstName: "John",
      lastName: "Doe",
      admin: true
    });
    fixture.detectChanges();

    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
    expect(createButton).toBeTruthy();
  });

  it('should not display Create button for non-admin users', () => {
    jest.spyOn(component, 'user', 'get').mockReturnValue({
      token: 'fake-token',
      type: 'type',
      id: 1,
      username: "Username",
      firstName: "John",
      lastName: "Doe",
      admin: false
    });
    fixture.detectChanges();

    const createButton = fixture.nativeElement.querySelector('button[routerLink="create"]');
    expect(createButton).toBeNull();
  });
});
