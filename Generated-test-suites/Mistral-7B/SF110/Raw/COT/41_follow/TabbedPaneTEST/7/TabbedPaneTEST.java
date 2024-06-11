package ghm.follow.gui.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import ghm.follow.gui.FileFollowingPane;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 // Test constructor
 @Test
 public void testTabbedPaneConstructor() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 // Test findComponentAt method with valid coordinates
 @Test
 public void testFindComponentAtValidCoordinates() {
 Component component = new JLabel("Test Component");
 tabbedPane.add(component, "Test Tab");
 Point point = new Point(50, 50);
 Component actualComponent = tabbedPane.findComponentAt(point.x, point.y);
 assertEquals(component, actualComponent);
 }

 // Test findComponentAt method with invalid coordinates
 @Test(expected = NullPointerException.class)
 public void testFindComponentAtInvalidCoordinates() {
 Component actualComponent = tabbedPane.findComponentAt(-10, -10);
 fail("Expected NullPointerException but got " + actualComponent);
 }

 // Test findComponentAt method when no component is found
 @Test
 public void testFindComponentAtNoComponentFound() {
 Component actualComponent = tabbedPane.findComponentAt(0, 0);
 assertNull(actualComponent);
 }

 // Test setSelectedIndex method
 @Test
 public void testSetSelectedIndex() {
 tabbedPane.add("Tab 1", new JLabel("Tab 1"));
 tabbedPane.add("Tab 2", new JLabel("Tab 2"));
 tabbedPane.setSelectedIndex(1);
 JLabel expectedLabel = (JLabel) tabbedPane.getComponentAt(1);
 JLabel actualLabel = (JLabel) tabbedPane.getComponentAt(0);
 assertEquals("Tab 2", expectedLabel.getText());
 assertEquals("Tab 1", actualLabel.getText());
 }

 // Test setSelectedComponent method
 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("Test File"));
 tabbedPane.add("Tab 1", new JLabel("Tab 1"));
 tabbedPane.add("Tab 2", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
 }

 // Test removeTabAt method
 @Test
 public void testRemoveTabAt() {
 tabbedPane.add("Tab 1", new JLabel("Tab 1"));
 tabbedPane.add("Tab 2", new JLabel("Tab 2"));
 tabbedPane.removeTabAt(0);
 assertFalse(tabbedPane.getComponentCount() > 1);
 }

 // Test handleSelectedFile method when a FileFollowingPane is selected
 @Test
 public void testHandleSelectedFileWithFileFollowingPaneSelected() {
 File file = new File("Test File");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(file);
 tabbedPane.add("Tab 1", new JLabel("Tab 1"));
 tabbedPane.add("Tab 2", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 // Test handleSelectedFile method when no FileFollowingPane is selected
 @Test
 public void testHandleSelectedFileWithNoFileFollowingPaneSelected() {
 JLabel label = new JLabel("Test Label");
 tabbedPane.add("Tab 1", new JLabel("Tab 1"));
 tabbedPane.add("Tab 2", label);
 tabbedPane.setSelectedComponent(label);
 assertNull(attributes.getLastFileChooserDirectory());
 }
}