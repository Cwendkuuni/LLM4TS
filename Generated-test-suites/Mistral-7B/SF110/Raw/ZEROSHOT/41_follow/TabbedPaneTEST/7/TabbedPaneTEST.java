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
 assertNotNull(tabbedPane);
 assertEquals(SwingConstants.TOP, tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAt() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

 JPanel panel1 = new JPanel();
 JLabel label1 = new JLabel("Label 1");
 panel1.add(label1);
 JButton button1 = new JButton("Button 1");
 panel1.add(button1);

 JPanel panel2 = new JPanel();
 JLabel label2 = new JLabel("Label 2");
 panel2.add(label2);
 JButton button2 = new JButton("Button 2");
 panel2.add(button2);

 tabbedPane.addTab("Tab 1", panel1);
 tabbedPane.addTab("Tab 2", panel2);
 frame.getContentPane().add(tabbedPane);
 frame.pack();
 frame.setSize(300, 200);
 frame.setVisible(true);

 int x = 50, y = 50;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);

 x = 50;
 y = 50;
 tabbedPane.setSelectedIndex(0);
 component = tabbedPane.findComponentAt(x, y);
 assertNotNull(component);
 assertSame(panel1, component);

 x = 100;
 y = 50;
 tabbedPane.setSelectedIndex(1);
 component = tabbedPane.findComponentAt(x, y);
 assertNotNull(component);
 assertSame(button2, component);
 }

 @Test
 public void testSetSelectedIndex() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

 JPanel panel1 = new JPanel();
 JLabel label1 = new JLabel("Label 1");
 panel1.add(label1);
 JButton button1 = new JButton("Button 1");
 panel1.add(button1);

 JPanel panel2 = new JPanel();
 JLabel label2 = new JLabel("Label 2");
 panel2.add(label2);
 JButton button2 = new JButton("Button 2");
 panel2.add(button2);

 tabbedPane.addTab("Tab 1", panel1);
 tabbedPane.addTab("Tab 2", panel2);
 frame.getContentPane().add(tabbedPane);
 frame.pack();
 frame.setSize(300, 200);
 frame.setVisible(true);

 tabbedPane.setSelectedIndex(0);
 assertEquals("Tab 1", tabbedPane.getTitleAt(0));

 tabbedPane.setSelectedIndex(1);
 assertEquals("Tab 2", tabbedPane.getTitleAt(1));
 }

 @Test
 public void testSetSelectedComponent() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

 JPanel panel1 = new JPanel();
 JLabel label1 = new JLabel("Label 1");
 panel1.add(label1);
 JButton button1 = new JButton("Button 1");
 panel1.add(button1);

 JPanel panel2 = new JPanel();
 JLabel label2 = new JLabel("Label 2");
 panel2.add(label2);
 JButton button2 = new JButton("Button 2");
 panel2.add(button2);

 tabbedPane.addTab("Tab 1", panel1);
 tabbedPane.addTab("Tab 2", panel2);
 frame.getContentPane().add(tabbedPane);
 frame.pack();
 frame.setSize(300, 200);
 frame.setVisible(true);

 File file1 = new File("file1.txt");
 FileFollowingPane fileFollowingPane1 = new FileFollowingPane(attributes, file1);
 tabbedPane.add(fileFollowingPane1);
 tabbedPane.setSelectedComponent(fileFollowingPane1);
 assertEquals(fileFollowingPane1, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

 JPanel panel1 = new JPanel();
 JLabel label1 = new JLabel("Label 1");
 panel1.add(label1);
 JButton button1 = new JButton("Button 1");
 panel1.add(button1);

 JPanel panel2 = new JPanel();
 JLabel label2 = new JLabel("Label 2");
 panel2.add(label2);
 JButton button2 = new JButton("Button 2");
 panel2.add(button2);

 tabbedPane.addTab("Tab 1", panel1);
 tabbedPane.addTab("Tab 2", panel2);
 frame.getContentPane().add(tabbedPane);
 frame.pack();
 frame.setSize(300, 200);
 frame.setVisible(true);

 tabbedPane.setSelectedIndex(0);
 FileFollowingPane fileFollowingPane1 = (FileFollowingPane)tabbedPane.getSelectedComponent();
 tabbedPane.removeTabAt(0);
 assertFalse(tabbedPane.containsComponent(fileFollowingPane1));
 }

 @Test
 public void testHandleSelectedFile() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

 JPanel panel1 = new JPanel();
 JLabel label1 = new JLabel("Label 1");
 panel1.add(label1);
 JButton button1 = new JButton("Button 1");
 panel1.add(button1);

 JPanel panel2 = new JPanel();
 JLabel label2 = new JLabel("Label 2");
 panel2.add(label2);
 JButton button2 = new JButton("Button 2");
 panel2.add(button2);

 File file1 = new File("file1.txt");
 FileFollowingPane fileFollowingPane1 = new FileFollowingPane(attributes, file1);
 tabbedPane.addTab("Tab 1", fileFollowingPane1);
 tabbedPane.addTab("Tab 2", panel2);
 frame.getContentPane().add(tabbedPane);
 frame.pack();
 frame.setSize(300, 200);
 frame.setVisible(true);

 tabbedPane.setSelectedIndex(0);
 assertEquals(file1.getParentFile(), attributes.getLastFileChooserDirectory());

 tabbedPane.setSelectedIndex(1);
 assertNull(attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testSetAndGetLastFileChooserDirectory() {
 File file = new File("test.txt");
 File parentDirectory = file.getParentFile();
 attributes.setLastFileChooserDirectory(parentDirectory);
 assertEquals(parentDirectory, attributes.getLastFileChooserDirectory());
 }
}