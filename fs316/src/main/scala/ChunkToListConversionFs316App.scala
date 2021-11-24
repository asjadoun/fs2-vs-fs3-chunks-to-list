import cats.effect.unsafe.IORuntime
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

object ChunkToListConversionFs316App extends IOApp {
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
      .chunkN(args(0).toInt)
      .evalMap(c => IO(time(s"""Using fs2 3.1.6, Chunk of ${c.size} elements conversion 'toList'""", c.toList)))
//      .evalMap(c => IO(time(s"""Using fs2 3.1.6, Chunk of ${c.size} elements conversion 'compact.toList'""", c.compact.toList)))
//      .evalMap(c => IO(time(s"""Using fs2 3.1.6, Chunk of ${c.size} elements conversion 'toArraySlice.toList'""", c.toArraySlice.toList)))
      .take(args(1).toInt)
      .compile
      .drain
      .unsafeRunSync()

    IO(ExitCode.Success)
  }}