import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CrudSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import {CrudPacienteModule} from "app/entities/paciente/paciente.module";

@NgModule({
  imports: [CrudSharedModule, RouterModule.forChild([HOME_ROUTE]), CrudPacienteModule],
  declarations: [HomeComponent],
})
export class CrudHomeModule {}
