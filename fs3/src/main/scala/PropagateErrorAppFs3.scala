import cats.effect.unsafe.IORuntime
import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

object PropagateErrorAppFs3 extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    implicit val runtime: IORuntime = IORuntime.global

    val s1 = Stream.emit(s"Hello @ ${java.time.LocalDateTime.now().toString}").covary.repeat.evalTap(x => IO(println(x)))
    val s2 = Stream.emit(s"Hi    @ ${java.time.LocalDateTime.now().toString}").covary.repeat.evalTap(x => IO(println(x)))
    val s3 = Stream.emit(s"Bye   @ ${java.time.LocalDateTime.now().toString}").covary.repeat.evalTap(x => IO(println(x)))
    val err = Stream.emit(s"BOOM! @ ${java.time.LocalDateTime.now().toString}").covary.repeat.evalTap(x => IO.raiseError(new RuntimeException(s"$x")))

    val s = for {
      _ <- Stream(s1, s2, s3, err)
        .parJoinUnbounded
    } yield ()
    s.compile.drain.as(ExitCode.Success)
//    java.lang.RuntimeException: BOOM!

//    Stream(s1, s2, s3, err).parJoinUnbounded.compile.drain.as(ExitCode.Success)
//    java.lang.RuntimeException: BOOM!

//    Stream(s1, s2, s3).covary[IO].parJoinUnbounded.concurrently(err).compile.drain.as(ExitCode.Success)
//    java.lang.RuntimeException: BOOM!

//    s1.merge(s1).merge(s2).merge(s3).merge(err).compile.drain.as(ExitCode.Success)
//    java.lang.RuntimeException: BOOM!

//    s1.concurrently(s2).concurrently(s3).concurrently(err).compile.drain.as(ExitCode.Success)
//    java.lang.RuntimeException: BOOM!

  }
}
