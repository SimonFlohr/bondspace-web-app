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
    if (this.email
      && this.password
      && this.passwordConfirm
      && this.firstName
      && this.lastName
    ) {
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
  }

}
