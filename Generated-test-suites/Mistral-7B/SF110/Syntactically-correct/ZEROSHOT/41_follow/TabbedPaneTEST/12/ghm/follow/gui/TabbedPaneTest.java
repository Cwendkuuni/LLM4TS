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
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 int x = 10;
 int y = 20;
 Component component = new JLabel("Test Label");
 tabbedPane.addTab("Test Tab", component);
 tabbedPane.setSelectedIndex(0);
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(component, actualComponent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtOutOfBounds() {
 int x = 10;
 int y = 20;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertNull(actualComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 int index = 1;
 tabbedPane.setSelectedIndex(index);
 FileFollowingPane expectedPane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Test Tab", expectedPane);
 FileFollowingPane actualPane = (FileFollowingPane) tabbedPane.getComponentAt(index);
 assertEquals(expectedPane, actualPane);
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane pane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.setSelectedComponent(pane);
 FileFollowingPane expectedPane = (FileFollowingPane) tabbedPane.getComponentAt(0);
 assertEquals(expectedPane, pane);
 }

 @Test
 public void testRemoveTabAt() {
 int index = 1;
 FileFollowingPane pane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Test Tab 1", pane);
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));
 tabbedPane.removeTabAt(index);
 assertTrue(tabbedPane.getComponentCount() == 1);
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.setSelectedComponent(pane);
 File expectedParent = new File("testDirectory");
 attributes.setLastFileChooserDirectory(expectedParent);
 tabbedPane.handleSelectedFile();
 File actualParent = attributes.getLastFileChooserDirectory();
 assertEquals(expectedParent, actualParent);
 }
}