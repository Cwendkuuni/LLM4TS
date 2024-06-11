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
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

 JLabel label = new JLabel("Test Label");
 JPanel panel = new JPanel();
 panel.add(label);

 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab", panel);
 frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
 frame.pack();
 frame.setSize(200, 200);
 frame.setVisible(true);

 int x = 50, y = 50;
 Component expectedComponent = label;
 Component result = tabbedPane.findComponentAt(x, y);

 assertTrue("Component found at expected location", result == expectedComponent);
 frame.dispose();
 }

 @Test
 public void testFindComponentAt_OutOfBounds() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab", new JLabel("Test Label"));

 int x = 200, y = 200;
 Component result = tabbedPane.findComponentAt(x, y);

 assertNull("No component found at out-of-bounds location", result);
 }

 @Test
 public void testFindComponentAt_ComponentNotVisible() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));
 tabbedPane.setSelectedIndex(0);

 int x = 50, y = 50;
 Component expectedComponent = null;
 Component result = tabbedPane.findComponentAt(x, y);

 assertNull("No component found when tab is not visible", result);
 tabbedPane.setSelectedIndex(1);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));

 int initialIndex = tabbedPane.getSelectedIndex();
 tabbedPane.setSelectedIndex(1);
 int indexAfterSet = tabbedPane.getSelectedIndex();

 assertEquals("Selected index is changed correctly", indexAfterSet, 1);
 }

 @Test
 public void testSetSelectedIndex_OutOfBounds() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));

 int initialIndex = tabbedPane.getSelectedIndex();
 tabbedPane.setSelectedIndex(2);
 int indexAfterSet = tabbedPane.getSelectedIndex();

 assertEquals("Selected index remains the same when out of bounds", initialIndex, indexAfterSet);
 }

 @Test
 public void testSetSelectedComponent() {
 tabbedPane = new TabbedPane(attributes);
 JLabel label1 = new JLabel("Test Label 1");
 JLabel label2 = new JLabel("Test Label 2");
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 panel1.add(label1);
 panel2.add(label2);
 tabbedPane.addTab("Test Tab 1", panel1);
 tabbedPane.addTab("Test Tab 2", panel2);

 FileFollowingPane pane1 = (FileFollowingPane) tabbedPane.getComponentAt(0);
 FileFollowingPane pane2 = (FileFollowingPane) tabbedPane.getComponentAt(1);

 tabbedPane.setSelectedComponent(pane2);
 File followedFile2 = pane2.getFollowedFile();

 tabbedPane.setSelectedComponent(pane1);
 File followedFile1 = pane1.getFollowedFile();

 assertEquals("Last file chooser directory is updated correctly", followedFile2.getParentFile(), attributes.getLastFileChooserDirectory());
 assertEquals("Last file chooser directory is updated correctly", followedFile1.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testSetSelectedComponent_NullComponent() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));

 tabbedPane.setSelectedComponent(null);
 assertEquals("Selected component remains the same when null is passed", tabbedPane.getSelectedComponent(), tabbedPane.getComponentAt(0));
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));

 FileFollowingPane pane1 = (FileFollowingPane) tabbedPane.getComponentAt(0);
 tabbedPane.removeTabAt(0);
 FileFollowingPane pane2 = (FileFollowingPane) tabbedPane.getComponentAt(0);

 assertNotEquals("Selected component changes when tab is removed", pane1, pane2);
 }

 @Test
 public void testRemoveTabAt_IndexOutOfBounds() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Label 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Label 2"));

 tabbedPane.removeTabAt(2);
 assertEquals("Number of tabs remains unchanged when index is out of bounds", tabbedPane.getComponentCount(), 2);
 }
}