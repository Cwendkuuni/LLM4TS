package ghm.follow.gui;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;

 @Test
 public void testTabbedPaneConstruction() throws Exception {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
 }

 @Test
 public void testFindComponentAt() throws Exception {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(500, 500);
 frame.setVisible(true);

 JLabel label = new JLabel("Test Label");
 JPanel panel = new JPanel();
 panel.add(label);

 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab 1", label);
 tabbedPane.addTab("Test Tab 2", panel);
 tabbedPane.setSize(500, 500);
 tabbedPane.setLocation(0, 0);
 frame.getContentPane().add(tabbedPane);

 int x = 10, y = 10;
 Component expectedComponent = label;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals(expectedComponent, actualComponent);
 }

 @Test
 public void testSetSelectedIndex() throws Exception {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(500, 500);
 frame.setVisible(true);

 JLabel label1 = new JLabel("Test Label 1");
 JLabel label2 = new JLabel("Test Label 2");
 JPanel panel1 = new JPanel();
 panel1.add(label1);
 JPanel panel2 = new JPanel();
 panel2.add(label2);

 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab 1", label1);
 tabbedPane.addTab("Test Tab 2", label2);
 tabbedPane.addTab("Test Tab 3", panel1);
 tabbedPane.addTab("Test Tab 4", panel2);
 tabbedPane.setSize(500, 500);
 tabbedPane.setLocation(0, 0);
 frame.getContentPane().add(tabbedPane);

 tabbedPane.setSelectedIndex(1);
 tabbedPane.handleSelectedFile();
 assertEquals("Test Tab 2", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
 }

 @Test
 public void testSetSelectedComponent() throws Exception {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(500, 500);
 frame.setVisible(true);

 JLabel label1 = new JLabel("Test Label 1");
 JLabel label2 = new JLabel("Test Label 2");
 JPanel panel1 = new JPanel();
 panel1.add(label1);
 JPanel panel2 = new JPanel();
 panel2.add(label2);

 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab 1", label1);
 tabbedPane.addTab("Test Tab 2", label2);
 tabbedPane.addTab("Test Tab 3", panel1);
 tabbedPane.addTab("Test Tab 4", panel2);
 tabbedPane.setSize(500, 500);
 tabbedPane.setLocation(0, 0);
 frame.getContentPane().add(tabbedPane);

 FileFollowingPane pane1 = (FileFollowingPane) tabbedPane.getComponentAt(1);
 FileFollowingPane pane2 = (FileFollowingPane) tabbedPane.getComponentAt(2);
 tabbedPane.setSelectedComponent(pane2);
 tabbedPane.handleSelectedFile();
 assertEquals(pane2.getFollowedFile().getParent(), tabbedPane.getAttributes().getLastFileChooserDirectory());
 }

 @Test
 public void testRemoveTabAt() throws Exception {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(500, 500);
 frame.setVisible(true);

 JLabel label1 = new JLabel("Test Label 1");
 JLabel label2 = new JLabel("Test Label 2");
 JLabel label3 = new JLabel("Test Label 3");
 JPanel panel1 = new JPanel();
 panel1.add(label1);
 JPanel panel2 = new JPanel();
 panel2.add(label2);
 JPanel panel3 = new JPanel();
 panel3.add(label3);

 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab 1", label1);
 tabbedPane.addTab("Test Tab 2", label2);
 tabbedPane.addTab("Test Tab 3", label3);
 tabbedPane.addTab("Test Tab 4", panel1);
 tabbedPane.addTab("Test Tab 5", panel2);
 tabbedPane.addTab("Test Tab 6", panel3);
 tabbedPane.setSize(500, 500);
 tabbedPane.setLocation(0, 0);
 frame.getContentPane().add(tabbedPane);

 tabbedPane.setSelectedIndex(3);
 tabbedPane.handleSelectedFile();
 tabbedPane.removeTabAt(3);
 tabbedPane.handleSelectedFile();
 assertFalse(tabbedPane.getComponentAt(3) instanceof Container);
 assertNull(tabbedPane.getComponentAt(3));
 assertEquals(panel1.getFollowedFile().getParent(), tabbedPane.getAttributes().getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFile() throws Exception {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(500, 500);
 frame.setVisible(true);

 JLabel label1 = new JLabel("Test Label 1");
 JLabel label2 = new JLabel("Test Label 2");
 JPanel panel1 = new JPanel();
 panel1.add(label1);
 JPanel panel2 = new JPanel();
 panel2.add(label2);

 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Test Tab 1", label1);
 tabbedPane.addTab("Test Tab 2", label2);
 tabbedPane.addTab("Test Tab 3", panel1);
 tabbedPane.addTab("Test Tab 4", panel2);
 tabbedPane.setSize(500, 500);
 tabbedPane.setLocation(0, 0);
 frame.getContentPane().add(tabbedPane);

 File tmpFile = File.createTempFile("test", null);
 FileFollowingPane pane1 = (FileFollowingPane) tabbedPane.getComponentAt(0);
 pane1.setFollowedFile(tmpFile);
 tabbedPane.setSelectedComponent(pane1);
 tabbedPane.handleSelectedFile();
 assertEquals(tmpFile.getParent(), tabbedPane.getAttributes().getLastFileChooserDirectory());

 tabbedPane.setSelectedComponent(panel2);
 tabbedPane.handleSelectedFile();
 assertNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
 }

 @After
 public void tearDown() throws Exception {
 if (tabbedPane != null) {
 tabbedPane.dispose();
 }
 if (attributes != null) {
 attributes.dispose();
 }
 }

}