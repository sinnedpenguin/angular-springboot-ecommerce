import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { User, UserService } from '../services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  isLoggedIn = false;
  userDetails?: User;

  constructor(
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit() {
    this.isLoggedIn = !!localStorage.getItem('access_token');
    this.authService.loggedIn.subscribe((loggedIn) => {
      this.isLoggedIn = loggedIn;
  
      if (loggedIn) {
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
      } else {
        this.userDetails = undefined;
      }
    });
  
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

  logout() {
    localStorage.removeItem('access_token');
    this.isLoggedIn = false;
  }
}