package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import org.junit.Test;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Test
 public void testTabbedPaneConstructor() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 JPanel panel = new JPanel();
 frame.getContentPane().add(panel);
 frame.pack();
 frame.setVisible(true);
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 tabbedPane.add(panel, "Test Tab");
 int x = frame.getX() + frame.getWidth() / 2;
 int y = frame.getY() + frame.getHeight() / 2;
 Component component = tabbedPane.findComponentAt(x, y);
 assertTrue(component instanceof JPanel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtNegativeCoordinates() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 tabbedPane.add(new JLabel("Test Label"), "Test Tab");
 int x = -10;
 int y = -10;
 Component component = tabbedPane.findComponentAt(x, y);
 fail("Expected IllegalArgumentException but found " + component);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtOutsideTabbedPane() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 tabbedPane.add(new JLabel("Test Label"), "Test Tab");
 int x = Integer.MAX_VALUE;
 int y = Integer.MAX_VALUE;
 Component component = tabbedPane.findComponentAt(x, y);
 fail("Expected IllegalArgumentException but found " + component);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 JPanel panel1 = new JPanel();
 tabbedPane.add(panel1, "Tab 1");
 JPanel panel2 = new JPanel();
 tabbedPane.add(panel2, "Tab 2");
 tabbedPane.setSelectedIndex(0);
 assertEquals(panel1, tabbedPane.getSelectedComponent());
 tabbedPane.setSelectedIndex(1);
 assertEquals(panel2, tabbedPane.getSelectedComponent());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexNegativeIndex() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 JPanel panel = new JPanel();
 tabbedPane.add(panel, "Test Tab");
 tabbedPane.setSelectedIndex(-1);
 fail("Expected IndexOutOfBoundsException but found " + tabbedPane.getSelectedComponent());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexIndexGreaterThanComponentCount() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 JPanel panel1 = new JPanel();
 tabbedPane.add(panel1, "Tab 1");
 tabbedPane.setSelectedIndex(1);
 tabbedPane.add(new JLabel("Test Label"), "Tab 2");
 tabbedPane.setSelectedIndex(2);
 fail("Expected IndexOutOfBoundsException but found " + tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 JPanel panel1 = new JPanel();
 tabbedPane.add(panel1, "Tab 1");
 JPanel panel2 = new JPanel();
 tabbedPane.add(panel2, "Tab 2");
 tabbedPane.setSelectedComponent(panel1);
 assertEquals(panel1, tabbedPane.getSelectedComponent());
 tabbedPane.setSelectedComponent(panel2);
 assertEquals(panel2, tabbedPane.getSelectedComponent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedComponentNullComponent() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 tabbedPane.setSelectedComponent(null);
 fail("Expected IllegalArgumentException but found " + tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 JPanel panel1 = new JPanel();
 tabbedPane.add(panel1, "Tab 1");
 JPanel panel2 = new JPanel();
 tabbedPane.add(panel2, "Tab 2");
 tabbedPane.removeTabAt(0);
 assertEquals(panel2, tabbedPane.getComponentAt(0));
 tabbedPane.removeTabAt(1);
 assertNull(tabbedPane.getComponentAt(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtNegativeIndex() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 JPanel panel = new JPanel();
 tabbedPane.add(panel, "Test Tab");
 tabbedPane.removeTabAt(-1);
 fail("Expected IndexOutOfBoundsException but found " + tabbedPane.getComponentCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtIndexGreaterThanComponentCount() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 JPanel panel1 = new JPanel();
 tabbedPane.add(panel1, "Tab 1");
 tabbedPane.removeTabAt(1);
 tabbedPane.add(new JLabel("Test Label"), "Tab 2");
 tabbedPane.removeTabAt(2);
 fail("Expected IndexOutOfBoundsException but found " + tabbedPane.getComponentCount());
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane1 = new FileFollowingPane(new File("Test File 1.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("Test File 2.txt"));
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 tabbedPane.add(pane1, "Tab 1");
 tabbedPane.add(pane2, "Tab 2");
 tabbedPane.setSelectedComponent(pane1);
 assertEquals(pane1.getFollowedFile().getParentFile(), tabbedPane.getAttributes().getLastFileChooserDirectory());
 tabbedPane.setSelectedComponent(pane2);
 assertEquals(pane2.getFollowedFile().getParentFile(), tabbedPane.getAttributes().getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFileNullComponent() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 tabbedPane.handleSelectedFile();
 assertNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
 }
}