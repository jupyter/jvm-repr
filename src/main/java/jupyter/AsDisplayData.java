package jupyter;

import java.util.Map;

/**
 * Convenience interface for classes whose display data is known in advance.
 * <p>
 * Classes implementing this interface are displayed via the {@link AsDisplayData#display} method
 * by default, without needing to register a {@link Displayer} in advance.
 */
public interface AsDisplayData {
  /**
   * Called to display this object.
   * <p>
   * This method should return a map of MIME type strings to representations of
   * this object in that MIME type.
   * <p>
   * To avoid extra conversion, kernels or front-ends can call
   * {@link #setMimeTypes(String...)} to pass supported MIME types.
   * Implementations may ignore these MIME type hints.
   *
   * @return a Map of representations of this object by MIME type
   */
  Map<String, String> display();

  /**
   * Called to pass the MIME types supported by the kernel or front-end.
   * <p>
   * Implementations may ignore these MIME type hints.
   *
   * @see Displayer#setMimeTypes(String...)
   *
   * @param types MIME types that are supported by the kernel
   */
  default void setMimeTypes(String... types) {
  }
}
