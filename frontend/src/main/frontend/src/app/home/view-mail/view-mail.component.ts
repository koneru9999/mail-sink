import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {SmtpMailService} from '../../shared/services/smtp-mail.service';
import {SmtpMail} from '../../shared/models/smtp-mail.model';
import {Subscription} from 'rxjs/Subscription';
import {InetAddress} from "../../shared/models/inet-address.model";

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
  }

  ngOnInit() {
    this.sub.push(this.route.params.subscribe(params => {
        this.id = params['id']; // (+) converts string 'id' to a number
        this.search();
      })
    );
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
    this.router.navigate(['/mail-list']);
  }

  fetchEmailAddress(recipient: InetAddress | InetAddress[]): string {
    if (recipient instanceof Array) {
      let retEmail = [];
      retEmail = recipient.reduce( function(coll,item){
        coll.push( item.address );
        return coll;
      }, retEmail);

      return retEmail.join(',');
    } else {
      return recipient.address;
    }
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.forEach(x => x.unsubscribe());
    }
  }
}
