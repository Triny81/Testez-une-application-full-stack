import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
    let service: AuthService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [AuthService],
        });
        service = TestBed.inject(AuthService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpMock.verify();
    });

    /*********** UNIT TESTS ***********/
    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    /*********** INTEGRATION TESTS ***********/
    it('should register a new user', () => {
        const mockRegisterRequest: RegisterRequest = {
            email: 'johndoe@test.com',
            firstName: 'John',
            lastName: 'Doe',
            password: 'password123'
        };

        service.register(mockRegisterRequest).subscribe((response) => {
            expect(response).toBeUndefined(); // Register request does not return a body
        });

        const req = httpMock.expectOne('api/auth/register');
        expect(req.request.method).toBe('POST');
        expect(req.request.body).toEqual(mockRegisterRequest);
        req.flush(null);
    });

    it('should log in a user and return session information', () => {
        const mockLoginRequest: LoginRequest = {
            email: 'johndoe@test.com',
            password: 'password123'
        };

        const mockSessionInformation: SessionInformation = {
            token: 'testToken',
            type: 'Bearer',
            id: 1,
            username: 'testUser',
            firstName: 'John',
            lastName: 'Doe',
            admin: false
        };

        service.login(mockLoginRequest).subscribe((sessionInfo) => {
            expect(sessionInfo).toEqual(mockSessionInformation);
        });

        const req = httpMock.expectOne('api/auth/login');
        expect(req.request.method).toBe('POST');
        expect(req.request.body).toEqual(mockLoginRequest);
        req.flush(mockSessionInformation);
    });

    it('should handle errors during registration', () => {
        const mockRegisterRequest: RegisterRequest = {
            email: 'johndoe@test.com',
            firstName: 'John',
            lastName: 'Doe',
            password: 'password123'
        };

        const errorMessage = 'Registration failed';

        service.register(mockRegisterRequest).subscribe({
            next: () => fail('Expected an error, but got a response'),
            error: (error) => {
                expect(error.status).toBe(400);
                expect(error.statusText).toBe('Bad Request');
            },
        });

        const req = httpMock.expectOne('api/auth/register');
        expect(req.request.method).toBe('POST');
        req.flush(errorMessage, { status: 400, statusText: 'Bad Request' });
    });

    it('should handle errors during login', () => {
        const mockLoginRequest: LoginRequest = {
            email: 'johndoe@test.com',
            password: 'password123'
        };

        const errorMessage = 'Login failed';

        service.login(mockLoginRequest).subscribe({
            next: () => fail('Expected an error, but got a response'),
            error: (error) => {
                expect(error.status).toBe(401);
                expect(error.statusText).toBe('Unauthorized');
            },
        });

        const req = httpMock.expectOne('api/auth/login');
        expect(req.request.method).toBe('POST');
        req.flush(errorMessage, { status: 401, statusText: 'Unauthorized' });
    });
});
