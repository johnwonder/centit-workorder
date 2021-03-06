(function() {
  'use strict'

  angular.module('workorder')
    .run(routerRun)

  /** @ngInject */
  function routerRun (routerHelper) {

    const states = [
      // 系统帮助
      {
        state: 'admin.document',
        config: {
          url: '/documents',
          views: {
            'main@': {
              templateUrl: 'app/views/document/document.admin.html',
              controller: 'DocumentAdminController',
              controllerAs: 'vm'
            },
            'breadcrumb@': ''
          },
          data: {
            title: '知识库',
            bodyClass: 'document'
          }
        }
      },

      // 系统帮助-详情
      {
        state: 'admin.document.view',
        config: {
          url: '/:docId',
          views: {
            'doc': {
              templateUrl: 'app/views/document/document.admin-view.html',
              controller: 'DocumentAdminViewController',
              controllerAs: 'vm'
            }
          },
          data: {
            title: '知识库详情',
            bodyClass: 'document-view'
          }
        }
      },
      // 知识库检索
      {
        state: 'admin.document.search',
        config: {
          url: '/key/:keyWord/:isAdmin',
          views: {
            'main@': {
              templateUrl: 'app/views/document/document-search.html',
              controller: 'DocumentSearchController',
              controllerAs: 'vm'
            }
          },
          data: {
            title: '知识库检索',
            bodyClass: 'document-view'
          }
        }
      }
    ]

    routerHelper.addRouterStates(states)
  }
})();
