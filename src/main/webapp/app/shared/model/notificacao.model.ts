import dayjs from 'dayjs';
import { ICompromisso } from 'app/shared/model/compromisso.model';

export interface INotificacao {
  id?: number;
  titulo?: string;
  prazo?: dayjs.Dayjs;
  compromisso?: ICompromisso | null;
}

export const defaultValue: Readonly<INotificacao> = {};
