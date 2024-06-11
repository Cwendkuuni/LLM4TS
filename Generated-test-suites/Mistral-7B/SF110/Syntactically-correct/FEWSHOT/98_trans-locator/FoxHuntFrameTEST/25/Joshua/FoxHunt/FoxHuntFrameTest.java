package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

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
    private JTable table;

    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
        table = frame.pos;
    }

    @After
    public void tearDown() {
        if (frame != null) {
            frame.dispose();
        }
    }

    @Test
    public void testFoxHuntFrameCreation() {
        assertEquals("Triangulation", frame.getTitle());
        assertNotNull(frame.getContentPane());
    }

    @Test
    public void testAddPoint() {
        frame.theta.setText("45");
        frame.lat.setText("51.5074");
        frame.lon.setText("-0.1278");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.add.doClick();
            }
        });
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
    }

    @Test
    public void testAddLandmarks() {
        try {
            File file = new File("./landmarksTest.cfg");
            FileWriter writer = new FileWriter(file);
            writer.write("NUM 5\n");
            writer.write("FILE image1.png\n");
            writer.write("LAT 51.5074\n");
            writer.write("LON -0.1278\n");
            writer.write("FILE image2.png\n");
            writer.write("LAT 51.5105\n");
            writer.write("LON -0.1290\n");
            writer.write("FILE image3.png\n");
            writer.write("LAT 51.5136\n");
            writer.write("LON -0.1302\n");
            writer.write("FILE image4.png\n");
            writer.write("LAT 51.5167\n");
            writer.write("LON -0.1314\n");
            writer.write("FILE image5.png\n");
            writer.write("LAT 51.5200\n");
            writer.write("LON -0.1326\n");
            writer.close();

            frame = new FoxHuntFrame();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    frame.displayPrep();
                }
            });

            Vector<Landmark> expectedLandmarks = new Vector<>();
            for (int i = 0; i < 5; i++) {
                double lat = Double.parseDouble(String.valueOf(frame.display.getLandmarks().elementAt(i)).split(" ")[1].split("\\.")[0]);
                double lon = Double.parseDouble(String.valueOf(frame.display.getLandmarks().elementAt(i)).split(" ")[2].split("\\.")[0]);
                expectedLandmarks.add(new Landmark(lat, lon, new ImageIcon(new File("image" + (i + 1) + ".png"))));
            }

            assertEquals(expectedLandmarks.size(), frame.display.getLandmarks().size());
            for (int i = 0; i < expectedLandmarks.size(); i++) {
                Landmark expectedLandmark = expectedLandmarks.get(i);
                Landmark actualLandmark = (Landmark) frame.display.getLandmarks().get(i);
                assertEquals(expectedLandmark.getX(), actualLandmark.getX(), 0.001);
                assertEquals(expectedLandmark.getY(), actualLandmark.getY(), 0.001);
                assertEquals(expectedLandmark.getIcon(), actualLandmark.getIcon());
            }

            File fileToDelete = new File("./landmarksTest.cfg");
            fileToDelete.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTableData() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.addPoint();
                frame.addPoint();
            }
        });

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        assertEquals(2, model.getRowCount());

        TableCellRenderer renderer = table.getDefaultRenderer(Object.class);
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1, 1);

        for (int i = 0; i < model.getRowCount(); i++) {
            Object renderedValue = renderer.getTableCellRendererComponent(table, model.getValueAt(i, 0), false, false, 0, i).getIcon();
            assertEquals(renderedValue, image);
        }
    }
}