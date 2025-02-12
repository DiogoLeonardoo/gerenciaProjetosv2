import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm, isNumber } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCompromissos } from 'app/entities/compromisso/compromisso.reducer';
import { StatusEtapa } from 'app/shared/model/enumerations/status-etapa.model';
import { createEntity, getEntity, updateEntity } from './etapa.reducer';

export const EtapaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const compromissos = useAppSelector(state => state.compromisso.entities);
  const etapaEntity = useAppSelector(state => state.etapa.entity);
  const loading = useAppSelector(state => state.etapa.loading);
  const updating = useAppSelector(state => state.etapa.updating);
  const updateSuccess = useAppSelector(state => state.etapa.updateSuccess);
  const statusEtapaValues = Object.keys(StatusEtapa);

  const handleClose = () => {
    navigate('/etapa');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getCompromissos({}));
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
    if (values.ordem !== undefined && typeof values.ordem !== 'number') {
      values.ordem = Number(values.ordem);
    }

    const entity = {
      ...etapaEntity,
      ...values,
      compromisso: compromissos.find(it => it.id.toString() === values.compromisso?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          status: 'PENDENTE',
          ...etapaEntity,
          compromisso: etapaEntity?.compromisso?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gerenciaProjetosApp.etapa.home.createOrEditLabel" data-cy="EtapaCreateUpdateHeading">
            Criar ou editar Etapa
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="etapa-id" label="Código" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Descricao"
                id="etapa-descricao"
                name="descricao"
                data-cy="descricao"
                type="text"
                validate={{
                  required: { value: true, message: 'O campo é obrigatório.' },
                }}
              />
              <ValidatedField label="Status" id="etapa-status" name="status" data-cy="status" type="select">
                {statusEtapaValues.map(statusEtapa => (
                  <option value={statusEtapa} key={statusEtapa}>
                    {statusEtapa}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Ordem"
                id="etapa-ordem"
                name="ordem"
                data-cy="ordem"
                type="text"
                validate={{
                  required: { value: true, message: 'O campo é obrigatório.' },
                  validate: v => isNumber(v) || 'Este campo é do tipo numérico.',
                }}
              />
              <ValidatedField id="etapa-compromisso" name="compromisso" data-cy="compromisso" label="Compromisso" type="select">
                <option value="" key="0" />
                {compromissos
                  ? compromissos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/etapa" replace color="info">
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

export default EtapaUpdate;
