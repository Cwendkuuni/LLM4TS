package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    @Test
    public void testTabbedPaneConstruction() {
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        TabbedPane tabbedPane = new TabbedPane(attributes);
        assertEquals(attributes.getTabPlacement(), tabbedPane.getTabPlacement());
        verify(attributes, times(2)).getTabPlacement();
    }

    @Test
    public void testFindComponentAt() {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        TabbedPane testedTabbedPane = new TabbedPane(tabbedPane) {
            @Override
            public Component findComponentAt(int x, int y) {
                return super.findComponentAt(x, y);
            }
        };

        Container container = mock(Container.class);
        Component comp = mock(Component.class);
        when(container.findComponentAt(anyInt(), anyInt())).thenReturn(comp);
        when(comp.isVisible()).thenReturn(true);

        JLabel label = new JLabel("Label");
        testedTabbedPane.addTab("Tab", container);

        Point point = new Point(5, 5);
        assertTrue(testedTabbedPane.findComponentAt(point.x, point.y) instanceof Container);
        assertEquals(container, testedTabbedPane.findComponentAt(point.x, point.y));

        Component component = testedTabbedPane.findComponentAt(point.x, point.y + label.getHeight());
        assertEquals(comp, component);
    }

    @Test
    public void testSetSelectedIndexAndHandleSelectedFile() {
        FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
        When selectFileFollowingPane = given(fileFollowingPane.getFollowedFile()).willReturn(new File("selectedFile"));

        TabbedPane tabbedPane = new TabbedPane(mock(FollowAppAttributes.class)) {
            @Override
            public void handleSelectedFile() {
                // do nothing
            }
        };

        tabbedPane.addTab("Tab", fileFollowingPane);
        tabbedPane.setSelectedIndex(0);

        verify(tabbedPane).handleSelectedFile();
        verify(fileFollowingPane).getFollowedFile();
        assertEquals("selectedFile", fileFollowingPane.getFollowedFile().getAbsolutePath());
    }

    @Test
    public void testSetSelectedComponentAndHandleSelectedFile() {
        FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
        When selectFileFollowingPane = given(fileFollowingPane.getFollowedFile()).willReturn(new File("selectedFile"));

        TabbedPane tabbedPane = new TabbedPane(mock(FollowAppAttributes.class)) {
            @Override
            public void handleSelectedFile() {
                // do nothing
            }
        };

        tabbedPane.addTab("Tab", fileFollowingPane);
        tabbedPane.setSelectedComponent(fileFollowingPane);

        verify(tabbedPane).handleSelectedFile();
        verify(fileFollowingPane).getFollowedFile();
        assertEquals("selectedFile", fileFollowingPane.getFollowedFile().getAbsolutePath());
    }

    @Test
    public void testRemoveTabAtAndHandleSelectedFile() {
        FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
        When selectFileFollowingPane = given(fileFollowingPane.getFollowedFile()).willReturn(new File("selectedFile"));

        TabbedPane tabbedPane = new TabbedPane(mock(FollowAppAttributes.class)) {
            @Override
            public void handleSelectedFile() {
                // do nothing
            }
        };

        tabbedPane.addTab("Tab", fileFollowingPane);
        tabbedPane.removeTabAt(0);

        verify(tabbedPane).handleSelectedFile();
        verify(fileFollowingPane, never()).getFollowedFile();
    }

    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane fileFollowingPane = mock(FileFollowingPane.class);
        When selectFileFollowingPane = given(fileFollowingPane.getFollowedFile()).willReturn(new File("selectedFile"));

        TabbedPane tabbedPane = new TabbedPane(mock(FollowAppAttributes.class));
        tabbedPane.addTab("Tab", fileFollowingPane);

        tabbedPane.setSelectedComponent(fileFollowingPane);

        tabbedPane.handleSelectedFile();

        verify(mock(FollowAppAttributes.class)).setLastFileChooserDirectory(new File("selectedFile").getParentFile());
    }
}