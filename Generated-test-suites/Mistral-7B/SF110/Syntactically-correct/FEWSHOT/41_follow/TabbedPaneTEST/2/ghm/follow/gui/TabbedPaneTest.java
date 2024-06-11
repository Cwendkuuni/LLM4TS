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
    public void testConstructor() {
        assertEquals(SwingConstants.TOP_ABOVE_TOP, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);
    }

    @Test
    public void testSetAttributes() {
        tabbedPane.setAttributes(attributes);
        assertEquals(attributes, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAt() {
        JPanel panel = new JPanel();
        tabbedPane.addTab("Test", panel);

        int x = 10, y = 10;

        Component component = tabbedPane.findComponentAt(x, y);
        assertNotNull(component);
        assertEquals(panel, component);

        x -= 5;
        y -= 5;

        component = tabbedPane.findComponentAt(x, y);
        assertNull(component);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", new JPanel());

        tabbedPane.setSelectedIndex(0);
        verify(fileFollowingPane).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", new JPanel());

        tabbedPane.setSelectedComponent(fileFollowingPane);
        verify(fileFollowingPane).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", new JPanel());

        tabbedPane.removeTabAt(0);
        verify(fileFollowingPane).handleSelectedFile();
        assertEquals(1, tabbedPane.getComponentCount());
    }

    @Test
    public void testHandleSelectedFile() {
        when(fileFollowingPane.getFollowedFile()).thenReturn(new File("test.txt"));
        when(fileFollowingPane.getFollowedFile().getParentFile()).thenReturn(new File("test_directory"));

        tabbedPane.handleSelectedFile();
        assertEquals("test_directory", attributes.getLastFileChooserDirectory().getAbsolutePath());
    }
}