import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from './service/user.service';
import { finalize } from 'rxjs/operators';
import { MessageService } from './service/message.service';
import { AuthenticationService } from './service/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title: string;
  
  constructor(
    private service: UserService,
    private http: HttpClient, 
    private router: Router,
    private messageService: MessageService,
    private authService: AuthenticationService    
    ) {
    this.title = 'angularclient';
    this.authService.authenticate(undefined, undefined);
  }
  //method to log user out
  logout() {
    this.messageService.add('logout');
    this.http.post('logout', {}).pipe( finalize(() => {
      this.service.authenticated = false;
      this.router.navigateByUrl('/login');
    })).subscribe();
  }
}
