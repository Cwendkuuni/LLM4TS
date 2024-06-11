package Joshua.FoxHunt.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

    private FoxHuntFrame frame;

    @Before
    public void setUp() {
        EventQueue.invokeLater(() -> {
            frame = new FoxHuntFrame();
        });
    }

    @After
    public void tearDown() {
        if (frame != null) {
            frame.dispose();
        }
    }

    @Test
    public void testFoxHuntFrameConstructor() {
        assertNotNull(frame.getTitle());
        assertEquals("Triangulation", frame.getTitle());
    }

    @Test
    public void testAddPoint() {
        final String thetaText = "45";
        final String latText = "51.507222";
        final String lonText = "0.127778";
        frame.theta.setText(thetaText);
        frame.lat.setText(latText);
        frame.lon.setText(lonText);
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));

        final Vector<TrigPoint> points = frame.points;
        final Vector<TrigPoint> fox = frame.fox;

        assertEquals(1, points.size());
        assertEquals(1, fox.size());

        final TrigPoint point = points.elementAt(0);
        final TrigPoint foxPoint = fox.elementAt(0);

        assertEquals(51.507222, point.getY(), 0.0001);
        assertEquals(0.127778, point.getX(), 0.0001);
        assertEquals(Double.parseDouble(thetaText), point.getAngle(), 0.1);

        assertEquals(51.507222, foxPoint.getY(), 0.0001);
        assertEquals(0.127778, foxPoint.getX(), 0.0001);
    }

    @Test
    public void testAddPointsAndCalculateFox() {
        final String theta1Text = "45";
        final String lat1Text = "51.507222";
        final String lon1Text = "0.127778";
        final String theta2Text = "135";
        final String lat2Text = "52.507222";
        final String lon2Text = "1.127778";

        frame.theta.setText(theta1Text);
        frame.lat.setText(lat1Text);
        frame.lon.setText(lon1Text);
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));

        final Vector<TrigPoint> points = frame.points;
        final Vector<TrigPoint> fox = frame.fox;

        assertEquals(1, points.size());
        assertEquals(0, fox.size());

        frame.theta.setText(theta2Text);
        frame.lat.setText(lat2Text);
        frame.lon.setText(lon2Text);
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));

        assertEquals(2, points.size());
        assertEquals(1, fox.size());

        final TrigPoint point1 = points.elementAt(0);
        final TrigPoint point2 = points.elementAt(1);
        final TrigPoint foxPoint = fox.elementAt(0);

        assertEquals(51.507222, point1.getY(), 0.0001);
        assertEquals(0.127778, point1.getX(), 0.0001);
        assertEquals(Double.parseDouble(theta1Text), point1.getAngle(), 0.1);

        assertEquals(52.507222, point2.getY(), 0.0001);
        assertEquals(1.127778, point2.getX(), 0.0001);
        assertEquals(Double.parseDouble(theta2Text), point2.getAngle(), 0.1);

        assertEquals(52.339537, foxPoint.getY(), 0.0001);
        assertEquals(0.710324, foxPoint.getX(), 0.0001);
    }

    @Test
    public void testTableModel() {
        frame.theta.setText("45");
        frame.lat.setText("51.507222");
        frame.lon.setText("0.127778");
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));

        final FoxTableModel tableModel = (FoxTableModel) frame.posModel;

        assertEquals(0, tableModel.getRowCount());

        frame.theta.setText("135");
        frame.lat.setText("52.507222");
        frame.lon.setText("1.127778");
        frame.actionPerformed(new ActionEvent(frame.add, 0, ""));

        assertEquals(1, tableModel.getRowCount());

        final JTable table = frame.pos;

        final TableColumn column1 = table.getColumnModel().getColumn(0);
        final TableColumn column2 = table.getColumnModel().getColumn(1);

        assertEquals("Lattitude", column1.getHeaderValue().toString());
        assertEquals("Longitude", column2.getHeaderValue().toString());

        final Object value1 = tableModel.getValueAt(0, 0);
        final Object value2 = tableModel.getValueAt(0, 1);

        assertEquals("51.507222", value1.toString());
        assertEquals("0.127778", value2.toString());

        final JLabel landmarkLabel = new JLabel();
        final String iconFilePath = "icon.gif";
        final Landmark landmark = new Landmark(51.507222, 0.127778, new ImageIcon(iconFilePath));
        landmarkLabel.setIcon(landmark.getImage());

        final Object landmarkValue = tableModel.getValueAt(0, 2);

        assertNotNull(landmarkValue);
        assertEquals(landmarkLabel, landmarkValue);
    }

    @Test
    public void testLoadLandmarks() throws Exception {
        final String filePath = "./landmarks.cfg";
        final File file = new File(filePath);
        final FileInputStream inFile = new FileInputStream(file);
        final BufferedReader in = new BufferedReader(new InputStreamReader(inFile));

        frame.loadLandmarks();

        final HuntDisplay display = frame.display;

        assertEquals(10, display.getLandmarks().size());

        int i = 0;
        for (final Landmark landmark : display.getLandmarks()) {
            final String iconFilePath = landmark.getIconFilePath();
            final BufferedImage iconImage = ImageIO.read(new File(iconFilePath));
            final double lat = landmark.getLatitude();
            final double lon = landmark.getLongitude();

            assertEquals(iconImage, landmark.getImage());
            assertEquals(lat, landmark.getLatitude(), 0.001);
            assertEquals(lon, landmark.getLongitude(), 0.001);

            i++;
        }

        in.close();
        inFile.close();
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadLandmarksFileNotFoundException() throws Exception {
        final String filePath = "invalidFile.cfg";
        frame.loadLandmarks();
    }

    @Test(expected = IOException.class)
    public void testLoadLandmarksIOException() throws Exception {
        final String filePath = "./landmarks.cfg";
        final File file = new File(filePath);
        final FileInputStream inFile = new FileInputStream(file);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(inFile))) {
            in.readLine();
            in.close();
        }

        frame.loadLandmarks();
    }
}