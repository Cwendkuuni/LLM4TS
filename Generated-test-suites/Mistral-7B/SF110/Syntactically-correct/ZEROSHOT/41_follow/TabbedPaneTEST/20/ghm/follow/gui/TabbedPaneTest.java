package ghm.follow.gui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import java.awt.*;
import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TabbedPaneTest {

 @Mock
 private FollowAppAttributes attributes;

 @InjectMocks
 private TabbedPane tabbedPane;

 @Before
 public void setUp() {
 when(attributes.getTabPlacement()).thenReturn(JTabbedPane.LEFT);
 }

 @Test
 public void testTabbedPaneConstruction() {
 TabbedPane tabbedPane = new TabbedPane(attributes);
 assertEquals(JTabbedPane.LEFT, tabbedPane.getTabPlacement());
 }

 @Test
 public void testFindComponentAt_WhenComponentIsNotFound_ThenReturnsNull() {
 int x = 10;
 int y = 10;
 assertNull(tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testFindComponentAt_WhenComponentIsFound_ThenReturnsComponent() {
 JPanel panel = new JPanel();
 tabbedPane.add(panel, "Test Tab");
 int x = 10;
 int y = 10;
 assertEquals(panel, tabbedPane.findComponentAt(x, y));
 }

 @Test
 public void testSetSelectedIndex() {
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 tabbedPane.add(panel1, "Tab 1");
 tabbedPane.add(panel2, "Tab 2");
 tabbedPane.setSelectedIndex(0);
 assertEquals(panel1, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedIndex_WhenIndexIsInvalid_ThenDoesNotChangeSelectedComponent() {
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 tabbedPane.add(panel1, "Tab 1");
 tabbedPane.add(panel2, "Tab 2");
 tabbedPane.setSelectedIndex(3);
 assertEquals(panel1, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testSetSelectedComponent() {
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 tabbedPane.add(panel1, "Tab 1");
 tabbedPane.add(panel2, "Tab 2");
 FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
 }

 @Test
 public void testRemoveTabAt() {
 JPanel panel1 = new JPanel();
 JPanel panel2 = new JPanel();
 JPanel panel3 = new JPanel();
 tabbedPane.add(panel1, "Tab 1");
 tabbedPane.add(panel2, "Tab 2");
 tabbedPane.add(panel3, "Tab 3");
 tabbedPane.removeTabAt(1);
 assertEquals(2, tabbedPane.getTabCount());
 assertNull(tabbedPane.getComponentAt(1));
 }

 @Test
 public void testHandleSelectedFile() {
 File followFile = new File("testFile.txt");
 FileFollowingPane fileFollowingPane = new FileFollowingPane(followFile);
 tabbedPane.add(fileFollowingPane, "Test Tab");
 tabbedPane.setSelectedComponent(fileFollowingPane);
 File parent = new File("testDirectory");
 when(attributes.getLastFileChooserDirectory()).thenReturn(parent);
 tabbedPane.handleSelectedFile();
 assertEquals(parent, attributes.getLastFileChooserDirectory());
 }
}