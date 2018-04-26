import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { SmtpMailService } from '../../shared/services/smtp-mail.service';
import { SmtpMail } from '../../shared/models/smtp-mail.model';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'app-view-mail',
  templateUrl: './view-email.component.html',
  styleUrls: ['./view-email.component.scss']
})
export class ViewMailComponent implements OnInit, OnDestroy {
    mail: SmtpMail;
    sub: Subscription[] = [];
    id: string;

  constructor(
    private mailService: SmtpMailService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.mail = new SmtpMail();
  }

  ngOnInit() {
    this.sub.push(this.route.params.subscribe(params => {
        this.id = params['id']; // (+) converts string 'id' to a number
        console.log('before search');
        this.search();
     })
    );
  }

  search() {
    this.sub.push(this.mailService.getMailById(this.id)
          .subscribe(
              (data) => {
                this.mail = data;
                console.log('inside search');
                console.log(this.mail);
              },
              err => console.log(err)
          )
        );
  }

  gotoHome() {
    this.router.navigate(['/mail-list']);
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.forEach(x => x.unsubscribe());
    }
  }
}
