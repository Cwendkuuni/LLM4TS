import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testFindComponentAt_ValidComponent() {
 final Component component = new JLabel("Test Component");
 tabbedPane.addTab("Test Tab", component);
 int x = 10, y = 10;
 Component actual = tabbedPane.findComponentAt(x, y);
 assertEquals("Test Component", actual);
 }

 @Test
 public void testFindComponentAt_InvalidXY() {
 int x = 10, y = 11;
 Component actual = tabbedPane.findComponentAt(x, y);
 assertNull(actual);
 }

 @Test
 public void testFindComponentAt_OutsideContainer() {
 int x = 10, y = 10;
 tabbedPane.setBounds(0, 0, 200, 200);
 Component actual = tabbedPane.findComponentAt(x, y);
 assertNull(actual);
 }

 @Test
 public void testSetSelectedIndex_ValidIndex() {
 tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.setSelectedIndex(0);
 assertEquals("Tab 1", tabbedPane.getTitleAt(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndex_InvalidIndex() {
 tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.setSelectedIndex(2);
 }

 @Test
 public void testSetSelectedComponent_ValidComponent() {
 final Component component = new JLabel("Test Component");
 tabbedPane.addTab("Tab 1", component);
 tabbedPane.setSelectedComponent(component);
 assertEquals(component, tabbedPane.getComponentAt(0));
 }

 @Test
 public void testRemoveTabAt_ValidIndex() {
 tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.removeTabAt(0);
 assertEquals(1, tabbedPane.getTabCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAt_InvalidIndex() {
 tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.removeTabAt(2);
 }

 @Test
 public void testHandleSelectedFile_ValidFileFollowingPane() {
 final FileFollowingPane pane1 = new FileFollowingPane(new File("test1.txt"));
 final FileFollowingPane pane2 = new FileFollowingPane(new File("test2.txt"));
 tabbedPane.addTab("Tab 1", pane1);
 tabbedPane.addTab("Tab 2", pane2);
 tabbedPane.setSelectedIndex(0);
 tabbedPane.handleSelectedFile();
 assertEquals(new File("test1.txt").getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testSetSelectedFile_NullAttribute() {
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testSetSelectedFile_InvalidComponent() {
 final Container container = new JPanel();
 tabbedPane.addTab("Tab 1", container);
 tabbedPane.setSelectedComponent(container);
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testSetSelectedFile_EmptyFileFollowingPane() {
 final FileFollowingPane emptyPane = new FileFollowingPane(null);
 tabbedPane.addTab("Tab 1", emptyPane);
 tabbedPane.setSelectedComponent(emptyPane);
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }
}