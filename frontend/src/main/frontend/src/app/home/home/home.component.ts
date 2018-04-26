import { Component, OnInit, OnDestroy } from '@angular/core';
import { SmtpMailService } from '../../shared/services/smtp-mail.service';
import { SmtpMail } from '../../shared/models/smtp-mail.model';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  mails: SmtpMail[] = [];
  page = 1;
  pageSize = 5;
  sub: Subscription;

  constructor(
    private mailService: SmtpMailService
  ) { }

  ngOnInit() {
    this.sub = this.mailService.getMails(this.page, this.pageSize).subscribe(
      (data) => {
        this.mails = data;
      },
      err => {
        console.log(err);
      }
    );
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }
}
