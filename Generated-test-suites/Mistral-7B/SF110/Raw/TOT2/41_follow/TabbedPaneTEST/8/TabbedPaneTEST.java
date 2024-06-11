import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JButton button = new JButton();
        tabbedPane.add(button, "Test Tab");
        int x = 10, y = 10;
        Component actual = tabbedPane.findComponentAt(x, y);
        assertTrue(actual == button);
    }

    @Test
    public void testFindComponentAt_NegativeCoordinates() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JButton button = new JButton();
        tabbedPane.add(button, "Test Tab");
        int x = -10, y = -10;
        Component actual = tabbedPane.findComponentAt(x, y);
        assertNull(actual);
    }

    @Test
    public void testFindComponentAt_OutOfBoundsCoordinates() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JButton button = new JButton();
        tabbedPane.add(button, "Test Tab");
        tabbedPane.setBounds(10, 10, 100, 100);
        int x = 110, y = 110;
        Component actual = tabbedPane.findComponentAt(x, y);
        assertNull(actual);
    }

    @Test
    public void testFindComponentAt_NoComponentPresentAtGivenCoordinates() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int x = 10, y = 10;
        Component actual = tabbedPane.findComponentAt(x, y);
        assertNull(actual);
    }

    // Add test cases for other methods
}

//