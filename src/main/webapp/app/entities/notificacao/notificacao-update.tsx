import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, Input, ListGroup, ListGroupItem } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCompromissos } from 'app/entities/compromisso/compromisso.reducer';
import { createEntity, getEntity, reset, updateEntity } from './notificacao.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';

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

  const users = useAppSelector(state => state.userManagement.users); // Lista de usuários
  const [convidados, setConvidados] = useState<string[]>([]);

  const handleClose = () => {
    navigate(`/notificacao${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
    dispatch(getCompromissos({}));

    const queryParams = {
      page: 1,
      size: 100, // Quantidade de itens por página
    };

    dispatch(getUsers(queryParams)); // Fetch users com parâmetros para pegar todos
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

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

  // Função para adicionar um novo convidado (a partir de um select)
  const adicionarConvidado = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const selectedUsers = Array.from(e.target.selectedOptions, option => option.value);
    setConvidados([...convidados, ...selectedUsers]);
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
              {/* Campo de Convidados com select */}
              <div className="form-group">
                <label htmlFor="convidados">Convidados</label>
                <div className="d-flex">
                  <select
                    id="convidados-select"
                    multiple
                    value={convidados}
                    onChange={adicionarConvidado}
                    size={5} // Exibe 5 opções no select ao mesmo tempo
                    className="form-control"
                  >
                    {users?.map(user => (
                      <option key={user.login} value={user.login}>
                        {user.login} {/* Exiba o nome do usuário */}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
              {/* List of invited users */}
              {convidados.length > 0 && (
                <ListGroup>
                  {convidados.map((convidado, index) => (
                    <ListGroupItem key={index} className="d-flex justify-content-between">
                      {convidado} {/* Aqui você pode exibir o nome do convidado, dependendo de como você armazena isso */}
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
