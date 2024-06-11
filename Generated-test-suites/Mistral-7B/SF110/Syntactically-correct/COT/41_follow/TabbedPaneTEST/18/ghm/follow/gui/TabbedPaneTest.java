package ghm.follow.gui;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private JFrame frame;
 private FollowAppAttributes attributes;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.getContentPane().add(tabbedPane);
 frame.pack();
 frame.setVisible(true);
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 // Test constructor
 @Test
 public void testTabbedPaneConstructor() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 // Test findComponentAt method
 @Test
 public void testFindComponentAt() {
 Point point = new Point(10, 10);
 Component component = tabbedPane.findComponentAt(point.x, point.y);
 assertNull(component);

 JLabel label = new JLabel("Label");
 tabbedPane.addTab("Tab", new JPanel(), label);
 tabbedPane.setSelectedIndex(0);
 point.translate(label.getX(), label.getY());
 component = tabbedPane.findComponentAt(point.x, point.y);
 assertNotNull(component);
 assertEquals(label, component);
 }

 // Test setSelectedIndex method
 @Test
 public void testSetSelectedIndex() {
 JLabel label1 = new JLabel("Label 1");
 JLabel label2 = new JLabel("Label 2");
 tabbedPane.addTab("Tab 1", new JPanel(), label1);
 tabbedPane.addTab("Tab 2", new JPanel(), label2);
 tabbedPane.setSelectedIndex(0);
 assertEquals(label1, tabbedPane.getComponentAt(0));
 tabbedPane.setSelectedIndex(1);
 assertEquals(label2, tabbedPane.getComponentAt(0));
 }

 // Test setSelectedComponent method
 @Test
 public void testSetSelectedComponent() {
 JLabel label1 = new JLabel("Label 1");
 JLabel label2 = new JLabel("Label 2");
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 tabbedPane.addTab("Tab 1", panel1, label1);
 tabbedPane.addTab("Tab 2", panel2, label2);
 tabbedPane.setSelectedComponent(panel1);
 assertEquals(label1, tabbedPane.getComponentAt(0).getComponentAt(0));
 tabbedPane.setSelectedComponent(panel2);
 assertEquals(label2, tabbedPane.getComponentAt(1).getComponentAt(0));
 }

 // Test removeTabAt method
 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveTabAtNegativeIndex() {
 tabbedPane.removeTabAt(-1);
 fail("Expected IndexOutOfBoundsException");
 }

 @Test
 public void testRemoveTabAtValidIndex() {
 JLabel label1 = new JLabel("Label 1");
 JLabel label2 = new JLabel("Label 2");
 tabbedPane.addTab("Tab 1", new JPanel(), label1);
 tabbedPane.addTab("Tab 2", new JPanel(), label2);
 tabbedPane.setSelectedIndex(0);
 assertEquals(label1, tabbedPane.getComponentAt(0).getComponentAt(0));
 tabbedPane.removeTabAt(0);
 assertFalse(tabbedPane.getComponentCount() > 1);
 }

 // Test handleSelectedFile method
 @Test
 public void testHandleSelectedFile() {
 File file = new File("file.txt");
 FileFollowingPane pane = new FileFollowingPane(file);
 tabbedPane.addTab("Tab", new JPanel(), pane);
 tabbedPane.setSelectedComponent(pane);
 assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 // Test handleSelectedFile with null pane
 @Test
 public void testHandleSelectedFileNullPane() {
 tabbedPane.setSelectedComponent(null);
 assertEquals(null, attributes.getLastFileChooserDirectory());
 }
}