package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

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
    public void testTabbedPaneWithNullAttributes() {
        assertEquals(SwingConstants.TOP_ABOVE_COMPONENTS, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);
    }

    @Test
    public void testTabbedPaneWithValidAttributes() {
        when(attributes.getTabPlacement()).thenReturn(SwingConstants.LEFT);
        tabbedPane = new TabbedPane(attributes);
        assertEquals(SwingConstants.LEFT, tabbedPane.getTabPlacement());
        assertNotNull(tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAtReturnsComponent() {
        JButton component = new JButton("Component");
        tabbedPane.addTab("Test Tab", component);
        Point point = new Point(10, 10);
        Component foundComponent = tabbedPane.findComponentAt(point.x, point.y);
        assertEquals(component, foundComponent);
    }

    @Test
    public void testFindComponentAtReturnsNull() {
        JButton component = new JButton("Component");
        tabbedPane.addTab("Test Tab", component);
        Point point = new Point(-10, -10);
        assertNull(tabbedPane.findComponentAt(point.x, point.y));
    }

    @Test
    public void testSetSelectedIndex() {
        JButton component1 = new JButton("Component 1");
        tabbedPane.addTab("Test Tab 1", component1);

        JButton component2 = new JButton("Component 2");
        tabbedPane.addTab("Test Tab 2", component2);

        tabbedPane.setSelectedIndex(1);
        verify(attributes).setLastFileChooserDirectory(component2.getParent());
    }

    @Test
    public void testSetSelectedComponent() {
        JButton component1 = new JButton("Component 1");
        tabbedPane.addTab("Test Tab 1", component1);

        JButton component2 = new JButton("Component 2");
        tabbedPane.addTab("Test Tab 2", component2);

        tabbedPane.setSelectedComponent(fileFollowingPane);
        verify(attributes).setLastFileChooserDirectory(fileFollowingPane.getFollowedFile().getParentFile());
    }

    @Test
    public void testRemoveTabAt() {
        JButton component1 = new JButton("Component 1");
        tabbedPane.addTab("Test Tab 1", component1);

        JButton component2 = new JButton("Component 2");
        tabbedPane.addTab("Test Tab 2", component2);

        tabbedPane.removeTabAt(1);
        verify(attributes, times(2)).setLastFileChooserDirectory(eq(null));
    }

    @Test
    public void testHandleSelectedFileWithValidFileFollowingPane() {
        File followedFile = new File("path/to/file.txt");
        FileFollowingPane fileFollowingPane = new FileFollowingPane(followedFile);
        tabbedPane.addTab("Test Tab", fileFollowingPane);
        tabbedPane.setSelectedComponent(fileFollowingPane);
        assertEquals(followedFile.getParentFile(), attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testHandleSelectedFileWithNullFileFollowingPane() {
        tabbedPane.setSelectedComponent(null);
        assertEquals(null, attributes.getLastFileChooserDirectory());
    }
}