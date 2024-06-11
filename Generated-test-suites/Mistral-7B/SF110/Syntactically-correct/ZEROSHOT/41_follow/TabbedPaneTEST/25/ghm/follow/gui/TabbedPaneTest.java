package ghm.follow.gui;

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

 @Test
 public void testTabbedPaneConstruction() {
 tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAtNullResult() {
 int x = 0, y = 0;
 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAtComponentResult() {
 JButton button = new JButton();
 tabbedPane.add(button, "Test Button");
 int x = button.getX() + button.getWidth() / 2;
 int y = button.getY() + button.getHeight() / 2;
 Component result = tabbedPane.findComponentAt(x, y);
 assertSame(button, result);
 }

 @Test
 public void testSetSelectedIndex() {
 JPanel panel = new JPanel();
 tabbedPane.add(panel, "Test Panel");
 tabbedPane.setSelectedIndex(0);
 assertSame(panel, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent() {
 JPanel panel = new JPanel();
 tabbedPane.add(panel, "Test Panel");
 tabbedPane.setSelectedComponent(panel);
 assertSame(panel, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 JPanel panel = new JPanel();
 tabbedPane.add(panel, "Test Panel");
 tabbedPane.removeTabAt(0);
 assertNull(tabbedPane.getComponentAt(0));
 }

 @Test
 public void testHandleSelectedFile() {
 File file = new File("test.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(file);
 tabbedPane.add(fileFollowingPane, "Test File");
 tabbedPane.setSelectedComponent(fileFollowingPane);
 File expectedDirectory = new File("."); // Working directory
 File actualDirectory = attributes.getLastFileChooserDirectory();
 assertEquals(expectedDirectory, actualDirectory);
 }

}