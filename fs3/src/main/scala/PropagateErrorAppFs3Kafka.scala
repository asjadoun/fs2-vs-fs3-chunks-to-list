import cats.effect._
import fs2.Stream
import fs2.kafka.{AutoOffsetReset, ConsumerSettings, Deserializer, KafkaConsumer}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object PropagateErrorAppFs3Kafka extends IOApp {

  def buildStream[F[_], K, V](servers: String, topic: String, groupId: String,
                              keyDeserializer: Deserializer[F, K],
                              valueDeserializer: Deserializer[F, V],
                             )
                       (implicit F: Async[F], logger: Logger[F]): Stream[F, Unit] = {
    val consumerSettings: ConsumerSettings[F, K, V] =
      ConsumerSettings[F, K, V](
        keyDeserializer = keyDeserializer,
        valueDeserializer = valueDeserializer
      )
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withGroupId(groupId)
        .withEnableAutoCommit(false)
        .withBootstrapServers(servers)

    val stream: Stream[F, Unit] =
      KafkaConsumer.stream[F, K, V](consumerSettings)
        .subscribeTo(topic)
        .flatMap(_.partitionsMapStream)
        .flatMap(x => Stream.fromIterator(x.iterator, 2048))
        .map {
          case (partition, stream) =>
            stream
              .evalMap {
                consumerRecord =>
                  logger.info(s"${partition.topic()}^${partition.partition()} - Message: ${consumerRecord.record.value}")
              }
            .evalMap(_ => F.raiseError(new RuntimeException("BOOM!!!")))
        }
        .parJoinUnbounded
    stream
  }

  def buildStream2[F[_], K, V](servers: String, topic: String, groupId: String,
                              keyDeserializer: Deserializer[F, K],
                              valueDeserializer: Deserializer[F, V],
                             )
                             (implicit F: Async[F], logger: Logger[F]): Stream[F, Unit] =
    for {
      settings <- Stream.emit(ConsumerSettings[F, K, V](
        keyDeserializer = keyDeserializer,
        valueDeserializer = valueDeserializer
      )
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withGroupId(groupId)
        .withEnableAutoCommit(false)
        .withBootstrapServers(servers))
      _ <- KafkaConsumer.stream[F, K, V](settings)
        .subscribeTo(topic)
        .flatMap(_.partitionsMapStream)
        .flatMap(x => Stream.fromIterator(x.iterator, 2048))
        .map {
          case (topicPartition, partitionedStream) =>
            partitionedStream
              .parEvalMap(12) {
                committable => F.delay(committable.record)
              }
              .evalMap {
                consumerRecord =>
                  logger.info(s"${topicPartition.topic()}^${topicPartition.partition()} - Message: ${consumerRecord.value}")
              }
              .evalTap(_ => F.raiseError(new RuntimeException("BOOM!!!")))
//              .evalTap(_ => F.delay(F.raiseError(new RuntimeException("BOOM!!!"))))
        }
        .parJoinUnbounded
    } yield ()

  override def run(args: List[String]): IO[ExitCode] = {
    implicit def logger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger[F]

    val keyDeserializer: Deserializer[IO, Option[String]] = Deserializer[IO, Option[String]]
    val valueDeserializer: Deserializer[IO, String] = Deserializer[IO, String]

    val program: Stream[IO, Unit] = buildStream2[IO, Option[String], String](
      servers = "localhost:9092",
      topic = "test.01", groupId = "test.01-group.01",
      keyDeserializer = keyDeserializer,
      valueDeserializer = valueDeserializer,
    )

    program.compile.drain.as(ExitCode.Success)

  }
}
