package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.FileFollowingPane;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

public class TabbedPaneTest {

    @Test
    public void testTabbedPaneWithAttributes() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        assertNotNull(tabbedPane.getAttributes());
        assertEquals(attributes.getTabPlacement(), tabbedPane.getTabPlacement());
    }

    @Test
    public void testFindComponentAt() {
        JTabbedPane tabbedPane = new JTabbedPane();
        int x = 10, y = 20;
        FileFollowingPane pane1 = new FileFollowingPane(mock(File.class));
        FileFollowingPane pane2 = new FileFollowingPane(mock(File.class));
        tabbedPane.add(pane1, "Tab1");
        tabbedPane.add(pane2, "Tab2");

        Component component = tabbedPane.findComponentAt(x, y);
        assertNull(component);

        component = pane1.findComponentAt(x - pane1.getX(), y - pane1.getY());
        assertNotNull(component);
        assertEquals(Color.WHITE, component.getBackground());

        tabbedPane.setBounds(0, 0, 100, 100);
        pane1.setBounds(0, 0, 50, 50);
        pane2.setBounds(50, 50, 50, 50);

        component = tabbedPane.findComponentAt(x, y);
        assertNull(component);

        x -= 10;
        y -= 10;

        component = pane1.findComponentAt(x - pane1.getX(), y - pane1.getY());
        assertNotNull(component);
        assertEquals(new Dimension(50, 50), pane1.getPreferredSize());
        assertEquals(Color.WHITE, component.getBackground());
    }

    @Test
    public void testSetSelectedIndex() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane1 = new FileFollowingPane(mock(File.class));
        FileFollowingPane pane2 = new FileFollowingPane(mock(File.class));
        tabbedPane.add(pane1, "Tab1");
        tabbedPane.add(pane2, "Tab2");

        tabbedPane.setSelectedIndex(0);
        tabbedPane.handleSelectedFile();
        assertEquals(pane1.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());

        tabbedPane.setSelectedIndex(1);
        tabbedPane.handleSelectedFile();
        assertNull(attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testSetSelectedComponent() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane1 = new FileFollowingPane(mock(File.class));
        FileFollowingPane pane2 = new FileFollowingPane(mock(File.class));
        tabbedPane.add(pane1, "Tab1");
        tabbedPane.add(pane2, "Tab2");

        tabbedPane.setSelectedComponent(pane1);
        tabbedPane.handleSelectedFile();
        assertEquals(pane1.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());

        tabbedPane.setSelectedComponent(pane2);
        tabbedPane.handleSelectedFile();
        assertNull(attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testRemoveTabAt() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane1 = new FileFollowingPane(mock(File.class));
        FileFollowingPane pane2 = new FileFollowingPane(mock(File.class));
        tabbedPane.add(pane1, "Tab1");
        tabbedPane.add(pane2, "Tab2");

        int index = tabbedPane.getSelectedIndex();
        tabbedPane.removeTabAt(index);
        tabbedPane.handleSelectedFile();
        assertNull(attributes.getLastFileChooserDirectory());

        tabbedPane.add(pane1, "Tab1");
        tabbedPane.add(pane2, "Tab2");
        tabbedPane.setSelectedIndex(1);
        tabbedPane.removeTabAt(1);
        tabbedPane.handleSelectedFile();
        assertNull(attributes.getLastFileChooserDirectory());
    }
}