package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    @Test
    public void testTabbedPaneConstruction() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        assertEquals(attributes.getTabPlacement(), tabbedPane.getTabPlacement());
    }

    @Test
    public void testFindComponentAtWithNullComponent() {
        TabbedPane tabbedPane = new TabbedPane(null);
        assertNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testFindComponentAtWithValidCoordinates() throws IOException {
        FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
        when(fileFollowingPane.getFollowedFile()).thenReturn(new File("file.txt"));
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.add(fileFollowingPane, "Tab 1");
        Component component = tabbedPane.findComponentAt(10, 10);
        assertTrue(component instanceof FileFollowingPane);
        assertEquals("Tab 1", tabbedPane.getTitleAt(tabbedPane.indexOfComponent(component)));
    }

    @Test
    public void testFindComponentAtWithInvalidCoordinates() {
        TabbedPane tabbedPane = new TabbedPane(null);
        assertNull(tabbedPane.findComponentAt(10, 10));
    }

    @Test
    public void testSetSelectedIndex() {
        FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
        when(fileFollowingPane.getFollowedFile()).thenReturn(new File("file.txt"));
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.add(fileFollowingPane, "Tab 1");
        tabbedPane.setSelectedIndex(0);
        verify(fileFollowingPane).setSelectedFile("file.txt");
    }

    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
        when(fileFollowingPane.getFollowedFile()).thenReturn(new File("file.txt"));
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.add(fileFollowingPane, "Tab 1");
        tabbedPane.setSelectedComponent(fileFollowingPane);
        verify(fileFollowingPane).setSelectedFile("file.txt");
    }

    @Test
    public void testRemoveTabAt() throws IOException {
        FileFollowingPane fileFollowingPane1 = mock(FileFollowingPane.class);
        when(fileFollowingPane1.getFollowedFile()).thenReturn(new File("file1.txt"));
        FileFollowingPane fileFollowingPane2 = mock(FileFollowingPane.class);
        when(fileFollowingPane2.getFollowedFile()).thenReturn(new File("file2.txt"));
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.add(fileFollowingPane1, "Tab 1");
        tabbedPane.add(fileFollowingPane2, "Tab 2");
        tabbedPane.removeTabAt(0);
        verifyZeroInteractions(fileFollowingPane1);
        assertEquals("Tab 2", tabbedPane.getTitleAt(0));
    }

    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
        when(fileFollowingPane.getFollowedFile()).thenReturn(new File("file.txt"));
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.add(fileFollowingPane, "Tab");
        tabbedPane.handleSelectedFile();
        assertEquals(new File("file.txt").getParentFile(), tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
}