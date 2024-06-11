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
    public void testTabbedPaneConstructor() {
        assertEquals(SwingConstants.LEFT, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);
    }

    @Test
    public void testTabbedPaneConstructorWithAttributes() {
        assertEquals(SwingConstants.LEFT, tabbedPane.getTabPlacement());
        assertEquals(attributes, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAt() {
        JLabel label = new JLabel("Test Label");
        tabbedPane.addTab("Test Tab", new JPanel(), label);
        tabbedPane.setSelectedIndex(0);

        Component component = tabbedPane.findComponentAt(10, 10);
        assertNotNull(component);
        assertEquals(label, component);

        component = tabbedPane.findComponentAt(10, 11);
        assertNull(component);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.addTab("Test Tab 1", new JPanel());
        tabbedPane.addTab("Test Tab 2", new JPanel());
        tabbedPane.setSelectedIndex(1);

        verify(attributes, never()).setLastFileChooserDirectory(any(File.class));

        tabbedPane.setSelectedIndex(0);
        verify(attributes).setLastFileChooserDirectory(eq(new File("")));
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.addTab("Test Tab 1", new JPanel(), fileFollowingPane);
        tabbedPane.setSelectedComponent(fileFollowingPane);

        verify(attributes, never()).setLastFileChooserDirectory(any(File.class));

        tabbedPane.setSelectedComponent(fileFollowingPane);
        verify(attributes).setLastFileChooserDirectory(eq(fileFollowingPane.getFollowedFile().getParentFile()));
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.addTab("Test Tab 1", new JPanel(), fileFollowingPane);
        tabbedPane.removeTabAt(0);

        verify(attributes).setLastFileChooserDirectory(eq(new File("")));
    }

    @Test
    public void testHandleSelectedFile() {
        File followedFile = new File("test.txt");
        when(fileFollowingPane.getFollowedFile()).thenReturn(followedFile);

        tabbedPane.handleSelectedFile();
        verify(attributes).setLastFileChooserDirectory(eq(followedFile.getParentFile()));
    }
}