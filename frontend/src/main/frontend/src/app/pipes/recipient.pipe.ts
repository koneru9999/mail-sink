import {Pipe, PipeTransform} from '@angular/core';
import { InetAddress } from '../shared/models/inet-address.model';

@Pipe({ name: 'recipient' })
export class RecipientPipe implements PipeTransform {
  transform(value: InetAddress | InetAddress[], join: string = ', '): string {
    if (value instanceof Array) {
      let retEmail = [];
      retEmail = value.reduce(function (coll, item) {
        coll.push(item.address);
        return coll;
      }, retEmail);

      return retEmail.join(join);
    } else {
      return value.address;
    }
  }
}
