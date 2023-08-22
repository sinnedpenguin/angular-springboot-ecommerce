import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  userDetails: any = {};

  constructor(private userService: UserService) { }

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
}