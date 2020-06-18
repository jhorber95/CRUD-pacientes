import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPaciente } from 'app/shared/model/paciente.model';
import { PacienteService } from './paciente.service';

@Component({
  templateUrl: './paciente-delete-dialog.component.html',
})
export class PacienteDeleteDialogComponent {
  paciente?: IPaciente;

  constructor(protected pacienteService: PacienteService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pacienteService.delete(id).subscribe(() => {
      this.eventManager.broadcast('pacienteListModification');
      this.activeModal.close();
    });
  }
}
