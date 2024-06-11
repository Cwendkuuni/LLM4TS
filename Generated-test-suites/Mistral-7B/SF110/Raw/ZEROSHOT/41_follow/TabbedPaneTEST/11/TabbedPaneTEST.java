package ghm.follow.gui;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.*;
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
 public void testTabbedPaneConstructor() {
 assertEquals(SwingConstants.TOP, tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAt() {
 final JLabel label = new JLabel("Test Label");
 tabbedPane.add("Test Tab", label);
 tabbedPane.setBounds(10, 10, 200, 200);
 tabbedPane.setVisible(true);

 int x = 50;
 int y = 50;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(label, actualComponent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtOutsidePane() {
 int x = 250;
 int y = 250;
 tabbedPane.findComponentAt(x, y);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtNullTabbedPane() {
 final JLabel label = new JLabel("Test Label");
 Component actualComponent = tabbedPane.findComponentAt(0, 0);
 assertNull(actualComponent);
 tabbedPane = null;
 tabbedPane.findComponentAt(0, 0);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testSetSelectedIndex() {
 JLabel label1 = new JLabel("Tab 1 Label");
 JLabel label2 = new JLabel("Tab 2 Label");
 tabbedPane.add("Tab 1", label1);
 tabbedPane.add("Tab 2", label2);
 tabbedPane.setSelectedIndex(0);
 assertEquals(label1, tabbedPane.getComponentAt(0));
 tabbedPane.setSelectedIndex(1);
 assertEquals(label2, tabbedPane.getComponentAt(0));
 }

 @Test
 public void testSetSelectedComponent() {
 JLabel label1 = new JLabel("Tab 1 Label");
 JLabel label2 = new JLabel("Tab 2 Label");
 tabbedPane.add("Tab 1", label1);
 tabbedPane.add("Tab 2", label2);
 tabbedPane.setSelectedComponent(label1);
 assertEquals(label1, tabbedPane.getComponentAt(0));
 tabbedPane.setSelectedComponent(label2);
 assertEquals(label2, tabbedPane.getComponentAt(0));
 }

 @Test
 public void testRemoveTabAt() {
 JLabel label1 = new JLabel("Tab 1 Label");
 JLabel label2 = new JLabel("Tab 2 Label");
 tabbedPane.add("Tab 1", label1);
 tabbedPane.add("Tab 2", label2);
 tabbedPane.removeTabAt(0);
 assertNull(tabbedPane.getComponentAt(0));
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.add("Test Tab", pane);
 tabbedPane.setSelectedComponent(pane);
 File parent = new File("testFolder");
 File expectedFile = new File("testFolder");
 assertEquals(expectedFile, attributes.getLastFileChooserDirectory());
 }

}