package ghm.follow.gui;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

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
 tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 int x = 10, y = 20;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);
 tabbedPane.add("Test Tab", new JLabel("Test Label"));
 component = tabbedPane.findComponentAt(x, y);
 assertNotNull(component);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIndexNegativeNumber() {
 tabbedPane.setSelectedIndex(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexOutOfBounds() {
 tabbedPane.add("Tab 1", new JLabel("Label 1"));
 tabbedPane.add("Tab 2", new JLabel("Label 2"));
 tabbedPane.setSelectedIndex(2);
 }

 @Test
 public void testSetSelectedComponentNull() {
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("TestFile.txt"));
 tabbedPane.add("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.add("Tab 1", new JLabel("Label 1"));
 tabbedPane.add("Tab 2", new JLabel("Label 2"));
 tabbedPane.removeTabAt(0);
 assertFalse(tabbedPane.containsComponent("Tab 1"));
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("TestFile.txt"));
 tabbedPane.add("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane.getFollowedFile().getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
 }

 @Test
 @SuppressWarnings("deprecation")
 public void testSetSelectedIndexWithComponent() {
 Component component = new JLabel("Test Label");
 tabbedPane.add("Test Tab", component);
 tabbedPane.setSelectedComponent(component);
 tabbedPane.setSelectedIndex(0);
 assertFalse(tabbedPane.isSelectedComponent(component));
 }

 @Test
 public void testSetSelectedIndexWithNullComponent() {
 tabbedPane.setSelectedIndex(0);
 assertFalse(tabbedPane.isSelectedComponent(null));
 }

 @Test
 public void testSetSelectedIndexWithNonVisibleComponent() {
 tabbedPane.add("Tab 1", new JLabel("Label 1"));
 tabbedPane.add("Tab 2", new JLabel("Label 2"));
 tabbedPane.setSelectedIndex(0);
 tabbedPane.setSelectedComponentAt(1, new JLabel("Label 3"));
 assertFalse(tabbedPane.isSelectedComponent(tabbedPane.getComponentAt(0)));
 }

 @Test
 public void testSetSelectedIndexWithNullIndex() {
 tabbedPane.setSelectedIndex(-1);
 }

 @Test
 public void testSetSelectedIndexWithGreaterThanNumberOfTabs() {
 tabbedPane.add("Tab 1", new JLabel("Label 1"));
 tabbedPane.setSelectedIndex(1);
 }

 @Test
 public void testRemoveTabAtWithNegativeIndex() {
 tabbedPane.add("Tab 1", new JLabel("Label 1"));
 tabbedPane.add("Tab 2", new JLabel("Label 2"));
 tabbedPane.removeTabAt(-1);
 }

 @Test
 public void testRemoveTabAtWithGreaterThanNumberOfTabs() {
 tabbedPane.add("Tab 1", new JLabel("Label 1"));
 tabbedPane.add("Tab 2", new JLabel("Label 2"));
 tabbedPane.add("Tab 3", new JLabel("Label 3"));
 tabbedPane.removeTabAt(3);
 }

 @Test
 public void testRemoveTabAtWithNullIndex() {
 tabbedPane.add("Tab 1", new JLabel("Label 1"));
 tabbedPane.removeTabAt(null);
 }

}