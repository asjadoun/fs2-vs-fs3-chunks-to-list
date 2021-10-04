# fs2-vs-fs3-chunks-to-list
fs2 vs fs3 chunks to list conversion performance

Link to fs2 code: https://github.com/asjadoun/fs2-vs-fs3-chunks-to-list/blob/main/fs2/src/main/scala/ChunkToListConversionFs2App.scala

    implementation group: 'co.fs2', name: 'fs2-core_2.13', version: '2.5.9'

    Using fs2 2.5.9, Chunk of 10000 elements conversion 'toList' elapsed time : 5 ms
    Using fs2 2.5.9, Chunk of 10000 elements conversion 'toList' elapsed time : 1 ms
    Using fs2 2.5.9, Chunk of 10000 elements conversion 'toList' elapsed time : 2 ms

Link to fs3 code: https://github.com/asjadoun/fs2-vs-fs3-chunks-to-list/blob/main/fs2/src/main/scala/ChunkToListConversionFs2App.scala

    implementation group: 'co.fs2', name: 'fs2-core_2.13', version: '3.1.2'
    
    Using fs2 3.1.2, Chunk of 10000 elements conversion 'toList' elapsed time : 1518 ms
    Using fs2 3.1.2, Chunk of 10000 elements conversion 'toList' elapsed time : 1429 ms
    Using fs2 3.1.2, Chunk of 10000 elements conversion 'toList' elapsed time : 1455 ms

FS3 is nearly 1000 times slower than fs2

Link to scala3 & fs3 code: https://github.com/asjadoun/fs2-vs-fs3-chunks-to-list/blob/main/fs3scala3/src/main/scala/ChunkToListConversionFs3Scala3App.scala

    Using scala 3.0.2 and fs2 3.1.2, Chunk of 10000 elements conversion 'toList' elapsed time : 1456 ms
    Using scala 3.0.2 and fs2 3.1.2, Chunk of 10000 elements conversion 'toList' elapsed time : 1324 ms
    Using scala 3.0.2 and fs2 3.1.2, Chunk of 10000 elements conversion 'toList' elapsed time : 1525 ms

Scala3 and FS3 has same poor performance as well

### Performance test after upgrade to fs2 ver 3.1.3 

Link to fs313 code: https://github.com/asjadoun/fs2-vs-fs3-chunks-to-list/blob/main/fs313/src/main/scala/ChunkToListConversionFs313App.scala

    Using fs2 3.1.3, Chunk of 10000 elements conversion 'toList' elapsed time : 16 ms
    Using fs2 3.1.3, Chunk of 10000 elements conversion 'toList' elapsed time : 0 ms
    Using fs2 3.1.3, Chunk of 10000 elements conversion 'toList' elapsed time : 0 ms

Link to scala3 & fs313 code: https://github.com/asjadoun/fs2-vs-fs3-chunks-to-list/blob/main/fs313scala3/src/main/scala/ChunkToListConversionFs313Scala3App.scala

    Using scala 3.0.2 and fs2 3.1.3, Chunk of 10000 elements conversion 'toList' elapsed time : 18 ms
    Using scala 3.0.2 and fs2 3.1.3, Chunk of 10000 elements conversion 'toList' elapsed time : 5 ms
    Using scala 3.0.2 and fs2 3.1.3, Chunk of 10000 elements conversion 'toList' elapsed time : 7 ms

