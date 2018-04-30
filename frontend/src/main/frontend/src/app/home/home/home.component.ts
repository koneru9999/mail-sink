import {Component, OnDestroy, OnInit} from '@angular/core';
import { Router } from '@angular/router';

import {SmtpMailService} from '../../shared/services/smtp-mail.service';
import {SmtpMail} from '../../shared/models/smtp-mail.model';
import {Subscription} from 'rxjs/Subscription';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  mails: SmtpMail[] = [];
  page = 1;
  pageSize = 5;
  totalCount = 0;
  sub: Subscription[] = [];

  constructor(
    private mailService: SmtpMailService,
    private router: Router
  ) { }

  ngOnInit() {
    this.fetchMails();
  }

  fetchMails() {
    this.sub.push(
      this.mailService.getMails(this.page, this.pageSize).subscribe(
        (data) => {
          this.mails = data;
        },
        err => {
          console.log(err);
        }
      )
    );

    this.getCount();
  }

  clearMails() {
    this.sub.push(
      this.mailService.clearMails()
        .subscribe(
          (data) => this.fetchMails()
        )
    );
  }

  getCount() {
    this.sub.push(
      this.mailService.count().subscribe(
        (data) => this.totalCount = data
      )
    );
  }

  viewEmailContent(event: Event, mail: SmtpMail) {
    event.preventDefault;
    console.log(mail.messageId);
    this.router.navigate(['/mail', encodeURI(mail.messageId)])
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.forEach((x) => x.unsubscribe());
    }
  }
}
