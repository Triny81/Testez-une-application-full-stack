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

  /*********** UNIT TESTS ***********/
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
      username: "Username",
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
      username: "Username",
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
      username: "Username",
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

  /*********** INTEGRATION TESTS ***********/
  it('should correctly handle a full user session lifecycle', (done) => {
    const mockSession: SessionInformation = {
      token: 'fake-token',
      type: 'admin',
      id: 42,
      username: 'adminUser',
      firstName: 'Admin',
      lastName: 'User',
      admin: true,
    };

    const states: boolean[] = [];

    service.$isLogged().subscribe((isLogged) => {
      states.push(isLogged);

      if (states.length === 3) {
        expect(states).toEqual([false, true, false]);
        done();
      }
    });

    // Log in and out
    service.logIn(mockSession);
    service.logOut();
  });

  it('should emit the correct isLogged state after multiple changes', (done) => {
    const mockSession1: SessionInformation = {
      token: 'token-1',
      type: 'user',
      id: 1,
      username: 'user1',
      firstName: 'User',
      lastName: 'One',
      admin: false,
    };

    const mockSession2: SessionInformation = {
      token: 'token-2',
      type: 'admin',
      id: 2,
      username: 'user2',
      firstName: 'User',
      lastName: 'Two',
      admin: true,
    };

    const states: boolean[] = [];

    service.$isLogged().subscribe((isLogged) => {
      states.push(isLogged);

      if (states.length === 5) {
        expect(states).toEqual([false, true, false, true, false]);
        done();
      }
    });

    // Multiple log in and out scenarios
    service.logIn(mockSession1);
    service.logOut();
    service.logIn(mockSession2);
    service.logOut();
  });

  it('should reset session information on logOut', () => {
    const mockSession: SessionInformation = {
      token: 'test-token',
      type: 'user',
      id: 123,
      username: 'testUser',
      firstName: 'Test',
      lastName: 'User',
      admin: false,
    };

    service.logIn(mockSession);
    expect(service.sessionInformation).toEqual(mockSession);

    service.logOut();
    expect(service.sessionInformation).toBeUndefined();
  });
});
