package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;

import org.junit.Test;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 // Testing constructor
 @Test
 public void testTabbedPaneConstructor() {
 tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 // Testing findComponentAt method
 @Test
 public void testFindComponentAt() {
 int x = 10, y = 10;
 Component expectedComponent = new JButton("Expected");
 tabbedPane.add(expectedComponent, "TestTab");
 tabbedPane.setBounds(0, 0, 200, 200);
 tabbedPane.setSelectedIndex(0);
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(expectedComponent, actualComponent);
 }

 // Testing findComponentAt method when component is not contained
 @Test
 public void testFindComponentAtOutside() {
 int x = 210, y = 10;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertNull(actualComponent);
 }

 // Testing findComponentAt method when component is not visible
 @Test
 public void testFindComponentAtInvisible() {
 JPanel panel = new JPanel();
 tabbedPane.add(panel, "TestTab");
 tabbedPane.setBounds(0, 0, 200, 200);
 tabbedPane.setSelectedIndex(0);
 panel.setVisible(false);
 Component actualComponent = tabbedPane.findComponentAt(0, 0);
 assertNull(actualComponent);
 }

 // Testing setSelectedIndex method
 @Test
 public void testSetSelectedIndex() {
 int index = 1;
 tabbedPane.setSelectedIndex(index);
 FileFollowingPane expectedPane = new FileFollowingPane(new File("TestFile1.txt"), attributes);
 tabbedPane.add(expectedPane, "TestTab1");
 Component actualComponent = tabbedPane.getComponentAt(index);
 assertEquals(expectedPane, actualComponent);
 }

 // Testing setSelectedComponent method
 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane pane = new FileFollowingPane(new File("TestFile2.txt"), attributes);
 tabbedPane.setSelectedComponent(pane);
 FileFollowingPane actualPane = (FileFollowingPane)tabbedPane.getSelectedComponent();
 assertEquals(pane, actualPane);
 }

 // Testing removeTabAt method
 @Test
 public void testRemoveTabAt() {
 FileFollowingPane pane = new FileFollowingPane(new File("TestFile3.txt"), attributes);
 tabbedPane.add(pane, "TestTab3");
 tabbedPane.removeTabAt(0);
 assertTrue(tabbedPane.getComponentCount() < 2);
 }

 // Testing handleSelectedFile method when selected component is null
 @Test(expected = NullPointerException.class)
 public void testHandleSelectedFileNullComponent() {
 tabbedPane.handleSelectedFile();
 }

 // Testing handleSelectedFile method when selected file is null
 @Test
 public void testHandleSelectedFileNullFile() {
 FileFollowingPane pane = new FileFollowingPane(null, attributes);
 tabbedPane.setSelectedComponent(pane);
 tabbedPane.handleSelectedFile();
 assertNotNull(attributes.getLastFileChooserDirectory());
 }

 // Testing handleSelectedFile method
 @Test
 public void testHandleSelectedFile() {
 File file = new File("TestFile4.txt");
 FileFollowingPane pane = new FileFollowingPane(file, attributes);
 tabbedPane.setSelectedComponent(pane);
 tabbedPane.handleSelectedFile();
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }
}