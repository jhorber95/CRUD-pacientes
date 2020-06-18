import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { CrudSharedModule } from 'app/shared/shared.module';
import { CrudCoreModule } from 'app/core/core.module';
import { CrudAppRoutingModule } from './app-routing.module';
import { CrudHomeModule } from './home/home.module';
import { CrudEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    CrudSharedModule,
    CrudCoreModule,
    CrudHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    CrudEntityModule,
    CrudAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class CrudAppModule {}
