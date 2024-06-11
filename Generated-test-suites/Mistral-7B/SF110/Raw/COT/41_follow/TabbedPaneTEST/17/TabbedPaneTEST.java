package ghm.follow.gui.test;

import static org.junit.Assert.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;

import org.junit.Test;

public class TabbedPaneTest {

private FollowAppAttributes attributes = new FollowAppAttributes();
private TabbedPane tabbedPane;

@Test
public void testTabbedPaneConstructor() {
 tabbedPane = new TabbedPane(attributes);
 assertNotNull(tabbedPane);
}

@Test
public void testFindComponentAt() {
 JLabel label = new JLabel("Test Label");
 tabbedPane.add("Test Tab", label);
 int x = 10, y = 10;
 Component component = tabbedPane.findComponentAt(x, y);
 assertTrue(component instanceof JLabel);
 assertEquals("Test Label", component.toString());
}

@Test
public void testFindComponentAtOutsideComponent() {
 tabbedPane.add("Test Tab", new JLabel("Test Label"));
 int x = 50, y = 50;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);
}

@Test
public void testFindComponentAtInvisibleComponent() {
 JLabel label = new JLabel("Test Label");
 tabbedPane.add("Test Tab", label);
 label.setVisible(false);
 int x = 10, y = 10;
 Component component = tabbedPane.findComponentAt(x, y);
 assertNull(component);
}

@Test
public void testFindComponentAtInContainerComponent() throws IOException {
 JLabel label = new JLabel("Test Label");
 JPanel panel = new JPanel();
 panel.add(label);
 tabbedPane.add("Test Tab", panel);
 int x = 10 + panel.getX(), y = 10 + panel.getY();
 Component component = tabbedPane.findComponentAt(x, y);
 assertTrue(component instanceof JLabel);
 assertEquals("Test Label", component.toString());
}

@Test
public void testSetSelectedIndex() {
 JLabel label1 = new JLabel("Test Label 1");
 JLabel label2 = new JLabel("Test Label 2");
 tabbedPane.add("Tab 1", label1);
 tabbedPane.add("Tab 2", label2);
 tabbedPane.setSelectedIndex(0);
 Component component = tabbedPane.getSelectedComponent();
 assertTrue(component instanceof JLabel);
 assertEquals("Test Label 1", component.toString());
}

@Test(expected = IndexOutOfBoundsException.class)
public void testSetSelectedIndexNegative() {
 tabbedPane.setSelectedIndex(-1);
 fail("Expected IndexOutOfBoundsException");
}

@Test(expected = IndexOutOfBoundsException.class)
public void testSetSelectedIndexGreaterThanNumberOfComponents() {
 tabbedPane.add("Test Tab", new JLabel("Test Label"));
 tabbedPane.setSelectedIndex(2);
 fail("Expected IndexOutOfBoundsException");
}

@Test
public void testSetSelectedComponent() {
 JLabel label = new JLabel("Test Label");
 tabbedPane.add("Test Tab", label);
 tabbedPane.setSelectedComponent(label);
 Component component = tabbedPane.getSelectedComponent();
 assertTrue(component instanceof JLabel);
 assertEquals("Test Label", component.toString());
}

@Test
public void testSetSelectedComponentNull() {
 tabbedPane.setSelectedComponent(null);
 assertNull(tabbedPane.getSelectedComponent());
}

@Test
public void testRemoveTabAt() {
 JLabel label = new JLabel("Test Label");
 tabbedPane.add("Test Tab", label);
 int index = tabbedPane.getTabCount() - 1;
 tabbedPane.removeTabAt(index);
 assertFalse(tabbedPane.getComponentAt(index) instanceof JLabel);
}

@Test(expected = IllegalArgumentException.class)
public void testRemoveTabAtNegative() {
 tabbedPane.removeTabAt(-1);
 fail("Expected IllegalArgumentException");
}

@Test(expected = IllegalArgumentException.class)
public void testRemoveTabAtGreaterThanNumberOfTabs() {
 tabbedPane.removeTabAt(5);
 fail("Expected IllegalArgumentException");
}

@Test
public void testHandleSelectedFile() {
 File file = new File("test.txt");
 FileFollowingPane pane = new FileFollowingPane(file);
 tabbedPane.add("Test Tab", pane);
 tabbedPane.setSelectedComponent(pane);
 assertEquals(file.getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
}

@Test
public void testHandleSelectedFileNullComponent() {
 tabbedPane.setSelectedComponent(null);
 assertNull(tabbedPane.attributes.getLastFileChooserDirectory());
}

}