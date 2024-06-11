package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.*;

import Joshua.FoxHunt.*;
import javax.swing.*;
import javax.swing.table.*;

@RunWith(MockitoJUnitRunner.class)
public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrameConstruction() {
        FoxHuntFrame actualFoxHuntFrame = new FoxHuntFrame();
        assertNotNull(actualFoxHuntFrame);
    }

    @Test
    public void testActionPerformedAddPointMethod() {
        FoxHuntFrame actualFoxHuntFrame = new FoxHuntFrame();
        actualFoxHuntFrame.theta.setText("45");
        actualFoxHuntFrame.lat.setText("51.5074");
        actualFoxHuntFrame.lon.setText("0.1278");
        actualFoxHuntFrame.add.doClick();

        TrigPoint expectedPoint = new TrigPoint(51.5074, 0.1278, 45.0);
        TrigPoint actualPoint = (TrigPoint) actualFoxHuntFrame.points.lastElement();

        assertEquals(expectedPoint.getX(), actualPoint.getX(), 0.01);
        assertEquals(expectedPoint.getY(), actualPoint.getY(), 0.01);
        assertEquals(expectedPoint.getAngle(), actualPoint.getAngle(), 0.1);

        double[] expectedFoxLat = {51.5074, 51.5335};
        double[] expectedFoxLon = {0.1278, 0.1224};
        TrigPoint[] actualFox = actualFoxHuntFrame.fox.toArray(new TrigPoint[0]);

        for (int i = 0; i < actualFox.length; ++i) {
            assertEquals(expectedFoxLat[i], actualFox[i].getY(), 0.01);
            assertEquals(expectedFoxLon[i], actualFox[i].getX(), 0.01);
        }
    }

    @Test
    public void testDisplayAddPointMethod() {
        FoxHuntFrame actualFoxHuntFrame = new FoxHuntFrame();
        FoxTableModel expectedPosModel = Mockito.spy(new FoxTableModel());
        HuntDisplay expectedDisplay = Mockito.spy(new HuntDisplay());

        Mockito.when(expectedDisplay.addPoint(Mockito.any(TrigPoint.class)))
                .thenReturn(true);

        Mockito.when(expectedPosModel.fireTableDataChanged())
                .thenCallRealMethod();

        actualFoxHuntFrame.display = expectedDisplay;
        actualFoxHuntFrame.posModel = expectedPosModel;

        actualFoxHuntFrame.theta.setText("45");
        actualFoxHuntFrame.lat.setText("51.5074");
        actualFoxHuntFrame.lon.setText("0.1278");
        actualFoxHuntFrame.add.doClick();

        Mockito.verify(expectedDisplay, Mockito.times(1)).addPoint(Mockito.argThat(trigPoint ->
                trigPoint.getX() == 0.1278 && trigPoint.getY() == 51.5074 &&
                        trigPoint.getAngle() == 45.0));
        Mockito.verify(expectedPosModel, Mockito.times(1)).fireTableDataChanged();
    }

    @Test
    public void testDisplayAddLandMarkMethod() {
        FoxHuntFrame actualFoxHuntFrame = new FoxHuntFrame();
        HuntDisplay expectedDisplay = Mockito.spy(new HuntDisplay());

        Mockito.when(expectedDisplay.addLandMark(Mockito.any(Landmark.class)))
                .thenReturn(true);

        actualFoxHuntFrame.display = expectedDisplay;

        actualFoxHuntFrame.loadLandmarks();

        Mockito.verify(expectedDisplay, Mockito.times(3)).addLandMark(Mockito.argThat(landmark -> landmark.getX() > 0 && landmark.getY() > 0));
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadLandmarksMethodWithFileNotFoundException() {
        FoxHuntFrame actualFoxHuntFrame = new FoxHuntFrame();
        actualFoxHuntFrame.loadLandmarks();
    }

    @Test(expected = IOException.class)
    public void testLoadLandmarksMethodWithIOException() {
        FoxHuntFrame actualFoxHuntFrame = new FoxHuntFrame();
        Mockito.doThrow(new IOException("IOException")).when(actualFoxHuntFrame).error(Mockito.anyString());

        actualFoxHuntFrame.loadLandmarks();
    }
}