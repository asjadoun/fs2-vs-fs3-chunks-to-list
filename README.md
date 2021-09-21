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