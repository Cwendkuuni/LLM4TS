package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
 int x = 10, y = 20;
 assertNull(tabbedPane.findComponentAt(x, y));

 JLabel label = new JLabel("Label");
 tabbedPane.addTab("Tab", label);
 int labelX = label.getX(), labelY = label.getY();
 assertSame(label, tabbedPane.findComponentAt(x + labelX, y + labelY));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtWithInvalidCoordinates() {
 assertNull(tabbedPane.findComponentAt(-1, 0));
 assertNull(tabbedPane.findComponentAt(0, -1));
 }

 @Test
 public void testSetSelectedIndex() {
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 tabbedPane.addTab("Tab1", panel1);
 tabbedPane.addTab("Tab2", panel2);
 tabbedPane.setSelectedIndex(1);
 assertEquals(panel2, tabbedPane.getSelectedComponent());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexWithInvalidIndex() {
 tabbedPane.setSelectedIndex(-1);
 }

 @Test
 public void testSetSelectedComponent() {
 JPanel panel3 = new JPanel();
 tabbedPane.addTab("Tab3", panel3);
 tabbedPane.setSelectedComponent(panel3);
 assertEquals(panel3, tabbedPane.getSelectedComponent());
 }

 @Test(expected = NullPointerException.class)
 public void testSetSelectedComponentWithNullComponent() {
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testRemoveTabAt() {
 JPanel panel4 = new JPanel();
 tabbedPane.addTab("Tab4", panel4);
 tabbedPane.removeTabAt(3); // Index out of bounds, as there are only 3 tabs
 assertEquals(3, tabbedPane.getComponentCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtWithInvalidIndex() {
 tabbedPane.removeTabAt(-1);
 }

 @Test
 public void testHandleSelectedFile() {
 File file = new File("testFile.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(file);
 tabbedPane.addTab("Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertNotNull(attributes.getLastFileChooserDirectory());
 File expected = file.getParentFile();
 assertSame(expected, attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFileWithNullSelectedComponent() {
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }

 @After
 public void tearDown() {
 tabbedPane = null;
 attributes = null;
 }

}