// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/anton/project1/conf/routes
// @DATE:Sun Aug 26 20:02:05 EDT 2018


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}