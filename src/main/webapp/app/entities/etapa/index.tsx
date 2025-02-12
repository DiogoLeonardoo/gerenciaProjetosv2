import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Etapa from './etapa';
import EtapaDetail from './etapa-detail';
import EtapaUpdate from './etapa-update';
import EtapaDeleteDialog from './etapa-delete-dialog';

const EtapaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Etapa />} />
    <Route path="new" element={<EtapaUpdate />} />
    <Route path=":id">
      <Route index element={<EtapaDetail />} />
      <Route path="edit" element={<EtapaUpdate />} />
      <Route path="delete" element={<EtapaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EtapaRoutes;
