import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SmtpMail } from '../models/smtp-mail.model';
import { environment } from '../../../environments/environment';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class SmtpMailService {

  constructor(
    private http: HttpClient
  ) { }

  getMails(pageNumber: number, size: number): Observable<any> {
    // Setup query parameter
    const qp: HttpParams = new HttpParams();
    qp.set('page', pageNumber.toString());
    qp.set('size', size.toString());
    return this.http.get(environment.api + '/mails', {params: {
      page: pageNumber.toString(),
      size: size.toString()
    }});
  }

  clearMails(): Observable<any> {
    return this.http.delete(environment.api + '/reset');
  }

  getMailById(messageId: string): Observable<any>  {
    return this.http.get(environment.api + '/mails/' + messageId);
  }

  deleteMailById(messageId: string): Observable<any>  {
    return this.http.delete(environment.api + '/mails/' + messageId);
  }
}
