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
        assertEquals(SwingConstants.TOP_ABOVE_COMPONENTS, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);

        TabbedPane tabbedPaneWithAttributes = new TabbedPane(attributes);
        assertEquals(SwingConstants.TOP_ABOVE_COMPONENTS, tabbedPaneWithAttributes.getTabPlacement());
        assertNotNull(tabbedPaneWithAttributes.attributes);
        verify(attributes).getTabPlacement();
    }

    @Test
    public void testFindComponentAt() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);
        Container container = mock(Container.class);
        tabbedPane.addTab("Tab 1", component1);
        tabbedPane.addTab("Tab 2", container);

        int x = 10, y = 20;

        // Test finding a component within a tab
        when(container.findComponentAt(0, 0)).thenReturn(component2);
        Point componentPosition = tabbedPane.findComponentAt(x, y);
        assertEquals(component2, tabbedPane.getComponentAt(tabbedPane.indexOfTab("Tab 2")));
        assertEquals(x - component2.getX(), componentPosition.x);
        assertEquals(y - component2.getY(), componentPosition.y);

        // Test finding a component outside the tabbed pane
        Point componentPositionOutside = tabbedPane.findComponentAt(x, y);
        assertNull(componentPositionOutside);

        // Test finding a component within a component
        JComponent nestedComponent = mock(JComponent.class);
        when(component2.getComponentAt(0, 0)).thenReturn(nestedComponent);
        Point nestedComponentPosition = tabbedPane.findComponentAt(x, y);
        assertEquals(nestedComponent, tabbedPane.getComponentAt(tabbedPane.getComponentCount() - 1));
        assertEquals(x - nestedComponent.getX(), nestedComponentPosition.x);
        assertEquals(y - nestedComponent.getY(), nestedComponentPosition.y);
    }

    @Test
    public void testSetSelectedIndex() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);
        tabbedPane.addTab("Tab 1", component1);
        tabbedPane.addTab("Tab 2", component2);

        tabbedPane.setSelectedIndex(0);
        verify(component1, times(1)).requestFocus();

        tabbedPane.setSelectedIndex(1);
        verify(component2, times(1)).requestFocus();
    }

    @Test
    public void testSetSelectedComponent() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);
        tabbedPane.addTab("Tab 1", component1);
        tabbedPane.addTab("Tab 2", component2);

        tabbedPane.setSelectedComponent(component1);
        verify(component1, times(1)).requestFocus();

        tabbedPane.setSelectedComponent(component2);
        verify(component2, times(1)).requestFocus();
    }

    @Test
    public void testRemoveTabAt() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);
        tabbedPane.addTab("Tab 1", component1);
        tabbedPane.addTab("Tab 2", component2);

        int index = 0;
        tabbedPane.removeTabAt(index);

        verify(component1, times(1)).dispose();
        assertEquals(1, tabbedPane.getComponentCount());

        index = 1;
        tabbedPane.removeTabAt(index);
        verifyNoMoreInteractions(component2);
        assertEquals(0, tabbedPane.getComponentCount());
    }

    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane pane1 = mock(FileFollowingPane.class);
        FileFollowingPane pane2 = mock(FileFollowingPane.class);
        tabbedPane.addTab("Tab 1", pane1);
        tabbedPane.addTab("Tab 2", pane2);

        File file1 = mock(File.class);
        File file2 = mock(File.class);
        when(pane1.getFollowedFile()).thenReturn(file1);
        when(pane2.getFollowedFile()).thenReturn(file2);

        tabbedPane.setSelectedComponent(pane1);
        tabbedPane.handleSelectedFile();
        assertEquals(file1.getParentFile(), attributes.getLastFileChooserDirectory());

        tabbedPane.setSelectedComponent(pane2);
        tabbedPane.handleSelectedFile();
        assertEquals(file2.getParentFile(), attributes.getLastFileChooserDirectory());
    }
}