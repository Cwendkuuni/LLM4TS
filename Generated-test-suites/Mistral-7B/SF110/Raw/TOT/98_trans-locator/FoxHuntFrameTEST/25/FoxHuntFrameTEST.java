package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private JTextField theta;
 private JTextField lat;
 private JTextField lon;
 private JButton add;
 private JTable pos;
 private FoxTableModel posModel;
 private HuntDisplay display;
 private JPanel panel1;
 private JPanel panel2;
 private JPanel panel3;
 private JPanel panel;
 private Vector points;
 private Vector fox;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 theta = mock(JTextField.class);
 lat = mock(JTextField.class);
 lon = mock(JTextField.class);
 add = mock(JButton.class);
 pos = mock(JTable.class);
 posModel = mock(FoxTableModel.class);
 display = mock(HuntDisplay.class);
 points = new Vector();
 fox = new Vector();
 when(theta.getText()).thenReturn("180");
 when(lat.getText()).thenReturn("37.42123333");
 when(lon.getText()).thenReturn("122.08425152");
 when(add.getActionCommands()).thenReturn("Add It!!!");
 doReturn(frame).when(frame).getPoints();
 doReturn(frame).when(frame).getFox();
 doReturn(points).when(frame).getPoints();
 doReturn(fox).when(frame).getFox();
 doReturn(theta).when(frame).getTheta();
 doReturn(lat).when(frame).getLat();
 doReturn(lon).when(frame).getLon();
 doReturn(add).when(frame).getAdd();
 doReturn(pos).when(frame).getPos();
 doReturn(posModel).when(frame).getPosModel();
 doReturn(display).when(frame).getDisplay();
 doReturn(panel1).when(frame).getPanel1();
 doReturn(panel2).when(frame).getPanel2();
 doReturn(panel3).when(frame).getPanel3();
 doReturn(panel).when(frame).getPanel();
 }

 @Test
 public void testFoxHuntFrameConstruction() {
 assertTrue(frame instanceof FoxHuntFrame);
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testActionPerformed() {
 frame.actionPerformed(new ActionEvent("Add It!!!", 0, "Add It!!!"));
 verify(display).addPoint(any(TrigPoint.class));
 verifyNoMoreInteractions(display);
 }

 @Test
 public void testAddPoint() {
 final double expectedLAT = 37.42123333;
 final double expectedLON = 122.08425152;
 final double expectedAngle = 180.0;
 final TrigPoint expectedPoint = new TrigPoint(expectedLAT, expectedLON, expectedAngle);
 frame.addPoint();
 verify(points).add(expectedPoint);
 verify(display).addPoint(expectedPoint);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithInvalidData() {
 when(theta.getText()).thenReturn("X");
 when(lat.getText()).thenReturn("37.42123333");
 when(lon.getText()).thenReturn("122.08425152");
 frame.addPoint();
 verifyNoInteractions(points);
 verifyNoInteractions(display);
 }

 @Test
 public void testAddPointWithTwoPoints() {
 frame.addPoint();
 frame.addPoint();
 TrigPoint point1 = (TrigPoint) frame.getPoints().elementAt(0);
 TrigPoint point2 = (TrigPoint) frame.getPoints().elementAt(1);
 TrigPoint expectedFox = FoxCalc.calcFox(point1, point2);
 verify(fox).add(expectedFox);
 verify(posModel).fireTableDataChanged();
 verifyNoMoreInteractions(display);
 }

 @Test
 public void testLoadLandmarks() {
 // TODO: Implement this test
 }

 @Test
 public void testError() {
 frame.error("Can't Find the file");
 verify(JOptionPane.showOptionDialog).withArguments(any(Component.class), "Error", "Error", anyInt(), anyInt(), any(Object[].class), any(Object[].class), any(Object.class));
 }
}