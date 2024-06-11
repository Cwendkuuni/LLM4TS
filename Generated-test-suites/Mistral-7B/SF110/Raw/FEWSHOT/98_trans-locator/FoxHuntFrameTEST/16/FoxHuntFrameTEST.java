package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.*;
import java.util.*;
import java.util.event.ActionEvent;

import javax.swing.*;
import javax.swing.table.*;

import Joshua.FoxHunt.*;

@RunWith(MockitoJUnitRunner.class)
public class FoxHuntFrameTest {

    @Test
    public void testFoxHuntFrameConstructor() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        assertNotNull(fhf.getTable());
        assertNotNull(fhf.getDisplay());
    }

    @Test
    public void testAddPoint() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        JTextField theta = Mockito.mock(JTextField.class);
        JTextField lat = Mockito.mock(JTextField.class);
        JTextField lon = Mockito.mock(JTextField.class);
        Mockito.when(theta.getText()).thenReturn("123");
        Mockito.when(lat.getText()).thenReturn("51.5074");
        Mockito.when(lon.getText()).thenReturn("-0.1278");
        fhf.theta = theta;
        fhf.lat = lat;
        fhf.lon = lon;
        fhf.addActionListener(Mockito.mock(ActionListener.class));
        fhf.add();
        assertEquals(1, fhf.points.size());
        assertEquals(new TrigPoint(51.5074, -0.1278, 123), fhf.points.elementAt(0));
        verify(fhf, times(1)).addPoint();
    }

    @Test
    public void testFinalPrep() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        JPanel panel = Mockito.mock(JPanel.class);
        JSplitPane map = Mockito.mock(JSplitPane.class);
        JSplitPane controls = Mockito.mock(JSplitPane.class);
        JPanel panel1 = Mockito.mock(JPanel.class);
        JButton add = Mockito.mock(JButton.class);
        JTextField theta = Mockito.mock(JTextField.class);
        JTextField lat = Mockito.mock(JTextField.class);
        JTextField lon = Mockito.mock(JTextField.class);
        JLabel zoomL = Mockito.mock(JLabel.class);
        JTextField zoom = Mockito.mock(JTextField.class);
        JLabel thetaL = Mockito.mock(JLabel.class);
        JLabel latL = Mockito.mock(JLabel.class);
        JLabel lonL = Mockito.mock(JLabel.class);
        Mockito.when(panel.getLayout()).thenReturn(new BorderLayout());
        Mockito.when(map.getLayout()).thenReturn(new BorderLayout());
        Mockito.when(controls.getLayout()).thenReturn(new GridLayout(1, 2));
        Mockito.when(panel1.getLayout()).thenReturn(new GridLayout(4, 2));
        Mockito.when(panel.add).thenReturn(panel);
        Mockito.when(map.add).thenAnswer(invocation -> panel);
        Mockito.when(controls.add).thenAnswer(invocation -> map);
        Mockito.when(map.add).thenAnswer(invocation -> panel);
        Mockito.when(panel1.add).thenAnswer(invocation -> panel1);
        Mockito.when(panel.add).thenAnswer(invocation -> panel);
        Mockito.when(panel1.add).thenAnswer(invocation -> panel1);
        Mockito.when(panel1.add).thenAnswer(invocation -> theta);
        Mockito.when(panel1.add).thenAnswer(invocation -> lat);
        Mockito.when(panel1.add).thenAnswer(invocation -> lon);
        Mockito.when(panel1.add).thenAnswer(invocation -> add);
        Mockito.when(panel1.add).thenAnswer(invocation -> thetaL);
        Mockito.when(panel1.add).thenAnswer(invocation -> latL);
        Mockito.when(panel1.add).thenAnswer(invocation -> lonL);
        Mockito.when(panel1.add).thenAnswer(invocation -> zoomL);
        Mockito.when(panel1.add).thenAnswer(invocation -> zoom);
        Mockito.when(zoomL.getText()).thenReturn("Zoom");
        Mockito.when(zoom.getText()).thenReturn("1");
        Mockito.when(add.getText()).thenReturn("Add It!!!");
        Mockito.when(thetaL.getText()).thenReturn("Theta in Degrees");
        Mockito.when(latL.getText()).thenReturn("Lat. dd*mm.mmm");
        Mockito.when(lonL.getText()).thenReturn("lon. ddd*mm.mmm");
        Mockito.when(panel.getComponentCount()).thenReturn(1);
        Mockito.when(map.getComponentCount()).thenReturn(2);
        Mockito.when(controls.getComponent(0)).thenReturn(panel1);
        Mockito.when(map.getComponent(1)).thenReturn(panel3);
        Mockito.when(panel3.getComponentCount()).thenReturn(1);
        Mockito.when(panel3.getComponent(0)).thenReturn(display);
        Mockito.when(display.getPreferredSize()).thenReturn(new Dimension(500, 500));
        fhf.textPrep();
        assertEquals(panel, fhf.panel);
    }

    @Test
    public void testTablePrep() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        FoxTableModel model = new FoxTableModel();
        JTable table = Mockito.mock(JTable.class);
        JScrollPane scroll = Mockito.mock(JScrollPane.class);
        Mockito.when(table.getModel()).thenReturn(model);
        Mockito.when(scroll.getViewport()).thenReturn(table);
        fhf.tablePrep();
        assertEquals(table, fhf.pos);
    }

    @Test
    public void testDisplayPrep() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        HuntDisplay display = Mockito.mock(HuntDisplay.class);
        fhf.display = display;
        fhf.loadLandmarks();
        verify(display, times(1)).addLandMark(Mockito.any(Landmark.class));
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame fhf = new FoxHuntFrame();
        File file = Mockito.mock(File.class);
        FileInputStream inFile = Mockito.mock(FileInputStream.class);
        BufferedReader in = Mockito.mock(BufferedReader.class);
        String input = "NUM 5";
        Mockito.when(file.exists()).thenReturn(true);
        Mockito.when(file.isFile()).thenReturn(true);
        Mockito.when(file.getAbsolutePath()).thenReturn("/path/to/file.cfg");
        Mockito.when(inFile.available()).thenReturn(17);
        Mockito.when(inFile.readAllBytes()).thenReturn("NUM 5\nFILE icon1.jpg\nLAT 51.5074\nLON -0.1278\nFILE icon2.jpg\nLAT 51.5139\nLON -0.1318\nLAT 51.5205\nLON -0.1360\nLAT 51.5270\nLON -0.1402".getBytes());
        Mockito.when(inFile.open()).thenAnswer(invocation -> inFile);
        Mockito.when(in.readLine()).thenAnswer(invocation -> input);
        Mockito.when(input.substring(4)).thenReturn("5");
        Mockito.when(in.readLine()).thenAnswer(invocation -> "FILE icon1.jpg");
        Mockito.when(in.readLine()).thenAnswer(invocation -> "LAT 51.5074");
        Mockito.when(in.readLine()).thenAnswer(invocation -> "LON -0.1278");
        Mockito.when(in.readLine()).thenAnswer(invocation -> "FILE icon2.jpg");
        Mockito.when(in.readLine()).thenAnswer(invocation -> "LAT 51.5139");
        Mockito.when(in.readLine()).thenAnswer(invocation -> "LON -0.1318");
        Mockito.when(in.readLine()).thenAnswer(invocation -> "LAT 51.5205");
        Mockito.when(in.readLine()).thenAnswer(invocation -> "LON -0.1360");
        Mockito.when(in.readLine()).thenAnswer(invocation -> "LAT 51.5270");
        Mockito.when(in.readLine()).thenAnswer(invocation -> "LON -0.1402");
        Mockito.when(in.close()).thenAnswer(invocation -> in);
        fhf.loadLandmarks();
        assertEquals(5, fhf.display.landmarks.size());
    }
}