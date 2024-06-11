package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import org.junit.*;

import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;

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
 frame.add(tabbedPane, BorderLayout.CENTER);
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
 int x = 10, y = 10;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);

 JTabbedPane tabbedPaneWithComponent = new JTabbedPane();
 JButton button = new JButton("Test");
 tabbedPaneWithComponent.addTab("Test Tab", button);
 tabbedPaneWithComponent.setBounds(0, 0, 200, 200);
 tabbedPaneWithComponent.setComponentAt(0, new JScrollPane(button));
 tabbedPaneWithComponent.setSelectedIndex(0);
 tabbedPaneWithComponent.setBounds(0, 0, 300, 300);
 tabbedPaneWithComponent.setLocationRelativeTo(null);

 Component testComponent = tabbedPaneWithComponent.findComponentAt(x, y);
 assertNotNull(testComponent);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtNegativeValues() {
 tabbedPane.findComponentAt(-1, -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindComponentAtOutsideTabbedPaneBounds() {
 tabbedPane.findComponentAt(tabbedPane.getWidth() + 1, tabbedPane.getHeight() + 1);
 }

 @Test
 public void testSetSelectedIndex() {
 int index = 0;
 tabbedPane.setSelectedIndex(index);
 assertTrue(tabbedPane.isSelectedIndex(index));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexNegativeIndex() {
 tabbedPane.setSelectedIndex(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexGreaterThanTabCount() {
 int tabCount = tabbedPane.getTabCount();
 tabbedPane.setSelectedIndex(tabCount + 1);
 }

 @Test
 public void testSetSelectedComponent() {
 JPanel panel = new JPanel();
 tabbedPane.addTab("Test Tab", panel);
 tabbedPane.setSelectedComponent(panel);
 assertTrue(tabbedPane.isSelectedComponent(panel));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedComponentNullComponent() {
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testRemoveTabAt() {
 int index = 0;
 JPanel panel = new JPanel();
 tabbedPane.addTab("Test Tab", panel);
 tabbedPane.removeTabAt(index);
 assertFalse(tabbedPane.isSelectedComponent(panel));
 assertFalse(tabbedPane.getTabCount() > 0);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtNegativeIndex() {
 tabbedPane.removeTabAt(-1);
 }
}