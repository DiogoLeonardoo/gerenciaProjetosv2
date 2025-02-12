import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { CompromissoClassificacao } from 'app/shared/model/enumerations/compromisso-classificacao.model';
import { StatusCompromisso } from 'app/shared/model/enumerations/status-compromisso.model';

export interface ICompromisso {
  id?: number;
  titulo?: string;
  descricao?: string;
  dataHorario?: dayjs.Dayjs;
  classificacao?: keyof typeof CompromissoClassificacao;
  status?: keyof typeof StatusCompromisso;
  usuario?: IUser | null;
}

export const defaultValue: Readonly<ICompromisso> = {};
