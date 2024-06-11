import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.gui.TabbedPane;
import ghm.follow.config.FollowAppAttributes;
import javax.swing.*;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 // Test constructor method
 @Test
 public void testTabbedPaneConstructor() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 // Test findComponentAt method with valid x and y coordinates
 @Test
 public void testFindComponentAtValidCoordinates() {
 Component component = tabbedPane.getComponentAt(0, 0);
 int x = component.getX();
 int y = component.getY();
 Component result = tabbedPane.findComponentAt(x, y);
 assertEquals(component, result);
 }

 // Test findComponentAt method with out-of-bound x and y coordinates
 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindComponentAtOutOfBoundsCoordinates() {
 tabbedPane.findComponentAt(100, 100);
 }

 // Test findComponentAt method with null component
 @Test
 public void testFindComponentAtNullComponent() {
 Component nullComponent = null;
 tabbedPane.add(nullComponent);
 tabbedPane.findComponentAt(0, 0); // should not throw an exception
 }

 // Test setSelectedIndex method
 @Test
 public void testSetSelectedIndex() {
 int index = 1;
 tabbedPane.setSelectedIndex(index);
 assertEquals(index, tabbedPane.getSelectedIndex());
 }

 // Test setSelectedIndex method with out-of-bound index
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testSetSelectedIndexOutOfBoundsIndex() {
 int index = tabbedPane.getTabCount();
 tabbedPane.setSelectedIndex(index);
 }

 // Test setSelectedComponent method with null component
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedComponentNullComponent() {
 FileFollowingPane nullPane = null;
 tabbedPane.setSelectedComponent(nullPane);
 }

 // Test removeTabAt method with valid index
 @Test
 public void testRemoveTabAtValidIndex() {
 int index = 0;
 tabbedPane.removeTabAt(index);
 assertEquals(tabbedPane.getTabCount(), 1);
 }

 // Test removeTabAt method with out-of-bound index
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testRemoveTabAtOutOfBoundsIndex() {
 int index = tabbedPane.getTabCount();
 tabbedPane.removeTabAt(index);
 }

 // Test handleSelectedFile method with null pane
 @Test
 public void testHandleSelectedFileNullPane() {
 FileFollowingPane nullPane = null;
 tabbedPane.handleSelectedFile();
 }

 // Test handleSelectedFile method with valid pane and file
 @Test
 public void testHandleSelectedFileValidPaneAndFile() {
 File file = new File("testFile.txt");
 FileFollowingPane pane = new FileFollowingPane(file);
 tabbedPane.add(pane);
 tabbedPane.setSelectedComponent(pane);
 tabbedPane.handleSelectedFile();
 File parent = pane.getFollowedFile().getParentFile();
 assertEquals(parent, attributes.getLastFileChooserDirectory());
 }

 // Test handleSelectedFile method with valid pane and no file
 @Test
 public void testHandleSelectedFileValidPaneNoFile() {
 FileFollowingPane pane = new FileFollowingPane(null);
 tabbedPane.add(pane);
 tabbedPane.setSelectedComponent(pane);
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }
}