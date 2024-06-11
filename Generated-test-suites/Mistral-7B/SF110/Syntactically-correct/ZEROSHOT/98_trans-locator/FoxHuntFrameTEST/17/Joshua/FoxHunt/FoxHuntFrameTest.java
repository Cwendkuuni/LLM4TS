package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractTableModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setup() {
 try {
 UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
 } catch (Exception e) {
 e.printStackTrace();
 }
 frame = new FoxHuntFrame();
 }

 @Test
 public void testFoxHuntFrameConstructor() {
 assertEquals("Triangulation", frame.getTitle());
 assertTrue(frame.isVisible());
 assertEquals("Center", frame.getContentPane().getLayout().getName());
 assertNotNull(frame.getIconImage());
 }

 @Test(expected = HeadlessException.class)
 public void testFoxHuntFrameNoGUI() {
 new FoxHuntFrame();
 }

 @Test
 public void testAddPoint() {
 final String theta = "20";
 final String lat = "40.12345678";
 final String lon = "80.123456789";
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);
 frame.add.doClick();

 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());

 final TrigPoint point = (TrigPoint) frame.points.lastElement();
 assertEquals(Double.parseDouble(lat), point.getY(), 0.01);
 assertEquals(Double.parseDouble(lon), point.getX(), 0.01);
 assertEquals(Integer.parseInt(theta), point.getAngle());

 final TrigPoint fox = (TrigPoint) frame.fox.lastElement();
 assertEquals(point.getY(), fox.getY(), 0.01);
 assertEquals(point.getX(), fox.getX(), 0.01);
 assertEquals(point.getAngle(), fox.getAngle(), 0.1);
 }

 @Test
 public void testAddMultiplePoints() {
 final int numPoints = 5;
 final String thetaTemplate = "20";
 final String latTemplate = "40.%d.%1$06d";
 final String lonTemplate = "80.%d.%1$06d";

 for (int i = 0; i < numPoints; ++i) {
 final String theta = String.format(thetaTemplate, i);
 final String lat = String.format(latTemplate, i);
 final String lon = String.format(lonTemplate, i);
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);
 frame.add.doClick();
 }

 assertEquals(numPoints, frame.points.size());
 assertEquals(numPoints, frame.fox.size());

 final DecimalFormat formatter = new DecimalFormat("###.##############");
 final TrigPoint[] points = new TrigPoint[numPoints];
 final TrigPoint[] foxes = new TrigPoint[numPoints];

 for (int i = 0; i < numPoints; ++i) {
 points[i] = (TrigPoint) frame.points.elementAt(i);
 foxes[i] = (TrigPoint) frame.fox.elementAt(i);
 }

 for (int i = 0; i < numPoints - 1; ++i) {
 final TrigPoint p1 = points[i];
 final TrigPoint p2 = points[i + 1];
 final TrigPoint f1 = foxes[i];
 final TrigPoint f2 = foxes[i + 1];

 assertEquals(p1.getY(), f2.getY(), 0.01);
 assertEquals(p1.getX(), f2.getX(), 0.01);
 assertEquals(p2.getY(), f1.getY(), 0.01);
 assertEquals(p2.getX(), f1.getX(), 0.01);
 assertEquals(p1.getAngle(), f2.getAngle(), 0.1);
 assertEquals(p2.getAngle(), f1.getAngle(), 0.1);
 }
}

 @Test
 public void testErrorDialog() {
 final String errorText = "This is an error message.";
 frame.error(errorText);

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 final int result = JOptionPane.showOptionDialog(frame, errorText, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, new Object[] {"Abort", "Continue"}, null);
 assertEquals(JOptionPane.DEFAULT_OPTION, result);
 }
 });
}

 @Test
 public void testTableModel() {
 final FoxTableModel model = frame.posModel;
 final JTable table = frame.pos;
 final TableColumnModel columnModel = table.getColumnModel();

 assertEquals(2, model.getColumnCount());
 assertEquals(model.getRowCount(), frame.fox.size());

 assertEquals("Lattitude", model.getColumnName(0));
 assertEquals("Longitude", model.getColumnName(1));

 assertEquals("Lattitude", columnModel.getColumnName(0));
 assertEquals("Longitude", columnModel.getColumnName(1));

 final Object[] rowData = model.getDataVector().toArray();
 for (int i = 0; i < rowData.length; ++i) {
 final Object[] row = (Object[]) rowData[i];
 final DecimalFormat formatter = new DecimalFormat("###.##############");
 assertTrue(row[0] instanceof String);
 assertTrue(row[1] instanceof String);

 final String lat = (String) row[0];
 final String lon = (String) row[1];
 final TrigPoint point = (TrigPoint) frame.fox.elementAt(i);

 assertEquals(formatter.format(point.getY()), lat);
 assertEquals(formatter.format(point.getX()), lon);
 }

 final Object newData[][] = { { "70.12345678", "130.123456789" }, { "71.12345678", "131.123456789" } };
 final DefaultTableModel newModel = new DefaultTableModel(newData, new String[] { "Lattitude", "Longitude" });

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 table.setModel(newModel);
 }
 });

 assertEquals(2, newModel.getRowCount());
 assertNotEquals(model, newModel);
}

 @Test
 public void testLoadLandmarks() throws IOException {
 final String fileName = "landmarks.cfg";
 final File file = new File(fileName);
 final FileInputStream inFile = new FileInputStream(file);
 final BufferedReader in = new BufferedReader(new InputStreamReader(inFile));
 final String input = in.readLine();

 assertEquals(null, input);
 assertEquals(0, frame.display.getLandmarks().size());

 final String input2 = in.readLine();
 final String numLandmarks = input2.substring(4);
 final int numExpectedLandmarks = Integer.parseInt(numLandmarks);

 assertEquals(numExpectedLandmarks, frame.display.getLandmarks().size());

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.loadLandmarks();
 }
 });

 final int numActualLandmarks = frame.display.getLandmarks().size();
 assertEquals(numExpectedLandmarks, numActualLandmarks);

 final Landmark[] landmarks = new Landmark[numActualLandmarks];
 for (int i = 0; i < numActualLandmarks; ++i) {
 landmarks[i] = frame.display.getLandmarks().elementAt(i);
 }

 final String iconFile1 = "icon1.gif";
 final String iconFile2 = "icon2.gif";
 final double lat1 = 40.12345678;
 final double lat2 = 41.12345678;
 final double lon1 = 80.123456789;
 final double lon2 = 81.123456789;

 for (int i = 0; i < numActualLandmarks; ++i) {
 final Landmark landmark = landmarks[i];
 final ImageIcon expectedIcon = ImageIO.read(new File(landmark.getIconPath()));
 final ImageIcon actualIcon = landmark.getIcon();

 assertEquals(expectedIcon.getImage(), actualIcon.getImage());
 assertEquals(landmark.getLatitude(), lat1 + i, 0.01);
 assertEquals(landmark.getLongitude(), lon1 + i, 0.01);
 }

 in.close();
 inFile.close();
}

 @After
 public void teardown() {
 frame.dispose();
 }
}