import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  email: string = '';
  password: string = '';
  passwordConfirm: string = '';
  firstName = '';
  lastName = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit() {
    if (
      !this.email ||
      !this.password ||
      !this.passwordConfirm ||
      !this.firstName ||
      !this.lastName
    ) {
      return; // Stops execution if any field is empty
    }
  
    if (this.password !== this.passwordConfirm) {
      return; // Stops execution if passwords don't match
    }
  
    // Ensure email format is valid
    if (!this.isValidEmail(this.email)) {
      return; // Stop if email is invalid
    }

    this.authService.register(
      this.email,
      this.password,
      this.firstName,
      this.lastName
    ).subscribe({
      next: (response) => {
        console.log('Registration response:', response);
        this.router.navigate(['/login']);
      },
      error: (error) => {
        console.log('Registration failed:', error);
        alert(error.error || 'Registration failed')
      }
    });
  }

  // Helper function for validating email format
  isValidEmail(email: string): boolean {
    const emailRegex = /^(?!.*?\.\.)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
  }  

}
