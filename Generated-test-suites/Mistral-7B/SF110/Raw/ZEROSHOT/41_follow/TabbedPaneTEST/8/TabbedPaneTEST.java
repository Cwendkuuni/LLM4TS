package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneCreation() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 Point point = new Point(10, 10);
 Component component = new JLabel("Test Component");
 JPanel panel = new JPanel();
 panel.add(component);
 tabbedPane.add("Test Tab", panel);
 tabbedPane.setSelectedComponent(panel);
 assertEquals(component, tabbedPane.findComponentAt(point.x, point.y));
 }

 @Test
 public void testSetSelectedIndex() {
 int index = 0;
 tabbedPane.add("Tab 1", new JLabel("Tab 1"));
 tabbedPane.add("Tab 2", new JLabel("Tab 2"));
 tabbedPane.setSelectedIndex(index);
 assertEquals("Tab 1", tabbedPane.getTitleAt(index));
 }

 @Test
 public void testSetSelectedComponent() {
 JLabel label1 = new JLabel("Label 1");
 JLabel label2 = new JLabel("Label 2");
 tabbedPane.add("Tab 1", label1);
 tabbedPane.add("Tab 2", label2);
 tabbedPane.setSelectedComponent(label1);
 assertEquals(label1, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() throws IOException {
 File file = new File("testFile.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(file, null);
 tabbedPane.add("Tab 1", fileFollowingPane);
 tabbedPane.removeTabAt(0);
 assertEquals(0, tabbedPane.getComponentCount());
 assertNull(tabbedPane.getSelectedComponent());
}

 @Test
 public void testHandleSelectedFile() {
 File file = new File("testFile.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(file, null);
 tabbedPane.add("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @After
 public void tearDown() {
 tabbedPane = null;
 attributes = null;
 }
}