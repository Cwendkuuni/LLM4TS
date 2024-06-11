import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import java.awt.*;
import java.io.File;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private JFrame frame;
 private JTabbedPane expectedTabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 frame = new JFrame();
 expectedTabbedPane = new JTabbedPane(SwingConstants.TOP);
 frame.setJMenuBar(expectedTabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 int x = 10, y = 20;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component); // No component at given position

 JLabel label = new JLabel("Label");
 tabbedPane.addTab("Tab 1", label);
 label.setLocation(5, 5);
 label.setSize(50, 50);
 component = tabbedPane.findComponentAt(x, y);
 assertNull(component); // Label not in the way

 label.setLocation(0, 0);
 component = tabbedPane.findComponentAt(x, y);
 assertNotNull(component); // Label is now under the point
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.addTab("Tab 3", new JLabel("Tab 3"));

 tabbedPane.setSelectedIndex(1);
 assertEquals("Tab 2", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
 }

 @Test
 public void testSetSelectedComponent() {
 JLabel label1 = new JLabel("Tab 1");
 JLabel label2 = new JLabel("Tab 2");
 tabbedPane.addTab("Tab 1", label1);
 tabbedPane.addTab("Tab 2", label2);

 FileFollowingPane fileFollowingPane = new FileFollowingPane();
 tabbedPane.setSelectedComponent(fileFollowingPane);
 tabbedPane.setSelectedComponent(label2);

 assertEquals(label2, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.addTab("Tab 3", new JLabel("Tab 3"));

 tabbedPane.removeTabAt(1);
 assertFalse(tabbedPane.containsComponent("Tab 2"));
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane1 = new FileFollowingPane();
 tabbedPane.addTab("Tab 1", pane1);
 File file = new File("testFile.txt");
 pane1.setFollowedFile(file);
 tabbedPane.handleSelectedFile();
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_NoFile() {
 tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
 tabbedPane.handleSelectedFile();
 assertNull(attributes.getLastFileChooserDirectory());
 }
}