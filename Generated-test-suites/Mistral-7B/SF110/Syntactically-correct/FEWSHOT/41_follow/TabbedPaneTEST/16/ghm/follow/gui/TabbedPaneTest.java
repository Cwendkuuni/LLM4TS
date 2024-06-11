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
    public void testTabbedPaneConstructor() {
        assertEquals(SwingConstants.LEFT, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);

        // Set up attributes and check if tabbedPane is initialized with it
        when(attributes.getTabPlacement()).thenReturn(SwingConstants.RIGHT);
        tabbedPane = new TabbedPane(attributes);
        assertEquals(SwingConstants.RIGHT, tabbedPane.getTabPlacement());
        verify(attributes).setTabPlacement(SwingConstants.RIGHT);
    }

    @Test
    public void testFindComponentAt() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);
        JComponent container = mock(JComponent.class);

        // Test null component
        assertNull(tabbedPane.findComponentAt(0, 0));

        // Test component not contained
        tabbedPane.add(component1, "Tab 1");
        assertNull(tabbedPane.findComponentAt(0, 0));

        tabbedPane.add(container, "Tab 2");
        tabbedPane.add(component2, "Tab 3");

        // Test component contained with no parents
        assertEquals(component1, tabbedPane.findComponentAt(0, 0));

        // Test component contained with a parent
        container.add(component2);
        assertEquals(component2, tabbedPane.findComponentAt(0, 0));

        // Test this component
        tabbedPane.addTab("", tabbedPane);
        assertEquals(tabbedPane, tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testSetSelectedIndex() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);

        tabbedPane.add(component1, "Tab 1");
        tabbedPane.add(component2, "Tab 2");

        tabbedPane.setSelectedIndex(0);
        verify(component1).requestFocus();

        tabbedPane.setSelectedIndex(1);
        verify(component2).requestFocus();
    }

    @Test
    public void testSetSelectedComponent() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);

        tabbedPane.add(component1, "Tab 1");
        tabbedPane.add(component2, "Tab 2");

        tabbedPane.setSelectedComponent(component1);
        verify(component1).requestFocus();

        tabbedPane.setSelectedComponent(component2);
        verify(component2).requestFocus();
    }

    @Test
    public void testRemoveTabAt() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);

        tabbedPane.add(component1, "Tab 1");
        tabbedPane.add(component2, "Tab 2");

        tabbedPane.removeTabAt(0);
        verifyZeroInteractions(component1);
        verify(component2).requestFocus();

        tabbedPane.removeTabAt(1);
        verifyZeroInteractions(component1);
        verifyZeroInteractions(component2);
    }

    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
        File followedFile = new File("path/to/file");
        when(fileFollowingPane.getFollowedFile()).thenReturn(followedFile);

        tabbedPane.handleSelectedFile();
        assertEquals(followedFile.getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
    }
}