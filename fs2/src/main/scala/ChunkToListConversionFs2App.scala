import cats.effect.{ContextShift, ExitCode, IO, IOApp, Timer}
import fs2.Stream

import scala.concurrent.ExecutionContext

object ChunkToListConversionFs2App extends IOApp {
  private def time[R](action: String, thunk: => R): R = {
    val start = System.currentTimeMillis()
    val result = thunk
    val end = System.currentTimeMillis()
    println(s"$action elapsed time : ${end - start} ms")
    result
  }
  override def run(args: List[String]): IO[ExitCode] = {
    implicit val ec: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
    implicit val timer: Timer[IO] = IO.timer(ExecutionContext.global)
    val stream = Stream
      .repeatEval(IO(System.currentTimeMillis()))
      .chunkN(10000)
      .evalMap(c => IO(time(s"""Using fs2 2.5.9, Chunk of ${c.size} elements conversion 'toList'""", c.toList)))
      .take(3)
      .compile
      .drain
      .unsafeRunSync()

    IO(ExitCode.Success)
  }
}
