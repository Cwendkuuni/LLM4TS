package ghm.follow.gui;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;
    private FileFollowingPane fileFollowingPane;

    @Before
    public void setUp() {
        attributes = mock(FollowAppAttributes.class);
        fileFollowingPane = mock(FileFollowingPane.class);
        tabbedPane = new TabbedPane(attributes);
        tabbedPane.addTab("Test Tab", fileFollowingPane);
    }

    @Test
    public void testTabbedPaneConstruction() {
        assertNotNull(tabbedPane);
        assertSame(attributes.getTabPlacement(), tabbedPane.getTabPlacement());
    }

    @Test
    public void testFindComponentAt() {
        int x = 10, y = 10;
        Component component = tabbedPane.findComponentAt(x, y);
        assertSame(fileFollowingPane, component);
    }

    @Test
    public void testSetSelectedIndex() {
        int index = 0;
        tabbedPane.setSelectedIndex(index);
        assertSame(fileFollowingPane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.setSelectedComponent(fileFollowingPane);
        assertSame(fileFollowingPane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        int index = 0;
        tabbedPane.removeTabAt(index);
        assertFalse(tabbedPane.contains(fileFollowingPane));
    }

    @Test
    public void testHandleSelectedFile() {
        when(fileFollowingPane.getFollowedFile()).thenReturn(new File("testFile"));
        tabbedPane.setSelectedComponent(fileFollowingPane);
        tabbedPane.handleSelectedFile();
        File parent = new File("testDirectory");
        when(attributes.getLastFileChooserDirectory()).thenReturn(parent);
        assertEquals(parent, attributes.getLastFileChooserDirectory());
    }
}