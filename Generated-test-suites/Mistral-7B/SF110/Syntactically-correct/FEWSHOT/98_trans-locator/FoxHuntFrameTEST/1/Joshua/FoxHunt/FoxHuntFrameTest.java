package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;

import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrame() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        assertNotNull( fhf );
    }

    @Test
    public void testActionPerformed() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        fhf.actionPerformed( new ActionEvent(new Object(), 0, "Test") );
        assertTrue( fhf.points.size() > 0 );
        assertTrue( fhf.fox.size() > 0 );
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        TrigPoint point = new TrigPoint(0.0, 0.0, 0);
        fhf.addPoint();
        fhf.points.add(point);
        TrigPoint expectedFox = FoxCalc.calcFox(point, (TrigPoint) fhf.points.lastElement());
        fhf.fox.add(expectedFox);
        TrigPoint actualFox = (TrigPoint) fhf.fox.lastElement();
        assertEquals(expectedFox.getX(), actualFox.getX(), 0.001);
        assertEquals(expectedFox.getY(), actualFox.getY(), 0.001);
    }

    @Test
    public void testTableModelData() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        FoxTableModel tableModel = fhf.posModel;
        assertEquals(tableModel.getColumnCount(), 2);
        assertEquals(tableModel.getRowCount(), fhf.fox.size());
        TrigPoint expectedPoint = (TrigPoint) fhf.fox.elementAt(0);
        Object actualPoint = tableModel.getValueAt(0, 0);
        DecimalFormat formatter = new DecimalFormat("###.##############");
        assertEquals(formatter.format(expectedPoint.getY()), actualPoint.toString());
        expectedPoint = (TrigPoint) fhf.fox.elementAt(1);
        actualPoint = tableModel.getValueAt(1, 1);
        assertEquals(formatter.format(expectedPoint.getX()), actualPoint.toString());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        Landmark landmark = null;
        try {
            fhf.loadLandmarks();
            landmark = fhf.display.getLandmarks().lastElement();
        } catch (Exception e) {
            fail("Error loading landmarks file");
        }
        assertNotNull(landmark);
    }
}