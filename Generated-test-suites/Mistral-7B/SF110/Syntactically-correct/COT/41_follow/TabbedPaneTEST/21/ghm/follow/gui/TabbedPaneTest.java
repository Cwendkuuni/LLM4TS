package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;

import org.junit.Test;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Test
 public void testTabbedPaneConstructor() {
 tabbedPane = new TabbedPane(new FollowAppAttributes());
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

 JTabbedPane tabbedPaneComponent = (JTabbedPane) tabbedPane;
 JPanel firstTab = new JPanel();
 tabbedPaneComponent.addTab("Tab 1", firstTab);
 JPanel secondTab = new JPanel();
 tabbedPaneComponent.addTab("Tab 2", secondTab);

 frame.getContentPane().add(tabbedPaneComponent);
 frame.pack();
 frame.setVisible(true);

 int x = 100;
 int y = 100;

 Component result = tabbedPane.findComponentAt(x, y);
 assertNull(result);

 JLabel label = new JLabel();
 firstTab.add(label);
 x -= label.getX();
 y -= label.getY();

 result = tabbedPane.findComponentAt(x, y);
 assertSame(label, result);
 }

 @Test(expected = NullPointerException.class)
 public void testFindComponentAtNullAttributes() {
 tabbedPane = new TabbedPane(null);
 int x = 100;
 int y = 100;
 tabbedPane.findComponentAt(x, y);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testFindComponentAtOutOfBounds() {
 tabbedPane = new TabbedPane(attributes);
 int x = 100;
 int y = 100;
 tabbedPane.setComponentCount(0);
 tabbedPane.findComponentAt(x, y);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane = new TabbedPane(attributes);
 JTabbedPane tabbedPaneComponent = (JTabbedPane) tabbedPane;
 tabbedPaneComponent.addTab("Tab 1", new JPanel());
 tabbedPaneComponent.addTab("Tab 2", new JPanel());
 tabbedPaneComponent.setSelectedIndex(1);
 assertEquals(1, tabbedPaneComponent.getSelectedIndex());
 }

 @Test
 public void testSetSelectedComponentNull() {
 tabbedPane = new TabbedPane(attributes);
 JTabbedPane tabbedPaneComponent = (JTabbedPane) tabbedPane;
 tabbedPaneComponent.addTab("Tab 1", new JPanel());
 tabbedPaneComponent.setSelectedComponent(null);
 assertNull(tabbedPaneComponent.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponentNullFileFollowingPane() {
 tabbedPane = new TabbedPane(attributes);
 JTabbedPane tabbedPaneComponent = (JTabbedPane) tabbedPane;
 tabbedPaneComponent.addTab("Tab 1", new JPanel());
 FileFollowingPane fileFollowingPane = null;
 tabbedPaneComponent.setSelectedComponent(fileFollowingPane);
 assertNull(tabbedPaneComponent.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane = new TabbedPane(attributes);
 JTabbedPane tabbedPaneComponent = (JTabbedPane) tabbedPane;
 tabbedPaneComponent.addTab("Tab 1", new JPanel());
 tabbedPaneComponent.addTab("Tab 2", new JPanel());
 tabbedPaneComponent.removeTabAt(0);
 assertEquals(1, tabbedPaneComponent.getComponentCount());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtOutOfBounds() {
 tabbedPane = new TabbedPane(attributes);
 JTabbedPane tabbedPaneComponent = (JTabbedPane) tabbedPane;
 tabbedPaneComponent.addTab("Tab 1", new JPanel());
 tabbedPaneComponent.removeTabAt(10);
 }

 @Test
 public void testHandleSelectedFile() {
 tabbedPane = new TabbedPane(attributes);
 FileFollowingPane pane1 = new FileFollowingPane(new File("test1.txt"));
 tabbedPaneComponent.addTab("Tab 1", pane1);
 tabbedPaneComponent.setSelectedIndex(0);

 FileFollowingPane pane2 = new FileFollowingPane(new File("test2.txt"));
 tabbedPaneComponent.addTab("Tab 2", pane2);
 tabbedPaneComponent.setSelectedIndex(1);

 assertEquals(pane1.getFollowedFile().getParentFile(), tabbedPane.getAttributes().getLastFileChooserDirectory());
 tabbedPaneComponent.setSelectedIndex(0);
 assertEquals(pane2.getFollowedFile().getParentFile(), tabbedPane.getAttributes().getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFileNullComponent() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPaneComponent.addTab("Tab 1", new JPanel());
 tabbedPaneComponent.setSelectedComponent(null);
 assertNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
 }

}