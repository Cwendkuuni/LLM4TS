package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
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

 // Test constructor
 @Test
 public void testTabbedPaneConstructor() {
 tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 // Test findComponentAt method
 @Test
 public void testFindComponentAt() {
 JLabel label = new JLabel("Test Label");
 tabbedPane.addTab("Test Tab", new JPanel(), label);
 tabbedPane.setSelectedIndex(0);
 int x = label.getX() + label.getWidth() / 2;
 int y = label.getY() + label.getHeight() / 2;
 Component component = tabbedPane.findComponentAt(x, y);
 assertEquals(label, component);
 }

 // Test setSelectedIndex method
 @Test
 public void testSetSelectedIndex() {
 JLabel label1 = new JLabel("Tab 1 Label");
 JLabel label2 = new JLabel("Tab 2 Label");
 tabbedPane.addTab("Tab 1", new JPanel(), label1);
 tabbedPane.addTab("Tab 2", new JPanel(), label2);
 tabbedPane.setSelectedIndex(0);
 Component component1 = tabbedPane.findComponentAt(0, 0);
 assertEquals(label1, component1);
 tabbedPane.setSelectedIndex(1);
 Component component2 = tabbedPane.findComponentAt(0, 0);
 assertNull(component2);
 component2 = tabbedPane.findComponentAt(120, 120); // outside component area
 assertNull(component2);
 }

 // Test setSelectedComponent method
 @Test
 public void testSetSelectedComponent() {
 JLabel label1 = new JLabel("Tab 1 Label");
 JLabel label2 = new JLabel("Tab 2 Label");
 JPanel tab1 = new JPanel();
 JPanel tab2 = new JPanel();
 tab1.add(label1);
 tab2.add(label2);
 tabbedPane.addTab("Tab 1", tab1);
 tabbedPane.addTab("Tab 2", tab2);
 tabbedPane.setSelectedComponent(tab1);
 Component component1 = tabbedPane.findComponentAt(0, 0);
 assertEquals(label1, component1);
 tabbedPane.setSelectedComponent(tab2);
 Component component2 = tabbedPane.findComponentAt(0, 0);
 assertEquals(label2, component2);
 }

 // Test removeTabAt method
 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtNegativeIndex() {
 tabbedPane.removeTabAt(-1);
 fail("Exception not thrown");
 }

 @Test
 public void testRemoveTabAtValidIndex() {
 JLabel label = new JLabel("Test Label");
 tabbedPane.addTab("Test Tab", new JPanel(), label);
 tabbedPane.setSelectedIndex(0);
 tabbedPane.removeTabAt(0);
 Component component = tabbedPane.findComponentAt(0, 0);
 assertNull(component);
 }

 // Test handleSelectedFile method
 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane = new FileFollowingPane(new File("Test File"));
 tabbedPane.addTab("Test Tab", pane);
 tabbedPane.setSelectedComponent(pane);
 File parent = new File("Parent Directory");
 assertEquals(parent, attributes.getLastFileChooserDirectory());
 }

 // Test handleSelectedFile method with null selected component
 @Test
 public void testHandleSelectedFileWithNullSelectedComponent() {
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }

}