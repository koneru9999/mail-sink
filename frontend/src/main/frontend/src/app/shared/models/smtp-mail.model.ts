import {InetAddress} from "./inet-address.model";

'./inet-address.model';

export class SmtpMail {
    body: string;
    subject: string;
    to: InetAddress[];
    cc: InetAddress[];
    from: InetAddress[];
    sent: Date;
    messageId: string;
    contentId: string;
    mimeVersion: string;
    contentType: string;
}
