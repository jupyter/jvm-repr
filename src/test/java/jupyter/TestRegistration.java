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

import jupyter.Displayers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestRegistration {

  private interface TestSuperInterface {
  }

  private interface TestInterface extends TestSuperInterface {
  }

  private static class TestObject implements TestInterface {
  }

  private static class TestObjectSubclass extends TestObject {
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
  public void testWalkSuperclasses() {
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

    Assert.assertEquals("Should return registered displayer for sub-class",
        expected, Displayers.registration().find(TestObjectSubclass.class));
    Assert.assertEquals("Should return registered displayer for instance",
        asMap(MIMETypes.TEXT, expectedString),
        Displayers.display(new TestObjectSubclass()));
  }

  @Test
  public void testWalkInterfaces() {
    // generate a random string to validate the display method. only the right
    // displayer will return this string.
    Random rand = new Random();
    final String expectedString = Double.toString(rand.nextDouble());

    Displayer<TestInterface> expected = new Displayer<TestInterface>() {
      @Override
      public Map<String, String> display(TestInterface obj) {
        return asMap(MIMETypes.TEXT, expectedString);
      }
    };

    Displayers.register(TestInterface.class, expected);

    Assert.assertEquals("Should return registered displayer for interface",
        expected, Displayers.registration().find(TestInterface.class));
    Assert.assertEquals("Should return registered displayer for class",
        expected, Displayers.registration().find(TestObject.class));
    Assert.assertEquals("Should return registered displayer for sub-class",
        expected, Displayers.registration().find(TestObjectSubclass.class));
    Assert.assertEquals("Should return registered displayer for instance",
        asMap(MIMETypes.TEXT, expectedString),
        Displayers.display(new TestObjectSubclass()));
  }

  @Test
  public void testWalkSuperInterfaces() {
    // generate a random string to validate the display method. only the right
    // displayer will return this string.
    Random rand = new Random();
    final String expectedString = Double.toString(rand.nextDouble());

    Displayer<TestSuperInterface> expected = new Displayer<TestSuperInterface>() {
      @Override
      public Map<String, String> display(TestSuperInterface obj) {
        return asMap(MIMETypes.TEXT, expectedString);
      }
    };

    Displayers.register(TestSuperInterface.class, expected);

    Assert.assertEquals("Should return registered displayer for interface",
        expected, Displayers.registration().find(TestInterface.class));
    Assert.assertEquals("Should return registered displayer for interface",
        expected, Displayers.registration().find(TestSuperInterface.class));
    Assert.assertEquals("Should return registered displayer for class",
        expected, Displayers.registration().find(TestObject.class));
    Assert.assertEquals("Should return registered displayer for sub-class",
        expected, Displayers.registration().find(TestObjectSubclass.class));
    Assert.assertEquals("Should return registered displayer for instance",
        asMap(MIMETypes.TEXT, expectedString),
        Displayers.display(new TestObjectSubclass()));
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

  @Test
  public void testNullDefault() {
    Displayers.registration().setDefault(null);
    Assert.assertEquals("Should return null default displayer for class",
        null, Displayers.registration().find(TestObject.class));
    Assert.assertEquals("Should return null default display for instance",
        Collections.emptyMap(),
        Displayers.display(new TestObject()));
  }

  private Map<String, String> asMap(String mimeType, String asText) {
    Map<String, String> result = new HashMap<>();
    result.put(mimeType, asText);
    return result;
  }
}
