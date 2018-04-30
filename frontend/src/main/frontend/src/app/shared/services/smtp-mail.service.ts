import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {environment} from '../../../environments/environment';
import {SmtpMail} from "../models/smtp-mail.model";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class SmtpMailService {

  constructor(
    private http: HttpClient
  ) { }

  getMails(pageNumber: number, size: number): Observable<SmtpMail[]> {
    // Setup query parameter
    return this.http.get<SmtpMail[]>(environment.api + '/mails', {
      params: {
        page: pageNumber.toString(),
        size: size.toString()
      }
    });
  }

  clearMails(): Observable<any> {
    return this.http.delete(environment.api + '/reset');
  }

  getMailById(messageId: string): Observable<SmtpMail>  {
    return this.http.get<SmtpMail>(environment.api + '/mails/' + messageId);
  }

  deleteMailById(messageId: string): Observable<any>  {
    return this.http.delete(environment.api + '/mails/' + messageId);
  }

  count(): Observable<number> {
    return this.http.get<number>(environment.api + '/mails/count');
  }
}
