(function() {
  'use strict'

  angular.module('workorder')
    .run(routerRun)

  /** @ngInject */
  function routerRun (routerHelper) {

    const states = [
      // 系统帮助
      {
        state: 'root.document',
        config: {
          url: '/documents',
          views: {
            'main@': {
              templateUrl: 'app/views/document/document.html',
              controller: 'DocumentController',
              controllerAs: 'vm'
            }
          },
          data: {
            title: '系统帮助',
            bodyClass: 'document',
            requireLogin: true,
            requireAuthentication: true
          }
        }
      },

      // 系统帮助-详情
      {
        state: 'root.document.view',
        config: {
          url: '/:documentId',
          views: {
            'main@': {
              templateUrl: 'app/views/document/document-view.html',
              controller: 'DocumentViewController',
              controllerAs: 'vm'
            }
          },
          data: {
            title: '系统帮助详情',
            bodyClass: 'document-view',
            requireLogin: true,
            requireAuthentication: true
          }
        }
      }
    ]

    routerHelper.addRouterStates(states)
  }
})()