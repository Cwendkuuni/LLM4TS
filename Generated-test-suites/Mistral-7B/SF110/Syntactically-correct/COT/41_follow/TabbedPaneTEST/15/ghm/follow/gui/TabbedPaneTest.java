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

  JButton button = new JButton("Test Button");
  tabbedPane.add("Test Tab", button);

  int x = 50;
  int y = 50;

  Component result = tabbedPane.findComponentAt(x, y);
  assertEquals("Unexpected component returned", button, result);
 }

 @Test
 public void testFindComponentAtOutsideTabbedPane() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  int x = 50;
  int y = 50;

  Component result = tabbedPane.findComponentAt(x, y);
  assertNull("Unexpected component returned", result);
 }

 @Test
 public void testFindComponentAtInvisibleTabComponent() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  JButton invisibleButton = new JButton("Invisible Button");
  invisibleButton.setVisible(false);
  tabbedPane.add("Invisible Tab", invisibleButton);

  int x = 50;
  int y = 50;

  Component result = tabbedPane.findComponentAt(x, y);
  assertNull("Unexpected component returned", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtNegativeX() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  int x = -50;
  int y = 50;

  tabbedPane.findComponentAt(x, y);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtNegativeY() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  int x = 50;
  int y = -50;

  tabbedPane.findComponentAt(x, y);
 }

 @Test
 public void testSetSelectedIndex() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  JButton button1 = new JButton("Test Button 1");
  tabbedPane.add("Test Tab 1", button1);

  JButton button2 = new JButton("Test Button 2");
  tabbedPane.add("Test Tab 2", button2);

  tabbedPane.setSelectedIndex(0);
  assertEquals("Unexpected selected index", 0, tabbedPane.getSelectedIndex());
 }

 @Test
 public void testSetSelectedIndexOutOfBounds() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  JButton button1 = new JButton("Test Button 1");
  tabbedPane.add("Test Tab 1", button1);

  JButton button2 = new JButton("Test Button 2");
  tabbedPane.add("Test Tab 2", button2);

  tabbedPane.setSelectedIndex(10);
  assertEquals("Unexpected selected index", -1, tabbedPane.getSelectedIndex());
 }

 @Test
 public void testSetSelectedComponent() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  JButton button1 = new JButton("Test Button 1");
  tabbedPane.add("Test Tab 1", button1);

  JButton button2 = new JButton("Test Button 2");
  tabbedPane.add("Test Tab 2", button2);

  tabbedPane.setSelectedComponent(button1);
  assertEquals("Unexpected selected index", 0, tabbedPane.getSelectedIndex());
 }

 @Test
 public void testRemoveTabAt() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  JButton button1 = new JButton("Test Button 1");
  tabbedPane.add("Test Tab 1", button1);

  JButton button2 = new JButton("Test Button 2");
  tabbedPane.add("Test Tab 2", button2);

  tabbedPane.removeTabAt(0);
  assertEquals("Unexpected number of tabs", 1, tabbedPane.getTabCount());
 }

 @Test
 public void testRemoveTabAtOutOfBounds() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  JButton button1 = new JButton("Test Button 1");
  tabbedPane.add("Test Tab 1", button1);

  JButton button2 = new JButton("Test Button 2");
  tabbedPane.add("Test Tab 2", button2);

  tabbedPane.removeTabAt(10);
  assertEquals("Unexpected number of tabs", 2, tabbedPane.getTabCount());
 }

 @Test
 public void testHandleSelectedFile() {
  attributes = new FollowAppAttributes();
  tabbedPane = new TabbedPane(attributes);

  File followingFile = new File("testFile.txt");
  JFileChooser fileChooser = new JFileChooser();
  fileChooser.setCurrentDirectory(followingFile.getParentFile());
  FileFollowingPane fileFollowingPane = new FileFollowingPane(fileChooser, followingFile);
  tabbedPane.add("Test Tab", fileFollowingPane);
  tabbedPane.setSelectedComponent(fileFollowingPane);

  assertEquals("Unexpected last file chooser directory", followingFile.getParentFile(), attributes.getLastFileChooserDirectory());
 }
}