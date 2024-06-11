package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    private FollowAppAttributes attributesMock;
    private TabbedPane tabbedPane;
    private FileFollowingPane fileFollowingPaneMock;

    @Before
    public void setUp() {
        attributesMock = mock(FollowAppAttributes.class);
        fileFollowingPaneMock = mock(FileFollowingPane.class);
        tabbedPane = new TabbedPane(attributesMock);
    }

    @Test
    public void testTabbedPaneConstruction() {
        assertSame(JTabbedPane.TOP_ALIGNMENT, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);
    }

    @Test
    public void testSetTabbedPaneAttributes() {
        tabbedPane.setAttributes(attributesMock);
        assertEquals(attributesMock, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAtNullArguments() {
        assertNull(tabbedPane.findComponentAt(-1, -1));
    }

    @Test
    public void testFindComponentAtVisibleComponent() {
        final Component visibleComponent = mock(Component.class);
        tabbedPane.add(visibleComponent, "Test Tab");
        tabbedPane.setSelectedIndex(0);
        final Point point = new Point(10, 20);
        final Component foundComponent = tabbedPane.findComponentAt(point.x, point.y);
        assertEquals(visibleComponent, foundComponent);
    }

    @Test
    public void testFindComponentAtNonVisibleComponent() {
        final Component nonVisibleComponent = mock(Component.class);
        tabbedPane.add(nonVisibleComponent, "Test Tab");
        tabbedPane.setSelectedIndex(1);
        final Point point = new Point(10, 20);
        final Component foundComponent = tabbedPane.findComponentAt(point.x, point.y);
        assertNull(foundComponent);
    }

    @Test
    public void testFindComponentAtNullComponent() {
        final Component nullComponent = null;
        tabbedPane.add(nullComponent, "Test Tab");
        final Point point = new Point(10, 20);
        final Component foundComponent = tabbedPane.findComponentAt(point.x, point.y);
        assertNull(foundComponent);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.add(fileFollowingPaneMock, "Test Tab");
        tabbedPane.setSelectedIndex(0);
        verify(fileFollowingPaneMock).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.add(fileFollowingPaneMock, "Test Tab");
        tabbedPane.setSelectedComponent(fileFollowingPaneMock);
        verify(fileFollowingPaneMock).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.add(fileFollowingPaneMock, "Test Tab 1");
        tabbedPane.add(fileFollowingPaneMock, "Test Tab 2");
        tabbedPane.removeTabAt(0);
        verify(attributesMock, times(1)).setLastFileChooserDirectory(any(File.class));
    }

    @Test
    public void testHandleSelectedFile() {
        final File file = new File("test.txt");
        final FileFollowingPane pane = new FileFollowingPane(file);
        tabbedPane.add(pane, "Test Tab");
        tabbedPane.setSelectedComponent(pane);
        final File parent = new File("test_parent/");
        assertEquals(parent, tabbedPane.attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testHandleSelectedFileNullSelectedComponent() {
        tabbedPane.setSelectedComponent(null);
        assertNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }
}