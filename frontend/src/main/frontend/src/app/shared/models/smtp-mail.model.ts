export class SmtpMail {
    body: string;
    subject: string;
    to: string[];
    cc: string[];
    from: string[];
    sent: Date;
    messageId: string;
    mimeVersion: string;
    contentType: string;
}
