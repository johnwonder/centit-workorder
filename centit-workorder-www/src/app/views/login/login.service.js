(function() {
  'use strict'

  angular.module('workorder')
    .factory('Authentication', Authentication)

  /** @ngInject */
  function Authentication($q, $cookies, Error, Constant) {
    const name = 'Workorder_User'
    let _user_

    return {
      save,
      clear,
      get,
      canAccess,
      hasLogin,
      isAdmin
    }

    /**
     * 存储用户信息
     * @param user
     */
    function save(user) {
      if (!user) return clear()

      _user_ = user
      $cookies.putObject(name, user)
      return $q.resolve(_user_)
    }

    /**
     * 清空用户信息
     */
    function clear() {
      _user_ = null
      $cookies.remove(name)
      return $q.resolve(_user_)
    }

    /**
     * 获取用户信息
     */
    function get() {
      if (!_user_) {
        _user_ = $cookies.getObject(name)
      }
      return $q.resolve(_user_)
    }

    /**
     * 根据osId判断用户是否有权限访问
     * @param osId
     */
    function canAccess(osId) {
      return hasLogin()
        .then(user => {
          if (!!osId && user.osId !== osId) {
            return isAdmin(true)
          }

          return user
        })
    }

    /**
     * 判断用户是否登录
     */
    function hasLogin() {
      return get()
        .then(user => {
          if (!user) {
            return $q.reject(Error.ErrorNotLogin)
          }

          return user
        })
    }

    /**
     * 判断用户是否是管理员
     * @param isSuper 是否是超级管理员
     */
    function isAdmin(isSuper = false) {
      return hasLogin()
        .then(user => {
          if (isSuper && user.userType !== Constant.UserTypeSuperAdmin) {
            return $q.reject(Error.ErrorNotAllow)
          } else if (user.userType !== Constant.UserTypeAdmin) {
            return $q.reject(Error.ErrorNotAllow)
          }

          return user
        })
    }
  }
})()