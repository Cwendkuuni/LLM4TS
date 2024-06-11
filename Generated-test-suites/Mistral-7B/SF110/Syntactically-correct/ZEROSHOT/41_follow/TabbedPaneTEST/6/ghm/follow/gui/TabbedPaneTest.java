import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstruction() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 int x = 10;
 int y = 10;
 Component component = new JLabel("Test Component");
 tabbedPane.addTab("Test Tab", component);
 tabbedPane.setSelectedComponent(component);

 Component foundComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(component, foundComponent);
 }

 @Test
 public void testFindComponentAt_OutsideComponent() {
 int x = 10;
 int y = 11;
 Component foundComponent = tabbedPane.findComponentAt(x, y);
 assertNull(foundComponent);
 }

 @Test
 public void testFindComponentAt_OutsideTabbedPane() {
 int x = 10;
 int y = 15;
 Component foundComponent = tabbedPane.findComponentAt(x, y);
 assertNull(foundComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 int index = 1;
 tabbedPane.setSelectedIndex(index);
 FileFollowingPane expectedPane = new FileFollowingPane(new File("testFile.txt"));
 assertEquals(expectedPane, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane pane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.setSelectedComponent(pane);
 FileFollowingPane expectedPane = pane;
 assertEquals(expectedPane, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 int index = 0;
 Component tabToRemove = tabbedPane.getComponentAt(index);
 tabbedPane.removeTabAt(index);
 assertNull(tabbedPane.getComponentAt(index));
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