/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.internal.config

import java.util.concurrent.TimeUnit

import org.apache.spark.network.util.ByteUnit

private[spark] object Python {
  val PYTHON_WORKER_REUSE = ConfigBuilder("spark.python.worker.reuse")
    .version("1.2.0")
    .booleanConf
    .createWithDefault(true)

  val PYTHON_TASK_KILL_TIMEOUT = ConfigBuilder("spark.python.task.killTimeout")
    .version("2.2.2")
    .timeConf(TimeUnit.MILLISECONDS)
    .createWithDefaultString("2s")

  val PYTHON_USE_DAEMON = ConfigBuilder("spark.python.use.daemon")
    .doc("Because forking processes from Java is expensive, " +
      "PySpark prefers launching a single Python daemon, `pyspark/daemon.py` (by default) " +
      "and tell it to fork new workers for our tasks. " +
      "This daemon currently only works on UNIX-based systems " +
      "because it uses signals for child management, " +
      "so we can also fall back to launching workers, `pyspark/worker.py` (by default) directly.")
    .version("2.3.0")
    .booleanConf
    .createWithDefault(!System.getProperty("os.name").startsWith("Windows"))

  val PYTHON_LOG_INFO = ConfigBuilder("spark.executor.python.worker.log.details")
    .version("3.5.0")
    .booleanConf
    .createWithDefault(false)

  val PYTHON_DAEMON_MODULE = ConfigBuilder("spark.python.daemon.module")
    .doc("The Python module to run the daemon to execute Python workers." +
      "Note that this configuration has effect only when " +
      s"'${PYTHON_USE_DAEMON.key}' is enabled and the platform is not Windows.")
    .version("2.4.0")
    .stringConf
    .createWithDefaultString("pyspark.daemon")

  val PYTHON_WORKER_MODULE = ConfigBuilder("spark.python.worker.module")
    .doc("The Python module to run Python workers." +
      "Note that this configuration has effect when " +
      s"'${PYTHON_USE_DAEMON.key}' is disabled or the platform is Windows.")
    .version("2.4.0")
    .stringConf
    .createWithDefaultString("pyspark.worker")

  val PYSPARK_EXECUTOR_MEMORY = ConfigBuilder("spark.executor.pyspark.memory")
    .version("2.4.0")
    .bytesConf(ByteUnit.MiB)
    .createOptional

  val PYTHON_AUTH_SOCKET_TIMEOUT = ConfigBuilder("spark.python.authenticate.socketTimeout")
    .internal()
    .version("3.1.0")
    .timeConf(TimeUnit.SECONDS)
    .createWithDefaultString("15s")

  val PYTHON_WORKER_FAULTHANLDER_ENABLED = ConfigBuilder("spark.python.worker.faulthandler.enabled")
    .doc("When true, Python workers set up the faulthandler for the case when the Python worker " +
      "exits unexpectedly (crashes), and shows the stack trace of the moment the Python worker " +
      "crashes in the error message if captured successfully.")
    .version("3.2.0")
    .booleanConf
    .createWithDefault(false)
}
