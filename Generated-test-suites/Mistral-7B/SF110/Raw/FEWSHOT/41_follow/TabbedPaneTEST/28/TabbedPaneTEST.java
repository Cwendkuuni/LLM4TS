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
    public void testConstructor() {
        assertEquals(SwingConstants.TOP, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);
    }

    @Test
    public void testSetAttributes() {
        tabbedPane.setAttributes(attributes);
        assertEquals(attributes, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAt_NullComponent() {
        assertNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testFindComponentAt_VisibleComponent() {
        JComponent visibleComponent = new JComponent();
        tabbedPane.add(visibleComponent, "Test Tab");
        visibleComponent.setVisible(true);
        visibleComponent.setLocation(0, 0);
        assertEquals(visibleComponent, tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testFindComponentAt_NonVisibleComponent() {
        JComponent nonVisibleComponent = new JComponent();
        tabbedPane.add(nonVisibleComponent, "Test Tab");
        nonVisibleComponent.setVisible(false);
        assertNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testFindComponentAt_ContainerComponent() {
        JComponent containerComponent = new JPanel();
        JComponent childComponent = new JComponent();
        containerComponent.add(childComponent);
        tabbedPane.add(containerComponent, "Test Tab");
        childComponent.setVisible(true);
        childComponent.setLocation(0, 0);
        assertEquals(childComponent, tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.add(fileFollowingPane, "Test Tab");
        tabbedPane.setSelectedIndex(0);
        verify(fileFollowingPane).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.add(fileFollowingPane, "Test Tab");
        tabbedPane.setSelectedComponent(fileFollowingPane);
        verify(fileFollowingPane).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.add(fileFollowingPane, "Test Tab");
        tabbedPane.removeTabAt(0);
        verify(fileFollowingPane, never()).handleSelectedFile();
    }

    @Test
    public void testHandleSelectedFile() {
        File followedFile = new File("testFile.txt");
        when(fileFollowingPane.getFollowedFile()).thenReturn(followedFile);
        tabbedPane.add(fileFollowingPane, "Test Tab");
        tabbedPane.setSelectedIndex(0);
        tabbedPane.handleSelectedFile();
        assertEquals(followedFile.getParentFile(), attributes.getLastFileChooserDirectory());
    }
}