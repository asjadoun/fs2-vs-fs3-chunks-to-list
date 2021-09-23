import cats.effect.{IO, IOApp}
import fs2.Stream

object ChunkToListConversionFs3Scala3App extends IOApp.Simple {

  private def time[R](action: String, thunk: => R): R = {
    val start = System.currentTimeMillis()
    val result = thunk
    val end = System.currentTimeMillis()
    println(s"$action elapsed time : ${end - start} ms")
    result
  }

  val stream = Stream
    .repeatEval(IO(System.currentTimeMillis()))
    .chunkN(10000)
    .evalTap{c => IO(time(s"""Using scala 3.0.2 and fs2 3.1.2, Chunk of ${c.size} elements conversion 'toList'""", c.toList))}
//    .evalTap{c => IO(time(s"""Using scala 3.0.2 and fs2 3.1.2, Chunk of ${c.size} elements conversion 'toArraySlice.toList'""", c.toArraySlice.toList))}
//    .evalTap{c => IO(time(s"""Using scala 3.0.2 and fs2 3.1.2, Chunk of ${c.size} elements conversion 'compact.toList'""", c.compact.toList))}
    .take(3)

  def run: IO[Unit] = stream.compile.drain
}