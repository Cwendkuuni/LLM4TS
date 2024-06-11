package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import org.junit.*;

import ghm.follow.config.*;
import ghm.follow.gui.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private JFrame frame;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 }

 @Test
 public void testTabbedPaneConstructor() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 Component component = new JButton("Test");
 tabbedPane.add("Test", component);
 int x = 10, y = 10;
 Component result = tabbedPane.findComponentAt(x, y);
 assertEquals(component, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtOutsideBoundary() {
 tabbedPane.findComponentAt(-10, 10);
 }

 @Test
 public void testFindComponentAtWithNestedComponent() {
 Container container = new JPanel();
 JButton button = new JButton("Test");
 container.add(button);
 tabbedPane.add("Test", container);
 int x = 10, y = 10;
 Component result = tabbedPane.findComponentAt(x, y);
 assertEquals(button, result);
 }

 @Test
 public void testSetSelectedIndex() {
 FileFollowingPane pane1 = new FileFollowingPane(new File("test1.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("test2.txt"));
 tabbedPane.add("Test1", pane1);
 tabbedPane.add("Test2", pane2);
 tabbedPane.setSelectedIndex(0);
 FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getComponentAt(0);
 assertEquals(pane1, selectedPane);
 }

 @Test
 public void testSetSelectedComponentNull() {
 tabbedPane.setSelectedComponent(null);
 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane pane1 = new FileFollowingPane(new File("test1.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("test2.txt"));
 tabbedPane.add("Test1", pane1);
 tabbedPane.add("Test2", pane2);
 tabbedPane.setSelectedComponent(pane1);
 FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getSelectedComponent();
 assertEquals(pane1, selectedPane);
 }

 @Test
 public void testRemoveTabAt() {
 FileFollowingPane pane1 = new FileFollowingPane(new File("test1.txt"));
 FileFollowingPane pane2 = new FileFollowingPane(new File("test2.txt"));
 tabbedPane.add("Test1", pane1);
 tabbedPane.add("Test2", pane2);
 tabbedPane.removeTabAt(0);
 assertNull(tabbedPane.getComponentAt(0));
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
 tabbedPane.add("Test", pane);
 tabbedPane.setSelectedComponent(pane);
 File parent = new File("testFolder");
 assertEquals(parent, attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFileNull() {
 tabbedPane.setSelectedComponent(null);
 assertNull(attributes.getLastFileChooserDirectory());
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

}