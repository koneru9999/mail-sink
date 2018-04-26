import { Component } from '@angular/core';
import { Router, Event, NavigationEnd } from '@angular/router';
import 'rxjs/add/operator/pairwise';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'app';

  constructor(private router: Router) {
    this.router.events.forEach((event) => {
      if (event instanceof NavigationEnd) {
        window.scrollTo(0, 0);
        console.log('scrolled');
      }
  });
  }
}
