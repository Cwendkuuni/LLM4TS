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

 @Test
 public void testFindComponentAt_ValidCoordinates_ReturnsComponent() {
 int x = 10, y = 20;
 JButton button = new JButton("Test Button");
 tabbedPane.addTab("Test Tab", button);
 tabbedPane.setSelectedComponentAt(0);
 Component component = tabbedPane.findComponentAt(x, y);
 assertTrue(component instanceof JButton);
 assertEquals(button, component);
 }

 @Test
 public void testFindComponentAt_OutsideTabbedPane_ReturnsNull() {
 int x = 10, y = 20;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);
 }

 @Test
 public void testFindComponentAt_OutsideSelectedComponent_ReturnsNull() {
 int x = 50, y = 50;
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label"));
 tabbedPane.setSelectedIndex(0);
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);
 }

 @Test
 public void testFindComponentAt_ComponentIsVisible_ReturnsComponent() {
 int x = 10, y = 20;
 tabbedPane.addTab("Test Tab", new JButton("Test Button"));
 tabbedPane.setSelectedComponentAt(0);
 tabbedPane.setVisible(true);
 tabbedPane.validate();
 tabbedPane.repaint();
 Component component = tabbedPane.findComponentAt(x, y);
 assertTrue(component instanceof JButton);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIndex_NegativeIndex_ThrowsException() {
 tabbedPane.setSelectedIndex(-1);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testSetSelectedIndex_IndexGreaterThanComponentCount_ThrowsException() {
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label"));
 tabbedPane.setSelectedIndex(1);
 tabbedPane.removeTabAt(0);
 }

 @Test
 public void testSetSelectedComponent_NullComponent_DoesNotChangeSelectedComponent() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));
 tabbedPane.setSelectedIndex(0);
 tabbedPane.setSelectedComponent(null);
 assertEquals("Test Tab 1", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
 }

 @Test
 public void testRemoveTabAt_ValidIndex_RemovesComponent() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));
 tabbedPane.setSelectedIndex(0);
 tabbedPane.removeTabAt(0);
 assertEquals("Test Tab 2", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAt_InvalidIndex_ThrowsException() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));
 tabbedPane.setSelectedIndex(0);
 tabbedPane.removeTabAt(2);
 }

 @Test
 public void testHandleSelectedFile_ValidFileFollowingPane_SetsLastFileChooserDirectory() {
 File file = new File("test.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(file);
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 File expectedDirectory = file.getParentFile();
 assertEquals(expectedDirectory, attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_NullSelectedComponent_DoesNotChangeLastFileChooserDirectory() {
 tabbedPane.setSelectedComponent(null);
 assertNull(attributes.getLastFileChooserDirectory());
 }
}