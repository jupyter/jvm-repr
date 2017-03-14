/*
 * Copyright 2017 Netflix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jupyter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles registration of {@link Displayer} instances.
 * <p>
 * Callers should use the singleton instance of this class, which is available
 * from {@link Displayers#registration()}.
 */
public class Registration {

  static final Registration INSTANCE = new Registration();

  private final Map<Class<?>, Displayer<?>> displayers = new HashMap<>();
  private Displayer<Object> defaultDisplayer = ToStringDisplayer.get();

  public Map<Class<?>, Displayer<?>> getAll() {
    return Collections.unmodifiableMap(displayers);
  }

  /**
   * Sets the MIME type hint for all registered {@link Displayer} instances.
   *
   * @param types supported MIME types
   */
  public void setMimeTypes(String... types) {
    for (Displayer<?> displayer : displayers.values()) {
      displayer.setMimeTypes(types);
    }
  }

  /**
   * Sets the default {@link Displayer} instance. This is used to display any
   * {@link Object} with no more specific displayer.
   *
   * @param displayer a Displayer for any object.
   */
  public void setDefault(Displayer<Object> displayer) {
    this.defaultDisplayer = displayer;
  }

  /**
   * Registers a Displayer instance for a class.
   *
   * @param objClass the class of objects to display
   * @param displayer a Displayer instance
   */
  public <T> void add(Class<T> objClass, Displayer<T> displayer) {
    displayers.put(objClass, displayer);
  }

  /**
   * Finds the most specific Displayer instance for a class.
   * <p>
   * A displayer is found by checking registrations for the given class and its
   * superclasses. The most specific displayer will be returned.
   *
   * @param objClass the class of objects to display
   * @return a Displayer instance for this class or one of its superclasses.
   */
  @SuppressWarnings("unchecked")
  public <T> Displayer<? super T> find(Class<T> objClass) {
    Class<? super T> currentClass = objClass;

    while (currentClass != Object.class) {
      Displayer<?> displayer = displayers.get(currentClass);
      if (displayer != null) {
        return (Displayer<? super T>) displayer;
      }

      currentClass = currentClass.getSuperclass();
    }

    return defaultDisplayer;
  }
}
