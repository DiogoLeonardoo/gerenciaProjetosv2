import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Notificacao from './notificacao';
import NotificacaoDetail from './notificacao-detail';
import NotificacaoUpdate from './notificacao-update';
import NotificacaoDeleteDialog from './notificacao-delete-dialog';

const NotificacaoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Notificacao />} />
    <Route path="new" element={<NotificacaoUpdate />} />
    <Route path=":id">
      <Route index element={<NotificacaoDetail />} />
      <Route path="edit" element={<NotificacaoUpdate />} />
      <Route path="delete" element={<NotificacaoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default NotificacaoRoutes;
