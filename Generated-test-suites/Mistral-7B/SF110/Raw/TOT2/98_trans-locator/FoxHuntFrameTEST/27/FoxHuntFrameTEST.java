package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.awt.event.*;
import javax.swing.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame fhf;

    @Before
    public void setUp() {
        fhf = new FoxHuntFrame();
    }

    

    @Test
    public void testFoxHuntFrameConstructor() {
        FoxHuntFrame fhf = new FoxHuntFrame();
    }

    @Test
    public void testActionPerformed() {
        ActionEvent e = new ActionEvent(new Object(), 0, "Test Event");
        fhf.actionPerformed(e);
    }

    @Test
    public void testAddPoint() {
        fhf.addPoint();
    }

    @Test
    public void testFinalPrep() {
        assertNotNull(ffh.panel);
    }

    @Test
    public void testTextPrep() {
        assertNotNull(ffh.theta);
    }

    @Test
    public void testTablePrep() {
        assertNotNull(ffh.pos);
    }

    @Test
    public void testDisplayPrep() {
        assertNotNull(ffh.display);
    }

    @Test
    public void testLoadLandmarks() {
        fhf.loadLandmarks();
    }

    @Test
    public void testError() {
        fhf.error("Error Message");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructor() {
        FoxHuntFrame fhf = new FoxHuntFrame();
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidInput() {
        fhf.theta.setText("Invalid Input");
        fhf.lat.setText("Invalid Input");
        fhf.lon.setText("Invalid Input");
        fhf.addActionPerformed(new ActionEvent(new Object(), 0, "Test Event"));
    }

    @Test(expected = FileNotFoundException.class)
    public void testInvalidFile() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.loadLandmarks();
    }

    @Test(expected = IOException.class)
    public void testIOException() {
        try {
            File file = new File("./landmarks.cfg");
            FileInputStream inFile = new FileInputStream(file);
            BufferedReader in = new BufferedReader(new InputStreamReader(inFile));
            String input = "";
            int num = 0;
            while ((input = in.readLine()) != null) {
                if (input.startsWith("FILE")) {
                    throw new IOException();
                }
            }
            in.close();
            inFile.close();
        } catch (FileNotFoundException e) {
            fail("Expected FileNotFoundException, but got " + e.getClass().getName());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLandmarkData() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        try {
            fhf.loadLandmarks();
        } catch (IOException e) {
            fail("Expected IOException, but got " + e.getClass().getName());
        }
        Landmark landmark = fhf.display.getLandMarks().get(0);
        assertNull(landmark.getIcon());
    }

    
}