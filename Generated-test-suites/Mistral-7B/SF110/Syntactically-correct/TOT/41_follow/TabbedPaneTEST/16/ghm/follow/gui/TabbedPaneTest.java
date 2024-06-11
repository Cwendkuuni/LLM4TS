import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import javax.swing.*;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testFindComponentAt_ValidCoordinates_ReturnsComponent() {
 final int x = 50, y = 50;
 final JButton button = new JButton("Test Button");
 tabbedPane.addTab("Test Tab", button);
 tabbedPane.setSelectedComponentAt(0);
 final Component result = tabbedPane.findComponentAt(x, y);
 assertTrue(result instanceof JButton);
 assertEquals(button, result);
 }

 @Test
 public void testFindComponentAt_InvalidCoordinates_ReturnsNull() {
 final int x = 10, y = 10;
 final JButton button = new JButton("Test Button");
 tabbedPane.addTab("Test Tab", button);
 tabbedPane.setSelectedComponentAt(0);
 final Component result = tabbedPane.findComponentAt(x, y);
 assertNull(result);
 }

 @Test
 public void testFindComponentAt_ComponentOutsideBounds_ReturnsNull() {
 final int x = 200, y = 200;
 final JButton button = new JButton("Test Button");
 tabbedPane.addTab("Test Tab", button);
 tabbedPane.setSelectedComponentAt(0);
 final Component result = tabbedPane.findComponentAt(x, y);
 assertNull(result);
 }

 @Test
 public void testFindComponentAt_ComponentWithinContainer_ReturnsComponent() {
 final int x = 50, y = 50;
 final JPanel panel = new JPanel();
 final JButton button = new JButton("Test Button");
 tabbedPane.addTab("Test Tab", panel);
 panel.add(button);
 tabbedPane.setSelectedComponentAt(0);
 final Component result = tabbedPane.findComponentAt(x, y);
 assertTrue(result instanceof JButton);
 assertEquals(button, result);
 }

 @Test
 public void testFindComponentAt_ComponentInvisible_ReturnsNull() {
 final int x = 50, y = 50;
 final JPanel panel = new JPanel();
 final JButton button = new JButton("Test Button");
 tabbedPane.addTab("Test Tab", panel);
 panel.setVisible(false);
 tabbedPane.setSelectedComponentAt(0);
 final Component result = tabbedPane.findComponentAt(x, y);
 assertNull(result);
 }

 @Test
 public void testSetSelectedIndex_ValidIndex_SetsSelectedIndex() {
 final JButton button = new JButton("Test Button");
 tabbedPane.addTab("Test Tab", button);
 tabbedPane.setSelectedIndex(0);
 final Component selectedComponent = tabbedPane.getSelectedComponent();
 assertTrue(selectedComponent instanceof JButton);
 assertEquals(button, selectedComponent);
 }

 @Test
 public void testSetSelectedIndex_InvalidIndex_DoesNotChangeSelectedComponent() {
 final JButton button = new JButton("Test Button");
 tabbedPane.addTab("Test Tab", button);
 tabbedPane.setSelectedIndex(-1);
 final Component selectedComponent = tabbedPane.getSelectedComponent();
 assertNotNull(selectedComponent);
 assertFalse(selectedComponent instanceof JButton);
 }

 @Test
 public void testSetSelectedComponent_ValidComponent_SetsSelectedComponent() {
 final JButton button = new JButton("Test Button");
 tabbedPane.addTab("Test Tab", button);
 tabbedPane.setSelectedComponent(button);
 final Component selectedComponent = tabbedPane.getSelectedComponent();
 assertTrue(selectedComponent instanceof JButton);
 assertEquals(button, selectedComponent);
 }

 @Test
 public void testSetSelectedComponent_NullComponent_ThrowsIllegalArgumentException() {
 tabbedPane.setSelectedComponent(null);
 try {
 final Component selectedComponent = tabbedPane.getSelectedComponent();
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testRemoveTabAt_ValidIndex_RemovesTab() {
 final JButton button = new JButton("Test Button");
 tabbedPane.addTab("Test Tab", button);
 tabbedPane.removeTabAt(0);
 final int newTabCount = tabbedPane.getTabCount();
 assertEquals(newTabCount, 0);
 }

 @Test
 public void testRemoveTabAt_InvalidIndex_ThrowsIndexOutOfBoundsException() {
 tabbedPane.removeTabAt(-1);
 try {
 final int newTabCount = tabbedPane.getTabCount();
 fail("Should throw IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // pass
 }
 }

 @Test
 public void testSetSelectedFile_ValidFile_SetsLastFileChooserDirectory() {
 final File file = new File("test.txt");
 tabbedPane.setSelectedFile(file);
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testSetSelectedFile_NullFile_ThrowsNullPointerException() {
 tabbedPane.setSelectedFile(null);
 try {
 final File lastFileChooserDirectory = attributes.getLastFileChooserDirectory();
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // pass
 }
 }
}