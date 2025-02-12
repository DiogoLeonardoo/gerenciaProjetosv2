import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './notificacao.reducer';

export const NotificacaoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const notificacaoEntity = useAppSelector(state => state.notificacao.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="notificacaoDetailsHeading">Notificacao</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Código</span>
          </dt>
          <dd>{notificacaoEntity.id}</dd>
          <dt>
            <span id="titulo">Titulo</span>
          </dt>
          <dd>{notificacaoEntity.titulo}</dd>
          <dt>
            <span id="prazo">Prazo</span>
          </dt>
          <dd>{notificacaoEntity.prazo ? <TextFormat value={notificacaoEntity.prazo} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Compromisso</dt>
          <dd>{notificacaoEntity.compromisso ? notificacaoEntity.compromisso.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/notificacao" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Voltar</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/notificacao/${notificacaoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default NotificacaoDetail;
