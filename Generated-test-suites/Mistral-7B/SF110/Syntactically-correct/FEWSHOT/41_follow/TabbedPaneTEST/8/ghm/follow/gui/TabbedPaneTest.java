package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private JTabbedPane tabbedPane;
    private FileFollowingPane fileFollowingPane;

    @Before
    public void setup() {
        attributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributes);
        fileFollowingPane = mock(FileFollowingPane.class);
    }

    @Test
    public void testTabbedPaneConstructor() {
        assertEquals(SwingConstants.TOP, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);
    }

    @Test
    public void testSetAttributes() {
        tabbedPane.setAttributes(attributes);
        assertEquals(attributes, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAt() {
        JComponent component = mock(JComponent.class);
        when(component.isVisible()).thenReturn(true);
        when(component.getX()).thenReturn(10);
        when(component.getY()).thenReturn(20);
        when(tabbedPane.getComponentAt(0)).thenReturn(component);

        JComponent result = tabbedPane.findComponentAt(11, 21);
        assertEquals(component, result);

        assertNull(tabbedPane.findComponentAt(11, 19));
        assertNull(tabbedPane.findComponentAt(-1, 21));
        assertNull(tabbedPane.findComponentAt(11, -1));
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", fileFollowingPane);

        tabbedPane.setSelectedIndex(0);

        verify(fileFollowingPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", fileFollowingPane);

        tabbedPane.setSelectedComponent(fileFollowingPane);

        verify(fileFollowingPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", fileFollowingPane);

        tabbedPane.removeTabAt(0);

        verify(fileFollowingPane, times(1)).handleSelectedFile();
        assertEquals(1, tabbedPane.getComponentCount());
    }

    @Test
    public void testHandleSelectedFile() {
        File followedFile = new File("testFile.txt");
        when(fileFollowingPane.getFollowedFile()).thenReturn(followedFile);
        File parentFile = new File("testDirectory");
        when(followedFile.getParentFile()).thenReturn(parentFile);

        tabbedPane.handleSelectedFile();

        verify(attributes, times(1)).setLastFileChooserDirectory(parentFile);
    }
}