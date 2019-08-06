package io.github.free.lock.pcp

import io.github.free.lock.sjson.JSON

case class FunNode(
    val funName: String,
    val Params: List[Any] = List[Any]()
)

/**
  * simple calling protocol
  *
  * grammer based on json
  *
  * ["fun1", 1, 2, ["fun2", 3]] => fun1(1, 2, fun2(3))
  *
  * TODO escape and unescape list head string
  */
class PcpServer(_sandbox: Sandbox) {
  private val sandbox: Sandbox =
    Sandbox.mergeSanboxs(DefBox.getDefaultBox(), _sandbox)

  def parseJson(source: String) = parseJsonAst(JSON.parse(source))

  def execute(source: String): Any =
    executePureCallAST(parseJson(source))

  def executePureCallAST(source: Any): Any =
    source match {
      case FunNode(funName, params) => {
        sandbox.getSandboxFun(funName) match {
          case SandboxFun(fun) => {
            // execute sandbox function
            fun(params.map(executePureCallAST), this)
          }
          case LazySandboxFun(fun) => {
            // execute lazy sandbox function
            fun(params, this)
          }
        }
      }
      case _ => source
    }

  def parseJsonAst(source: Any): Any =
    source match {
      case arr: List[Any] => {
        if (arr.length == 0) {
          arr
        } else {
          arr.head match {
            case s: String => {
              if (s == "'") {
                arr.tail // tail as pure data, like (', 1, 2, 3) -> data list (1, 2, 3)
              } else {
                new FunNode(s, arr.tail.map(parseJsonAst)) // as function node
              }
            }

            case _ => arr
          }
        }
      }

      case _ => source
    }
}
