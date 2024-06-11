import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import ghm.follow.gui.FileFollowingPane;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;
 private FileFollowingPane fileFollowingPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 attributes.setTabPlacement(SwingConstants.TOP);
 tabbedPane = new TabbedPane(attributes);
 fileFollowingPane = new FileFollowingPane();
 tabbedPane.add(fileFollowingPane, "Test Tab");
 }

 @Test
 public void testFindComponentAt_ValidCoordinates_ReturnsComponent() {
 int x = 5;
 int y = 5;
 Component actual = tabbedPane.findComponentAt(x, y);
 assertEquals("Component should be FileFollowingPane", fileFollowingPane, actual);
 }

 @Test
 public void testFindComponentAt_InvalidCoordinates_ReturnsNull() {
 int x = 10;
 int y = 10;
 Component actual = tabbedPane.findComponentAt(x, y);
 assertNull("Component should be null", actual);
 }

 @Test
 public void testFindComponentAt_ComponentOutsideOfTabbedPane_ReturnsTabbedPane() {
 int x = 10;
 int y = 10;
 Component actual = tabbedPane.findComponentAt(x, y);
 assertEquals("Component should be TabbedPane", tabbedPane, actual);
 }

 @Test
 public void testSetSelectedIndex_ValidIndex_SetsSelectedIndex() {
 tabbedPane.setSelectedIndex(0);
 FileFollowingPane expected = (FileFollowingPane)tabbedPane.getComponentAt(0);
 FileFollowingPane actual = (FileFollowingPane)tabbedPane.getSelectedComponent();
 assertEquals("Selected components should be the same", expected, actual);
 }

 @Test
 public void testSetSelectedIndex_InvalidIndex_DoesNotChangeSelectedComponent() {
 tabbedPane.setSelectedIndex(1);
 FileFollowingPane expected = fileFollowingPane;
 FileFollowingPane actual = (FileFollowingPane)tabbedPane.getSelectedComponent();
 assertEquals("Selected component should not have changed", expected, actual);
 }

 @Test
 public void testSetSelectedComponent_ValidComponent_SetsSelectedComponent() {
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals("Selected components should be the same", fileFollowingPane, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent_NullComponent_DoesNotChangeSelectedComponent() {
 tabbedPane.setSelectedComponent(null);
 FileFollowingPane expected = fileFollowingPane;
 FileFollowingPane actual = (FileFollowingPane)tabbedPane.getSelectedComponent();
 assertEquals("Selected component should not have changed", expected, actual);
 }

 @Test
 public void testRemoveTabAt_ValidIndex_RemovesTab() {
 tabbedPane.removeTabAt(0);
 assertFalse("TabbedPane should not contain the FileFollowingPane after removal", tabbedPane.contains(fileFollowingPane));
 }

 @Test
 public void testRemoveTabAt_InvalidIndex_DoesNotChangeTabbedPane() {
 tabbedPane.removeTabAt(1);
 int numberOfComponents = tabbedPane.getComponentCount();
 assertEquals("TabbedPane should still contain the same number of components", 1, numberOfComponents);
 }

 @Test
 public void testHandleSelectedFile_ValidSelection_SetsLastFileChooserDirectory() {
 File testFile = new File("testFile.txt");
 FileFollowingPane newFileFollowingPane = new FileFollowingPane(testFile);
 tabbedPane.add(newFileFollowingPane, "New Tab");
 tabbedPane.setSelectedIndex(1);
 tabbedPane.handleSelectedFile();
 File expected = testFile.getParentFile();
 File actual = attributes.getLastFileChooserDirectory();
 assertEquals("LastFileChooserDirectory should be set to the parent directory of the selected file", expected, actual);
 }

 @Test
 public void testHandleSelectedFile_NullSelection_DoesNotChangeLastFileChooserDirectory() {
 tabbedPane.handleSelectedFile();
 File expected = null;
 File actual = attributes.getLastFileChooserDirectory();
 assertEquals("LastFileChooserDirectory should not have been changed", expected, actual);
 }

 @Test
 public void testHandleSelectedFile_EmptySelection_DoesNotChangeLastFileChooserDirectory() {
 tabbedPane.handleSelectedFile();
 File expected = null;
 File actual = attributes.getLastFileChooserDirectory();
 assertEquals("LastFileChooserDirectory should not have been changed", expected, actual);
 }
}