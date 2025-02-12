import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './compromisso.reducer';

export const CompromissoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const compromissoEntity = useAppSelector(state => state.compromisso.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="compromissoDetailsHeading">Compromisso</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Código</span>
          </dt>
          <dd>{compromissoEntity.id}</dd>
          <dt>
            <span id="titulo">Titulo</span>
          </dt>
          <dd>{compromissoEntity.titulo}</dd>
          <dt>
            <span id="descricao">Descricao</span>
          </dt>
          <dd>{compromissoEntity.descricao}</dd>
          <dt>
            <span id="dataHorario">Data Horario</span>
          </dt>
          <dd>
            {compromissoEntity.dataHorario ? (
              <TextFormat value={compromissoEntity.dataHorario} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="classificacao">Classificacao</span>
          </dt>
          <dd>{compromissoEntity.classificacao}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{compromissoEntity.status}</dd>
          <dt>Usuario</dt>
          <dd>{compromissoEntity.usuario ? compromissoEntity.usuario.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/compromisso" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Voltar</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/compromisso/${compromissoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Editar</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompromissoDetail;
