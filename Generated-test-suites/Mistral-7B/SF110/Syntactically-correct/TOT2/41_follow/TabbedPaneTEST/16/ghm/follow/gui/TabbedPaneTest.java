import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {
    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Add components and set the selected component
        Component expectedComponent = new JButton("Expected");
        tabbedPane.addTab("Test Tab", expectedComponent);
        tabbedPane.setSelectedIndex(0);

        // Test findComponentAt with valid coordinates
        int x = expectedComponent.getX();
        int y = expectedComponent.getY();
        Component actualComponent = tabbedPane.findComponentAt(x, y);
        assertEquals(expectedComponent, actualComponent);

        // Test findComponentAt with invalid coordinates (outside the component bounds)
        int xOutside = expectedComponent.getX() + expectedComponent.getWidth();
        int yOutside = expectedComponent.getY() + expectedComponent.getHeight();
        Component actualComponentOutside = tabbedPane.findComponentAt(xOutside, yOutside);
        assertNull(actualComponentOutside);

        // Test findComponentAt with a null component
        tabbedPane.removeTabAt(0);
        Component nullComponent = null;
        Component actualComponentNull = tabbedPane.findComponentAt(x, y);
        assertNull(actualComponentNull);

        // Test findComponentAt with an empty tabbed pane
        TabbedPane emptyTabbedPane = new TabbedPane(new FollowAppAttributes());
        Component actualComponentEmpty = emptyTabbedPane.findComponentAt(0, 0);
        assertNull(actualComponentEmpty);
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Add components and set the initial index
        tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
        tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
        tabbedPane.setSelectedIndex(0);

        // Test setting index within the tabbed pane bounds
        tabbedPane.setSelectedIndex(1);
        JLabel actualComponent = (JLabel) tabbedPane.getComponentAt(1);
        assertEquals("Tab 2", actualComponent.getText());

        // Test setting index to an invalid value (out of bounds)
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount());
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Add components and set the initial index
        tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
        tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
        tabbedPane.setSelectedIndex(0);

        // Test setting component with a valid FileFollowingPane
        FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.setSelectedComponent(fileFollowingPane);
        assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());

        // Test setting component with a null FileFollowingPane
        FileFollowingPane nullFileFollowingPane = null;
        tabbedPane.setSelectedComponent(nullFileFollowingPane);
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Add components and remove a tab
        tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
        tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
        tabbedPane.removeTabAt(0);

        // Test removing a tab within the tabbed pane bounds
        tabbedPane.removeTabAt(0);
        assertEquals(1, tabbedPane.getComponentCount());

        // Test removing a tab to an invalid index (out of bounds)
        tabbedPane.removeTabAt(tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Add components and set the selected component
        FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.addTab("Test Tab", fileFollowingPane);
        tabbedPane.setSelectedComponent(fileFollowingPane);

        // Test handleSelectedFile with a valid FileFollowingPane
        assertEquals(fileFollowingPane.getFollowedFile().getParentFile(), tabbedPane.getAttributes().getLastFileChooserDirectory());

        // Test handleSelectedFile with a null FileFollowingPane
        tabbedPane.removeTabAt(0);
        tabbedPane.setSelectedComponent(null);
    }
}