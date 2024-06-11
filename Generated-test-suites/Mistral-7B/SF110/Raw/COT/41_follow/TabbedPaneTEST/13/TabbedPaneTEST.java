package ghm.follow.gui.test;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Test
 public void testTabbedPaneConstructor() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt_VisibleComponent() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 JButton button = new JButton("Button");
 tabbedPane.add("Test Tab", button);
 tabbedPane.setSelectedIndex(0);
 int x = button.getX() + button.getWidth() / 2;
 int y = button.getY() + button.getHeight() / 2;
 Component component = tabbedPane.findComponentAt(x, y);
 assertEquals(button, component);
 }

 @Test
 public void testFindComponentAt_InvisibleComponent() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 JButton button = new JButton("Button");
 tabbedPane.add("Test Tab", button);
 tabbedPane.setSelectedIndex(1);
 int x = button.getX() + button.getWidth() / 2;
 int y = button.getY() + button.getHeight() / 2;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);
 }

 @Test
 public void testFindComponentAt_NonComponent() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 JButton button1 = new JButton("Button1");
 tabbedPane.add("Tab1", button1);
 JButton button2 = new JButton("Button2");
 tabbedPane.add("Tab2", button2);
 tabbedPane.setSelectedIndex(0);
 int x = button1.getX() + button1.getWidth() / 2;
 int y = button1.getY() + button1.getHeight() / 2;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIndex_NegativeIndex() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 tabbedPane.setSelectedIndex(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndex_OutOfBoundsIndex() {
 attributes = new FollowingAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 JButton button1 = new JButton("Button1");
 tabbedPane.add("Tab1", button1);
 JButton button2 = new JButton("Button2");
 tabbedPane.add("Tab2", button2);
 tabbedPane.setSelectedIndex(2);
 }

 @Test
 public void testSetSelectedComponent_NullComponent() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testSetSelectedComponent_NonTabbedPaneComponent() {
 attributes = new FollowingAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 JButton button = new JButton("Button");
 tabbedPane.add("Tab1", button);
 tabbedPane.setSelectedComponent(new JLabel("Label"));
 }

 @Test
 public void testRemoveTabAt_ValidIndex() {
 attributes = new FollowingAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 JButton button1 = new JButton("Button1");
 tabbedPane.add("Tab1", button1);
 JButton button2 = new JButton("Button2");
 tabbedPane.add("Tab2", button2);
 tabbedPane.removeTabAt(0);
 assertEquals(1, tabbedPane.getComponentCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAt_InvalidIndex() {
 attributes = new FollowingAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 JButton button1 = new JButton("Button1");
 tabbedPane.add("Tab1", button1);
 JButton button2 = new JButton("Button2");
 tabbedPane.add("Tab2", button2);
 tabbedPane.removeTabAt(2);
 }

}