import ghm.follow.gui.TabbedPane;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TabbedPaneTest {
    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JButton component = new JButton();
        tabbedPane.add(component);
        int x = 10, y = 10;
        Component actual = tabbedPane.findComponentAt(x, y);
        assertEquals(component, actual);
    }
}