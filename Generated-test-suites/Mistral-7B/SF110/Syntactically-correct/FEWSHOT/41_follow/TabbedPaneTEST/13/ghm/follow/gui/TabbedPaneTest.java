package ghm.follow.gui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {
    @Test
    public void testTabbedPaneConstruction() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        assertNotNull(tabbedPane);
        assertNull(tabbedPane.attributes);

        tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.attributes = new FollowAppAttributes();
        assertNotNull(tabbedPane);
        assertNotNull(tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAt() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);

        JLabel label = new JLabel("Label");
        tabbedPane.addTab("Tab", label);

        Component component = tabbedPane.findComponentAt(0, 0);
        assertNotNull(component);
        assertEquals(label, component);

        component = tabbedPane.findComponentAt(10, 10);
        assertNull(component);
    }

    @Test
    public void testSetSelectedIndex() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);

        JLabel label1 = new JLabel("Label 1");
        tabbedPane.addTab("Tab 1", label1);

        JLabel label2 = new JLabel("Label 2");
        tabbedPane.addTab("Tab 2", label2);

        tabbedPane.setSelectedIndex(0);

        assertEquals("Tab 1", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
        assertEquals(label1, tabbedPane.getComponentAt(tabbedPane.getSelectedIndex()));

        tabbedPane.setSelectedIndex(1);

        assertEquals("Tab 2", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
        assertEquals(label2, tabbedPane.getComponentAt(tabbedPane.getSelectedIndex()));
    }

    @Test
    public void testSetSelectedComponent() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);

        JLabel label1 = new JLabel("Label 1");
        FileFollowingPane fileFollowingPane1 = new FileFollowingPane(new File("file1.txt"));
        tabbedPane.addTab("Tab 1", label1, fileFollowingPane1);

        JLabel label2 = new JLabel("Label 2");
        FileFollowingPane fileFollowingPane2 = new FileFollowingPane(new File("file2.txt"));
        tabbedPane.addTab("Tab 2", label2, fileFollowingPane2);

        tabbedPane.setSelectedComponent(fileFollowingPane1);

        assertEquals("Tab 1", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
        assertEquals(label1, tabbedPane.getComponentAt(tabbedPane.getSelectedIndex()));
        assertEquals(fileFollowingPane1, tabbedPane.getSelectedComponent());

        tabbedPane.setSelectedComponent(fileFollowingPane2);

        assertEquals("Tab 2", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
        assertEquals(label2, tabbedPane.getComponentAt(tabbedPane.getSelectedIndex()));
        assertEquals(fileFollowingPane2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);

        JLabel label1 = new JLabel("Label 1");
        FileFollowingPane fileFollowingPane1 = new FileFollowingPane(new File("file1.txt"));
        tabbedPane.addTab("Tab 1", label1, fileFollowingPane1);

        JLabel label2 = new JLabel("Label 2");
        FileFollowingPane fileFollowingPane2 = new FileFollowingPane(new File("file2.txt"));
        tabbedPane.addTab("Tab 2", label2, fileFollowingPane2);

        tabbedPane.setSelectedIndex(1);

        tabbedPane.removeTabAt(0);

        assertEquals("Tab 2", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
        assertEquals(label2, tabbedPane.getComponentAt(tabbedPane.getSelectedIndex()));
        assertEquals(null, tabbedPane.getComponentAt(0));
    }

    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("file.txt"));
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.addTab("Tab", fileFollowingPane);
        tabbedPane.setSelectedComponent(fileFollowingPane);

        assertEquals(new File("file.txt").getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());

        File file = new File("newfile.txt");
        tabbedPane.setSelectedComponent(new FileFollowingPane(file));

        assertEquals(file.getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
    }
}