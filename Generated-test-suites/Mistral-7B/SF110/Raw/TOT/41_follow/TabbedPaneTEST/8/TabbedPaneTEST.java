package ghm.follow.gui;

import static org.junit.Assert.*;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.FileFollowingPane;

public class TabbedPaneTest {

 private JTabbedPane tabbedPane;
 private FileFollowingPane fileFollowingPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 fileFollowingPane = new FileFollowingPane(attributes);
 tabbedPane.add(fileFollowingPane, "File Following Pane");
 }

 @Test
 public void testFindComponentAt_ValidPosition() {
 int x = 10, y = 20;
 Component expectedComponent = fileFollowingPane;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(expectedComponent, actualComponent);
 }

 @Test
 public void testFindComponentAt_InvalidPosition() {
 int x = 10, y = 20;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertNull(actualComponent);
 }

 @Test
 public void testSetSelectedIndex_ValidIndex() {
 tabbedPane.setSelectedIndex(0);
 assertTrue(fileFollowingPane.isVisible());
 }

 @Test
 public void testSetSelectedIndex_InvalidIndex() {
 tabbedPane.setSelectedIndex(10);
 assertFalse(fileFollowingPane.isVisible());
 }

 @Test
 public void testSetSelectedComponent_ValidComponent() {
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertTrue(fileFollowingPane.isSelected());
 }

 @Test
 public void testSetSelectedComponent_InvalidComponent() {
 JFrame frame = new JFrame();
 Component invalidComponent = new JButton("Invalid Component");
 tabbedPane.setSelectedComponent(invalidComponent);
 assertFalse(fileFollowingPane.isSelected());
 }

 @Test
 public void testRemoveTabAt_ValidIndex() {
 Component tabToRemove = fileFollowingPane;
 int index = tabbedPane.indexOfComponent(tabToRemove);
 tabbedPane.removeTabAt(index);
 assertFalse(tabbedPane.contains(tabToRemove));
 }

 @Test
 public void testRemoveTabAt_InvalidIndex() {
 tabbedPane.removeTabAt(10);
 assertSame(fileFollowingPane, tabbedPane.getComponentAt(0));
 }

 @Test
 public void testHandleSelectedFile_ValidFile() {
 File followedFile = new File("path/to/valid/file");
 FileFollowingPane newFileFollowingPane = new FileFollowingPane(attributes);
 newFileFollowingPane.setFollowedFile(followedFile);
 tabbedPane.add(newFileFollowingPane, "New File Following Pane");
 tabbedPane.setSelectedComponent(newFileFollowingPane);
 tabbedPane.handleSelectedFile();
 File expectedParentDirectory = followedFile.getParentFile();
 assertEquals(expectedParentDirectory, attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_NullFile() {
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }
}