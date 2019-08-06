package io.github.free.lock.pcp

import io.github.free.lock.sjson.JSON

object DefBox {
  def getDefaultBox() =
    new Sandbox(Map[String, BoxFun] {
      "if" -> Sandbox.toLazySanboxFun((list: List[Any], pcs: PcpServer) => {
        if (list.length <= 2 || list.length > 3) {
          throw new Exception(
            """if grammer error. if must have at least 2 params, at most 3 params. eg: ["if", true, 1, 0], ["if", true, 1]"""
          );
        }
        val conditionExp = list(0);
        val successExp   = list(1);
        val failExp      = list.lift(2).getOrElse(null);

        val condition =
          JSON.convert[Boolean](pcs.executePureCallAST(conditionExp))
        pcs.executePureCallAST(if (condition) successExp else failExp)
      })
    })
}
