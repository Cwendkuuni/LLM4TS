package Joshua.FoxHunt;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testFoxHuntFrameConstructor() {
        // Basic functionality testing
        assertEquals("Triangulation", frame.getTitle());
        assertNotNull(frame.getContentPane());

        // Edge cases testing
        assertNull(frame.getIconImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.dispose();
        assertFalse(frame.isDisplayable());

        // Exception handling testing
        try {
            new FoxHuntFrame().setDefaultCloseOperation(JFrame.ABORTONCLOSE);
            fail("Should not allow JFrame.ABORTONCLOSE");
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testActionPerformed() {
        // Basic functionality testing
        frame.theta.setText("10");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.1278");
        frame.add.doClick();
        assertEquals(2, frame.points.size());

        // Edge cases testing
        frame.theta.setText("");
        frame.add.doClick();
        assertEquals(1, frame.points.size());

        frame.theta.setText("360");
        frame.lat.setText("");
        frame.lon.setText("");
        frame.add.doClick();
        assertEquals(1, frame.points.size());

        // Exception handling testing
        frame.theta.setText("asd");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.1278");
        frame.add.doClick();
        assertTrue(JOptionPane.showOptionDialog(frame, "Error reading input: 'asd' is not a valid number", "Error", 0, 0, null, new Object[]{"Abort", "Continue"}, 0) == 0);
        assertEquals(0, frame.points.size());
    }
}