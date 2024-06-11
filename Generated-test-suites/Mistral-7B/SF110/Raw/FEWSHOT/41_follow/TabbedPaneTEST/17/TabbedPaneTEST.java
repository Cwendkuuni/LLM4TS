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
        assertEquals(JTabbedPane.TOP, tabbedPane.getTabPlacement());
        verify(attributes).getTabPlacement();
        assertEquals(null, tabbedPane.attributes);
        tabbedPane.attributes = attributes;
        assertNotNull(tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAtNull() {
        TabbedPane tabbedPane = new TabbedPane(null);
        assertNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testFindComponentAtValidComponent() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.add("Test Tab", panel);
        tabbedPane.setBounds(0, 0, 100, 100);
        frame.add(tabbedPane);
        frame.setSize(150, 150);
        frame.setVisible(true);

        Component actual = tabbedPane.findComponentAt(10, 10);
        assertTrue(actual instanceof JPanel);
        assertEquals(panel, actual);
    }

    @Test
    public void testFindComponentAtInvalidX() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.add("Test Tab", panel);
        tabbedPane.setBounds(0, 0, 100, 100);
        frame.add(tabbedPane);
        frame.setSize(150, 150);
        frame.setVisible(true);

        Component actual = tabbedPane.findComponentAt(-1, 10);
        assertNull(actual);
    }

    @Test
    public void testFindComponentAtInvalidY() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.add("Test Tab", panel);
        tabbedPane.setBounds(0, 0, 100, 100);
        frame.add(tabbedPane);
        frame.setSize(150, 150);
        frame.setVisible(true);

        Component actual = tabbedPane.findComponentAt(10, -1);
        assertNull(actual);
    }

    @Test
    public void testFindComponentAtOutsideComponentBounds() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.add("Test Tab", panel);
        tabbedPane.setBounds(0, 0, 100, 100);
        frame.add(tabbedPane);
        frame.setSize(150, 150);
        frame.setVisible(true);

        Component actual = tabbedPane.findComponentAt(110, 110);
        assertNull(actual);
    }

    @Test
    public void testSetSelectedIndex() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        tabbedPane.add("Tab 1", new JPanel());
        tabbedPane.add("Tab 2", new JPanel());

        tabbedPane.setSelectedIndex(0);
        FileFollowingPane selectedPane1 = (FileFollowingPane) tabbedPane.getComponentAt(0);
        assertNotNull(selectedPane1);
        verify(attributes).setLastFileChooserDirectory(eq(selectedPane1.getFollowedFile().getParentFile()));

        tabbedPane.setSelectedIndex(1);
        FileFollowingPane selectedPane2 = (FileFollowingPane) tabbedPane.getComponentAt(1);
        assertNotNull(selectedPane2);
        verify(attributes).setLastFileChooserDirectory(eq(selectedPane2.getFollowedFile().getParentFile()));
    }

    @Test
    public void testSetSelectedComponentNull() {
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.setSelectedComponent(null);
        assertTrue(tabbedPane.getSelectedComponent() instanceof JTabbedPane);
    }

    @Test
    public void testSetSelectedComponentValidComponent() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        JPanel panel = new JPanel();
        tabbedPane.add("Tab 1", panel);

        FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.add("Tab 2", fileFollowingPane);
        tabbedPane.setSelectedComponent(fileFollowingPane);

        assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
        verify(attributes).setLastFileChooserDirectory(eq(fileFollowingPane.getFollowedFile().getParentFile()));
    }

    @Test
    public void testRemoveTabAtValidIndex() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        tabbedPane.add("Tab 1", new JPanel());
        tabbedPane.add("Tab 2", new JPanel());

        tabbedPane.removeTabAt(0);
        assertEquals(1, tabbedPane.getComponentCount());
        verify(attributes).setLastFileChooserDirectory(eq(tabbedPane.getComponentAt(0).getFollowedFile().getParentFile()));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveTabAtInvalidIndex() {
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.add("Tab 1", new JPanel());
        tabbedPane.removeTabAt(1);
    }
}