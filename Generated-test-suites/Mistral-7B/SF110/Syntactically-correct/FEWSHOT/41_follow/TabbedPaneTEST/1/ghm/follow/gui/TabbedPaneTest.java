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

        tabbedPane = new TabbedPane(attributes);
        verify(attributes, times(1)).getTabPlacement();
        assertEquals(attributes, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAt() {
        JComponent component = mock(JComponent.class);
        container(component).given().findComponentAt(0, 0).willReturn(component);

        tabbedPane.add(component, "test");
        Component result = tabbedPane.findComponentAt(0, 0);
        assertEquals(component, result);
    }

    @Test
    public void testFindComponentAt_NoComponentFound() {
        tabbedPane.add(fileFollowingPane, "test");

        Component result = tabbedPane.findComponentAt(1, 1);
        assertNull(result);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.add(fileFollowingPane, "test");

        tabbedPane.setSelectedIndex(0);
        verify(fileFollowingPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.add(fileFollowingPane, "test");

        tabbedPane.setSelectedComponent(fileFollowingPane);
        verify(fileFollowingPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.add(fileFollowingPane, "test");

        tabbedPane.removeTabAt(0);
        verify(fileFollowingPane, never()).handleSelectedFile();
    }

    @Test
    public void testHandleSelectedFile() {
        File file = new File("test");
        when(fileFollowingPane.getFollowedFile()).thenReturn(file);
        File parent = new File("parent");
        when(file.getParentFile()).thenReturn(parent);

        tabbedPane.setSelectedComponent(fileFollowingPane);
        tabbedPane.handleSelectedFile();

        verify(attributes, times(1)).setLastFileChooserDirectory(parent);
    }

    private Container container(JComponent component) {
        Container container = mock(Container.class);
        when(component.getParent()).thenReturn(container);
        return container;
    }
}