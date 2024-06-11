package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
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
        assertEquals(SwingConstants.LEFT, tabbedPane.getTabPlacement());
        verify(attributes, times(1)).getTabPlacement();
    }

    @Test
    public void testFindComponentAtNullInput() {
        TabbedPane tabbedPane = new TabbedPane(null);
        assertNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testFindComponentAtValidInput() throws IOException {
        JFrame frame = new JFrame();
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.LEFT);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Tab1", panel1);
        tabbedPane.addTab("Tab2", panel2);
        frame.getContentPane().add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        int x = 10, y = 10;
        Component expectedResult = panel1; // replace with the expected component
        Component result = tabbedPane.findComponentAt(x, y);
        assertEquals(expectedResult, result);

        frame.dispose();
    }

    @Test
    public void testSetSelectedIndex() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane1 = mock(FileFollowingPane.class);
        FileFollowingPane pane2 = mock(FileFollowingPane.class);
        tabbedPane.addTab("Tab1", pane1);
        tabbedPane.addTab("Tab2", pane2);
        tabbedPane.setSelectedIndex(0);

        verify(pane1, times(1)).handleSelectedFile();
        verifyNoInteractions(pane2);
    }

    @Test
    public void testSetSelectedComponent() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane1 = mock(FileFollowingPane.class);
        FileFollowingPane pane2 = mock(FileFollowingPane.class);
        tabbedPane.addTab("Tab1", pane1);
        tabbedPane.addTab("Tab2", pane2);
        tabbedPane.setSelectedComponent(pane1);

        verify(pane1, times(1)).handleSelectedFile();
        verifyNoInteractions(pane2);
    }

    @Test
    public void testRemoveTabAt() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane1 = mock(FileFollowingPane.class);
        FileFollowingPane pane2 = mock(FileFollowingPane.class);
        tabbedPane.addTab("Tab1", pane1);
        tabbedPane.addTab("Tab2", pane2);
        tabbedPane.removeTabAt(0);

        verify(pane1, times(1)).handleSelectedFile();
        verifyNoInteractions(pane2);
        assertEquals(1, tabbedPane.getComponentCount());
    }

    @Test
    public void testHandleSelectedFile() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane = mock(FileFollowingPane.class);
        File file = new File("testfile.txt");
        when(pane.getFollowedFile()).thenReturn(file);

        tabbedPane.handleSelectedFile();
        assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
    }
}