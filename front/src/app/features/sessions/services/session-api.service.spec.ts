import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers: [SessionApiService],
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify()
  });

  /*********** UNIT TESTS ***********/
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch sessions from API', () => {
    const mockSessions = [{ id: 1, name: 'Session 1' }];

    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  /*********** INTEGRATION TESTS ***********/

  it('should fetch all sessions', () => {
    const mockSessions: Session[] = [
      { id: 1, name: 'Session 1', date: new Date(), description: '', teacher_id: 1, users: [] },
    ];

    service.all().subscribe((sessions) => {
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should fetch session details by ID', () => {
    const mockSession: Session = {
      id: 1,
      name: 'New Session',
      date: new Date(),
      description: 'Description of new session',
      teacher_id: 1,
      users: [],
    };

    service.detail('1').subscribe((session) => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete a session by ID', () => {
    service.delete('1').subscribe((response) => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should create a new session', () => {
    const newSession: Session = {
      id: 1,
      name: 'New Session',
      date: new Date(),
      description: 'Description of new session',
      teacher_id: 1,
      users: [],
    };

    service.create(newSession).subscribe((session) => {
      expect(session).toEqual(newSession);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newSession);
    req.flush(newSession);
  });

  it('should update an existing session', () => {
    const updatedSession: Session = {
      id: 1,
      name: 'New Session',
      date: new Date(),
      description: 'Description of new session',
      teacher_id: 1,
      users: [],
    };

    service.update('1', updatedSession).subscribe((session) => {
      expect(session).toEqual(updatedSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession);
    req.flush(updatedSession);
  });

  it('should handle user participation in a session', () => {
    service.participate('1', '123').subscribe((response) => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/1/participate/123');
    expect(req.request.method).toBe('POST');
    req.flush(null);
  });

  it('should handle user un-participation in a session', () => {
    service.unParticipate('1', '123').subscribe((response) => {
      expect(response).toBeUndefined();
    });

    const req = httpMock.expectOne('api/session/1/participate/123');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
