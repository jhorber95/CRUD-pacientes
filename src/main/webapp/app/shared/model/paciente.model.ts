import { Moment } from 'moment';

export interface IPaciente {
  id?: number;
  nombres?: string;
  apellidos?: string;
  fechaNacimiento?: Moment;
  numeroIdentificacion?: string;
  direccion?: string;
  tipoIdentificacion?: string;
}

export class Paciente implements IPaciente {
  constructor(
    public id?: number,
    public nombres?: string,
    public apellidos?: string,
    public fechaNacimiento?: Moment,
    public numeroIdentificacion?: string,
    public direccion?: string,
    public tipoIdentificacion?: string
  ) {}
}
