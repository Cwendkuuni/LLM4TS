package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import org.junit.*;
import ghm.follow.config.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstruction() {
 tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 Point point = new Point(10, 10);
 Component component = new JLabel("Label");
 Container container = new JPanel();
 container.add(component);
 tabbedPane.addTab("Tab1", container);
 tabbedPane.setSelectedIndex(0);

 assertNull(tabbedPane.findComponentAt(point.x, point.y));

 container.add(new JLabel("Label2"));
 point.translate(5, 5);
 component = tabbedPane.findComponentAt(point.x, point.y);
 assertNotNull(component);
 assertEquals(container, component.getParent());
 }

 @Test
 public void testSetSelectedIndex() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Tab1", fileFollowingPane);
 tabbedPane.setSelectedIndex(0);
 tabbedPane.setSelectedIndex(1);
 tabbedPane.addTab("Tab2", new JLabel("Label2"));
 tabbedPane.setSelectedIndex(0);

 assertEquals(fileFollowingPane, tabbedPane.getComponentAt(0));
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Tab1", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 tabbedPane.addTab("Tab2", new JLabel("Label2"));
 tabbedPane.setSelectedComponent(fileFollowingPane);

 assertEquals(fileFollowingPane, tabbedPane.getComponentAt(0));
 }

 @Test
 public void testRemoveTabAt() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Tab1", fileFollowingPane);
 tabbedPane.removeTabAt(0);
 tabbedPane.addTab("Tab2", new JLabel("Label2"));

 assertFalse(tabbedPane.containsComponent(fileFollowingPane));
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Tab1", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);

 File parent = new File("testDirectory");
 attributes.setLastFileChooserDirectory(parent);

 tabbedPane.handleSelectedFile();

 assertEquals(parent, attributes.getLastFileChooserDirectory());
 }

 @After
 public void tearDown() {
 tabbedPane = null;
 attributes = null;
 }
}