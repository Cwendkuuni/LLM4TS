package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.*;
import org.junit.*;

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
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 int x = 10;
 int y = 20;
 Component component = new JLabel("Test Component");
 tabbedPane.add(component, "Test Tab");
 tabbedPane.setSelectedComponentAt(0);

 Component foundComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(component, foundComponent);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindComponentAtOutOfBounds() {
 Component foundComponent = tabbedPane.findComponentAt(10, 20);
 assertNull(foundComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 JLabel component1 = new JLabel("Component 1");
 JLabel component2 = new JLabel("Component 2");
 tabbedPane.add(component1, "Tab 1");
 tabbedPane.add(component2, "Tab 2");
 tabbedPane.setSelectedIndex(1);

 JLabel selectedComponent = (JLabel) tabbedPane.getComponentAt(1);
 assertEquals(component2, selectedComponent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIndexNegative() {
 tabbedPane.setSelectedIndex(-1);
 }

 @Test
 public void testSetSelectedComponent() {
 JLabel component1 = new JLabel("Component 1");
 JLabel component2 = new JLabel("Component 2");
 tabbedPane.add(component1, "Tab 1");
 tabbedPane.add(component2, "Tab 2");
 tabbedPane.setSelectedComponent(component2);

 JLabel selectedComponent = (JLabel) tabbedPane.getComponentAt(1);
 assertEquals(component2, selectedComponent);
 }

 @Test(expected = NullPointerException.class)
 public void testSetSelectedComponentNull() {
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 @ExpectedException(IllegalArgumentException.class)
 public void testRemoveTabAtNegative() {
 tabbedPane.removeTabAt(-1);
 }

 @Test
 public void testRemoveTabAt() {
 JLabel component1 = new JLabel("Component 1");
 JLabel component2 = new JLabel("Component 2");
 tabbedPane.add(component1, "Tab 1");
 tabbedPane.add(component2, "Tab 2");
 tabbedPane.removeTabAt(0);

 int componentCount = tabbedPane.getComponentCount();
 assertEquals(1, componentCount);
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane = new FileFollowingPane(new File("Test File"));
 tabbedPane.add(pane, "Test Tab");
 tabbedPane.setSelectedComponent(pane);
 File parent = new File("Test Parent Directory");
 assertEquals(parent, attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFileNull() {
 tabbedPane.setSelectedComponent(null);
 assertNull(attributes.getLastFileChooserDirectory());
 }
}