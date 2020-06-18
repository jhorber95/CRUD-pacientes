import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'paciente',
        loadChildren: () => import('./paciente/paciente.module').then(m => m.CrudPacienteModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class CrudEntityModule {}
