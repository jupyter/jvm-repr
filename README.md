# JVM Repr

[![Build Status](https://travis-ci.org/jupyter/jvm-repr.svg?branch=master)](https://travis-ci.org/jupyter/jvm-repr)
[![JVM repr on jitpack](https://jitpack.io/v/jupyter/jvm-repr.svg)](https://jitpack.io/#jupyter/jvm-repr)

Standardizing REPL outputs for across JVM kernels (Scala, Clojure, Groovy, ...)

## Purpose

JVM languages use various conventions to convert REPL outputs to forms that can be displayed. This project is an attempt to standardize by providing an API that libraries can use to register display methods for any REPL project, like Jupyter console or nteract.

## Using this API

This API has two main uses:
* For library authors to provide conversions from a library's JVM objects to useful representations by MIME type
* To provide a common way for kernel implementers to convert any JVM object to useful representations by MIME type

### Library authors

Library authors can register conversion code by implementing a `Displayer` and registering it with `Displayers`.

For example, the following will register a displayer for Vegas graphs:

```scala
import java.util.Map
import jupyter.Displayer
import jupyter.Displayers
import scala.collection.JavaConverters._
import vegas.DSL.ExtendedUnitSpecBuilder

...

  Displayers.register(classOf[ExtendedUnitSpecBuilder],
    new Displayer[ExtendedUnitSpecBuilder] {
      override def display(plot: ExtendedUnitSpecBuilder): Map[String, String] = {
        val plotAsJson = plot.toJson
        Map(
          "text/plain" -> plotAsJson,
          "application/json" -> plotAsJson,
          "text/html" -> new StaticHTMLRenderer(plotAsJson).frameHTML()
        ).asJava
      }
    })
```

Any kernel implementation can use the method to display Vegas graphs for the DSL objects.

Library authors can optionally implement `setMimeTypes(String...)` to receive hints for the MIME types that the kernel or front-end supports. It is recommended that library authors use these hints to avoid expensive conversions.

### Kernel implementers

Kernel implementors can use this API to display registered objects:

```java
import java.util.Map

...

  Object result = interpreter.eval(code);
  Map<String, String> resultByMIME = Displayers.display(result);
  Kernel.this.display(resultByMIME);
```

Kernel implementers can optionally call `Displayers.setMimeTypes(String...)` to send hints to display implementations with the set of MIME types that can be used by the kernel or front-end.
