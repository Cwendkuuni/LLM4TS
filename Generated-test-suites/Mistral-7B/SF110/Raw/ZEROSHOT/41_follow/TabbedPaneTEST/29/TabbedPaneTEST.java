package ghm.follow.gui;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;

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
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 JFrame frame = new JFrame();
 JLabel label = new JLabel();
 frame.add(label);
 tabbedPane.addTab("Test", new JScrollPane(frame));
 tabbedPane.setSelectedIndex(0);

 int x = label.getX() + label.getWidth() / 2;
 int y = label.getY() + label.getHeight() / 2;

 Component component = tabbedPane.findComponentAt(x, y);
 assertNotNull(component);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindComponentAtOutOfBounds() {
 tabbedPane.findComponentAt(-1, -1);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane.addTab("Test1", new JLabel("Test1"));
 tabbedPane.addTab("Test2", new JLabel("Test2"));
 tabbedPane.setSelectedIndex(0);

 assertEquals("Test1", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testSetSelectedComponent() {
 JLabel label1 = new JLabel("Test1");
 JLabel label2 = new JLabel("Test2");
 tabbedPane.addTab("Test1", new JScrollPane(label1));
 tabbedPane.addTab("Test2", new JScrollPane(label2));
 tabbedPane.setSelectedComponent(label1);

 assertEquals("Test1", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.addTab("Test1", new JLabel("Test1"));
 tabbedPane.addTab("Test2", new JLabel("Test2"));
 tabbedPane.removeTabAt(0);

 assertEquals("Test2", tabbedPane.getTitleAt(0));
 assertFalse(tabbedPane.getComponentAt(0) instanceof JLabel);
 }

 @Test
 public void testHandleSelectedFile() {
 File followFile = new File("test.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(followFile);
 tabbedPane.addTab("Test", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);

 assertSame(followFile.getParentFile(), attributes.getLastFileChooserDirectory());
 }

}