import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.gui.TabbedPane;
import ghm.follow.config.FollowAppAttributes;
import javax.swing.JLabel;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstructor() {
 tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt_ValidCoordinates() {
 final JLabel label = new JLabel("Test Label");
 tabbedPane.addTab("Test Tab", label);
 final Component component = tabbedPane.findComponentAt(10, 10);
 assertTrue(component instanceof JLabel);
 assertEquals(label, component);
 }

 @Test
 public void testFindComponentAt_OutsideComponent() {
 final JLabel label = new JLabel("Test Label");
 tabbedPane.addTab("Test Tab", label);
 final Component component = tabbedPane.findComponentAt(100, 100);
 assertNull(component);
 }

 @Test
 public void testFindComponentAt_ComponentWithChildren() {
 final JLabel label = new JLabel("Test Label");
 final JPanel panel = new JPanel(new FlowLayout());
 panel.add(label);
 tabbedPane.addTab("Test Tab", panel);
 final Component component = tabbedPane.findComponentAt(10, 10);
 assertTrue(component instanceof JPanel);
 final Component childComponent = component.getComponentAt(0, 0);
 assertTrue(childComponent instanceof JLabel);
 assertEquals(label, childComponent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIndex_NegativeIndex() {
 tabbedPane.setSelectedIndex(-1);
 }

 @Test
 public void testSetSelectedComponent_NullComponent() {
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testRemoveTabAt_ValidIndex() {
 final JLabel label1 = new JLabel("Tab 1");
 final JLabel label2 = new JLabel("Tab 2");
 tabbedPane.addTab("Tab 1", label1);
 tabbedPane.addTab("Tab 2", label2);
 tabbedPane.removeTabAt(0);
 assertEquals(1, tabbedPane.getComponentCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAt_InvalidIndex() {
 tabbedPane.removeTabAt(2);
 }

 @Test
 public void testHandleSelectedFile() {
 final File file = new File("test.txt");
 final FileFollowingPane pane = new FileFollowingPane(file, attributes);
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.setSelectedComponent(pane);
 assertNotNull(attributes.getLastFileChooserDirectory());
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_NoSelectedComponent() {
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }
}