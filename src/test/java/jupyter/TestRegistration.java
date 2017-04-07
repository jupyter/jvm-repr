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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestRegistration {

  private static class TestObject {
  }

  @Before
  @After
  public void clearGlobals() {
    Displayers.registration().clear();
  }

  @Test
  public void testClassRegistration() {
    // generate a random string to validate the display method. only the right
    // displayer will return this string.
    Random rand = new Random();
    final String expectedString = Double.toString(rand.nextDouble());

    Displayer<TestObject> expected = new Displayer<TestObject>() {
      @Override
      public Map<String, String> display(TestObject obj) {
        return asMap(MIMETypes.TEXT, expectedString);
      }
    };

    Displayers.register(TestObject.class, expected);

    Assert.assertEquals("Should return registered displayer for class",
        expected, Displayers.registration().find(TestObject.class));
    Assert.assertEquals("Should return registered displayer for instance",
        asMap(MIMETypes.TEXT, expectedString),
        Displayers.display(new TestObject()));
  }

  @Test
  public void testArrayRegistration() {
    // generate a random string to validate the display method. only the right
    // displayer will return this string.
    Random rand = new Random();
    final String expectedString = Double.toString(rand.nextDouble());

    Displayer<TestObject[]> expected = new Displayer<TestObject[]>() {
      @Override
      public Map<String, String> display(TestObject[] obj) {
        return asMap(MIMETypes.TEXT, expectedString);
      }
    };

    Displayers.register(TestObject[].class, expected);

    Assert.assertEquals("Should return registered displayer for array class",
        expected, Displayers.registration().find(TestObject[].class));
    Assert.assertEquals("Should return registered displayer for array instance",
        asMap(MIMETypes.TEXT, expectedString),
        Displayers.display(new TestObject[] {}));
  }

  private Map<String, String> asMap(String mimeType, String asText) {
    Map<String, String> result = new HashMap<>();
    result.put(mimeType, asText);
    return result;
  }
}
