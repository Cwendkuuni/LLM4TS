package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit. Assert.assertSame;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.FileFollowingPane;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TabbedPaneTest {

 private FollowAppAttributes attributes;
 private TabbedPane tabbedPane;
 @Mock
 private FileFollowingPane fileFollowingPane;
 @Mock
 private JComponent component;

 @Test
 public void testTabbedPaneConstructor() {
 attributes = Mockito.mock(FollowAppAttributes.class);
 tabbedPane = new TabbedPane(attributes);
 assertSame(attributes.getTabPlacement(), tabbedPane.getTabPlacement());
 assertNull(tabbedPane.attributes);
 }

 @Test
 public void testFindComponentAt() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab1", component);
 tabbedPane.addTab("Tab2", fileFollowingPane);

 int x = 0;
 int y = 0;
 JComponent expectedComponent = component;

 JComponent result = tabbedPane.findComponentAt(x, y);
 assertEquals(expectedComponent, result);

 x += component.getSize().width + 10;
 y += component.getSize().height + 10;
 result = tabbedPane.findComponentAt(x, y);
 assertNull(result);
 }

 @Test
 public void testSetSelectedIndex() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab1", component);
 tabbedPane.addTab("Tab2", fileFollowingPane);
 tabbedPane.setSelectedIndex(0);
 assertEquals("Tab1", tabbedPane.getTitleAt(0));
 tabbedPane.setSelectedIndex(1);
 assertEquals("Tab2", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testSetSelectedComponent() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab1", component);
 tabbedPane.addTab("Tab2", fileFollowingPane);
 tabbedPane.setSelectedComponent(fileFollowingPane);
 assertEquals("Tab2", tabbedPane.getTitleAt(0));
 tabbedPane.setSelectedComponent(component);
 assertEquals("Tab1", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testRemoveTabAt() {
 tabbedPane = new TabbedPane(attributes);
 tabbedPane.addTab("Tab1", component);
 tabbedPane.addTab("Tab2", fileFollowingPane);
 tabbedPane.removeTabAt(0);
 assertEquals("Tab2", tabbedPane.getTitleAt(0));
 }

 @Test
 public void testHandleSelectedFile() {
 tabbedPane = new TabbedPane(attributes);
 FileFollowingPane fileFollowingPane1 = Mockito.mock(FileFollowingPane.class);
 FileFollowingPane fileFollowingPane2 = Mockito.mock(FileFollowingPane.class);
 tabbedPane.addTab("Tab1", fileFollowingPane1);
 tabbedPane.addTab("Tab2", fileFollowingPane2);
 tabbedPane.handleSelectedFile();
 Mockito.verify(attributes, Mockito.times(1)).setLastFileChooserDirectory(fileFollowingPane1.getFollowedFile().getParentFile());
 tabbedPane.setSelectedComponent(fileFollowingPane2);
 tabbedPane.handleSelectedFile();
 Mockito.verify(attributes, Mockito.times(1)).setLastFileChooserDirectory(fileFollowingPane2.getFollowedFile().getParentFile());
 }
}