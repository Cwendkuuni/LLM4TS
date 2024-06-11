package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.FollowAppAttributes;
import java.awt.*;
import java.io.File;

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
    public void testFindComponentAt_Null() {
        assertNull(tabbedPane.findComponentAt(10, 10));
    }

    @Test
    public void testFindComponentAt_ComponentPresent() {
        addTab(tabbedPane, "Tab1", new JLabel("Label1"));
        Component component = new JLabel("Component");
        tabbedPane.add(component);
        tabbedPane.setSelectedIndex(0);

        Point point = new Point(5, 5);
        Component actual = tabbedPane.findComponentAt(point.x, point.y);
        assertEquals("Tab1", actual.getName());
    }

    @Test
    public void testFindComponentAt_ContainerPresent() {
        JPanel container = new JPanel();
        addTab(tabbedPane, "Tab1", container);
        JLabel label = new JLabel("Label1");
        container.add(label);
        tabbedPane.setSelectedIndex(0);

        Point point = new Point(5, 5);
        Component actual = tabbedPane.findComponentAt(point.x, point.y);
        assertEquals("Label1", actual.getName());
    }

    @Test
    public void testSetSelectedIndex() {
        addTab(tabbedPane, "Tab1", new JLabel("Label1"));
        addTab(tabbedPane, "Tab2", new JLabel("Label2"));
        tabbedPane.setSelectedIndex(1);
        verify(attributes).setLastFileChooserDirectory(tabbedPane.getSelectedComponent().getParentFile());
    }

    @Test
    public void testSetSelectedComponent() {
        addTab(tabbedPane, "Tab1", new JLabel("Label1"));
        addTab(tabbedPane, "Tab2", new JLabel("Label2"));
        JLabel label = new JLabel("Label3");
        tabbedPane.setSelectedComponent(label);
        verify(attributes).setLastFileChooserDirectory(label.getParent());
    }

    @Test
    public void testRemoveTabAt() {
        addTab(tabbedPane, "Tab1", new JLabel("Label1"));
        addTab(tabbedPane, "Tab2", new JLabel("Label2"));
        tabbedPane.removeTabAt(0);
        verify(attributes, times(2)).setLastFileChooserDirectory(any());
    }

    @Test
    public void testHandleSelectedFile_NullSelectedComponent() {
        tabbedPane.handleSelectedFile();
        verifyNoInteractions(attributes);
    }

    @Test
    public void testHandleSelectedFile_SelectedComponentExists() {
        File file = new File("testDirectory");
        FileFollowingPane fileFollowingPane = new FileFollowingPane(file);
        when(fileFollowingPane.getFollowedFile()).thenReturn(file);
        addTab(tabbedPane, "Tab1", fileFollowingPane);
        tabbedPane.setSelectedComponent(fileFollowingPane);
        tabbedPane.handleSelectedFile();
        verify(attributes).setLastFileChooserDirectory(file.getParentFile());
    }

    private void addTab(TabbedPane tabbedPane, String title, Component component) {
        tabbedPane.addTab(title, component);
    }
}