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
 public void testTabbedPaneConstructor() {
 // Test if TabbedPane constructor calls super with the correct argument
 assertSame(tabbedPane.getTabPlacement(), attributes.getTabPlacement());
 }

 @Test
 public void testFindComponentAt() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(300, 300);
 frame.setVisible(true);

 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 JLabel label1 = new JLabel("Label 1");
 JLabel label2 = new JLabel("Label 2");

 tabbedPane.addTab("Tab 1", panel1);
 tabbedPane.addTab("Tab 2", panel2);
 tabbedPane.add(frame, "Frame");
 frame.add(label1, "Label 1");
 frame.add(label2, "Label 2");
 frame.setLocation(10, 10);

 int x = label1.getX() + label1.getWidth() / 2;
 int y = label1.getY() + label1.getHeight() / 2;

 Component expectedComponent = label1;
 Component component = tabbedPane.findComponentAt(x, y);
 assertEquals(expectedComponent, component);
 }

 @Test
 public void testSetSelectedIndex() {
 JFrame frame1 = new JFrame();
 frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame1.setSize(300, 300);
 frame1.setVisible(true);

 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 JLabel label1 = new JLabel("Label 1");
 JLabel label2 = new JLabel("Label 2");

 tabbedPane.addTab("Tab 1", panel1);
 tabbedPane.addTab("Tab 2", panel2);
 tabbedPane.add(frame1, "Frame 1");
 frame1.add(label1, "Label 1");
 frame1.setLocation(10, 10);

 tabbedPane.setSelectedIndex(0);
 assertSame(tabbedPane.getSelectedComponent(), panel1);

 tabbedPane.setSelectedIndex(1);
 assertSame(tabbedPane.getSelectedComponent(), panel2);
 }

 @Test
 public void testSetSelectedComponent() {
 JFrame frame1 = new JFrame();
 frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame1.setSize(300, 300);
 frame1.setVisible(true);

 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 JLabel label1 = new JLabel("Label 1");
 JLabel label2 = new JLabel("Label 2");

 tabbedPane.addTab("Tab 1", panel1);
 tabbedPane.addTab("Tab 2", panel2);
 tabbedPane.add(frame1, "Frame 1");
 frame1.add(label1, "Label 1");
 frame1.setLocation(10, 10);

 tabbedPane.setSelectedComponent(frame1);
 assertSame(tabbedPane.getSelectedComponent(), frame1);
 }

 @Test
 public void testRemoveTabAt() {
 JFrame frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.setSize(300, 300);
 frame.setVisible(true);

 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 JLabel label1 = new JLabel("Label 1");
 JLabel label2 = new JLabel("Label 2");

 tabbedPane.addTab("Tab 1", panel1);
 tabbedPane.addTab("Tab 2", panel2);
 tabbedPane.add(frame, "Frame");
 frame.add(label1, "Label 1");
 frame.add(label2, "Label 2");
 frame.setLocation(10, 10);

 tabbedPane.removeTabAt(0);
 assertNull(tabbedPane.getComponentAt(0));
 assertSame(tabbedPane.getSelectedComponent(), panel2);
 }

 @Test
 public void testHandleSelectedFile() {
 File followedFile = new File("testFile.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(followedFile);
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);

 assertEquals(followedFile.getParentFile(), attributes.getLastFileChooserDirectory());
 }

 @Test
 public void testHandleSelectedFileWhenNoFileSelected() {
 tabbedPane.handleSelectedFile();
 assertEquals(null, attributes.getLastFileChooserDirectory());
 }
}