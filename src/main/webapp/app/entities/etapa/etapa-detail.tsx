import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './etapa.reducer';

export const EtapaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const etapaEntity = useAppSelector(state => state.etapa.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="etapaDetailsHeading">Etapa</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Código</span>
          </dt>
          <dd>{etapaEntity.id}</dd>
          <dt>
            <span id="descricao">Descricao</span>
          </dt>
          <dd>{etapaEntity.descricao}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{etapaEntity.status}</dd>
          <dt>
            <span id="ordem">Ordem</span>
          </dt>
          <dd>{etapaEntity.ordem}</dd>
          <dt>Compromisso</dt>
          <dd>{etapaEntity.compromisso ? etapaEntity.compromisso.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/etapa" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Voltar</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/etapa/${etapaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default EtapaDetail;
