package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

import Joshua.FoxHunt.*;
import javax.swing.*;
import javax.swing.table.*;

@RunWith(MockitoJUnitRunner.class)
public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        final FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        final JTextField theta = Mockito.spy(foxHuntFrame.theta);
        final JTextField lat = Mockito.spy(foxHuntFrame.lat);
        final JTextField lon = Mockito.spy(foxHuntFrame.lon);
        final JButton add = Mockito.spy(foxHuntFrame.add);
        final Vector points = Mockito.spy(foxHuntFrame.points);
        final Vector fox = Mockito.spy(foxHuntFrame.fox);
        final FoxTableModel posModel = Mockito.spy(foxHuntFrame.posModel);
        final HuntDisplay display = Mockito.spy(foxHuntFrame.display);

        Mockito.doReturn("45").when(theta).getText();
        Mockito.doReturn("51.123456789012345678901234567890").when(lat).getText();
        Mockito.doReturn("-0.1234567890123456789012345678901234567890").when(lon).getText();

        foxHuntFrame.actionPerformed(new ActionEvent(add, 0, "Add It!!!"));

        verify(points, times(1)).add(Mockito.any(TrigPoint.class));
        verify(display, times(1)).addPoint(Mockito.any(TrigPoint.class));
        verify(posModel, times(1)).fireTableDataChanged();

        final TrigPoint expectedPoint = new TrigPoint(51.1234567890123456789012345678901234567890D, -0.12345678901234567890123456789012345678901234567890D, 45);
        verify(points, times(1)).get(points.size() - 1).equals(expectedPoint);
        verify(fox, times(1)).get(fox.size() - 1).equals(expectedPoint);
    }

    @Test
    public void testLoadLandmarks() {
        final FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        final HuntDisplay display = Mockito.spy(foxHuntFrame.display);

        Mockito.doReturn(new File("./landmarks.cfg")).when(foxHuntFrame).new File("./landmarks.cfg");
        Mockito.doReturn(new BufferedReader(Mockito.mock(InputStreamReader.class))).when(foxHuntFrame).new BufferedReader(Mockito.any(InputStream.class));

        foxHuntFrame.loadLandmarks();

        verify(display, times(1)).addLandMark(Mockito.any(Landmark.class));
        verify(display, times(NUM_OF_LANDMARKS)).addLandMark(Mockito.any(Landmark.class));
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadLandmarks_FileNotFound() {
        final FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

        Mockito.doReturn(new File("./landmarks.cfg")).when(foxHuntFrame).new File("./landmarks.cfg");

        foxHuntFrame.loadLandmarks();

        fail("FileNotFoundException should be thrown");
    }

    @Test(expected = IOException.class)
    public void testLoadLandmarks_IOException() {
        final FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

        Mockito.doReturn(new File("./landmarks.cfg")).when(foxHuntFrame).new File("./landmarks.cfg");
        Mockito.doThrow(new IOException("Error communicating with the file")).when(foxHuntFrame).new BufferedReader(Mockito.any(InputStream.class));

        foxHuntFrame.loadLandmarks();

        fail("IOException should be thrown");
    }

    @Test
    public void testTableData() {
        final FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        final JTable table = Mockito.spy(foxHuntFrame.pos);
        final AbstractTableModel tableModel = Mockito.spy(foxHuntFrame.posModel);

        Mockito.doReturn(new String[]{"Lattitude", "Longitude"}).when(tableModel).getColumnNames();

        assertEquals("Lattitude", tableModel.getColumnName(0));
        assertEquals("Longitude", tableModel.getColumnName(1));

        final Object[] expectedData = new Object[]{
                new DecimalFormat("###.##############").format(51.1234567890123456789012345678901234567890D),
                new DecimalFormat("###.##############").format(-0.12345678901234567890123456789012345678901234567890D)
        };

        Mockito.doReturn(expectedData).when(tableModel).getValueAt(0, 0);
        Mockito.doReturn(expectedData[1]).when(tableModel).getValueAt(0, 1);

        assertEquals(expectedData[0], table.getValueAt(0, 0));
        assertEquals(expectedData[1], table.getValueAt(0, 1));
    }

}