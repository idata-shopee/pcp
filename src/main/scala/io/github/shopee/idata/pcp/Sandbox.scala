package io.github.shopee.idata.pcp

object CallableFun {
  type GeneralFun = (List[Any], PcpServer) => Any
}
trait BoxFun {}
case class SandboxFun(val fun: CallableFun.GeneralFun)     extends BoxFun
case class LazySandboxFun(val fun: CallableFun.GeneralFun) extends BoxFun

object Sandbox {
  def toSanboxFun(fun: CallableFun.GeneralFun): SandboxFun = new SandboxFun(fun)
  def toLazySanboxFun(fun: CallableFun.GeneralFun): LazySandboxFun =
    new LazySandboxFun(fun)
  def mergeSanboxs(box1: Sandbox, box2: Sandbox): Sandbox =
    new Sandbox(box1.funMap ++ box2.funMap)
}

class Sandbox(val funMap: Map[String, BoxFun]) {
  def getSandboxFun(funName: String): BoxFun =
    if (funMap.contains(funName)) {
      funMap(funName)
    } else {
      throw new Exception(s"missing function $funName in our sandbox.");
    }
}
