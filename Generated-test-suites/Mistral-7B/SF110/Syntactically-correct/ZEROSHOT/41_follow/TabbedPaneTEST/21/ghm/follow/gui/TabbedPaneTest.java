package ghm.follow.gui;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private JFrame frame;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(tabbedPane);
 frame.pack();
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testTabbedPaneConstructor() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 Component component = new JLabel("Test Component");
 tabbedPane.add("Test Tab", component);
 int x = component.getX();
 int y = component.getY();
 Component foundComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(component, foundComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 JTabbedPane tabbedPane = (JTabbedPane)this.tabbedPane;
 int index = 0;
 tabbedPane.setSelectedIndex(index);
 FileFollowingPane pane = (FileFollowingPane)tabbedPane.getComponentAt(index);
 FileFollowingPane expectedPane = (FileFollowingPane)this.tabbedPane.getComponentAt(index);
 assertEquals(expectedPane, pane);
 }

 @Test
 public void testSetSelectedComponent() {
 FileFollowingPane pane = new FileFollowingPane();
 tabbedPane.setSelectedComponent(pane);
 FileFollowingPane expectedPane = (FileFollowingPane)this.tabbedPane.getSelectedComponent();
 assertEquals(expectedPane, pane);
 }

 @Test
 public void testRemoveTabAt() {
 int index = 0;
 Component component = tabbedPane.getComponentAt(index);
 tabbedPane.removeTabAt(index);
 assertFalse(tabbedPane.contains(component));
 }

 @Test
 public void testHandleSelectedFile() {
 File file = new File("test.txt");
 FileFollowingPane pane = new FileFollowingPane(file);
 tabbedPane.add("Test Tab", pane);
 tabbedPane.setSelectedComponent(pane);
 assertEquals(pane.getFollowedFile().getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
 }

}