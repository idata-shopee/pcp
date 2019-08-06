package io.github.free.lock.pcp

import io.github.free.lock.sjson.JSON

/**
  * you can write request data by using pure json or client api
  *
  * pure json style looks like: ["add", 1, ["succ", 2]]
  *
  * client api style looks like: c("add", 1, c("succ", 2))
  */
case class CallResult(result: Any)

class PcpClient() {
  // convert to json string
  def call(funName: String, params: Any*): CallResult = {
    val newParams = for (param <- params.toList) yield {
      if (param.isInstanceOf[CallResult]) {
        param.asInstanceOf[CallResult].result
      } else {
        val value = param

        if (value.isInstanceOf[List[Any]]) {
          "'" :: value.asInstanceOf[List[Any]] // (1, 2, 3) => (', 1, 2, 3)
        } else {
          value
        }
      }
    }

    new CallResult(funName :: newParams)
  }

  def toJson(list: CallResult): String = JSON.stringify(list.result)
}
