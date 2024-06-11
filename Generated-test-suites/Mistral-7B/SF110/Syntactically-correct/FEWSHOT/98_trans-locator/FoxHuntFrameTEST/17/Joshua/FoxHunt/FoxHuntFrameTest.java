package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import Joshua.FoxHunt.*;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrameConstruction() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.getContentPane());
        assertEquals("Triangulation", frame.getTitle());
    }

    @Test
    public void testAddPointMethod() {
        FoxHuntFrame frame = new FoxHuntFrame();
        JTextField theta = new JTextField("10");
        JTextField lat = new JTextField("51.5074");
        JTextField lon = new JTextField("0.0759");
        frame.theta = theta;
        frame.lat = lat;
        frame.lon = lon;
        frame.actionPerformed(new ActionEvent(new JButton("Add It!!!"), 0, ""));
        assertEquals(1, frame.points.size());
        TrigPoint point = (TrigPoint) frame.points.lastElement();
        assertEquals("51.5074", point.getLatitude(), 0.001);
        assertEquals("0.0759", point.getLongitude(), 0.001);
        assertEquals(10, point.getAngle(), 0.01);
    }

    @Test
    public void testAddPointMethodWithTwoPoints() {
        FoxHuntFrame frame = new FoxHuntFrame();
        JTextField theta1 = new JTextField("10");
        JTextField lat1 = new JTextField("51.5074");
        JTextField lon1 = new JTextField("0.0759");
        frame.theta = theta1;
        frame.lat = lat1;
        frame.lon = lon1;
        frame.actionPerformed(new ActionEvent(new JButton("Add It!!!"), 0, ""));
        JTextField theta2 = new JTextField("20");
        JTextField lat2 = new JTextField("52.5074");
        JTextField lon2 = new JTextField("0.1759");
        frame.theta = theta2;
        frame.lat = lat2;
        frame.lon = lon2;
        frame.actionPerformed(new ActionEvent(new JButton("Add It!!!"), 0, ""));
        assertEquals(2, frame.points.size());
        TrigPoint firstPoint = (TrigPoint) frame.points.lastElement();
        assertEquals("52.5074", firstPoint.getLatitude(), 0.001);
        assertEquals("0.1759", firstPoint.getLongitude(), 0.001);
        assertEquals(20, firstPoint.getAngle(), 0.01);
        TrigPoint secondPoint = (TrigPoint) frame.points.elementAt(0);
        assertEquals("51.5074", secondPoint.getLatitude(), 0.001);
        assertEquals("0.0759", secondPoint.getLongitude(), 0.001);
        assertEquals(10, secondPoint.getAngle(), 0.01);
    }

    @Test
    public void testCalcFoxMethod() {
        TrigPoint A = new TrigPoint(51.5074, 0.0759, 10);
        TrigPoint C = new TrigPoint(51.5188, 0.0827, 20);
        TrigPoint expectedFox = new TrigPoint(51.5115, 0.0786, 14.86);
        TrigPoint fox = FoxCalc.calcFox(A, C);
        assertEquals(expectedFox.getLatitude(), fox.getLatitude(), 0.001);
        assertEquals(expectedFox.getLongitude(), fox.getLongitude(), 0.001);
        assertEquals(expectedFox.getAngle(), fox.getAngle(), 0.01);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidThetaInput() {
        FoxHuntFrame frame = new FoxHuntFrame();
        JTextField theta = new JTextField("a");
        frame.theta = theta;
        frame.actionPerformed(new ActionEvent(new JButton("Add It!!!"), 0, ""));
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidLatInput() {
        FoxHuntFrame frame = new FoxHuntFrame();
        JTextField lat = new JTextField("51.5074a");
        frame.lat = lat;
        frame.actionPerformed(new ActionEvent(new JButton("Add It!!!"), 0, ""));
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidLonInput() {
        FoxHuntFrame frame = new FoxHuntFrame();
        JTextField lon = new JTextField("51.50740ddd");
        frame.lon = lon;
        frame.actionPerformed(new ActionEvent(new JButton("Add It!!!"), 0, ""));
    }
}