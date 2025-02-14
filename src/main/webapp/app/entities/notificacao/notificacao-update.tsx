import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, Input, ListGroup, ListGroupItem } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCompromissos } from 'app/entities/compromisso/compromisso.reducer';
import { createEntity, getEntity, reset, updateEntity } from './notificacao.reducer';

export const NotificacaoUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const compromissos = useAppSelector(state => state.compromisso.entities);
  const notificacaoEntity = useAppSelector(state => state.notificacao.entity);
  const loading = useAppSelector(state => state.notificacao.loading);
  const updating = useAppSelector(state => state.notificacao.updating);
  const updateSuccess = useAppSelector(state => state.notificacao.updateSuccess);

  const [convidados, setConvidados] = useState<string[]>([]); // estado para gerenciar os convidados
  const [novoConvidado, setNovoConvidado] = useState<string>(''); // estado para o campo de novo convidado

  const handleClose = () => {
    navigate(`/notificacao${location.search}`);
  };

  // Carrega os dados da notificação ao editar
  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
    dispatch(getCompromissos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // Preenche os convidados com os dados da notificação ao editar
  useEffect(() => {
    if (!isNew && notificacaoEntity?.convidados) {
      setConvidados(notificacaoEntity.convidados);
    }
  }, [notificacaoEntity, isNew]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.prazo = convertDateTimeToServer(values.prazo);

    const entity = {
      ...notificacaoEntity,
      ...values,
      compromisso: compromissos.find(it => it.id.toString() === values.compromisso?.toString()),
      convidados, // Adiciona os convidados ao enviar os dados
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
          prazo: displayDefaultDateTime(),
        }
      : {
          ...notificacaoEntity,
          prazo: convertDateTimeFromServer(notificacaoEntity.prazo),
          compromisso: notificacaoEntity?.compromisso?.id,
          convidados: notificacaoEntity?.convidados || [], // Preenche com convidados existentes
        };

  // Função para adicionar um novo convidado
  const adicionarConvidado = () => {
    if (novoConvidado && !convidados.includes(novoConvidado)) {
      setConvidados([...convidados, novoConvidado]);
      setNovoConvidado('');
    }
  };

  // Função para remover um convidado
  const removerConvidado = (convidado: string) => {
    setConvidados(convidados.filter(c => c !== convidado));
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gerenciaProjetosApp.notificacao.home.createOrEditLabel" data-cy="NotificacaoCreateUpdateHeading">
            Criar ou editar Notificação
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
                <ValidatedField name="id" required readOnly id="notificacao-id" label="Código" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Título"
                id="notificacao-titulo"
                name="titulo"
                data-cy="titulo"
                type="text"
                validate={{
                  required: { value: true, message: 'O campo é obrigatório.' },
                }}
              />
              <ValidatedField
                label="Prazo"
                id="notificacao-prazo"
                name="prazo"
                data-cy="prazo"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'O campo é obrigatório.' },
                }}
              />
              <ValidatedField id="notificacao-compromisso" name="compromisso" data-cy="compromisso" label="Compromisso" type="select">
                <option value="" key="0" />
                {compromissos
                  ? compromissos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <div className="form-group">
                <label htmlFor="convidados">Convidados</label>
                <div className="d-flex">
                  <Input
                    id="novoConvidado"
                    name="novoConvidado"
                    value={novoConvidado}
                    onChange={e => setNovoConvidado(e.target.value)}
                    placeholder="Adicione um convidado"
                  />
                  <Button color="primary" onClick={adicionarConvidado} disabled={!novoConvidado}>
                    Adicionar
                  </Button>
                </div>
              </div>
              {/* Lista de convidados */}
              {convidados.length > 0 && (
                <ListGroup>
                  {convidados.map((convidado, index) => (
                    <ListGroupItem key={index} className="d-flex justify-content-between">
                      {convidado}
                      <Button color="danger" size="sm" onClick={() => removerConvidado(convidado)}>
                        <FontAwesomeIcon icon="trash" />
                      </Button>
                    </ListGroupItem>
                  ))}
                </ListGroup>
              )}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/notificacao" replace color="info">
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

export default NotificacaoUpdate;
