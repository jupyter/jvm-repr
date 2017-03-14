## Purpose

JVM languages use various conventions to convert REPL outputs to forms that can be displayed. This project is an attempt to standardize by providing an API that libraries can use to register display methods for any REPL project, like Jupyter console or nteract.

## Using this API

This API has two main uses:
* To provide conversions from a library's JVM objects to useful representations by MIME type
* To provide kernels conversions from any JVM object to useful representations by MIME type

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

### Kernel implementers

