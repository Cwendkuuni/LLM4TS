import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import ghm.follow.gui.FileFollowingPane;
import java.awt.Point;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;
 private FileFollowingPane fileFollowingPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 fileFollowingPane = new FileFollowingPane();
 tabbedPane.add("Test Tab", fileFollowingPane);
 }

 @Test
 public void testFindComponentAt_ValidCoordinates_ReturnsComponent() {
 int x = 10, y = 20;
 tabbedPane.add(new JLabel("Test Label"), "Test Label Tab");
 Component component = tabbedPane.findComponentAt(x, y);
 assertTrue(component instanceof JLabel);
}

 @Test
 public void testFindComponentAt_InvalidCoordinates_ReturnsSelf() {
 tabbedPane.add(new JLabel("Test Label"), "Test Label Tab");
 assertNotNull(tabbedPane.findComponentAt(Integer.MAX_VALUE, Integer.MAX_VALUE));
}

 @Test
 public void testFindComponentAt_InvalidComponent_ReturnsSelf() {
 Component invalidComponent = new JComponent();
 assertSame(tabbedPane, tabbedPane.findComponentAt(0, 0, invalidComponent));
}

 @Test
 public void testFindComponentAt_NonVisibleComponent_ReturnsNull() {
 tabbedPane.setSelectedIndex(0);
 tabbedPane.setVisible(false);
 assertNull(tabbedPane.findComponentAt(0, 0));
}

 @Test
 public void testHandleSelectedFile_ValidSelection_SetsLastDirectory() {
 File file = new File("test.txt");
 fileFollowingPane.setFollowedFile(file);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
}

 @Test
 public void testHandleSelectedFile_InvalidSelection_DoesNotSetLastDirectory() {
 tabbedPane.setSelectedComponent(null);
 assertNull(attributes.getLastFileChooserDirectory());
}

 @Test
 public void testSetSelectedIndex_ValidIndex_SetsSelectedFileAndCallsHandleSelectedFile() {
 int index = 1;
 tabbedPane.setSelectedIndex(index);
 tabbedPane.setComponentAt(index, fileFollowingPane);
 assertEquals(fileFollowingPane, tabbedPane.getComponentAt(index));
 verify(tabbedPane).handleSelectedFile();
}

 @Test
 public void testSetSelectedComponent_ValidComponent_SetsSelectedFileAndCallsHandleSelectedFile() {
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
 verify(tabbedPane).handleSelectedFile();
}

 @Test
 public void testRemoveTabAt_ValidIndex_RemovesTabAndCallsHandleSelectedFile() {
 int index = 0;
 tabbedPane.removeTabAt(index);
 assertFalse(tabbedPane.getComponentAt(index) instanceof FileFollowingPane);
 verify(tabbedPane).handleSelectedFile();
}

 @Test
 public void testRemoveTabAt_InvalidIndex_DoesNotRemoveTabOrCallHandleSelectedFile() {
 int index = 1;
 tabbedPane.removeTabAt(index);
 assertTrue(tabbedPane.getComponentAt(index) instanceof FileFollowingPane);
 verifyNoMoreInteractions(tabbedPane);
}
}