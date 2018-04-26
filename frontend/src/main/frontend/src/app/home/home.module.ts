import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ViewMailComponent } from './view-mail/view-mail.component';
import { MomentModule } from 'ngx-moment';
import { SmtpMailService } from '../shared/services/smtp-mail.service';

const homeRouting: ModuleWithProviders = RouterModule.forChild([
  {
    path: '', redirectTo: 'mail-list', pathMatch: 'full'
  },
  {
    path: 'mail-list',
    component: HomeComponent,
    data: {
      title: 'All'
    }
  },
  {
    path: 'mail/:id',
    component: ViewMailComponent,
    data: {
      title: 'View'
    }
  }
]);

@NgModule({
  imports: [
    CommonModule,
    MomentModule,
    homeRouting
  ],
  declarations: [
    HomeComponent,
    ViewMailComponent
  ],
  providers: [
    SmtpMailService
  ]
})
export class HomeModule { }
