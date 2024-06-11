package ghm.follow.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    private FollowAppAttributes attributesMock;
    private TabbedPane tabbedPane;
    private FileFollowingPane fileFollowingPaneMock;

    @Before
    public void setUp() {
        attributesMock = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributesMock);
        fileFollowingPaneMock = mock(FileFollowingPane.class);
    }

    @Test
    public void testTabbedPaneConstruction() {
        assertEquals(SwingConstants.TOP_ABOVE_COMPONENTS, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);
    }

    @Test
    public void testTabbedPaneConstructionWithAttributes() {
        tabbedPane = new TabbedPane(attributesMock);
        verify(attributesMock, times(1)).getTabPlacement();
        tabbedPane.attributes = attributesMock;
        assertEquals(attributesMock, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAtNullInput() {
        assertNull(tabbedPane.findComponentAt(-1, -1));
    }

    @Test
    public void testFindComponentAtOutOfBounds() {
        JComponent componentMock = mock(JComponent.class);
        when(componentMock.isVisible()).thenReturn(false);
        when(tabbedPane.getComponentAt(0)).thenReturn(componentMock);

        assertNull(tabbedPane.findComponentAt(10, 10));
    }

    @Test
    public void testFindComponentAtInsideComponent() {
        JComponent containerMock = mock(JComponent.class);
        when(containerMock.isVisible()).thenReturn(true);
        when(containerMock.findComponentAt(-5, -5)).thenReturn(new JButton("Test"));

        JComponent componentMock = mock(JComponent.class);
        when(componentMock.isVisible()).thenReturn(true);
        when(componentMock.getComponentAt(-5, -5)).thenReturn(new JLabel("Label"));
        when(tabbedPane.getComponentAt(0)).thenReturn(containerMock);
        when(containerMock.getComponentAt(0)).thenReturn(componentMock);

        JComponent result = tabbedPane.findComponentAt(-5, -5);
        verify(componentMock, times(1)).isVisible();
        assertEquals(result, componentMock);
    }

    @Test
    public void testFindComponentAtInsideContainer() {
        JComponent containerMock = mock(JComponent.class);
        when(containerMock.isVisible()).thenReturn(true);
        when(containerMock.findComponentAt(-5, -5)).thenReturn(new JButton("Test"));

        JComponent componentMock = mock(JComponent.class);
        when(componentMock.isVisible()).thenReturn(true);
        when(componentMock.getComponentAt(-5, -5)).thenReturn(new JLabel("Label"));
        when(tabbedPane.getComponentAt(0)).thenReturn(containerMock);

        JComponent result = tabbedPane.findComponentAt(5, 5);
        verify(containerMock, times(1)).findComponentAt(5, 5);
        assertEquals(result, containerMock);
    }

    @Test
    public void testSetSelectedIndex() {
        when(tabbedPane.getComponentAt(0)).thenReturn(fileFollowingPaneMock);
        tabbedPane.setSelectedIndex(0);
        verify(tabbedPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponentNullInput() {
        assertNull(tabbedPane.getSelectedComponent());
        tabbedPane.setSelectedComponent(null);
        assertNull(tabbedPane.getSelectedComponent());
    }

    @Test
    public void testSetSelectedComponentValidInput() {
        when(tabbedPane.getComponentAt(0)).thenReturn(fileFollowingPaneMock);
        tabbedPane.setSelectedComponent(fileFollowingPaneMock);
        verify(tabbedPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAtZeroIndex() {
        when(tabbedPane.getComponentAt(0)).thenReturn(fileFollowingPaneMock);
        tabbedPane.removeTabAt(0);
        verify(tabbedPane, times(1)).handleSelectedFile();
        verify(tabbedPane, times(1)).removeTabAt(0);
    }

    @Test
    public void testRemoveTabAtLastIndex() {
        JComponent componentMock = mock(JComponent.class);
        when(tabbedPane.getComponentAt(0)).thenReturn(componentMock);
        when(tabbedPane.getComponentAt(1)).thenReturn(fileFollowingPaneMock);

        tabbedPane.removeTabAt(tabbedPane.getTabCount() - 1);
        verify(tabbedPane, times(1)).handleSelectedFile();
        verify(tabbedPane, times(1)).removeTabAt(tabbedPane.getTabCount() - 1);
    }

    @Test
    public void testRemoveTabAtMiddleIndex() {
        JComponent componentMock1 = mock(JComponent.class);
        JComponent componentMock2 = mock(JComponent.class);
        when(tabbedPane.getComponentAt(0)).thenReturn(componentMock1);
        when(tabbedPane.getComponentAt(1)).thenReturn(fileFollowingPaneMock);
        when(tabbedPane.getComponentAt(2)).thenReturn(componentMock2);

        tabbedPane.removeTabAt(1);
        verify(tabbedPane, times(1)).handleSelectedFile();
        verify(tabbedPane, times(1)).removeTabAt(1);
    }

    @Test
    public void testHandleSelectedFileNullSelectedComponent() {
        tabbedPane.handleSelectedFile();
        verify(tabbedPane.attributes, times(0)).setLastFileChooserDirectory(any(File.class));
    }

    @Test
    public void testHandleSelectedFileValidSelectedComponent() {
        File fileMock = mock(File.class);
        when(fileFollowingPaneMock.getFollowedFile()).thenReturn(fileMock);

        tabbedPane.handleSelectedFile();
        verify(tabbedPane.attributes, times(1)).setLastFileChooserDirectory(fileMock.getParentFile());
    }
}