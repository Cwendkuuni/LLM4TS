import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.gui.*;
import javax.swing.*;

public class TabbedPaneTest {

    @Test
    public void testTabbedPaneConstructor() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        assertNotNull(tabbedPane);
    }

    @Test
    public void testFindComponentAt() {
        // Test case for findComponentAt()
    }

    @Test
    public void testSetSelectedIndex() {
        // Test case for setSelectedIndex()
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetSelectedIndexNegativeIndex() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Tab1", new JLabel("Label1"));
        tabbedPane.add("Tab2", new JLabel("Label2"));
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        tabbedPane.setSelectedIndex(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetSelectedIndexOutOfBoundsIndex() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Tab1", new JLabel("Label1"));
        tabbedPane.add("Tab2", new JLabel("Label2"));
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        tabbedPane.setSelectedIndex(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedIndexNonIntegerIndex() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Tab1", new JLabel("Label1"));
        tabbedPane.add("Tab2", new JLabel("Label2"));
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        tabbedPane.setSelectedIndex("2");
    }

    @Test
    public void testSetSelectedComponent() {
        // Test case for setSelectedComponent()
    }

    @Test
    public void testSetSelectedComponentNullFileFollowingPane() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Tab1", new JLabel("Label1"));
        tabbedPane.add("Tab2", new JLabel("Label2"));
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        FileFollowingPane fileFollowingPane = null;
        tabbedPane.setSelectedComponent(fileFollowingPane);
    }

    @Test(expected = NullPointerException.class)
    public void testSetSelectedComponentNullComponent() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Tab1", new JLabel("Label1"));
        tabbedPane.add("Tab2", new JLabel("Label2"));
        frame.add(tabbedPane);
        frame.pack();
        frame.setVisible(true);

        Component nullComponent = null;
        tabbedPane.setSelectedComponent(nullComponent);
    }

    @Test
    public void testHandleSelectedFile() {
        // Test case for handleSelectedFile()
    }

}