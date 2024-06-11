package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import javax.swing.JTabbedPane;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import ghm.follow.config.FollowAppAttributes;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.io.File;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstructor() {
 assertEquals(SwingConstants.TOP, tabbedPane.getTabPlacement());
 assertNull(tabbedPane.attributes);
 }

 @Test
 public void testTabbedPaneSetAttributes() {
 tabbedPane.setAttributes(attributes);
 assertSame(attributes, tabbedPane.attributes);
 }

 @Test
 public void testFindComponentAt() {
 final JLabel label = new JLabel("Label");
 tabbedPane.addTab("Label", label);
 tabbedPane.setSelectedComponent(label);

 final Point point = new Point(5, 5);
 final Component component = tabbedPane.findComponentAt(point.x, point.y);
 assertSame(label, component);
 }

 @Test
 public void testFindComponentAt_Outside() {
 final JLabel label = new JLabel("Label");
 tabbedPane.addTab("Label", label);

 final Point point = new Point(-5, -5);
 final Component component = tabbedPane.findComponentAt(point.x, point.y);
 assertNull(component);
 }

 @Test
 public void testFindComponentAt_NullComponent() {
 tabbedPane.addTab("Label", null);

 final Point point = new Point(5, 5);
 final Component component = tabbedPane.findComponentAt(point.x, point.y);
 assertNull(component);
 }

 @Test
 public void testFindComponentAt_ContainerComponent() {
 final JPanel panel = new JPanel();
 final JLabel label = new JLabel("Label");
 tabbedPane.addTab("Label", panel);
 tabbedPane.add(panel, "Panel");
 tabbedPane.setSelectedComponent(panel);
 tabbedPane.setTabComponentAt(0, new JLabel("New Label"));

 final Point point = new Point(5, 5);
 final Component component = tabbedPane.findComponentAt(point.x, point.y);
 assertSame(label, component);
 }

 @Test
 public void testFindComponentAt_ComponentInsideContainer() {
 final JPanel panel = new JPanel();
 final JLabel label = new JLabel("Label");
 tabbedPane.addTab("Label", panel);
 tabbedPane.add(panel, "Panel");
 tabbedPane.setSelectedComponent(panel);
 tabbedPane.setTabComponentAt(0, new JLabel("New Label"));

 final Component innerComponent = new JLabel("Inner Label");
 panel.add(innerComponent);

 final Point point = new Point(5, 5);
 final Component component = tabbedPane.findComponentAt(point.x, point.y);
 assertSame(innerComponent, component);
 }

 @Test
 public void testSetSelectedIndex() {
 final JLabel label1 = new JLabel("Label 1");
 final JLabel label2 = new JLabel("Label 2");
 tabbedPane.addTab("Label 1", label1);
 tabbedPane.addTab("Label 2", label2);
 tabbedPane.setSelectedIndex(0);

 final Component component = tabbedPane.getSelectedComponent();
 assertSame(label1, component);
 }

 @Test
 public void testSetSelectedComponent() {
 final JLabel label1 = new JLabel("Label 1");
 final JLabel label2 = new JLabel("Label 2");
 tabbedPane.addTab("Label 1", label1);
 tabbedPane.addTab("Label 2", label2);
 tabbedPane.setSelectedComponent(label2);

 final Component component = tabbedPane.getSelectedComponent();
 assertSame(label2, component);
 }

 @Test
 public void testRemoveTabAt() {
 final JLabel label1 = new JLabel("Label 1");
 final JLabel label2 = new JLabel("Label 2");
 tabbedPane.addTab("Label 1", label1);
 tabbedPane.addTab("Label 2", label2);
 tabbedPane.setSelectedComponent(label1);
 tabbedPane.removeTabAt(0);

 final Component component = tabbedPane.getSelectedComponent();
 assertSame(label2, component);
 }

 @Test
 public void testSetSelectedFile() {
 final File file = new File("test.txt");
 final FileFollowingPane pane = new FileFollowingPane(file);
 tabbedPane.addTab(file.getName(), pane);
 tabbedPane.setSelectedFile(file);

 final File lastDirectory = attributes.getLastFileChooserDirectory();
 assertEquals(file.getParentFile(), lastDirectory);
 }
}