package ghm.follow.gui.test;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import java.awt.*;
import java.io.File;

public class TabbedPaneTest {

private FollowAppAttributes attributes;
private TabbedPane tabbedPane;

@Test
public void testTabbedPaneConstructor() {
TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
assertNotNull(tabbedPane);
}

@Test
public void testFindComponentAt() {
JFrame frame = new JFrame();
JPanel panel = new JPanel();
JButton button = new JButton("Test");
tabbedPane = new TabbedPane(new FollowAppAttributes());
tabbedPane.addTab("Test Tab", panel);
tabbedPane.setSize(200, 200);
frame.getContentPane().add(tabbedPane);
frame.setSize(300, 300);
frame.setVisible(true);

int x = 50, y = 50;
Component actual = tabbedPane.findComponentAt(x, y);
assertNull(actual);

x = 50;
y = 100;
panel.add(button);
button.setLocation(0, 0);
panel.setSize(100, 50);
tabbedPane.revalidate();
tabbedPane.repaint();

Component expected = button;
actual = tabbedPane.findComponentAt(x, y);
assertEquals(expected, actual);
}

@Test(expected = IndexOutOfBoundsException.class)
public void testFindComponentAtOutOfBounds() {
tabbedPane = new TabbedPane(new FollowAppAttributes());
tabbedPane.addTab("Test Tab", new JPanel());
tabbedPane.setSize(200, 200);

int x = 50, y = 250;
tabbedPane.findComponentAt(x, y);
}

@Test
public void testSetSelectedIndex() {
JFrame frame = new JFrame();
JPanel panel1 = new JPanel();
JPanel panel2 = new JPanel();
JButton button1 = new JButton("Test1");
JButton button2 = new JButton("Test2");
tabbedPane = new TabbedPane(new FollowAppAttributes());
tabbedPane.addTab("Test1", panel1);
tabbedPane.addTab("Test2", panel2);
tabbedPane.setSize(200, 200);
frame.getContentPane().add(tabbedPane);
frame.setSize(300, 300);
frame.setVisible(true);

assertFalse(tabbedPane.isSelectedIndex(0));
tabbedPane.setSelectedIndex(0);
assertTrue(tabbedPane.isSelectedIndex(0));
}

@Test
public void testSetSelectedComponent() {
JFrame frame = new JFrame();
JPanel panel1 = new JPanel();
JPanel panel2 = new JFrame();
JButton button1 = new JButton("Test1");
JButton button2 = new JButton("Test2");
File file1 = new File("test1.txt");
File file2 = new File("test2.txt");
tabbedPane = new TabbedPane(new FollowAppAttributes());
tabbedPane.addTab("Test1", new FileFollowingPane(file1));
tabbedPane.addTab("Test2", new FileFollowingPane(file2));
tabbedPane.setSize(200, 200);
frame.getContentPane().add(tabbedPane);
frame.setSize(300, 300);
frame.setVisible(true);

assertFalse(tabbedPane.isSelectedComponent(button1));
tabbedPane.setSelectedComponent(button1);
assertTrue(tabbedPane.isSelectedComponent(button1));
}

@Test(expected = NullPointerException.class)
public void testSetSelectedComponentNull() {
tabbedPane.setSelectedComponent(null);
}

@Test
public void testRemoveTabAt() {
JFrame frame = new JFrame();
JPanel panel1 = new JPanel();
JPanel panel2 = new JPanel();
JButton button1 = new JButton("Test1");
JButton button2 = new JButton("Test2");
File file1 = new File("test1.txt");
File file2 = new File("test2.txt");
tabbedPane = new TabbedPane(new FollowAppAttributes());
tabbedPane.addTab("Test1", new FileFollowingPane(file1));
tabbedPane.addTab("Test2", new FileFollowingPane(file2));
tabbedPane.setSize(200, 200);
frame.getContentPane().add(tabbedPane);
frame.setSize(300, 300);
frame.setVisible(true);

assertEquals(2, tabbedPane.getTabCount());
tabbedPane.removeTabAt(0);
assertEquals(1, tabbedPane.getTabCount());
}

@Test
public void testRemoveTabAtInvalidIndex() {
TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
tabbedPane.addTab("Test Tab", new JPanel());
tabbedPane.setSize(200, 200);

assertEquals(1, tabbedPane.getTabCount());
tabbedPane.removeTabAt(10);
}

@Test
public void testHandleSelectedFile() {
JFrame frame = new JFrame();
JPanel panel = new JPanel();
JButton button = new JButton("Test");
FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("test.txt"));
tabbedPane = new TabbedPane(new FollowAppAttributes());
tabbedPane.addTab("Test", fileFollowingPane);
tabbedPane.setSize(200, 200);
frame.getContentPane().add(tabbedPane);
frame.setSize(300, 300);
frame.setVisible(true);
tabbedPane.setSelectedIndex(0);

assertTrue(attributes.getLastFileChooserDirectory() == null);
tabbedPane.handleSelectedFile();
assertEquals(fileFollowingPane.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());
}

@Test
public void testHandleSelectedFileNull() {
TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
tabbedPane.handleSelectedFile();
assertEquals(null, attributes.getLastFileChooserDirectory());
}

}