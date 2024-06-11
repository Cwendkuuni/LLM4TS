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

    @Before
    public void setUp() {
        attributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributes);
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
    public void testFindComponentAtNullInput() {
        assertNull(tabbedPane.findComponentAt(-1, -1));
    }

    @Test
    public void testFindComponentAtOutsideComponent() {
        JComponent component = mock(JComponent.class);
        tabbedPane.add(component, "Component 1");
        assertNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testFindComponentAtComponent() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);
        tabbedPane.add(component1, "Component 1");
        tabbedPane.add(component2, "Component 2");
        int component1X = 10, component1Y = 10;
        int component2X = 20, component2Y = 20;
        when(component1.getX()).thenReturn(component1X);
        when(component1.getY()).thenReturn(component1Y);
        when(component2.getX()).thenReturn(component2X);
        when(component2.getY()).thenReturn(component2Y);

        Point point = new Point(component1X + 5, component1Y + 5);
        Component result = tabbedPane.findComponentAt(point.x, point.y);
        assertEquals(component1, result);
    }

    @Test
    public void testSetSelectedIndex() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);
        tabbedPane.add(component1, "Component 1");
        tabbedPane.add(component2, "Component 2");

        tabbedPane.setSelectedIndex(0);
        verify(component1).setSelected(true);
        verify(component2).setSelected(false);

        tabbedPane.setSelectedIndex(1);
        verify(component1).setSelected(false);
        verify(component2).setSelected(true);
    }

    @Test
    public void testSetSelectedComponent() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);
        tabbedPane.add(component1, "Component 1");
        tabbedPane.add(component2, "Component 2");

        tabbedPane.setSelectedComponent(component1);
        verify(component1).setSelected(true);
        verify(component2).setSelected(false);

        tabbedPane.setSelectedComponent(component2);
        verify(component1).setSelected(false);
        verify(component2).setSelected(true);
    }

    @Test
    public void testRemoveTabAt() {
        JComponent component1 = mock(JComponent.class);
        JComponent component2 = mock(JComponent.class);
        tabbedPane.add(component1, "Component 1");
        tabbedPane.add(component2, "Component 2");

        tabbedPane.removeTabAt(0);
        verify(component1, never()).setSelected(anyInt());
        verify(component2).setSelected(true);
        assertEquals(1, tabbedPane.getComponentCount());

        tabbedPane.removeTabAt(1);
        verify(component2, never()).setSelected(anyInt());
        assertEquals(0, tabbedPane.getComponentCount());
    }

    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane pane1 = new FileFollowingPane(new File("File 1"));
        FileFollowingPane pane2 = new FileFollowingPane(new File("File 2"));
        tabbedPane.add(pane1, "Pane 1");
        tabbedPane.add(pane2, "Pane 2");
        tabbedPane.setSelectedIndex(0);

        when(pane1.getFollowedFile()).thenReturn(new File("Path/To/File 1"));
        tabbedPane.handleSelectedFile();
        assertEquals(new File("Path/To/File 1").getParentFile(), attributes.getLastFileChooserDirectory());

        tabbedPane.setSelectedIndex(1);
        tabbedPane.handleSelectedFile();
        assertNull(attributes.getLastFileChooserDirectory());
    }

    @After
    public void tearDown() {
        reset(attributes);
    }
}