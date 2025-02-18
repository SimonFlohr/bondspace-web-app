import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../service/auth.service'
import { RouterTestingModule } from '@angular/router/testing';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let router: Router;

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['register']);

    authServiceSpy.register.and.returnValue(of({}));

    await TestBed.configureTestingModule({
      declarations: [],
      imports: [RegisterComponent, ReactiveFormsModule, FormsModule, HttpClientTestingModule, RouterTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { paramMap: { get: () => null } },
            queryParams: of({}),
          },
        },
        { provide: AuthService, useValue: authServiceSpy },
      ],
    }).compileComponents();

    router = TestBed.inject(Router);
    spyOn(router, 'navigate');
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  // Test case: required fields must not be empty
  it('should not call register() if any field is empty', () => {
    component.email = '';
    component.password = 'ValidPass123!';
    component.passwordConfirm = 'ValidPass123!';
    component.firstName = 'John';
    component.lastName = 'Doe';

    component.onSubmit();

    expect(authServiceSpy.register).not.toHaveBeenCalled();
  });

  // Test case: passwordConfirm field must match the password field
  it('should not call register() if passwords do not match', () => {
    component.email = 'test@example.com';
    component.password = 'ValidPass123!';
    component.passwordConfirm = 'MismatchPass!';
    component.firstName = 'John';
    component.lastName = 'Doe';

    component.onSubmit();

    expect(authServiceSpy.register).not.toHaveBeenCalled();
  });

  // Test case: email format must be valid
  it('should not call register() if email format is invalid', () => {
    const invalidEmails = [
      'plainaddress',
      '@missingusername.com',
      'username@.com',
      'username@com',
      'username@domain..com'
    ];
  
    invalidEmails.forEach((invalidEmail) => {
      authServiceSpy.register.calls.reset();
      
      component.email = invalidEmail;
      component.password = 'ValidPass123!';
      component.passwordConfirm = 'ValidPass123!';
      component.firstName = 'John';
      component.lastName = 'Doe';

      expect(component.isValidEmail(component.email)).toBeFalse();
  
      component.onSubmit();
  
      expect(authServiceSpy.register).not.toHaveBeenCalled();
    });
  });  

  // Test case: successful registration
  it('should call register() and navigate on successful registration', () => {
    authServiceSpy.register.and.returnValue(of({ success: true }));

    component.email = 'test@example.com';
    component.password = 'ValidPass123!';
    component.passwordConfirm = 'ValidPass123!';
    component.firstName = 'John';
    component.lastName = 'Doe';

    component.onSubmit();

    expect(authServiceSpy.register).toHaveBeenCalledWith(
      'test@example.com',
      'ValidPass123!',
      'John',
      'Doe'
    );

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  // Test case: failed registration
  it('should show alert on failed registration', () => {
    spyOn(window, 'alert');
    authServiceSpy.register.and.returnValue(throwError(() => ({ error: 'Registration failed' })));

    component.email = 'test@example.com';
    component.password = 'ValidPass123!';
    component.passwordConfirm = 'ValidPass123!';
    component.firstName = 'John';
    component.lastName = 'Doe';

    component.onSubmit();

    expect(authServiceSpy.register).toHaveBeenCalled();
    expect(window.alert).toHaveBeenCalledWith('Registration failed');
  });
});