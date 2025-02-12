import { ICompromisso } from 'app/shared/model/compromisso.model';
import { StatusEtapa } from 'app/shared/model/enumerations/status-etapa.model';

export interface IEtapa {
  id?: number;
  descricao?: string;
  status?: keyof typeof StatusEtapa;
  ordem?: number;
  compromisso?: ICompromisso | null;
}

export const defaultValue: Readonly<IEtapa> = {};
