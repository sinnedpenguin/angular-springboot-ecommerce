import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserDto, UserService } from 'src/app/services/user.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  userDetails: any = {};

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router 
  ) {}

  ngOnInit(): void {
    const userId = localStorage.getItem('user_id');

    if (userId) {
      const userIdNumber = +userId;

      this.userService.getUserDetails(userIdNumber.toString()).subscribe(
        (response: any) => {
          console.log('User details:', response);
          this.userDetails = response;
        },
        (error: any) => {
          console.error('Error fetching user details:', error);
        }
      );
    }
  }

  updateAccount(): void {
    this.updateUser();
  }

  updateUser(): void {
    const userId = localStorage.getItem('user_id');
    if (userId) {
      const userDto: UserDto = {
        email: this.userDetails.email,
        password: this.userDetails.newPassword || undefined,
      };
      this.userService.updateUser(userId, userDto).subscribe(
        (response: any) => {
          console.log('User updated:', response);
          this.userDetails.email = response.email;
          this.userDetails.newPassword = '';
          alert('Your account has been updated successfully! Please relogin.');

          this.authService.loggedIn.emit(false);
          this.router.navigate(['/login']);

        },
        (error: any) => {
          console.error('Error updating user:', error);
        }
      );
    }
  }
}