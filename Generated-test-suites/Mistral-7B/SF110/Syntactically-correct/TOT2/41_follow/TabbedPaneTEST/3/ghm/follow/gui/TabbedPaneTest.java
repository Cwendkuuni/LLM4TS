import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {
    @Test
    public void testFindComponentAt_OutsideComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        assertNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testFindComponentAt_InsideComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        tabbedPane.add("TestTab", pane);
        assertNotNull(tabbedPane.findComponentAt(pane.getX(), pane.getY()));
    }

    @Test
    public void testFindComponentAt_NegativeCoordinates() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        tabbedPane.add("TestTab", pane);
        assertNull(tabbedPane.findComponentAt(-1, -1));
    }

    @Test
    public void testFindComponentAt_OutsideTabbedPane() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        tabbedPane.add("TestTab", pane);
        assertNull(tabbedPane.findComponentAt(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void testFindComponentAt_ComponentNotVisible() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        tabbedPane.add("TestTab", pane);
        pane.setVisible(false);
        assertNull(tabbedPane.findComponentAt(pane.getX(), pane.getY()));
    }

    @Test
    public void testFindComponentAt_ContainerWithNonVisibleChild() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel container = new JPanel();
        tabbedPane.add("TestTab", container);
        JLabel label = new JLabel();
        container.add(label);
        label.setVisible(false);
        assertNull(tabbedPane.findComponentAt(label.getX(), label.getY()));
    }

    @Test
    public void testSetSelectedIndex_ValidIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        FileFollowingPane pane2 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        tabbedPane.add("Tab1", pane1);
        tabbedPane.add("Tab2", pane2);
        tabbedPane.setSelectedIndex(0);
        assertSame(pane1, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testSetSelectedIndex_OutOfBoundIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        FileFollowingPane pane2 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        tabbedPane.add("Tab1", pane1);
        tabbedPane.add("Tab2", pane2);
        tabbedPane.setSelectedIndex(2);
        assertNull(tabbedPane.getSelectedComponent());
    }

    @Test
    public void testSetSelectedComponent_ValidComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        FileFollowingPane pane2 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        tabbedPane.add("Tab1", pane1);
        tabbedPane.add("Tab2", pane2);
        tabbedPane.setSelectedComponent(pane1);
        assertSame(pane1, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testSetSelectedComponent_NullComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        FileFollowingPane pane2 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        tabbedPane.add("Tab1", pane1);
        tabbedPane.add("Tab2", pane2);
        tabbedPane.setSelectedComponent(null);
        assertNull(tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt_ValidIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        FileFollowingPane pane2 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        tabbedPane.add("Tab1", pane1);
        tabbedPane.add("Tab2", pane2);
        tabbedPane.removeTabAt(1);
        assertNull(tabbedPane.getComponentAt(1));
    }

    @Test
    public void testRemoveTabAt_OutOfBoundIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        FileFollowingPane pane2 = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        tabbedPane.add("Tab1", pane1);
        tabbedPane.add("Tab2", pane2);
        tabbedPane.removeTabAt(100);
        assertEquals(2, tabbedPane.getComponentCount());
    }

    @Test
    public void testHandleSelectedFile_SelectedComponentIsNotNull() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane(); // Assuming FileFollowingPane is a valid component
        tabbedPane.add("Tab", pane);
        tabbedPane.setSelectedComponent(pane);
        assertNotNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testHandleSelectedFile_SelectedComponentIsNull() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.handleSelectedFile();
        assertNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }
}