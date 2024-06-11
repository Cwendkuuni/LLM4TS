package Joshua.FoxHunt;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {
    private FoxHuntFrame fhf;

    @Before
    public void setUp() {
        fhf = new FoxHuntFrame();
    }

    @Test
    public void testConstructor() {
        // Test if frame is created with the correct title
        assertEquals("Triangulation", fhf.getTitle());
    }

    @Test
    public void testActionPerformed() {
        // Given: Text fields are initialized with valid values
        fhf.theta.setText("45");
        fhf.lat.setText("51.5074");
        fhf.lon.setText("0.1278");

        // When: Add button is clicked
        fhf.add.doClick();

        // Then: New point is added to the list and displayed on the map
        assertEquals(1, fhf.points.size());
        assertEquals(1, fhf.fox.size());
    }

    @Test
    public void testAddPointWithInvalidTheta() {
        // Given: Text fields are initialized with invalid values
        fhf.theta.setText("abc");
        fhf.lat.setText("51.5074");
        fhf.lon.setText("0.1278");

        // When: Add button is clicked
        fhf.add.doClick();

        // Then: No new point is added to the list and no exception is thrown
        assertEquals(0, fhf.points.size());
        assertEquals(0, fhf.fox.size());
        assertFalse(foxHuntFrameThrowsException());
    }

    @Test
    public void testAddPointWithInvalidLat() {
        // Given: Text fields are initialized with valid values for theta and lon,
        // but invalid value for lat
        fhf.theta.setText("45");
        fhf.lat.setText("abc");
        fhf.lon.setText("0.1278");

        // When: Add button is clicked
        fhf.add.doClick();

        // Then: No new point is added to the list and no exception is thrown
        assertEquals(0, fhf.points.size());
        assertEquals(0, fhf.fox.size());
        assertFalse(foxHuntFrameThrowsException());
    }

    @Test
    public void testAddPointWithInvalidLon() {
        // Given: Text fields are initialized with valid values for theta and lat,
        // but invalid value for lon
        fhf.theta.setText("45");
        fhf.lat.setText("51.5074");
        fhf.lon.setText("abc");

        // When: Add button is clicked
        fhf.add.doClick();

        // Then: No new point is added to the list and no exception is thrown
        assertEquals(0, fhf.points.size());
        assertEquals(0, fhf.fox.size());
        assertFalse(foxHuntFrameThrowsException());
    }

    @Test
    public void testLoadLandmarks() {
        // Given: File with landmarks exists and contains valid data
        // Stub the File, InputStream, BufferedReader, and InputStreamReader classes
        // to simulate reading the file

        // When: The loadLandmarks method is called
        fhf.loadLandmarks();

        // Then: The expected number of landmarks is added to the display
        // and no exception is thrown
        // Add assertions based on the number of landmarks, file name, and icons
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadLandmarksWithFileNotFound() {
        // Given: The file with landmarks does not exist
        // Stub the File class to throw a FileNotFoundException

        // When: The loadLandmarks method is called
        fhf.loadLandmarks();

        // Then: A FileNotFoundException is thrown
        // Add any necessary assertions or checks
    }

    @Test(expected = IOException.class)
    public void testLoadLandmarksWithIOException() {
        // Given: An error occurs while reading the file
        // Stub the BufferedReader and InputStreamReader classes to throw an IOException

        // When: The loadLandmarks method is called
        fhf.loadLandmarks();

        // Then: An IOException is thrown
        // Add any necessary assertions or checks
    }

    private boolean foxHuntFrameThrowsException() {
        try {
            fhf.addPoint();
            return false;
        }
        catch (Exception e) {
            return true;
        }
    }
}