package ghm.follow.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.*;

public class TabbedPaneTest {

 private TabbedPane tabbedPane;
 private JFrame frame;
 private FollowAppAttributes attributes;

 @Before
 public void setup() {
 attributes = new FollowAppAttributes();
 tabbedPane = new TabbedPane(attributes);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 frame.add(tabbedPane);
 }

 @Test
 public void testTabbedPaneConstructor() {
 TabbedPane expectedTabbedPane = new TabbedPane(attributes);
 assertEquals("Expected tabbed pane tab placement to match constructor argument",
 attributes.getTabPlacement(), tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAtNull() {
 int x = 0;
 int y = 0;
 assertNull("Expected null when not containing given coordinates", tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAtContained() {
 JPanel testPanel = new JPanel();
 tabbedPane.addTab("Test Tab", testPanel);
 int x = testPanel.getX() + 10;
 int y = testPanel.getY() + 10;
 Component expectedComponent = testPanel;
 Component actualComponent = tabbedPane.findComponentAt(x, y);
 assertEquals("Expected component to match found component", expectedComponent, actualComponent);
 }

 @Test
 public void testSetSelectedIndex() {
 JPanel firstTab = new JPanel();
 tabbedPane.addTab("First Tab", firstTab);
 JPanel secondTab = new JPanel();
 tabbedPane.addTab("Second Tab", secondTab);
 tabbedPane.setSelectedIndex(1);
 assertEquals("Expected second tab to be selected", secondTab, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent() {
 JPanel firstTab = new JPanel();
 tabbedPane.addTab("First Tab", firstTab);
 JPanel secondTab = new JPanel();
 tabbedPane.addTab("Second Tab", secondTab);
 tabbedPane.setSelectedComponent(secondTab);
 assertEquals("Expected second tab to be selected", secondTab, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 JPanel tabToRemove = new JPanel();
 tabbedPane.addTab("Tab to remove", tabToRemove);
 tabbedPane.removeTabAt(0);
 assertFalse("Expected tabbed pane to no longer contain the removed tab", tabbedPane.getComponentAt(0) instanceof JPanel);
 }

 @Test
 public void testHandleSelectedFile() {
 FileFollowingPane fileFollowingPane = new FileFollowingPane(new File("testFile.txt"));
 tabbedPane.addTab("Test Tab", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 File expectedParent = new File("testDirectory");
 File actualParent = tabbedPane.attributes.getLastFileChooserDirectory();
 assertArrayEquals("Expected last file chooser directory to match test file's parent", expectedParent.getAbsFileSeparators(), actualParent.getAbsFileSeparators());
 }
}