package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private JTabbedPane tabbedPane;
    private FileFollowingPane fileFollowingPane;

    @Before
    public void setUp() {
        attributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributes);
        fileFollowingPane = mock(FileFollowingPane.class);
    }

    @Test
    public void testTabbedPaneConstructor() {
        assertEquals(SwingConstants.TOP_ABOVE_COMPONENTS, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);

        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        assertEquals(attributes, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAt() {
        JLabel label = new JLabel("Label");
        tabbedPane.addTab("Tab", label);

        int x = label.getX() + 10;
        int y = label.getY() + 10;

        Component component = tabbedPane.findComponentAt(x, y);
        assertSame(label, component);
    }

    @Test
    public void testFindComponentAt_OutsideComponents() {
        Component component = tabbedPane.findComponentAt(-1, -1);
        assertNull(component);
    }

    @Test
    public void testFindComponentAt_WhenComponentIsNotVisible() {
        JLabel label = new JLabel("Label");
        tabbedPane.addTab("Tab", label);
        label.setVisible(false);

        int x = label.getX() + 10;
        int y = label.getY() + 10;

        Component component = tabbedPane.findComponentAt(x, y);
        assertNull(component);
    }

    @Test
    public void testFindComponentAt_WhenComponentIsContainer() {
        JPanel panel = new JPanel();
        tabbedPane.addTab("Tab", panel);

        JButton button = new JButton("Button");
        panel.add(button);

        int x = button.getX() + 10;
        int y = button.getY() + 10;

        Component component = tabbedPane.findComponentAt(x, y);
        assertSame(button, component);
    }

    @Test
    public void testSetSelectedIndex() {
        JLabel label1 = new JLabel("Label 1");
        JLabel label2 = new JLabel("Label 2");
        tabbedPane.addTab("Tab 1", label1);
        tabbedPane.addTab("Tab 2", label2);

        tabbedPane.setSelectedIndex(0);
        assertEquals(label1, tabbedPane.getSelectedComponent());

        tabbedPane.setSelectedIndex(1);
        assertEquals(label2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testSetSelectedComponent() {
        JLabel label1 = new JLabel("Label 1");
        JLabel label2 = new JLabel("Label 2");
        tabbedPane.addTab("Tab 1", label1);
        tabbedPane.addTab("Tab 2", label2);

        tabbedPane.setSelectedComponent(label1);
        assertEquals(label1, tabbedPane.getSelectedComponent());

        tabbedPane.setSelectedComponent(label2);
        assertEquals(label2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        JLabel label1 = new JLabel("Label 1");
        JLabel label2 = new JLabel("Label 2");
        tabbedPane.addTab("Tab 1", label1);
        tabbedPane.addTab("Tab 2", label2);

        int indexBeforeRemoval = tabbedPane.getSelectedIndex();
        tabbedPane.removeTabAt(0);

        assertEquals(label2, tabbedPane.getSelectedComponent());
        assertEquals(1, tabbedPane.getComponentCount());
        assertFalse(tabbedPane.containsComponent(label1));

        tabbedPane.setSelectedIndex(indexBeforeRemoval);
    }

    @Test
    public void testHandleSelectedFile() {
        File file = new File("path/to/file");
        FileFollowingPane fileFollowingPane = new FileFollowingPane(file);
        tabbedPane.addTab("Tab", fileFollowingPane);
        when(fileFollowingPane.getFollowedFile()).thenReturn(file);

        tabbedPane.setSelectedComponent(fileFollowingPane);

        verify(attributes).setLastFileChooserDirectory(file.getParentFile());
    }
}