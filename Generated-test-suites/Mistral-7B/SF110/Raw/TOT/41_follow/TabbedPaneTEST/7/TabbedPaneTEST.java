import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import ghm.follow.gui.FileFollowingPane;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FileFollowingPane fileFollowingPane;
 private FollowAppAttributes attributes;

 @Before
 public void setup() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 fileFollowingPane = new FileFollowingPane();
 tabbedPane.add(fileFollowingPane, "Test Tab");
 }

 @Test
 public void testFindComponentAt_ValidCoordinates_ReturnsComponent() {
 int x = 10;
 int y = 20;
 Component expectedComponent = fileFollowingPane;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(expectedComponent, actualComponent);
 }

 @Test
 public void testFindComponentAt_InvalidCoordinates_ReturnsNull() {
 int x = 10;
 int y = 30;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertNull(actualComponent);
 }

 @Test
 public void testFindComponentAt_OutsideComponentBounds_ReturnsTabbedPane() {
 int x = 10;
 int y = 50;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(tabbedPane, actualComponent);
 }

 @Test
 public void testSetSelectedIndex_ValidIndex_SetsSelectedIndex() {
 int index = 0;
 tabbedPane.setSelectedIndex(index);
 assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndex_InvalidIndex_ThrowsException() {
 int index = 1;
 tabbedPane.setSelectedIndex(index);
 fail("Expected IndexOutOfBoundsException but no exception was thrown.");
 }

 @Test
 public void testSetSelectedComponent_ValidComponent_SetsSelectedComponent() {
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedComponent_NullComponent_ThrowsException() {
 FileFollowingPane nullPane = null;
 tabbedPane.setSelectedComponent(nullPane);
 fail("Expected IllegalArgumentException but no exception was thrown.");
 }

 @Test
 public void testRemoveTabAt_ValidIndex_RemovesTab() {
 int index = 0;
 tabbedPane.removeTabAt(index);
 assertTrue(tabbedPane.getComponentCount() == 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAt_InvalidIndex_ThrowsException() {
 int index = 1;
 tabbedPane.removeTabAt(index);
 fail("Expected IndexOutOfBoundsException but no exception was thrown.");
 }

 @Test
 public void testHandleSelectedFile_ValidFileFollowingPane_UpdatesFollowAppAttributes() {
 File file = new File("test.txt");
 FileFollowingPane testFileFollowingPane = new FileFollowingPane(file);
 tabbedPane.add(testFileFollowingPane, "Test Tab");
 tabbedPane.setSelectedComponent(testFileFollowingPane);
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_NullFileFollowingPane_DoesNotUpdateFollowAppAttributes() {
 tabbedPane.setSelectedComponent(null);
 assertNull(attributes.getLastFileChooserDirectory());
 }
}