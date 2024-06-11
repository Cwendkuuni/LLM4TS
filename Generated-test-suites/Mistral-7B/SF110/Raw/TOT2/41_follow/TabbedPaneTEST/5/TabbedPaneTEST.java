import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.gui.*;
import javax.swing.*;

public class TabbedPaneTest {

    @Test
    public void testTabbedPaneConstruction() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        assertNotNull(tabbedPane);
    }

    @Test
    public void testTabbedPaneConstruction_NullAttributes() {
        TabbedPane tabbedPane = new TabbedPane(null);
        assertNull(tabbedPane);
    }

    @Test
    public void testFindComponentAt_NoComponentAtLocation() {
        JTabbedPane tabbedPane = new JTabbedPane();
        assertNull(tabbedPane.findComponentAt(10, 10));
    }

    @Test
    public void testFindComponentAt_ComponentAtLocation() {
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel panel = new JPanel();
        tabbedPane.add(panel);
        Component component = panel.getComponentAt(0, 0);
        int x = tabbedPane.getComponentAt(0).getX();
        int y = tabbedPane.getComponentAt(0).getY();
        Component expectedComponent = tabbedPane.findComponentAt(x, y);
        assertEquals(component, expectedComponent);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindComponentAt_OutOfBounds() {
        JTabbedPane tabbedPane = new JTabbedPane();
        Component component = tabbedPane.findComponentAt(-1, -1);
        assertNull(component);
    }

    @Test
    public void testSetSelectedIndex_NegativeIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.setSelectedIndex(-1);
        assertEquals(-1, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedIndex_SameIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.setSelectedIndex(0);
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent_NullPane() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        assertNull(tabbedPane.getSelectedComponent());
        tabbedPane.setSelectedComponent(null);
        assertNull(tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt_NegativeIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.addTab("Tab 1", new JLabel("Label 1"));
        tabbedPane.addTab("Tab 2", new JLabel("Label 2"));
        tabbedPane.removeTabAt(-1);
        assertEquals(2, tabbedPane.getTabCount());
    }

    @Test
    public void testRemoveTabAt_OutOfBounds() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.addTab("Tab 1", new JLabel("Label 1"));
        tabbedPane.addTab("Tab 2", new JLabel("Label 2"));
        tabbedPane.removeTabAt(3);
        assertEquals(2, tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.setSelectedComponent(pane);
        File parent = new File("ParentFolder").getParentFile();
        assertEquals(parent, tabbedPane.attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testHandleSelectedFile_NullSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.handleSelectedFile();
        assertNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }
}