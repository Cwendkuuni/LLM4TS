package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.*;
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

 // Test constructor with valid attributes
 @Test
 public void testTabbedPaneConstruction() {
 assertEquals(JTabbedPane.TOP, tabbedPane.getTabPlacement());
 }

 // Test findComponentAt method with valid coordinates
 @Test
 public void testFindComponentAtValid() {
 Component component = new JLabel("Valid Component");
 tabbedPane.add(component, "Test Tab");
 int x = component.getX();
 int y = component.getY();
 Component foundComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(component, foundComponent);
 }

 // Test findComponentAt method with invalid coordinates
 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtInvalid() {
 Component component = new JLabel("Invalid Component");
 tabbedPane.add(component, "Test Tab");
 int x = component.getX() + 10;
 int y = component.getY() + 10;
 tabbedPane.findComponentAt(x, y);
 }

 // Test setSelectedIndex method
 @Test
 public void testSetSelectedIndex() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 tabbedPane.addTab("Test Tab 3", new JLabel("Test Component 3"));
 tabbedPane.setSelectedIndex(1);
 JLabel selectedComponent = (JLabel)tabbedPane.getComponentAt(1);
 assertEquals("Test Component 2", selectedComponent.getText());
 }

 // Test setSelectedComponent method with valid component
 @Test
 public void testSetSelectedComponentValid() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 JLabel componentToSelect = new JLabel("Test Component 2");
 tabbedPane.setSelectedComponent(componentToSelect);
 JLabel selectedComponent = (JLabel)tabbedPane.getSelectedComponent();
 assertEquals(componentToSelect, selectedComponent);
 }

 // Test setSelectedComponent method with null component
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedComponentNull() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.setSelectedComponent(null);
 }

 // Test removeTabAt method with valid index
 @Test
 public void testRemoveTabAtValidIndex() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 tabbedPane.addTab("Test Tab 3", new JLabel("Test Component 3"));
 tabbedPane.removeTabAt(1);
 int newNumberOfTabs = tabbedPane.getTabCount();
 assertEquals(2, newNumberOfTabs);
 }

 // Test removeTabAt method with invalid index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtInvalidIndex() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 tabbedPane.removeTabAt(2);
 }

 // Test handleSelectedFile method with valid FileFollowingPane
 @Test
 public void testHandleSelectedFileValid() {
 File file = new File("testFile.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(file);
 tabbedPane.add(fileFollowingPane, "Test Tab");
 tabbedPane.setSelectedComponent(fileFollowingPane);
 File parent = fileFollowingPane.getFollowedFile().getParentFile();
 assertEquals(parent, attributes.getLastFileChooserDirectory());
 }

 // Test handleSelectedFile method with null FileFollowingPane
 @Test(expected = IllegalArgumentException.class)
 public void testHandleSelectedFileNull() {
 FileFollowingPane nullFileFollowingPane = null;
 tabbedPane.setSelectedComponent(nullFileFollowingPane);
 }
}