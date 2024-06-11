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
        verify(attributes, times(1)).setLastFileChooserDirectory(null);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.setSelectedIndex(0);
        assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
        verify(fileFollowingPane, times(1)).getFollowedFile();
        assertNotNull(tabbedPane.attributes.getLastFileChooserDirectory());

        tabbedPane.setSelectedIndex(1);
        assertNull(tabbedPane.getSelectedComponent());
        verify(fileFollowingPane, times(1)).getFollowedFile();
        assertNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.setSelectedComponent(fileFollowingPane);
        assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
        verify(fileFollowingPane, times(1)).getFollowedFile();
        assertNotNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.removeTabAt(0);
        assertNull(tabbedPane.getComponentAt(0));
        verify(fileFollowingPane, times(1)).getFollowedFile();
        assertNotNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testFindComponentAt() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        int x = 10, y = 20;
        Component component = tabbedPane.findComponentAt(x, y);
        assertNull(component);

        tabbedPane.setBounds(0, 0, 100, 100);
        component = tabbedPane.findComponentAt(x, y);
        assertNull(component);

        component = fileFollowingPane;
        tabbedPane.setComponentAt(component, 0);
        component = tabbedPane.findComponentAt(x, y);
        assertEquals(fileFollowingPane, component);
    }
}