package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;
import java.util.*;

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
    public void testConstructor() {
        assertEquals(SwingConstants.TOP, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);

        TabbedPane tabbedPaneWithAttributes = new TabbedPane(new FollowAppAttributes());
        assertEquals(SwingConstants.TOP, tabbedPaneWithAttributes.getTabPlacement());
        assertNotNull(tabbedPaneWithAttributes.attributes);
    }

    @Test
    public void testFindComponentAt() {
        JComponent component1 = new JLabel("Component 1");
        JComponent component2 = new JLabel("Component 2");
        JComponent containerComponent = new JPanel();
        containerComponent.add(component2);

        tabbedPane.addTab("Tab 1", component1);
        tabbedPane.addTab("Tab 2", containerComponent);

        int x = 50, y = 50;

        Component expectedComponent = null;
        if (x >= containerComponent.getX() && x < containerComponent.getX() + containerComponent.getWidth() &&
            y >= containerComponent.getY() && y < containerComponent.getY() + containerComponent.getHeight()) {
            expectedComponent = component2;
        } else if (x >= component1.getX() && x < component1.getX() + component1.getWidth() &&
                   y >= component1.getY() && y < component1.getY() + component1.getHeight()) {
            expectedComponent = component1;
        }

        assertEquals(expectedComponent, tabbedPane.findComponentAt(x, y));
        assertNull(tabbedPane.findComponentAt(-1, -1));
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));

        tabbedPane.setSelectedIndex(0);
        verify(attributes).setLastFileChooserDirectory(fileFollowingPane.getFollowedFile().getParentFile());

        tabbedPane.setSelectedIndex(1);
        verifyNoMoreInteractions(attributes);
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));

        tabbedPane.setSelectedComponent(fileFollowingPane);
        verify(attributes).setLastFileChooserDirectory(fileFollowingPane.getFollowedFile().getParentFile());

        tabbedPane.setSelectedComponent(new JLabel("Tab 2"));
        verifyNoMoreInteractions(attributes);
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.addTab("Tab 1", fileFollowingPane);
        tabbedPane.addTab("Tab 2", new JLabel("Tab 2"));

        tabbedPane.removeTabAt(0);
        verify(attributes).setLastFileChooserDirectory(fileFollowingPane.getFollowedFile().getParentFile());
        assertEquals(1, tabbedPane.getTabCount());

        tabbedPane.removeTabAt(0);
        verifyNoMoreInteractions(attributes);
        assertEquals(0, tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        when(fileFollowingPane.getFollowedFile()).thenReturn(new File("test.txt"));

        tabbedPane.setSelectedIndex(0);
        tabbedPane.handleSelectedFile();
        assertEquals(new File("test.txt").getParentFile(), attributes.getLastFileChooserDirectory());
    }
}