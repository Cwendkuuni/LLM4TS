import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import javax.swing.*;

public class TabbedPaneTest {

 private FollowAppAttributes attributes = new FollowAppAttributes();
 private TabbedPane tabbedPane;

 @Test
 public void testFindComponentAt() {
 JFrame frame = new JFrame();
 JPanel panel = new JPanel();
 frame.getContentPane().add(panel);
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab1", panel);

 int x = 50, y = 50;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);

 x += 100;
 y += 100;
 panel.add(new JLabel("Label"));
 component = tabbedPane.findComponentAt(x, y);
 assertNotNull(component);
 }

 @Test
 public void testSetSelectedIndex() {
 JFrame frame = new JFrame();
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 frame.getContentPane().add(tabbedPane = new TabbedPane(attributes));
 tabbedPane.addTab("Tab1", panel1);
 tabbedPane.addTab("Tab2", panel2);

 tabbedPane.setSelectedIndex(0);
 assertEquals("Tab1", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testSetSelectedComponentNull() {
 JFrame frame = new JFrame();
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab1", new JPanel());
 tabbedPane.setSelectedComponent(null);
 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponentInvalidComponent() {
 JFrame frame = new JFrame();
 JPanel panel1 = new JPanel();
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab1", panel1);
 JButton button = new JButton();
 tabbedPane.setSelectedComponent(button);
 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponentValidComponent() {
 JFrame frame = new JFrame();
 JPanel panel1 = new JPanel();
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab1", panel1);
 tabbedPane.setSelectedComponent(panel1);
 assertEquals(panel1, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 JFrame frame = new JFrame();
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab1", panel1);
 tabbedPane.addTab("Tab2", panel2);
 tabbedPane.removeTabAt(1);
 assertFalse(tabbedPane.getComponentCount() > 1);
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab1", pane);
 tabbedPane.setSelectedComponent(pane);
 assertEquals(new File("test"), attributes.getLastFileChooserDirectory());
 }
}