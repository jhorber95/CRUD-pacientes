import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRouteSnapshot, Resolve, Router, Routes} from '@angular/router';
import {JhiResolvePagingParams} from 'ng-jhipster';
import {EMPTY, Observable, of} from 'rxjs';
import {flatMap} from 'rxjs/operators';

import {Authority} from 'app/shared/constants/authority.constants';
import {IPaciente, Paciente} from 'app/shared/model/paciente.model';
import {PacienteService} from './paciente.service';
import {PacienteComponent} from './paciente.component';
import {PacienteDetailComponent} from './paciente-detail.component';
import {PacienteUpdateComponent} from './paciente-update.component';

@Injectable({ providedIn: 'root' })
export class PacienteResolve implements Resolve<IPaciente> {
  constructor(private service: PacienteService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPaciente> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((paciente: HttpResponse<Paciente>) => {
          if (paciente.body) {
            return of(paciente.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Paciente());
  }
}

export const pacienteRoute: Routes = [
  {
    path: '',
    component: PacienteComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams,
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Pacientes',
    },
  },
  {
    path: ':id/view',
    component: PacienteDetailComponent,
    resolve: {
      paciente: PacienteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Pacientes',
    },
  },
  {
    path: 'new',
    component: PacienteUpdateComponent,
    resolve: {
      paciente: PacienteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Pacientes',
    },
  },
  {
    path: ':id/edit',
    component: PacienteUpdateComponent,
    resolve: {
      paciente: PacienteResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Pacientes',
    },
  },
];
