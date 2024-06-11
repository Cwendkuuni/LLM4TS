package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.*;
import org.junit.Test;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private JFrame frame;
 private JTabbedPane originalTabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setup() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 frame = new JFrame();
 originalTabbedPane = new JTabbedPane(SwingConstants.TOP);
 frame.setContentPane(originalTabbedPane);
 }

 @After
 public void teardown() {
 frame.dispose();
 tabbedPane = null;
 originalTabbedPane = null;
 attributes = null;
 }

 @Test
 public void testTabbedPaneConstructor() {
 TabbedPane testTabbedPane = new TabbedPane(attributes);
 assertNotNull(testTabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 JComponent testComponent = new JButton("Test Component");
 tabbedPane.addTab("Test Tab", testComponent);
 int x = 50;
 int y = 50;
 Component result = tabbedPane.findComponentAt(x, y);
 assertNotNull(result);
 assertEquals(testComponent, result);
 }

 @Test(expected = NullPointerException.class)
 public void testFindComponentAtOutsideComponent() {
 Component result = tabbedPane.findComponentAt(-10, -10);
 assertNull(result);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testFindComponentAtIndexOutOfBounds() {
 tabbedPane.addTab("Test Tab", new JLabel("Test Label"));
 tabbedPane.removeTabAt(0);
 Component result = tabbedPane.findComponentAt(0, 0);
 assertNull(result);
 }

 @Test
 public void testSetSelectedIndex() {
 int index = 1;
 tabbedPane.setSelectedIndex(index);
 assertEquals(index, tabbedPane.getSelectedIndex());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexOutOfBounds() {
 int index = tabbedPane.getTabCount();
 tabbedPane.setSelectedIndex(index);
 }

 @Test
 public void testSetSelectedComponent() {
 JComponent newComponent = new JButton("New Component");
 tabbedPane.addTab("New Tab", newComponent);
 tabbedPane.setSelectedComponent(newComponent);
 assertEquals(newComponent, tabbedPane.getSelectedComponent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedComponentNullComponent() {
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testRemoveTabAt() {
 int indexToRemove = 1;
 tabbedPane.removeTabAt(indexToRemove);
 assertFalse(tabbedPane.containsComponent(tabbedPane.getComponentAt(indexToRemove)));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtIndexOutOfBounds() {
 int indexToRemove = tabbedPane.getTabCount();
 tabbedPane.removeTabAt(indexToRemove);
 }

 @Test
 public void testHandleSelectedFile() {
 File followedFile = new File("testFile.txt");
 FileFollowingPane testPane = new FileFollowingPane(followedFile);
 tabbedPane.addTab("Test Tab", testPane);
 tabbedPane.setSelectedComponent(testPane);
 assertEquals(followedFile.getParentFile(), attributes.getLastFileChooserDirectory());
 }

}