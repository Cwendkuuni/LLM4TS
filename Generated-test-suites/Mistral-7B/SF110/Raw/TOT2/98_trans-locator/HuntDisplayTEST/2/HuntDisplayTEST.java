import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.*; //assuming this is the package name

public class HuntDisplayTest {
    @Test
    public void testConstructor() {
        HuntDisplay display = new HuntDisplay();
        assertEquals(new Dimension(200, 200), display.getPrefSize());
        assertEquals(new Dimension(50, 50), display.getMinSize());
    }

    @Test
    public void testGetPreferredSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals(new Dimension(200, 200), display.getPreferredSize());
    }

    @Test
    public void testGetMinimumSize() {
        HuntDisplay display = new HuntDisplay();
        assertEquals(new Dimension(50, 50), display.getMinimumSize());
    }

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 2.0, 3.0, false);
        display.addPoint(point);
        //Add assertions to check if point is added correctly
        try {
            display.addPoint(null);
            fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            //Expected exception
        }
    }

    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
        display.addLandMark(landmark);
        //Add assertions to check if landmark is added correctly
        try {
            display.addLandMark(null);
            fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            //Expected exception
        }

        try {
            Landmark landmark2 = new Landmark(null, 2.0, new ImageIcon("icon.png"));
            display.addLandMark(landmark2);
            fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            //Expected exception
        }

        try {
            Landmark landmark3 = new Landmark(1.0, null, new ImageIcon("icon.png"));
            display.addLandMark(landmark3);
            fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            //Expected exception
        }

        try {
            Landmark landmark4 = new Landmark(1.0, 2.0, null);
            display.addLandMark(landmark4);
            fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            //Expected exception
        }
    }

    @Test
    public void testPaintComponentWithNegativeDimensions() {
        HuntDisplay display = new HuntDisplay();
        Graphics2D g = null; //Assume Graphics2D is used instead of Graphics
        try {
            display.paintComponent(g);
            fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            //Expected exception
        }

        HuntDisplay display2 = new HuntDisplay();
        display2.setSize(-10, -10);
        try {
            display2.paintComponent(g);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            //Expected exception
        }
    }

    @Test
    public void testPaintComponentWithLargePointsAndLandmarks() {
        //TODO: Add test data and assertions
    }
}