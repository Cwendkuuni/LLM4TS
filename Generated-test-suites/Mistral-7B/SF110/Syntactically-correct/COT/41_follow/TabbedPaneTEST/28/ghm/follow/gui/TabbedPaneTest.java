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
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);
  assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  JFrame frame = new JFrame();
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  JPanel tab1 = new JPanel();
  JButton button1 = new JButton("Tab 1");
  tab1.add(button1);
  tabbedPane.addTab("Tab 1", tab1);

  JPanel tab2 = new JPanel();
  JButton button2 = new JButton("Tab 2");
  tab2.add(button2);
  tabbedPane.addTab("Tab 2", tab2);

  frame.getContentPane().add(tabbedPane);
  frame.pack();
  frame.setSize(300, 200);
  frame.setVisible(true);

  int x = 10;
  int y = 10;

  Component expectedComponent = tab1.getComponentAt(0, 0);
  Component actualComponent = tabbedPane.findComponentAt(x, y);

  assertEquals(expectedComponent, actualComponent);

  frame.dispose();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIndexWithNegativeIndex() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  tabbedPane.setSelectedIndex(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexWithIndexGreaterThanNumberOfTabs() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  JFrame frame = new JFrame();
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  JPanel tab1 = new JPanel();
  JButton button1 = new JButton("Tab 1");
  tab1.add(button1);
  tabbedPane.addTab("Tab 1", tab1);

  tabbedPane.setSelectedIndex(0);

  frame.getContentPane().add(tabbedPane);
  frame.pack();
  frame.setSize(300, 200);
  frame.setVisible(true);

  tabbedPane.setSelectedIndex(2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedComponentWithNullComponent() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testSetSelectedComponent() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  JFrame frame = new JFrame();
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  JPanel tab1 = new JPanel();
  JButton button1 = new JButton("Tab 1");
  tab1.add(button1);
  tabbedPane.addTab("Tab 1", tab1);

  JPanel tab2 = new JPanel();
  JButton button2 = new JButton("Tab 2");
  tab2.add(button2);
  tabbedPane.addTab("Tab 2", tab2);

  tabbedPane.setSelectedIndex(0);

  FileFollowingPane fileFollowingPane1 = (FileFollowingPane)tabbedPane.getComponentAt(0);
  tabbedPane.setSelectedComponent(fileFollowingPane1);

  assertEquals(fileFollowingPane1, tabbedPane.getSelectedComponent());

  frame.dispose();
 }

 @Test
 public void testRemoveTabAt() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  JFrame frame = new JFrame();
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  JPanel tab1 = new JPanel();
  JButton button1 = new JButton("Tab 1");
  tab1.add(button1);
  tabbedPane.addTab("Tab 1", tab1);

  JPanel tab2 = new JPanel();
  JButton button2 = new JButton("Tab 2");
  tab2.add(button2);
  tabbedPane.addTab("Tab 2", tab2);

  tabbedPane.setSelectedIndex(0);

  tabbedPane.removeTabAt(0);

  assertFalse(tabbedPane.containsComponent(tab1));
  assertFalse(tabbedPane.getTitleAt(0).equals("Tab 1"));

  tabbedPane.setSelectedIndex(1);

  frame.dispose();
 }

 @Test
 public void testHandleSelectedFile() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  JFrame frame = new JFrame();
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  FileFollowingPane fileFollowingPane1 = new FileFollowingPane(new File("test1.txt"));
  tabbedPane.addTab("Tab 1", fileFollowingPane1);

  FileFollowingPane fileFollowingPane2 = new FileFollowingPane(new File("test2.txt"));
  tabbedPane.addTab("Tab 2", fileFollowingPane2);

  tabbedPane.setSelectedIndex(0);

  assertEquals(new File("test1.txt").getParentFile(), attributes.getLastFileChooserDirectory());

  tabbedPane.setSelectedIndex(1);

  assertEquals(new File("test2.txt").getParentFile(), attributes.getLastFileChooserDirectory());

  frame.dispose();
 }
}