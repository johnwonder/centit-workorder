const SubmitOrderAdmin = () => import(/* webpackChunkName: "page-workflow" */ '@/modules/submitOrder/admin')
const SubmitOrderUser = () => import(/* webpackChunkName: "page-workflow" */ '@/modules/submitOrder/user')

export default [
  {
    path: '/admin/SubmitOrder',
    name: 'WORKYW',
    component: SubmitOrderAdmin,
    meta: {
      title: '流程业务',
    },
  },
  {
    path: '/user/SubmitOrder',
    name: 'WORKYW',
    component: SubmitOrderUser,
    meta: {
      title: '流程业务',
    },
  }
]