package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstruction() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() {
 JLabel label = new JLabel("Test Label");
 tabbedPane.add("Test Tab", label);
 tabbedPane.setBounds(0, 0, 200, 200);

 int x = 50;
 int y = 50;

 Component component = tabbedPane.findComponentAt(x, y);
 assertEquals(label, component);
 }

 @Test
 public void testSetSelectedIndex() {
 JLabel label1 = new JLabel("Tab 1");
 JLabel label2 = new JLabel("Tab 2");
 tabbedPane.add("Tab 1", label1);
 tabbedPane.add("Tab 2", label2);
 tabbedPane.setSelectedIndex(0);

 Component component1 = tabbedPane.getComponentAt(0);
 assertEquals(label1, component1);

 tabbedPane.setSelectedIndex(1);
 Component component2 = tabbedPane.getComponentAt(0);
 assertEquals(label2, component2);
 }

 @Test
 public void testSetSelectedComponent() {
 JLabel label1 = new JLabel("Tab 1");
 JLabel label2 = new JLabel("Tab 2");
 tabbedPane.add("Tab 1", label1);
 tabbedPane.add("Tab 2", label2);

 JComponent selectedComponent = tabbedPane.getComponentAt(0);
 tabbedPane.setSelectedComponent(selectedComponent);

 Component component1 = tabbedPane.getComponentAt(0);
 assertEquals(label1, component1);

 JComponent component2 = tabbedPane.getComponentAt(1);
 tabbedPane.setSelectedComponent(component2);

 Component component3 = tabbedPane.getComponentAt(0);
 assertEquals(label2, component3);
 }

 @Test
 public void testRemoveTabAt() {
 JLabel label1 = new JLabel("Tab 1");
 JLabel label2 = new JLabel("Tab 2");
 tabbedPane.add("Tab 1", label1);
 tabbedPane.add("Tab 2", label2);

 int index = tabbedPane.getTabCount() - 1;
 tabbedPane.removeTabAt(index);

 assertFalse(tabbedPane.containsComponent(label2));
 }

 @Test
 public void testHandleSelectedFile() throws IOException {
 File file = new File("testFile.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(file);
 tabbedPane.add("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);

 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @After
 public void tearDown() {
 tabbedPane = null;
 attributes = null;
 }
}