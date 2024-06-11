package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstruction() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 Point point = new Point(0, 0);
 Component component = tabbedPane.findComponentAt(point.x, point.y);
 assertNull(component);

 JLabel label = new JLabel("Test Label");
 tabbedPane.addTab("Test Tab", label);
 point = new Point(10, 10);
 component = tabbedPane.findComponentAt(point.x, point.y);
 assertNotNull(component);
 assertEquals(label, component);
 }

 @Test
 public void testSetSelectedIndex() {
 JLabel label1 = new JLabel("Test Label 1");
 JLabel label2 = new JLabel("Test Label 2");
 tabbedPane.addTab("Test Tab 1", label1);
 tabbedPane.addTab("Test Tab 2", label2);
 tabbedPane.setSelectedIndex(0);
 assertEquals("Test Tab 1", tabbedPane.getTitleAt(0));

 tabbedPane.setSelectedIndex(1);
 assertEquals("Test Tab 2", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testSetSelectedComponent() {
 JLabel label1 = new JLabel("Test Label 1");
 JLabel label2 = new JLabel("Test Label 2");
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 tabbedPane.addTab("Test Tab 1", label1);
 tabbedPane.addTab("Test Tab 2", label2);
 tabbedPane.addTab("Test Tab 3", panel1);
 tabbedPane.setSelectedComponent(panel1);
 assertEquals(panel1, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 JLabel label1 = new JLabel("Test Label 1");
 JLabel label2 = new JLabel("Test Label 2");
 tabbedPane.addTab("Test Tab 1", label1);
 tabbedPane.addTab("Test Tab 2", label2);
 tabbedPane.removeTabAt(0);
 assertFalse(tabbedPane.containsComponent(label1));
 }

 @Test
 public void testHandleSelectedFile() {
 File file = new File("test.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(file);
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 File expectedParent = new File(".");
 assertNotNull(attributes.getLastFileChooserDirectory());
 assertEquals(expectedParent, attributes.getLastFileChooserDirectory());
 }
}