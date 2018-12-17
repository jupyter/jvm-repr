package jupyter;

import java.util.Map;

/**
 * Convenience interface for classes whose display data is known in advance.
 *
 * Classes implementing this interface are displayed via the {@link AsDisplayData#display} method
 * by default, without needing to register a {@link Displayer} in advance.
 */
public interface AsDisplayData {
    Map<String, String> display();

    Displayer<AsDisplayData> displayer = new Displayer<AsDisplayData>() {
        public Map<String, String> display(AsDisplayData obj) {
            return obj.display();
        }
    };
}
