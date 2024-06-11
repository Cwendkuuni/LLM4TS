package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;
    private FileFollowingPane fileFollowingPane;

    @Before
    public void setUp() {
        attributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributes);
        fileFollowingPane = mock(FileFollowingPane.class);
    }

    @Test
    public void testTabbedPaneConstruction() {
        assertEquals(SwingConstants.LEFT, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);

        tabbedPane = new TabbedPane(attributes);
        assertEquals(attributes, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAt() {
        JLabel label = new JLabel("Label");
        JPanel panel = new JPanel();
        panel.add(label);

        tabbedPane.addTab("Tab 1", panel);
        tabbedPane.addTab("Tab 2", label);

        int x = 10;
        int y = 10;

        Component component = tabbedPane.findComponentAt(x, y);
        assertNull(component);

        x += panel.getWidth() + 10;
        y += panel.getHeight() + 10;

        component = tabbedPane.findComponentAt(x, y);
        assertEquals(label, component);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", fileFollowingPane);

        tabbedPane.setSelectedIndex(0);
        verify(fileFollowingPane, times(1)).handleSelectedFile();

        tabbedPane.setSelectedIndex(1);
        verify(fileFollowingPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", fileFollowingPane);

        tabbedPane.setSelectedComponent(fileFollowingPane);
        verify(fileFollowingPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", fileFollowingPane);

        tabbedPane.removeTabAt(0);
        verify(fileFollowingPane, never()).handleSelectedFile();

        tabbedPane.removeTabAt(1);
        verify(fileFollowingPane, never()).handleSelectedFile();
    }

    @Test
    public void testHandleSelectedFile() {
        File file = new File("file.txt");
        when(fileFollowingPane.getFollowedFile()).thenReturn(file);

        tabbedPane.handleSelectedFile();
        assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
    }
}