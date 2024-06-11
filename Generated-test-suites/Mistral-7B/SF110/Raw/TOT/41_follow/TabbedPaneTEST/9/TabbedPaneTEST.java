import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import ghm.follow.gui.FileFollowingPane;
import javax.swing.JFrame;
import java.awt.*;
import java.io.File;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;
 private JFrame frame;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 }

 @Test
 public void testFindComponentAt_ValidCoords_ReturnsComponent() {
 int x = 50;
 int y = 50;
 FileFollowingPane expectedComponent = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.add(expectedComponent, "Test Tab");
 tabbedPane.setSelectedComponent(expectedComponent);
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(expectedComponent, actualComponent);
 }

 @Test
 public void testFindComponentAt_InvalidCoords_ReturnsNull() {
 int x = 50;
 int y = 50;
 tabbedPane.add(new JLabel("Test Label"), "Test Tab");
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertNull(actualComponent);
 }

 @Test
 public void testFindComponentAt_ComponentOutsideViewPort_ReturnsNull() {
 int x = 100;
 int y = 100;
 tabbedPane.add(new JLabel("Test Label"), "Test Tab");
 tabbedPane.setSize(200, 200);
 tabbedPane.setLocation(10, 10);
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertNull(actualComponent);
 }

 @Test
 public void testSetSelectedIndex_ValidIndex_SetsSelectedTab() {
 tabbedPane.add(new JLabel("Tab 1"), "Tab 1");
 tabbedPane.add(expectedComponent, "Tab 2");
 tabbedPane.setSelectedIndex(1);
 FileFollowingPane actualComponent = (FileFollowingPane) tabbedPane.getComponentAt(1);
 assertEquals(expectedComponent, actualComponent);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testSetSelectedIndex_InvalidIndex_ThrowsException() {
 tabbedPane.add(new JLabel("Tab 1"), "Tab 1");
 tabbedPane.setSelectedIndex(2);
 }

 @Test
 public void testSetSelectedComponent_ValidComponent_SetsSelectedTab() {
 FileFollowingPane componentToSelect = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.add(componentToSelect, "Test Tab");
 tabbedPane.setSelectedComponent(componentToSelect);
 FileFollowingPane actualComponent = (FileFollowingPane) tabbedPane.getSelectedComponent();
 assertEquals(componentToSelect, actualComponent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedComponent_NullComponent_ThrowsException() {
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testRemoveTabAt_ValidIndex_RemovesTab() {
 tabbedPane.add(new JLabel("Tab 1"), "Tab 1");
 tabbedPane.add(expectedComponent, "Tab 2");
 tabbedPane.removeTabAt(1);
 int actualNumTabs = tabbedPane.getComponentCount();
 assertEquals(1, actualNumTabs);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAt_InvalidIndex_ThrowsException() {
 tabbedPane.add(new JLabel("Tab 1"), "Tab 1");
 tabbedPane.removeTabAt(2);
 }

 @Test
 public void testHandleSelectedFile_ValidComponent_SetsLastFileChooserDirectory() {
 FileFollowingPane pane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.add(pane, "Test Tab");
 tabbedPane.setSelectedComponent(pane);
 File expectedLastFileChooserDirectory = new File("testFile.txt").getParentFile();
 File actualLastFileChooserDirectory = attributes.getLastFileChooserDirectory();
 assertEquals(expectedLastFileChooserDirectory, actualLastFileChooserDirectory);
 }

 @Test
 public void testHandleSelectedFile_NullComponent_DoesNotSetLastFileChooserDirectory() {
 tabbedPane.setSelectedComponent(null);
 File actualLastFileChooserDirectory = attributes.getLastFileChooserDirectory();
 assertNull(actualLastFileChooserDirectory);
 }
}