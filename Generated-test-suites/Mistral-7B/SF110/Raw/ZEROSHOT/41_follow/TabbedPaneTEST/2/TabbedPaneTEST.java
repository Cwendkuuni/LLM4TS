package ghm.follow.gui;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;
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
 public void testTabbedPaneWithValidAttributes() {
 FollowAppAttributes attributes = new FollowAppAttributes();
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt_WhenCalledWithValidCoordinates_ReturnsComponent() {
 JFrame frame = new JFrame();
 JLabel label = new JLabel("Test Label");
 JPanel panel = new JPanel();
 panel.add(label);
 tabbedPane.addTab("Test Tab", panel);
 frame.add(tabbedPane);
 frame.pack();
 frame.setVisible(true);

 int x = 50;
 int y = 50;
 Component component = tabbedPane.findComponentAt(x, y);
 assertEquals(label, component);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAt_WhenCalledWithInvalidCoordinates_ThrowsException() {
 int x = 50;
 int y = 50;
 tabbedPane.findComponentAt(x, y);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane.addTab("Test Tab 1", new JPanel());
 tabbedPane.addTab("Test Tab 2", new JPanel());
 tabbedPane.setSelectedIndex(1);
 assertEquals("Test Tab 2", tabbedPane.getTitleAt(1));
 }

 @Test
 public void testSetSelectedComponent() {
 JPanel tab1 = new JPanel();
 JPanel tab2 = new JPanel();
 tabbedPane.addTab("Test Tab 1", tab1);
 tabbedPane.addTab("Test Tab 2", tab2);
 tabbedPane.setSelectedComponent(tab2);
 assertEquals(tab2, tabbedPane.getComponentAt(1));
 }

 @Test
 public void testRemoveTabAt() {
 JPanel tab1 = new JPanel();
 JPanel tab2 = new JPanel();
 tabbedPane.addTab("Test Tab 1", tab1);
 tabbedPane.addTab("Test Tab 2", tab2);
 tabbedPane.removeTabAt(1);
 assertEquals(1, tabbedPane.getComponentCount());
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(null);
 tabbedPane.add(fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane.getFollowedFile().getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
 }

 @After
 public void tearDown() {
 tabbedPane = null;
 attributes = null;
 }

}