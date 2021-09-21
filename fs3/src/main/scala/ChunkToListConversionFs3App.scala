import cats.effect.unsafe.IORuntime
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

object ChunkToListConversionFs3App extends IOApp {
  private def time[R](action: String, thunk: => R): R = {
    val start = System.currentTimeMillis()
    val result = thunk
    val end = System.currentTimeMillis()
    println(s"$action elapsed time : ${end - start} ms")
    result
  }
  override def run(args: List[String]): IO[ExitCode] = {
    implicit val runtime: IORuntime = IORuntime.global

    val stream: Unit = Stream
      .repeatEval(IO(System.currentTimeMillis()))
      .chunkN(10000)
      .evalMap(c => IO(time(s"""Using fs2 3.1.2, Chunk of ${c.size} elements conversion 'toList'""", c.toList)))
      .take(3)
      .compile
      .drain
      .unsafeRunSync()

    IO(ExitCode.Success)
  }}