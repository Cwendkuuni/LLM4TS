package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;

public class FoxHuntFrameTest {
    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testConstructor() {
        assertNotNull(frame);
    }

    @Test
    public void testThetaTextFields() {
        assertEquals("Theta in Degrees", frame.panel1.getComponent(0).getComponent(0).getText());
        assertEquals(3, frame.theta.getText().length());
    }

    @Test
    public void testAddPointMethod() {
        frame.theta.setText("10");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.0898");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
    }

    @Test(expected = NumberFormatException.class)
    public void testAddPointInvalidTheta() {
        frame.theta.setText("abc");
        frame.lat.setText("51.5074");
        frame.lon.setText("0.0898");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
    }

    @Test(expected = NumberFormatException.class)
    public void testAddPointInvalidLat() {
        frame.theta.setText("10");
        frame.lat.setText("ddddddddddd");
        frame.lon.setText("0.0898");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
    }

    @Test(expected = NumberFormatException.class)
    public void testAddPointInvalidLon() {
        frame.theta.setText("10");
        frame.lat.setText("51.5074");
        frame.lon.setText("ddddddddddddddddddd");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));
    }

    @Test
    public void testAddPointWithTwoPoints() {
        frame.theta.setText("10");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.0898");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));

        frame.theta.setText("20");
        frame.lat.setText("52.5074");
        frame.lon.setText("1.0898");
        frame.actionPerformed(new ActionEvent(frame.add, 0, "ActionEvent"));

        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());
    }
}