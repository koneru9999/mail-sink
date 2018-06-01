import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {SmtpMailService} from '../../shared/services/smtp-mail.service';
import {SmtpMail} from '../../shared/models/smtp-mail.model';
import {Subscription} from 'rxjs/Subscription';
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  mails: Observable<SmtpMail[]>;
  page: number;
  pageSize = 10;
  totalCount = 0;
  sub: Subscription[] = [];

  constructor(
    private mailService: SmtpMailService,
    private route: ActivatedRoute,
    private router: Router
  ) {
  }

  ngOnInit() {
    if (this.route.snapshot.queryParamMap.has('page')) {
      this.page = +this.route.snapshot.queryParamMap.get('page');
    } else {
      this.page = 1;
    }

    this.sub.push(this.route.queryParamMap.subscribe(
      paramMap => {
        if (paramMap.has('page')) {
          this.page = +paramMap.get('page');
        }

        this.fetchMails();
      }
    ));
  }

  fetchMails() {
    this.mails = this.mailService.getMails(this.page, this.pageSize);
    this.getCount();
  }

  onPageChange(event: number) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParamsHandling: 'merge',
      queryParams: {
        ['page']: event
      }
    });
  }

  clearMails() {
    this.sub.push(
      this.mailService.clearMails()
        .subscribe(
          (data) => this.router.navigate([], {
            relativeTo: this.route,
            queryParamsHandling: 'merge',
            queryParams: {
              ['page']: undefined
            }
          })
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
    event.preventDefault();
    this.router.navigate(['/mail', encodeURI(mail.messageId)], {
      queryParams: {
        ['fp']: this.page
      }
    });
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.forEach((x) => x.unsubscribe());
    }
  }
}
