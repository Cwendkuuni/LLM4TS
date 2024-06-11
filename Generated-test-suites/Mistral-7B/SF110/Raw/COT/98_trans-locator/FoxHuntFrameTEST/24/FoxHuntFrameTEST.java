package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
 frame = new FoxHuntFrame();
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.dispose();
 }
 });
 }

 @Test
 public void testConstructor() {
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testAddPoint_ValidInput() {
 final String theta = "45";
 final String lat = "51.507389";
 final String lon = "0.127795";
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.theta.setText(theta);
 frame.lat.setText(lat);
 frame.lon.setText(lon);
 frame.add.doClick();
 }
 });

 // Check if point was added to the list
 final int sizeBefore = frame.points.size();
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 final TrigPoint point = (TrigPoint) frame.points.lastElement();
 assertEquals(theta, point.getAngle());
 assertEquals(lat, String.valueOf(point.getY()));
 assertEquals(lon, String.valueOf(point.getX()));
 }
 });

 // Check if fox point was calculated and added to the list
 final int sizeAfter = frame.fox.size();
 assertEquals(sizeBefore + 1, sizeAfter);
 }

 @Test(expected = NumberFormatException.class)
 public void testAddPoint_InvalidThetaInput() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.theta.setText("abcd");
 frame.lat.setText("51.507389");
 frame.lon.setText("0.127795");
 frame.add.doClick();
 }
 });

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 final int size = frame.points.size();
 assertEquals(0, size);
 }
 });

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 final JOptionPane optionPane = JOptionPane.showOptionPane("Input is invalid", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
 assertEquals(JOptionPane.DEFAULT_OPTION, optionPane.getValue());
 }
 });
}

 @Test(expected = NumberFormatException.class)
 public void testAddPoint_InvalidLatInput() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.theta.setText("45");
 frame.lat.setText("abcd");
 frame.lon.setText("0.127795");
 frame.add.doClick();
 }
 });

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 final int size = frame.points.size();
 assertEquals(0, size);
 }
 });

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 final JOptionPane optionPane = JOptionPane.showOptionPane("Input is invalid", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
 assertEquals(JOptionPane.DEFAULT_OPTION, optionPane.getValue());
 }
 });
}

 @Test(expected = NumberFormatException.class)
 public void testAddPoint_InvalidLonInput() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.theta.setText("45");
 frame.lat.setText("51.507389");
 frame.lon.setText("abcd");
 frame.add.doClick();
 }
 });

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 final int size = frame.points.size();
 assertEquals(0, size);
 }
 });

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 final JOptionPane optionPane = JOptionPane.showOptionPane("Input is invalid", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
 assertEquals(JOptionPane.DEFAULT_OPTION, optionPane.getValue());
 }
 });
}

 @Test
 public void testLoadLandmarks() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.loadLandmarks();
 }
 });

 final int numLandmarks = frame.display.getLandmarks().size();
 assertTrue(numLandmarks > 0);
 }

 @Test
 public void testErrorMessage() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.error("Test error message");
 }
 });

 final int option = SwingUtilities.invokeLater(new Runnable() {
 @Override
 public int run() {
 final JOptionPane optionPane = JOptionPane.showOptionPane("", JOptionPane.PLAIN_MESSAGE);
 return optionPane.getValue();
 }
 });

 assertEquals(JOptionPane.DEFAULT_OPTION, option);
}

 @Test
 public void testTableModel() {
 final TableModel model = frame.posModel;
 final int numColumns = model.getColumnCount();
 final int numRows = model.getRowCount();

 assertEquals(2, numColumns);
 assertEquals(frame.fox.size(), numRows);
 }

 @Test
 public void testTableCellValue() {
 final DecimalFormat formatter = new DecimalFormat("###.##############");
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 final JTable table = frame.pos;
 final int row = 0;
 final int col = 0;
 final Object value = table.getValueAt(row, col);
 final String expected = formatter.format(frame.fox.elementAt(row).getY());
 assertEquals(expected, value);
 }
 });

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 final JTable table = frame.pos;
 final int row = 0;
 final int col = 1;
 final Object value = table.getValueAt(row, col);
 final String expected = formatter.format(frame.fox.elementAt(row).getX());
 assertEquals(expected, value);
 }
 });
}

 @Test
 public void testAddPoint_TwoPoints() {
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.theta.setText("45");
 frame.lat.setText("51.507389");
 frame.lon.setText("0.127795");
 frame.add.doClick();
 }
 });

 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 frame.theta.setText("60");
 frame.lat.setText("52.507389");
 frame.lon.setText("0.227795");
 frame.add.doClick();
 }
 });

 final int sizeBefore = frame.fox.size();
 SwingUtilities.invokeLater(new Runnable() {
 @Override
 public void run() {
 final TrigPoint fox1 = (TrigPoint) frame.fox.lastElement();
 final TrigPoint fox2 = (TrigPoint) frame.fox.lastElement();
 assertEquals(frame.points.elementAt(frame.points.size() - 2), fox1.getPreviousPoint());
 assertEquals(frame.points.elementAt(frame.points.size() - 1), fox2.getPreviousPoint());
 assertEquals(fox2, FoxCalc.calcFox(fox1, fox2));
 }
 });

 final int sizeAfter = frame.fox.size();
 assertEquals(sizeBefore + 1, sizeAfter);
}
}