# pcp

A simple Lisp-Style protocol for communication

![travis-ci build status](https://travis-ci.com/idata-shopee/pcp.svg?branch=master)

## Features

- lisp style composation

- function sanbox, user can control every functions and details by provide the sandbox

- based on json grammer

## Quick example

```scala
package io.github.idata.pcp.example

import io.github.shopee.idata.pcp.{PcpServer, Sandbox, PcpClient}

case class User(name: String, age: Int)

object Main {
  def main(args: Array[String]) {
    val pcpServer = new PcpServer(
      // define the sandbox
      new Sandbox(
        Map[String, BoxFun](
          // define add function
          "add" -> Sandbox.toSanboxFun((params: List[Any], pcs: PcpServer) => {
            val a = params(0).asInstanceOf[Int]
            val b = params(1).asInstanceOf[Int]
            a + b
          })
        )
      )
    )

    // we can just use json array string
    // output: 3
    pcpServer.execute("""["add", 1, 2]""")

    // we can also use PcpClient, instead of raw string
    val p = new PcpClient()
    // """["add", 1, ["add", 2, 3]]"""
    val command = p.toJson(
      p.call("add", 1, p.call("add", 2, 3))
    )
    // output: 6
    pcpServer.execute(command)
  }
}
```

