package ghm.follow.gui;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.*;
import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private FollowAppAttributes attributes;

 @Before
 public void setUp() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 }

 @Test
 public void testTabbedPaneConstructor() {
 assertEquals(SwingConstants.TOP, tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAtNull() {
 int x = 0;
 int y = 0;
 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAt() throws Exception {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(500, 500);
 frame.setVisible(true);

 JLabel label = new JLabel("Label");
 JPanel panel = new JPanel();
 panel.add(label);

 tabbedPane.addTab("Tab1", label);
 tabbedPane.addTab("Tab2", panel);
 tabbedPane.setBounds(0, 0, 500, 500);
 frame.add(tabbedPane);

 int x = 50;
 int y = 50;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNotNull(component);

 int tab1X = tabbedPane.getComponentAt(0).getX();
 int tab1Y = tabbedPane.getComponentAt(0).getY();
 int labelX = tabbedPane.getComponentAt(0).getComponentAt(0).getX();
 int labelY = tabbedPane.getComponentAt(0).getComponentAt(0).getY();

 assertEquals(x - tab1X, labelX);
 assertEquals(y - tab1Y, labelY);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane.addTab("Tab1", new JLabel("Tab1"));
 tabbedPane.addTab("Tab2", new JLabel("Tab2"));
 tabbedPane.setSelectedIndex(1);
 assertEquals(1, tabbedPane.getSelectedIndex());
 }

 @Test
 public void testSetSelectedComponentNull() {
 assertNull(tabbedPane.getSelectedComponent());
 tabbedPane.setSelectedComponent(null);
 }

 @Test
 public void testSetSelectedComponent() throws Exception {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(500, 500);
 frame.setVisible(true);

 FileFollowingPane pane1 = new FileFollowingPane(new File("File1.txt"));
 tabbedPane.addTab("Tab1", pane1);
 tabbedPane.setBounds(0, 0, 500, 500);
 frame.add(tabbedPane);

 FileFollowingPane pane2 = new FileFollowingPane(new File("File2.txt"));
 tabbedPane.addTab("Tab2", pane2);
 tabbedPane.setSelectedComponent(pane2);
 assertEquals(pane2, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane.addTab("Tab1", new JLabel("Tab1"));
 tabbedPane.addTab("Tab2", new JLabel("Tab2"));
 tabbedPane.removeTabAt(0);
 assertNull(tabbedPane.getComponentAt(0));
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane pane = new FileFollowingPane(new File("File1.txt"));
 tabbedPane.addTab("Tab1", pane);
 tabbedPane.setSelectedComponent(pane);
 File parent = pane.getFollowedFile().getParentFile();
 assertEquals(parent, attributes.getLastFileChooserDirectory());
 }
}