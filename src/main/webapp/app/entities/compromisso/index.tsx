import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Compromisso from './compromisso';
import CompromissoDetail from './compromisso-detail';
import CompromissoUpdate from './compromisso-update';
import CompromissoDeleteDialog from './compromisso-delete-dialog';

const CompromissoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Compromisso />} />
    <Route path="new" element={<CompromissoUpdate />} />
    <Route path=":id">
      <Route index element={<CompromissoDetail />} />
      <Route path="edit" element={<CompromissoUpdate />} />
      <Route path="delete" element={<CompromissoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompromissoRoutes;
