import React from 'react';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Compromisso from './compromisso';
import Etapa from './etapa';
import Notificacao from './notificacao';
import { Route } from 'react-router';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="compromisso/*" element={<Compromisso />} />
        <Route path="etapa/*" element={<Etapa />} />
        <Route path="notificacao/*" element={<Notificacao />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
