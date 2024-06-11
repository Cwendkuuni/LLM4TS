
package ghm.follow.gui;
import static org.junit.Assert.*;
import org.junit.Test;

public class TabbedPaneTest {
    @Test
    public void testConstructor() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        assertNotNull(tabbedPane);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullAttributes() {
        TabbedPane tabbedPane = new TabbedPane(null);
    }

    @Test
    public void testFindComponentAt_ValidParameters() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        int x = 0;
        int y = 0;
        Component component = tabbedPane.findComponentAt(x, y);
        assertNotNull(component);
    }

    @Test
    public void testFindComponentAt_OutOfBounds() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        int x = 100;
        int y = 100;
        Component component = tabbedPane.findComponentAt(x, y);
        assertNull(component);
    }

    @Test
    public void testSetSelectedIndex() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        for (int i = 0; i < 5; i++) {
            JComponent tabComponent = new JLabel("Tab " + i);
            tabbedPane.addTab("Tab " + i, tabComponent);
        }

        tabbedPane.setSelectedIndex(2);
        assertEquals("Tab 2", tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
    }

    @Test
    public void testSetSelectedComponent() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        FileFollowingPane fileFollowingPane = new FileFollowingPane();
        tabbedPane.add(fileFollowingPane, "Test Tab");

        tabbedPane.setSelectedComponent(fileFollowingPane);
        assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        for (int i = 0; i < 5; i++) {
            JComponent tabComponent = new JLabel("Tab " + i);
            tabbedPane.addTab("Tab " + i, tabComponent);
        }

        tabbedPane.removeTabAt(2);
        assertEquals(4, tabbedPane.getComponentCount());
    }

    @Test
    public void testHandleSelectedFile() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        FileFollowingPane fileFollowingPane = new FileFollowingPane();
        tabbedPane.add(fileFollowingPane, "Test Tab");
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        tabbedPane.setSelectedComponent(fileFollowingPane);

        File followedFile = new File("test.txt");
        fileFollowingPane.setFollowedFile(followedFile);

        tabbedPane.handleSelectedFile();
        assertEquals(followedFile.getParentFile(), tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
}
