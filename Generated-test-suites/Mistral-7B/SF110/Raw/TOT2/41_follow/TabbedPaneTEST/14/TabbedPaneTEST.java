package ghm.follow.gui;

import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.JFrame;
import javax.swing.*;
import ghm.follow.config.*;

public class TabbedPaneTest {

    @Test
    public void testTabbedPaneConstructor() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        assertNotNull(tabbedPane);
    }

    @Test
    public void testFindComponentAtNormalCase() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        Component component = new JLabel("Test");
        tabbedPane.add("Test", component);
        tabbedPane.setBounds(0, 0, 200, 200);
        frame.getContentPane().add(tabbedPane);
        frame.setSize(200, 200);
        frame.setVisible(true);
        int x = 50;
        int y = 50;
        Component actualComponent = tabbedPane.findComponentAt(x, y);
        assertTrue(actualComponent == component);
        frame.dispose();
    }

    // Test other basic scenarios

    @Test(expected = NullPointerException.class)
    public void testTabbedPaneConstructorNullAttributes() {
        TabbedPane tabbedPane = new TabbedPane(null);
    }

    @Test
    public void testFindComponentAtOutsideVisibleArea() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        Component component = new JLabel("Test");
        tabbedPane.add("Test", component);
        tabbedPane.setBounds(0, 0, 200, 200);
        frame.getContentPane().add(tabbedPane);
        frame.setSize(200, 200);
        frame.setVisible(true);
        int x = 210;
        int y = 210;
        Component actualComponent = tabbedPane.findComponentAt(x, y);
        assertNull(actualComponent);
        frame.dispose();
    }

    // Test edge cases and exception handling scenarios

    @Test
    public void testSetSelectedIndexNegativeValue() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        Component component = new JLabel("Test");
        tabbedPane.add("Test", component);
        tabbedPane.setBounds(0, 0, 200, 200);
        frame.getContentPane().add(tabbedPane);
        frame.setSize(200, 200);
        frame.setVisible(true);
        tabbedPane.setSelectedIndex(-1);
        frame.dispose();
        // Add assertion here if needed
    }

    // Add test cases for other potential edge cases and exceptions

    @After
    public void tearDown() {
        // Add any cleanup code if needed
    }

}