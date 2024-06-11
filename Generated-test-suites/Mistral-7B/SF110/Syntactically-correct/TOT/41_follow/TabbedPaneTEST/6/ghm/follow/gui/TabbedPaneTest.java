import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.FileFollowingPane;
import ghm.follow.gui.TabbedPane;
import javax.swing.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private JFrame frame;
 private JTabbedPane defaultTabbedPane;
 private JTabbedPane tabbedPaneWithSwingPosition;
 private JTabbedPane tabbedPaneWithFlowLayout;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 frame = new JFrame();
 defaultTabbedPane = new TabbedPane(attributes);
 tabbedPaneWithSwingPosition = new TabbedPane(attributes);
 tabbedPaneWithSwingPosition.setTabPlacement(SwingConstants.TOP);
 tabbedPaneWithFlowLayout = new TabbedPane(attributes);
 tabbedPaneWithFlowLayout.setTabLayoutPolicy(JTabbedPane.WRAP_COMPONENTS);
 }

 @Test
 public void testFindComponentAt() {
 Component componentToFind = new JLabel("Test Component");
 JPanel panelToContainComponent = new JPanel();
 panelToContainComponent.add(componentToFind);
 tabbedPane.addTab("Test Tab", panelToContainComponent);
 tabbedPane.setSelectedIndex(0);
 int x = componentToFind.getX() + panelToContainComponent.getX();
 int y = componentToFind.getY() + panelToContainComponent.getY();
 assertEquals(componentToFind, tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAt_ComponentNotVisible() {
 tabbedPane.setSelectedIndex(1);
 Component componentToFind = new JLabel("Test Component");
 JPanel panelToContainComponent = new JPanel();
 panelToContainComponent.add(componentToFind);
 tabbedPane.addTab("Test Tab", panelToContainComponent);
 int x = componentToFind.getX() + panelToContainComponent.getX();
 int y = componentToFind.getY() + panelToContainComponent.getY();
 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAt_NoComponentAtLocation() {
 int x = 0;
 int y = 0;
 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAt_OutOfBounds() {
 int x = 100;
 int y = 100;
 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 tabbedPane.setSelectedIndex(0);
 assertEquals("Test Tab 1", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testSetSelectedIndex_IndexOutOfBounds() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 tabbedPane.setSelectedIndex(2);
 assertFalse(tabbedPane.isSelectedComponent(tabbedPane.getComponentAt(0)));
 }

 @Test
 public void testSetSelectedComponent() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("Test.txt"));
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent_NullComponent() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 tabbedPane.setSelectedComponent(null);
 assertNull(tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 tabbedPane.removeTabAt(0);
 assertFalse(tabbedPane.getComponentCount() > 1);
 }

 @Test
 public void testRemoveTabAt_IndexOutOfBounds() {
 tabbedPane.addTab("Test Tab 1", new JLabel("Test Component 1"));
 tabbedPane.addTab("Test Tab 2", new JLabel("Test Component 2"));
 tabbedPane.removeTabAt(2);
 assertFalse(tabbedPane.getComponentCount() > 1);
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("Test.txt"));
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_NoSelectedComponent() {
 tabbedPaneWithSwingPosition.setSelectedComponent(null);
 assertNull(attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile_NoFileFollowingPane() {
 JLabel label = new JLabel("Test Component");
 tabbedPaneWithFlowLayout.setSelectedComponent(label);
 assertNull(attributes.getLastFileChooserDirectory());
 }
}