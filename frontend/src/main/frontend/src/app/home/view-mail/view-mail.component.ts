import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {SmtpMailService} from '../../shared/services/smtp-mail.service';
import {SmtpMail} from '../../shared/models/smtp-mail.model';
import {Subscription} from 'rxjs/Subscription';

@Component({
  selector: 'app-view-mail',
  templateUrl: './view-email.component.html',
  styleUrls: ['./view-email.component.scss']
})
export class ViewMailComponent implements OnInit, OnDestroy {
  mail: SmtpMail;
  sub: Subscription[] = [];
  id: string;
  fromPage: number;

  constructor(
    private mailService: SmtpMailService,
    private route: ActivatedRoute,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.sub.push(this.route.params.subscribe(params => {
        this.id = params['id']; // (+) converts string 'id' to a number
        this.search();
      })
    );

    if (this.route.snapshot.queryParamMap.has('fp')) {
      this.fromPage = +this.route.snapshot.queryParamMap.get('fp');
    }
  }

  search() {
    this.sub.push(this.mailService.getMailById(this.id)
      .subscribe(
        (data) => {
          this.mail = data;
        },
        err => console.log(err)
      )
    );
  }

  gotoHome() {
    this.router.navigate(['/mail-list'], this.fromPage ? {
        queryParams: {
          ['page']: this.fromPage
        }
      } : {}
    );
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.forEach(x => x.unsubscribe());
    }
  }
}
