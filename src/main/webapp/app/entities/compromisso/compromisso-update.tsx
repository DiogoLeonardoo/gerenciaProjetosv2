import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { CompromissoClassificacao } from 'app/shared/model/enumerations/compromisso-classificacao.model';
import { StatusCompromisso } from 'app/shared/model/enumerations/status-compromisso.model';
import { createEntity, getEntity, reset, updateEntity } from './compromisso.reducer';

export const CompromissoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const compromissoEntity = useAppSelector(state => state.compromisso.entity);
  const loading = useAppSelector(state => state.compromisso.loading);
  const updating = useAppSelector(state => state.compromisso.updating);
  const updateSuccess = useAppSelector(state => state.compromisso.updateSuccess);
  const compromissoClassificacaoValues = Object.keys(CompromissoClassificacao);
  const statusCompromissoValues = Object.keys(StatusCompromisso);

  const handleClose = () => {
    navigate(`/compromisso${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.dataHorario = convertDateTimeToServer(values.dataHorario);

    const entity = {
      ...compromissoEntity,
      ...values,
      usuario: users.find(it => it.id.toString() === values.usuario?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dataHorario: displayDefaultDateTime(),
        }
      : {
          classificacao: 'TRABALHO',
          status: 'PENDENTE',
          ...compromissoEntity,
          dataHorario: convertDateTimeFromServer(compromissoEntity.dataHorario),
          usuario: compromissoEntity?.usuario?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gerenciaProjetosApp.compromisso.home.createOrEditLabel" data-cy="CompromissoCreateUpdateHeading">
            Criar ou editar Compromisso
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="compromisso-id" label="Código" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Titulo"
                id="compromisso-titulo"
                name="titulo"
                data-cy="titulo"
                type="text"
                validate={{
                  required: { value: true, message: 'O campo é obrigatório.' },
                }}
              />
              <ValidatedField
                label="Descricao"
                id="compromisso-descricao"
                name="descricao"
                data-cy="descricao"
                type="text"
                validate={{
                  required: { value: true, message: 'O campo é obrigatório.' },
                }}
              />
              <ValidatedField
                label="Data Horario"
                id="compromisso-dataHorario"
                name="dataHorario"
                data-cy="dataHorario"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'O campo é obrigatório.' },
                }}
              />
              <ValidatedField
                label="Classificacao"
                id="compromisso-classificacao"
                name="classificacao"
                data-cy="classificacao"
                type="select"
              >
                {compromissoClassificacaoValues.map(compromissoClassificacao => (
                  <option value={compromissoClassificacao} key={compromissoClassificacao}>
                    {compromissoClassificacao}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Status" id="compromisso-status" name="status" data-cy="status" type="select">
                {statusCompromissoValues.map(statusCompromisso => (
                  <option value={statusCompromisso} key={statusCompromisso}>
                    {statusCompromisso}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/compromisso" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Voltar</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Salvar
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CompromissoUpdate;
