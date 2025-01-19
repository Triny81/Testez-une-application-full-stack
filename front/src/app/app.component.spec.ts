import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { of } from 'rxjs';

const mockAuthService = {
  login: jest.fn()
};

const mockRouter = {
  navigate: jest.fn()
};

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
  });

  /*********** UNIT TESTS ***********/
  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should call $isLogged from sessionService', () => {
    const mockSessionService = {
      $isLogged: jest.fn().mockReturnValue(of(true))
    };

    const component = new AppComponent(
      mockAuthService as any,
      mockRouter as any,
      mockSessionService as any
    );

    const result = component.$isLogged();

    expect(mockSessionService.$isLogged).toHaveBeenCalled();
    result.subscribe((isLogged) => {
      expect(isLogged).toBe(true);
    });
  });

  /*********** INTEGRATION TESTS ***********/
  it('should call logOut from sessionService and navigate to the home page on logout', () => {
    const mockSessionService = {
      logOut: jest.fn()
    };
    const mockRouter = {
      navigate: jest.fn()
    };

    const component = new AppComponent(
      mockAuthService as any,
      mockRouter as any,
      mockSessionService as any
    );

    component.logout();

    expect(mockSessionService.logOut).toHaveBeenCalled();

    expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
  });
});
