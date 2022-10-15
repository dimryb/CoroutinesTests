import kotlinx.coroutines.*
import kotlin.coroutines.*

fun main() {
    runTest("Test1", false) {
        runBlocking {
            val job = CoroutineScope(EmptyCoroutineContext).launch {
                launch {
                    delay(500)
                    println("ok") // <--
                }
                launch {
                    delay(500)
                    println("ok")
                }
            }
            delay(100)
            job.cancelAndJoin()
        }
    }

    runTest("Test2", false) {
        CoroutineScope(EmptyCoroutineContext).launch {
            try {
                coroutineScope {
                    throw Exception("something bad happened")
                }
            } catch (e: Exception) {
                e.printStackTrace() // <--
            }
        }
        Thread.sleep(1000)
    }

    runTest("test3", false) {
        CoroutineScope(EmptyCoroutineContext).launch {
            try {
                supervisorScope {
                    throw Exception("something bad happened")
                }
            } catch (e: Exception) {
                e.printStackTrace() // <--
            }
        }
        Thread.sleep(1000)
    }

    runTest("test4", false) {
        CoroutineScope(EmptyCoroutineContext).launch {
            try {
                coroutineScope {
                    launch {
                        delay(500)
                        throw Exception("something bad happened") // <--
                    }
                    launch {
                        throw Exception("something bad happened")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Thread.sleep(1000)
    }

    runTest("test5", false) {
        CoroutineScope(EmptyCoroutineContext).launch {
            try {
                supervisorScope {
                    launch {
                        delay(500)
                        throw Exception("something bad happened") // <--
                    }
                    launch {
                        throw Exception("something bad happened")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // <--
            }
        }
        Thread.sleep(1000)
    }

    runTest("test6", false) {
        CoroutineScope(EmptyCoroutineContext).launch {
            CoroutineScope(EmptyCoroutineContext).launch {
                launch {
                    delay(1000)
                    println("ok") // <--
                }
                launch {
                    delay(500)
                    println("ok")
                }
                throw Exception("something bad happened")
            }
        }
        Thread.sleep(1000)
    }

    runTest("test7") {
        CoroutineScope(EmptyCoroutineContext).launch {
            CoroutineScope(EmptyCoroutineContext + SupervisorJob()).launch {
                launch {
                    delay(1000)
                    println("ok") // <--
                }
                launch {
                    delay(500)
                    println("ok")
                }
                throw Exception("something bad happened")
            }
        }
        Thread.sleep(1000)
    }
}

fun runTest(testName: String, enable: Boolean = true, testFunc: () -> Unit) {
    if (!enable) return
    println("Test <$testName> start")
    testFunc()
    println("Test <$testName> end")
}
