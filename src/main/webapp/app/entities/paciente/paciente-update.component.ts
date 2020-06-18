import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPaciente, Paciente } from 'app/shared/model/paciente.model';
import { PacienteService } from './paciente.service';
import {TIPO_IDENTIFICACION } from "app/shared/constants/input.constants";

@Component({
  selector: 'jhi-paciente-update',
  templateUrl: './paciente-update.component.html',
})
export class PacienteUpdateComponent implements OnInit {
  isSaving = false;
  fechaNacimientoDp: any;

  editForm = this.fb.group({
    id: [],
    nombres: [null, [Validators.required]],
    apellidos: [null, [Validators.required]],
    fechaNacimiento: [null, [Validators.required]],
    numeroIdentificacion: [null, [Validators.required]],
    tipoIdentificacion: [null, [Validators.required]],
    direccion: [],
  });
  tipoIdentificacion = TIPO_IDENTIFICACION;

  constructor(protected pacienteService: PacienteService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paciente }) => {
      this.updateForm(paciente);
    });
  }

  updateForm(paciente: IPaciente): void {
    this.editForm.patchValue({
      id: paciente.id,
      nombres: paciente.nombres,
      apellidos: paciente.apellidos,
      fechaNacimiento: paciente.fechaNacimiento,
      tipoIdentificacion: paciente.tipoIdentificacion,
      numeroIdentificacion: paciente.numeroIdentificacion,
      direccion: paciente.direccion,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paciente = this.createFromForm();
    if (paciente.id !== undefined) {
      this.subscribeToSaveResponse(this.pacienteService.update(paciente));
    } else {
      this.subscribeToSaveResponse(this.pacienteService.create(paciente));
    }
  }

  private createFromForm(): IPaciente {
    return {
      ...new Paciente(),
      id: this.editForm.get(['id'])!.value,
      nombres: this.editForm.get(['nombres'])!.value,
      apellidos: this.editForm.get(['apellidos'])!.value,
      fechaNacimiento: this.editForm.get(['fechaNacimiento'])!.value,
      tipoIdentificacion: this.editForm.get(['tipoIdentificacion'])!.value,
      numeroIdentificacion: this.editForm.get(['numeroIdentificacion'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaciente>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
