package ghm.follow.gui.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = mock(FollowAppAttributes.class);
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstructor() {
 assertSame(attributes.getTabPlacement(), tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAtNullInput() {
 assertNull(tabbedPane.findComponentAt(-1, -1));
 }

 @Test
 public void testFindComponentAtValidComponent() {
 JPanel panel = new JPanel();
 tabbedPane.add(panel);
 assertSame(panel, tabbedPane.findComponentAt(0, 0));
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testFindComponentAtInvalidComponentIndex() {
 tabbedPane.findComponentAt(10, 0);
 }

 @Test
 public void testFindComponentAtComponentOutsideBounds() {
 JPanel panel = new JPanel();
 tabbedPane.add(panel);
 tabbedPane.setBounds(new Rectangle(0, 0, 100, 100));
 tabbedPane.findComponentAt(101, 0);
 }

 @Test
 public void testSetSelectedIndex() {
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 tabbedPane.add(panel1, "Tab 1");
 tabbedPane.add(panel2, "Tab 2");
 tabbedPane.setSelectedIndex(0);
 assertSame(panel1, tabbedPane.getSelectedComponent());
 tabbedPane.setSelectedIndex(1);
 assertSame(panel2, tabbedPane.getSelectedComponent());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexInvalidIndex() {
 tabbedPane.setSelectedIndex(2);
 }

 @Test
 public void testSetSelectedComponent() {
 JPanel panel = new JPanel();
 tabbedPane.add(panel, "Tab 1");
 tabbedPane.setSelectedComponent(panel);
 assertSame(panel, tabbedPane.getSelectedComponent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedComponentNullComponent() {
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testRemoveTabAtValidIndex() {
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 tabbedPane.add(panel1, "Tab 1");
 tabbedPane.add(panel2, "Tab 2");
 tabbedPane.removeTabAt(0);
 assertNull(tabbedPane.getComponentAt(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtInvalidIndex() {
 tabbedPane.removeTabAt(10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveTabAtNullIndex() {
 tabbedPane.removeTabAt(-1);
 }
}