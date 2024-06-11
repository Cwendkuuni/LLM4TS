package ghm.follow.gui;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.*;
import org.junit.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testFindComponentAt_ValidCoordinates() {
 final JLabel label = new JLabel("Test Label");
 tabbedPane.add("Test Tab", new JPanel());
 tabbedPane.add("Test Tab 2", new JPanel());
 tabbedPane.add(1, label);
 tabbedPane.setSelectedIndex(1);
 Point point = new Point(label.getX(), label.getY());
 Component actual = tabbedPane.findComponentAt(point.x, point.y);
 assertEquals("Test Label", actual);
 }

 @Test
 public void testFindComponentAt_InvalidCoordinates() {
 Component actual = tabbedPane.findComponentAt(100, 100);
 assertNull(actual);
 }

 @Test
 public void testSetSelectedIndex() {
 FileFollowingPane firstPane = new FileFollowingPane(new File("test1.txt"));
 tabbedPane.add("Test Tab", firstPane);
 tabbedPane.setSelectedIndex(0);
 assertEquals("Test Tab", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane firstPane = new FileFollowingPane(new File("test1.txt"));
 tabbedPane.add("Test Tab", firstPane);
 tabbedPane.setSelectedComponent(firstPane);
 assertEquals("Test Tab", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testRemoveTabAt() {
 FileFollowingPane firstPane = new FileFollowingPane(new File("test1.txt"));
 tabbedPane.add("Test Tab", firstPane);
 tabbedPane.removeTabAt(0);
 assertNull(tabbedPane.getComponentAt(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIndex_NegativeIndex() {
 tabbedPane.setSelectedIndex(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndex_OutOfBoundsIndex() {
 tabbedPane.add("Test Tab 1", new JPanel());
 tabbedPane.add("Test Tab 2", new JPanel());
 tabbedPane.setSelectedIndex(3);
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane firstPane = new FileFollowingPane(new File("test1.txt"));
 tabbedPane.add("Test Tab", firstPane);
 tabbedPane.setSelectedComponent(firstPane);
 assertEquals(new File("test1.txt").getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_NullSelectedComponent() {
 tabbedPane.setSelectedComponent(null);
 assertNull(attributes.getLastFileChooserDirectory());
 }
}