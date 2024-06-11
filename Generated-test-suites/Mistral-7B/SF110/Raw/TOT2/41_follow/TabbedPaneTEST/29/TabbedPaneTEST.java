package ghm.follow.gui;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import javax.swing.*;
import org.junit.Test;

public class TabbedPaneTest {
    @Test
    public void testConstructor() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        assertNotNull(tabbedPane);
    }

    @Test
    public void testFindComponentAt() {
        JFrame frame = new JFrame();
        Container contentPane = frame.getContentPane();
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JTabbedPane testTabbedPane = new JTabbedPane();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Test Tab 1", panel1);
        tabbedPane.addTab("Test Tab 2", panel2);

        tabbedPane.setSelectedIndex(0);

        Point point = new Point(10, 10);

        assertEquals(panel1, tabbedPane.findComponentAt(point.x, point.y));
    }

    @Test
    public void testSetSelectedIndex() {
        JFrame frame = new JFrame();
        Container contentPane = frame.getContentPane();
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JTabbedPane testTabbedPane = new JTabbedPane();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Test Tab 1", panel1);
        tabbedPane.addTab("Test Tab 2", panel2);

        tabbedPane.setSelectedIndex(1);

        assertEquals(panel2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testFindComponentAtEdgeCase() {
        JFrame frame = new JFrame();
        Container contentPane = frame.getContentPane();
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JTabbedPane testTabbedPane = new JTabbedPane();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Test Tab 1", panel1);
        tabbedPane.addTab("Test Tab 2", panel2);

        tabbedPane.setSelectedComponent(panel2);

        Point point = new Point(10, 10);

        assertNull(tabbedPane.findComponentAt(point.x, point.y));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFindComponentAtException() {
        JFrame frame = new JFrame();
        Container contentPane = frame.getContentPane();
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JTabbedPane testTabbedPane = new JTabbedPane();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Test Tab 1", panel1);
        tabbedPane.addTab("Test Tab 2", panel2);

        Point point = new Point(-10, 10);

        tabbedPane.findComponentAt(point.x, point.y);
    }
}