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

public class TestToStringDisplayer {

  @Before
  @After
  public void clearGlobals() {
    Displayers.registration().clear();
  }

  private final ToStringDisplayer toString = new ToStringDisplayer();

  private static class CustomClass {
    private final String field;

    public CustomClass(String field) {
      this.field = field;
    }

    @Override
    public String toString() {
      return "CustomClass(" + field + ")";
    }
  }

  @Test
  public void testToString() {
    Assert.assertEquals("Integer",
        asMap(MIMETypes.TEXT, "3"),
        toString.display(3));
    Assert.assertEquals("Boolean",
        asMap(MIMETypes.TEXT, "false"),
        toString.display(false));
    Assert.assertEquals("Double",
        asMap(MIMETypes.TEXT, "0.9"),
        toString.display(0.9));
    Assert.assertEquals("String",
        asMap(MIMETypes.TEXT, "crunchy"),
        toString.display("crunchy"));
    Assert.assertEquals("Custom class",
        asMap(MIMETypes.TEXT, "CustomClass(sledding)"),
        toString.display(new CustomClass("sledding")));
    Assert.assertEquals("int[]",
        asMap(MIMETypes.TEXT, "[34, 35, 36]"),
        toString.display(new int[] { 34, 35, 36 }));
    Assert.assertEquals("Object[]",
        asMap(MIMETypes.TEXT, "[CustomClass(tangerine), 34]"),
        toString.display(new Object[] { new CustomClass("tangerine"), 34 }));
    Assert.assertEquals("Object[]",
        asMap(MIMETypes.TEXT, "[CustomClass(tangerine), null, 34]"),
        toString.display(new Object[] { new CustomClass("tangerine"), null, 34 }));
  }

  @Test
  public void testCustomDisplayer() {
    Displayers.register(CustomClass.class, new Displayer<CustomClass>() {
      @Override
      public Map<String, String> display(CustomClass obj) {
        return asMap(MIMETypes.TEXT, obj.field);
      }
    });

    Assert.assertEquals("Object[] with custom displayer",
        asMap(MIMETypes.TEXT, "[tangerine, 34]"),
        toString.display(new Object[] { new CustomClass("tangerine"), 34 }));
  }

  private Map<String, String> asMap(String mimeType, String asText) {
    Map<String, String> result = new HashMap<>();
    result.put(mimeType, asText);
    return result;
  }
}
