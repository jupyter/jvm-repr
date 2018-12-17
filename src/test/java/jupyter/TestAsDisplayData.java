package jupyter;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestAsDisplayData {

    private class Thing implements AsDisplayData {
        private int n;
        public Thing(int n) {
            this.n = n;
        }
        public Map<String, String> display() {
            Map<String, String> result = new HashMap<>();
            result.put(MIMETypes.TEXT, "Thing(" + n + ")");
            return result;
        }
    }

    @Test
    public void simple() {
        Map<String, String> expectedResult = new HashMap<>();
        expectedResult.put(MIMETypes.TEXT, "Thing(2)");

        Thing thing = new Thing(2);
        Map<String, String> result = Displayers.display(thing);
        Assert.assertEquals("Should rely on AsDisplayData" + expectedResult + " " + result, expectedResult, result);

        Displayers.registration().clear();
        Assert.assertEquals("Should rely on AsDisplayData" + expectedResult + " " + result, expectedResult, result);
    }
}
