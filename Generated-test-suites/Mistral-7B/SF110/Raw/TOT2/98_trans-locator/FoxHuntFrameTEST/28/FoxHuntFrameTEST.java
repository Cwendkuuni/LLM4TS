package Joshua.FoxHunt.test;

import org.junit.*;
import static org.junit.Assert.*;
import Joshua.FoxHunt.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        this.frame = new FoxHuntFrame();
    }

    @Test
    public void testFoxHuntFrameConstructor() {
        new FoxHuntFrame();
        assertEquals("Triangulation", this.frame.getTitle());
    }
    
    @Test
    public void testAddPoint() {
        this.frame = new FoxHuntFrame();
        JTextField theta = this.frame.theta;
        JTextField lat = this.frame.lat;
        JTextField lon = this.frame.lon;

        // Test empty input fields
        theta.setText("");
        lat.setText("");
        lon.setText("");
        this.frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));
        assertEquals(0, this.frame.points.size());

        // Test non-numeric input for latitude or longitude
        lat.setText("abcdefg");
        this.frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));
        assertEquals(0, this.frame.points.size());

        // Test negative latitude or longitude values
        theta.setText("180");
        lat.setText("-90.1234567890");
        lon.setText("-180.0123456789");
        this.frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));
        assertEquals(0, this.frame.points.size());

        // Test invalid angle value
        theta.setText("361");
        lat.setText("51.5074");
        lon.setText("-0.0123456789");
        this.frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));
        assertEquals(0, this.frame.points.size());

        // Test adding points before the first point is added
        theta.setText("50");
        lat.setText("51.5074");
        lon.setText("-0.0123456789");
        this.frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));
        theta.setText("50");
        lat.setText("51.5074");
        lon.setText("-0.0123456789");
        this.frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));
        assertEquals(1, this.frame.points.size());

        // Test adding points when the list already contains the maximum number of points
        this.frame.add(new JLabel("Maximum number of points reached"));
        this.frame.textPrep();
        this.frame.finalPrep();
        theta.setText("50");
        lat.setText("51.5074");
        lon.setText("-0.0123456789");
        this.frame.actionPerformed(new ActionEvent(new Object(), 0, "Add It!!!"));
        assertEquals(10, this.frame.points.size());
    }

    // Add test cases for other methods here

    @After
    public void tearDown() {
        this.frame = null;
    }
}