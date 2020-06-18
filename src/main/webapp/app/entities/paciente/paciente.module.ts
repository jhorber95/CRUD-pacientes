import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CrudSharedModule } from 'app/shared/shared.module';
import { PacienteComponent } from './paciente.component';
import { PacienteDetailComponent } from './paciente-detail.component';
import { PacienteUpdateComponent } from './paciente-update.component';
import { PacienteDeleteDialogComponent } from './paciente-delete-dialog.component';
import { pacienteRoute } from './paciente.route';

@NgModule({
  imports: [CrudSharedModule, RouterModule.forChild(pacienteRoute)],
  declarations: [PacienteComponent, PacienteDetailComponent, PacienteUpdateComponent, PacienteDeleteDialogComponent],
  entryComponents: [PacienteDeleteDialogComponent],
  exports: [PacienteComponent]
})
export class CrudPacienteModule {}
