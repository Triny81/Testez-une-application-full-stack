import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

import { SessionApiService } from '../features/sessions/services/session-api.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    const apiServiceMock = {
      getAllSessions: jest.fn(),
      getSessionById: jest.fn(),
    };

    TestBed.configureTestingModule({
      providers: [
        SessionService,
        { provide: SessionApiService, useValue: apiServiceMock },
      ],
    });

    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initialize with isLogged as false', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should emit isLogged as false initially', (done) => {
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
      done();
    });
  });

  it('should update isLogged to true on logIn', (done) => {
    const mockSession: SessionInformation = {
      token: 'fake-token',
      type: 'type',
      id: 1,
      username: "Usernm",
      firstName: "John",
      lastName: "Doe",
      admin: true
    };

    service.logIn(mockSession);

    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockSession);

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      done();
    });
  });

  it('should update isLogged to false on logOut', (done) => {
    const mockSession: SessionInformation = {
      token: 'fake-token',
      type: 'type',
      id: 1,
      username: "Usernm",
      firstName: "John",
      lastName: "Doe",
      admin: true
    };

    service.logIn(mockSession);
    service.logOut();

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
      done();
    });
  });

  it('should emit isLogged correctly when state changes', (done) => {
    const mockSession: SessionInformation = {
      token: 'fake-token',
      type: 'type',
      id: 1,
      username: "Usernm",
      firstName: "John",
      lastName: "Doe",
      admin: true
    };

    const values: boolean[] = [];

    service.$isLogged().subscribe((isLogged) => {
      values.push(isLogged);

      if (values.length === 3) {
        expect(values).toEqual([false, true, false]);
        done();
      }
    });

    service.logIn(mockSession);
    service.logOut();
  });
});
