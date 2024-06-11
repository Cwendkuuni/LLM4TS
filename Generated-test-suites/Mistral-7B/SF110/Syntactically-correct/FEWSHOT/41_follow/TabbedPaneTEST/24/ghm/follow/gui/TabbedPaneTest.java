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

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        attributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testTabbedPaneConstructor() {
        assertEquals(JTabbedPane.TOP, tabbedPane.getTabPlacement());
        assertNull(tabbedPane.attributes);
    }

    @Test
    public void testSetAttributes() {
        assertEquals(JTabbedPane.TOP, tabbedPane.getTabPlacement());
        tabbedPane.setAttributes(attributes);
        assertEquals(attributes, tabbedPane.attributes);
    }

    @Test
    public void testFindComponentAt() {
        JComponent comp1 = new JLabel("Component 1");
        JComponent comp2 = new JLabel("Component 2");
        JComponent containerComp = new JPanel();
        containerComp.add(new JLabel("Container Component"));

        tabbedPane.add("Tab 1", comp1);
        tabbedPane.add("Tab 2", containerComp);
        tabbedPane.add("Tab 3", comp2);

        int x = 0, y = 0;
        Component expectedComponent = null;

        // Test when component is not contained
        assertNull(tabbedPane.findComponentAt(x, y));

        // Test when component is inside container
        x += containerComp.getSize().width;
        y += containerComp.getSize().height;
        expectedComponent = comp2;
        assertEquals(expectedComponent, tabbedPane.findComponentAt(x, y));

        // Test when component is directly under tabbed pane
        x -= containerComp.getSize().width;
        y -= comp1.getSize().height;
        expectedComponent = comp1;
        assertEquals(expectedComponent, tabbedPane.findComponentAt(x, y));

        // Test when component is under tabbed pane and outside container
        x += comp1.getSize().width;
        y -= comp1.getSize().height;
        expectedComponent = tabbedPane;
        assertEquals(expectedComponent, tabbedPane.findComponentAt(x, y));
    }

    @Test
    public void testSetSelectedIndex() {
        JComponent comp1 = new JLabel("Component 1");
        JComponent comp2 = new JLabel("Component 2");
        tabbedPane.add("Tab 1", comp1);
        tabbedPane.add("Tab 2", comp2);

        tabbedPane.setSelectedIndex(0);
        assertEquals(comp1, tabbedPane.getSelectedComponent());

        tabbedPane.setSelectedIndex(1);
        assertEquals(comp2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testSetSelectedComponent() {
        JComponent comp1 = new JLabel("Component 1");
        JComponent comp2 = new JLabel("Component 2");
        tabbedPane.add("Tab 1", comp1);
        tabbedPane.add("Tab 2", comp2);

        tabbedPane.setSelectedComponent(comp1);
        assertEquals(comp1, tabbedPane.getSelectedComponent());

        tabbedPane.setSelectedComponent(comp2);
        assertEquals(comp2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        JComponent comp1 = new JLabel("Component 1");
        JComponent comp2 = new JLabel("Component 2");
        tabbedPane.add("Tab 1", comp1);
        tabbedPane.add("Tab 2", comp2);

        int index = 0;
        tabbedPane.removeTabAt(index);

        assertEquals(comp2, tabbedPane.getComponentAt(0));
        assertNull(tabbedPane.getComponentAt(1));
    }

    @Test
    public void testHandleSelectedFile() {
        JFileChooser fileChooser = mock(JFileChooser.class);
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"), fileChooser);
        tabbedPane.add("Tab 1", pane);
        tabbedPane.setSelectedComponent(pane);

        File parent = new File("test_parent_directory");
        when(fileChooser.getCurrentDirectory()).thenReturn(parent);

        tabbedPane.handleSelectedFile();
        verify(attributes).setLastFileChooserDirectory(parent);
    }
}