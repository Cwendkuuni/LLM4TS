package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private JFrame frame;
 private JTabbedPane expectedTabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() throws Exception {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 frame = new JFrame();
 expectedTabbedPane = new JTabbedPane(SwingConstants.TOP);
 }

 @Test
 public void testTabbedPaneConstructor() {
 assertNotNull(tabbedPane);
 assertEquals(SwingConstants.TOP, tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAt() {
 final Component component = new JLabel("Test Component");
 tabbedPane.addTab("Test Tab", component);
 final int x = 0;
 final int y = 0;
 final Component result = tabbedPane.findComponentAt(x, y);
 assertSame(component, result);
 }

 @Test
 public void testFindComponentAt_OutOfBounds() {
 final int x = 10;
 final int y = 10;
 final Component result = tabbedPane.findComponentAt(x, y);
 assertNull(result);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.setSelectedIndex(1);
 final Component selectedComponent = tabbedPane.getComponentAt(1);
 assertSame(selectedComponent, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent() {
 tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 final JLabel tab2 = (JLabel) tabbedPane.getComponentAt(1);
 tabbedPane.setSelectedComponent(tab2);
 final Component selectedComponent = tabbedPane.getComponentAt(1);
 assertSame(selectedComponent, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.addTab("Tab 1", new JLabel("Tab 1"));
 tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));
 tabbedPane.removeTabAt(0);
 assertEquals(1, tabbedPane.getTabCount());
 }

 @Test
 public void testHandleSelectedFile() throws IOException {
 final File file = new File("test.txt");
 final FileFollowingPane fileFollowingPane = new FileFollowingPane(file);
 tabbedPane.addTab("Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(file.getParentFile().getAbsolutePath(), attributes.getLastFileChooserDirectory());
 }

 @After
 public void tearDown() throws Exception {
 frame.dispose();
 }
}