package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;
    private FileFollowingPane fileFollowingPane;

    @Before
    public void setUp() {
        attributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributes);
        fileFollowingPane = mock(FileFollowingPane.class);
    }

    @Test
    public void testTabbedPaneConstructor() {
        assertEquals(SwingConstants.TOP_ABOVE_COMPONENTS, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);
    }

    @Test
    public void testSetAttributes() {
        tabbedPane.setAttributes(attributes);
        assertEquals(attributes, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAtNull() {
        assertNull(tabbedPane.findComponentAt(-1, -1));
    }

    @Test
    public void testFindComponentAtNoComponent() {
        assertNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testFindComponentAtComponentFound() {
        JButton button = new JButton();
        tabbedPane.add(button, "Test");
        int x = button.getX();
        int y = button.getY();
        Component component = tabbedPane.findComponentAt(x, y);
        assertEquals(button, component);
    }

    @Test
    public void testFindComponentAtContainerFound() {
        JPanel panel = new JPanel();
        JButton button = new JButton();
        tabbedPane.add(panel, "Test");
        panel.add(button);
        int x = button.getX();
        int y = button.getY();
        Component component = tabbedPane.findComponentAt(x, y);
        assertEquals(button, component);
    }

    @Test
    public void testSetSelectedIndex() {
        JPanel panel = new JPanel();
        JButton button = new JButton();
        tabbedPane.add(panel, "Test1");
        tabbedPane.add(button, "Test2");
        tabbedPane.setSelectedIndex(1);
        verify(attributes).setLastFileChooserDirectory(null);
        assertEquals(button, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testSetSelectedComponent() {
        JPanel panel = new JPanel();
        JButton button = new JButton();
        tabbedPane.add(panel, "Test1");
        tabbedPane.add(button, "Test2");
        tabbedPane.setSelectedComponent(button);
        verify(attributes).setLastFileChooserDirectory(null);
        assertEquals(button, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        JPanel panel = new JPanel();
        JButton button = new JButton();
        tabbedPane.add(panel, "Test1");
        tabbedPane.add(button, "Test2");
        tabbedPane.removeTabAt(0);
        verify(attributes).setLastFileChooserDirectory(null);
        assertEquals(button, tabbedPane.getComponentAt(0));
    }

    @Test
    public void testHandleSelectedFileNullSelectedComponent() {
        tabbedPane.handleSelectedFile();
        verify(attributes, never()).setLastFileChooserDirectory(any());
    }

    @Test
    public void testHandleSelectedFileValidSelectedComponent() {
        File file = new File("test.txt");
        FileFollowingPane fileFollowingPane = new FileFollowingPane(file, null);
        tabbedPane.add(fileFollowingPane, "Test");
        tabbedPane.handleSelectedFile();
        assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
    }
}