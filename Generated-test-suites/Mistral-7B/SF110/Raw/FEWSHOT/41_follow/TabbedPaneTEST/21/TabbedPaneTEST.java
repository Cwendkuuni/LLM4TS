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

    @Before
    public void setUp() {
        attributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testTabbedPaneConstruction() {
        assertEquals(JTabbedPane.TOP, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);
    }

    @Test
    public void testTabbedPaneConstructionWithAttributes() {
        tabbedPane = new TabbedPane(attributes);
        verify(attributes, times(2)).getTabPlacement();
        assertEquals(attributes, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAt() {
        JComponent component = mock(JComponent.class);
        Container container = mock(Container.class);
        tabbedPane.addTab("Tab1", component);
        tabbedPane.addTab("Tab2", container);

        int x = 10, y = 10;
        Component result = tabbedPane.findComponentAt(x, y);

        // Test when component is visible and within container bounds
        verify(component).isVisible();
        verify(container).findComponentAt(x - component.getX(), y - component.getY());
        assertEquals(component, result);

        // Test when component is not visible
        tabbedPane.setSelectedIndex(1);
        component.setState(Component.STATE_DISABLED);
        result = tabbedPane.findComponentAt(x, y);
        verify(component, never()).isVisible();
        assertNull(result);

        // Test when component is null
        tabbedPane.remove(component);
        result = tabbedPane.findComponentAt(x, y);
        assertNull(result);

        // Test when component is a container
        result = tabbedPane.findComponentAt(x, y);
        verify(container).findComponentAt(x - container.getX(), y - container.getY());
        assertEquals(container, result);

        // Test when no component is found
        tabbedPane.removeAll();
        result = tabbedPane.findComponentAt(x, y);
        assertNull(result);
    }

    @Test
    public void testSetSelectedIndex() {
        FileFollowingPane pane1 = mock(FileFollowingPane.class);
        FileFollowingPane pane2 = mock(FileFollowingPane.class);
        tabbedPane.addTab("Tab1", pane1);
        tabbedPane.addTab("Tab2", pane2);

        tabbedPane.setSelectedIndex(0);
        verify(pane1).handleSelectedFile();

        tabbedPane.setSelectedIndex(1);
        verify(pane2).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane1 = mock(FileFollowingPane.class);
        FileFollowingPane pane2 = mock(FileFollowingPane.class);
        tabbedPane.addTab("Tab1", pane1);
        tabbedPane.addTab("Tab2", pane2);

        tabbedPane.setSelectedComponent(pane1);
        verify(pane1).handleSelectedFile();

        tabbedPane.setSelectedComponent(pane2);
        verify(pane2).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAt() {
        FileFollowingPane pane1 = mock(FileFollowingPane.class);
        FileFollowingPane pane2 = mock(FileFollowingPane.class);
        tabbedPane.addTab("Tab1", pane1);
        tabbedPane.addTab("Tab2", pane2);

        tabbedPane.removeTabAt(0);
        verify(pane1, never()).handleSelectedFile();

        tabbedPane.removeTabAt(1);
        verify(pane2, never()).handleSelectedFile();

        tabbedPane.removeAll();
        verify(pane1, never()).handleSelectedFile();
        verify(pane2, never()).handleSelectedFile();
    }

    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane pane = mock(FileFollowingPane.class);
        File file = mock(File.class);

        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();

        assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
    }

}