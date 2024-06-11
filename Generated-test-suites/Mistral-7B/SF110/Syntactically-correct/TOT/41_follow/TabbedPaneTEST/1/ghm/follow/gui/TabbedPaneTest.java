import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.FileFollowingPane;
import ghm.follow.gui.TabbedPane;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FileFollowingPane fileFollowingPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 fileFollowingPane = new FileFollowingPane();
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 }

 @Test
 public void testFindComponentAt_ValidCoordinates_ReturnsComponent() {
 int x = 50, y = 50;
 tabbedPane.add(new JLabel("Test Component"), "Test Tab");
 Component component = tabbedPane.findComponentAt(x, y);
 assertTrue(component instanceof JLabel);
 assertEquals("Test Tab", component.getName());
 }

 @Test
 public void testFindComponentAt_InvalidCoordinates_ReturnsNull() {
 int x = 50, y = 100;
 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAt_CoordinatesOutsideComponent_ReturnsParent() {
 int x = 10, y = 10;
 Component parent = tabbedPane.getParent();
 Component component = tabbedPane.findComponentAt(x, y);
 assertSame(parent, component);
 }

 @Test
 public void testSetSelectedIndex_ValidIndex_SetsSelectedTab() {
 tabbedPane.setSelectedIndex(0);
 assertEquals("Test Tab", tabbedPane.getTitleAt(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndex_InvalidIndex_ThrowsException() {
 tabbedPane.setSelectedIndex(100);
 }

 @Test
 public void testSetSelectedComponent_ValidComponent_SetsSelectedTab() {
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals("Test Tab", tabbedPane.getTitleAt(0));
 }

 @Test(expected = NullPointerException.class)
 public void testSetSelectedComponent_NullComponent_ThrowsException() {
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testRemoveTabAt_ValidIndex_RemovesTab() {
 tabbedPane.removeTabAt(0);
 assertFalse(tabbedPane.containsComponent(fileFollowingPane));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAt_InvalidIndex_ThrowsException() {
 tabbedPane.removeTabAt(100);
 }

 @Test
 public void testHandleSelectedFile_ValidComponent_SetsLastFileChooserDirectory() {
 File file = new File("test.txt");
 fileFollowingPane.setFollowedFile(file);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }
}