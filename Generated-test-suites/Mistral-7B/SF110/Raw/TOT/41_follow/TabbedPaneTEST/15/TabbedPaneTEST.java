import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.gui.TabbedPane;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.FileFollowingPane;

public class TabbedPaneTest {

 private FollowAppAttributes attributes = new FollowAppAttributes();
 private TabbedPane tabbedPane;
 private FileFollowingPane fileFollowingPane;

 @Test
 public void testFindComponentAt() {
 // Test when no component is found
 int x = 10, y = 10;
 assertNull(tabbedPane.findComponentAt(x, y));

 // Test when component is found within a tab
 fileFollowingPane = new FileFollowingPane(new File("path/to/file.txt"));
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedIndex(0);
 int xComponent = fileFollowingPane.getX() + 10, yComponent = fileFollowingPane.getY() + 10;
 Component expectedComponent = fileFollowingPane.getComponentAt(0, 0);
 Component actualComponent = tabbedPane.findComponentAt(xComponent, yComponent);
 assertEquals(expectedComponent, actualComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 // Test when index is out of bounds
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab 1", new JLabel("Label 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Label 2"));
 tabbedPane.setSelectedIndex(-1);
 assertFalse(tabbedPane.isSelectedComponent(tabbedPane.getComponentAt(0)));
 assertFalse(tabbedPane.isSelectedComponent(tabbedPane.getComponentAt(1)));

 // Test when index is within bounds
 tabbedPane.setSelectedIndex(0);
 assertTrue(tabbedPane.isSelectedComponent(tabbedPane.getComponentAt(0)));
 assertFalse(tabbedPane.isSelectedComponent(tabbedPane.getComponentAt(1)));
 }

 @Test
 public void testSetSelectedComponent() {
 // Test when null component is passed
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab 1", new JLabel("Label 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Label 2"));
 tabbedPane.setSelectedComponent(null);
 assertFalse(tabbedPane.isSelectedIndexIsZero());
 assertFalse(tabbedPane.isSelectedComponentVisible());

 // Test when valid component is passed
 fileFollowingPane = new FileFollowingPane(new File("path/to/file.txt"));
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertTrue(tabbedPane.isSelectedIndexIsZero());
 assertTrue(tabbedPane.isSelectedComponentVisible());
 }

 @Test
 public void testRemoveTabAt() {
 // Test when index is out of bounds
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab 1", new JLabel("Label 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Label 2"));
 tabbedPane.removeTabAt(-1);
 assertEquals(2, tabbedPane.getTabCount());

 // Test when index is within bounds
 tabbedPane.removeTabAt(0);
 assertEquals(1, tabbedPane.getTabCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetSelectedFileWithNullAttributes() {
 tabbedPane = new TabbedPane(null);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 }

 @Test
 public void testHandleSelectedFile() {
 // Test when selected component is not a FileFollowingPane
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.setSelectedComponent(new JLabel("Label"));
 assertFalse(attributes.getLastFileChooserDirectory() != null);

 // Test when selected component is a FileFollowingPane
 fileFollowingPane = new FileFollowingPane(new File("path/to/file.txt"));
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals("path/to/file.txt".substring(0, "path/to/file.txt".lastIndexOf("/")), attributes.getLastFileChooserDirectory().getAbsolutePath());
 }
}