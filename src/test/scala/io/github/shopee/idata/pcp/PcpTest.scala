package io.github.shopee.idata.pcp

class PcpTest extends org.scalatest.FunSuite {
  test("purecall: base") {
    val pureCallServer = new PcpServer(
      new Sandbox(
        Map[String, BoxFun](
          "add" -> Sandbox.toSanboxFun((list: List[Any], pcs: PcpServer) => {
            val a = list(0).asInstanceOf[Int]
            val b = list(1).asInstanceOf[Int]
            a + b
          })
        )
      )
    )

    assert(pureCallServer.execute("""["add", 1, 2]""") == 3);
  }

  test("purecallclient") {
    val pureCallClient = new PcpClient()
    assert(
      pureCallClient
        .toJson(pureCallClient.call("add", 1, 2)) == """["add",1,2]"""
    );
  }
  test("purecallclient: nest") {
    val pcc = new PcpClient()
    assert(pcc.toJson(pcc.call("add", 1, pcc.call("succ", 3))) == """["add",1,["succ",3]]""");
  }

  test("purecall: concat string") {
    val pureCallServer = new PcpServer(
      new Sandbox(
        Map[String, BoxFun](
          "concat" -> Sandbox.toSanboxFun((list: List[Any], pcs: PcpServer) => {
            val a = list(0).asInstanceOf[String]
            val b = list(1).asInstanceOf[String]

            a + b
          })
        )
      )
    )

    assert(
      pureCallServer
        .execute("""["concat", "hello,", "world"]""") == "hello,world"
    );
  }

  test("purecall: if expression-fail") {
    val pureCallServer = new PcpServer(
      new Sandbox(
        Map[String, BoxFun](
          ">" -> Sandbox.toSanboxFun((list: List[Any], pcs: PcpServer) => {
            val a = list(0).asInstanceOf[Int]
            val b = list(1).asInstanceOf[Int]
            a > b
          })
        )
      )
    )

    assert(pureCallServer.execute("""["if", [">", 3, 4], 1, 2]""") == 2);
  }

  test("purecall: if expression-success") {
    val pureCallServer = new PcpServer(
      new Sandbox(
        Map[String, BoxFun](
          ">" -> Sandbox.toSanboxFun((list: List[Any], pcs: PcpServer) => {
            val a = list(0).asInstanceOf[Int]
            val b = list(1).asInstanceOf[Int]
            a > b
          })
        )
      )
    )

    assert(pureCallServer.execute("""["if", [">", 6, 4], 1, 2]""") == 1)
  }

  test("purecall: if exception") {
    val pureCallServer = new PcpServer(new Sandbox(Map[String, BoxFun]()))

    assertThrows[Exception] {
      pureCallServer.execute("""["if", 0]""")
    }
  }

  test("purecall: raw data") {
    val pureCallServer = new PcpServer(new Sandbox(Map[String, BoxFun]()))

    assert(pureCallServer.execute("""["'", 1, 2, 3]""") == List(1, 2, 3));
  }

  test("missing function name") {
    val pureCallServer = new PcpServer(new Sandbox(Map[String, BoxFun]()))
    assertThrows[Exception] {
      pureCallServer.execute("""["fun", 2, 3]""")
    }
  }
}
