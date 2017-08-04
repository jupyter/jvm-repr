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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestMimeTypeNotification {

  @Before
  @After
  public void clearGlobals() {
    Displayers.registration().clear();
  }

  private static class LimitingDisplayer extends Displayer<String> {
    private Set<String> mimeTypes = new HashSet<>(Arrays.asList(
        MIMETypes.TEXT, MIMETypes.HTML));

    @Override
    public Map<String, String> display(String obj) {
      Map<String, String> result = new HashMap<>();
      if (mimeTypes.contains(MIMETypes.TEXT)) {
        result.put(MIMETypes.TEXT, obj);
      }
      if (mimeTypes.contains(MIMETypes.HTML)) {
        result.put(MIMETypes.HTML, "<div>" + obj + "</div>");
      }
      return result;
    }

    @Override
    public void setMimeTypes(String... types) {
      mimeTypes = new HashSet<>(Arrays.asList(types));
    }
  }

  @Test
  public void testNotificationAfterRegistration() {
    Set<String> expectedTypes = new HashSet<>(Collections.singletonList(
        MIMETypes.TEXT));

    LimitingDisplayer disp = new LimitingDisplayer();
    Displayers.register(String.class, disp);
    Displayers.setMimeTypes(MIMETypes.TEXT);

    Assert.assertEquals("Should set mime types on registered displayer",
        expectedTypes, disp.mimeTypes);
  }

  @Test
  public void testNotificationBeforeRegistration() {
    Set<String> expectedTypes = new HashSet<>(Collections.singletonList(
        MIMETypes.TEXT));

    Displayers.setMimeTypes(MIMETypes.TEXT);

    LimitingDisplayer disp = new LimitingDisplayer();
    Displayers.register(String.class, disp);

    Assert.assertEquals("Should set mime types when displayer registers",
        expectedTypes, disp.mimeTypes);
  }
}
