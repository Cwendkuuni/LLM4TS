package ghm.follow.gui;

import static org.junit.Assert.*;

import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;

import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {

 private JTabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testFindComponentAt() {
 int x = 10;
 int y = 20;
 Component component = new JLabel("Test Label");
 tabbedPane.addTab("Test Tab", component);
 tabbedPane.setSelectedComponent(component);

 assertTrue(tabbedPane.findComponentAt(x, y) instanceof JLabel);
 }

 @Test
 public void testFindComponentAt_OutOfBounds() {
 int x = 0;
 int y = 0;
 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAt_NoVisibleComponent() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));
 tabbedPane.setSelectedIndex(0);
 int x = 10;
 int y = 20;

 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testSetSelectedIndex() {
 FileFollowingPane expectedPane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.addTab("Test Tab", expectedPane);
 tabbedPane.setSelectedIndex(0);
 FileFollowingPane actualPane = (FileFollowingPane)tabbedPane.getComponentAt(0);
 assertEquals(expectedPane, actualPane);
 }

 @Test
 public void testSetSelectedIndex_OutOfBounds() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));
 tabbedPane.setSelectedIndex(2);
 assertTrue(tabbedPane.getComponentAt(0) instanceof JLabel);
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane expectedPane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.addTab("Test Tab", expectedPane);
 tabbedPane.setSelectedComponent(expectedPane);
 FileFollowingPane actualPane = (FileFollowingPane)tabbedPane.getComponentAt(0);
 assertEquals(expectedPane, actualPane);
 }

 @Test
 public void testSetSelectedComponent_Null() {
 tabbedPane.setSelectedComponent(null);
 assertTrue(tabbedPane.getComponentAt(0) instanceof JTabbedPane);
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));
 tabbedPane.removeTabAt(0);
 assertTrue(tabbedPane.getComponentCount() == 1);
 }

 @Test
 public void testRemoveTabAt_OutOfBounds() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));
 tabbedPane.removeTabAt(2);
 assertTrue(tabbedPane.getComponentCount() == 2);
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.setSelectedComponent(pane);
 File parent = new File("test_parent");
 assertEquals(parent, attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_NullSelectedComponent() {
 tabbedPane.setSelectedComponent(null);
 assertEquals(null, attributes.getLastFileChooserDirectory());
 }
}