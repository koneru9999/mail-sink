import { NgModule } from '@angular/core';

import { RecipientPipe} from "./recipient.pipe";

@NgModule({
  declarations: [
    RecipientPipe
  ],
  exports: [
    RecipientPipe
  ],
  providers: [
    RecipientPipe
  ]
})
export class PipesModule {}
