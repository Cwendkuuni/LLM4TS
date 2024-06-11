package Joshua.FoxHunt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FoxHuntFrameTest {

 @Mock
 private HuntDisplay display;
 @Mock
 private FoxTableModel posModel;
 @Mock
 private JFrame frame;
 @Mock
 private JTextField theta;
 @Mock
 private JTextField lat;
 @Mock
 private JTextField lon;
 @Mock
 private JTextField zoom;
 @Mock
 private JButton add;
 @Mock
 private JTable pos;
 @Mock
 private JPanel panel1;
 @Mock
 private JPanel panel2;
 @Mock
 private JPanel panel3;
 @Mock
 private JPanel panel;
 @Mock
 private Vector points;
 @Mock
 private Vector fox;

 @InjectMocks
 private FoxHuntFrame frameUnderTest;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 when(theta.getText()).thenReturn("45");
 when(lat.getText()).thenReturn("40.7128");
 when(lon.getText()).thenReturn("74.0060");
 when(zoom.getText()).thenReturn("1");

 when(add.getActionCommands()).thenReturn("Add It!!!");
 when(add.doClick()).then(() -> frameUnderTest.addPoint());

 frameUnderTest = new FoxHuntFrame();
 frameUnderTest.display = display;
 frameUnderTest.posModel = posModel;
 frameUnderTest.points = points;
 frameUnderTest.fox = fox;
 frameUnderTest.pos = pos;
 frameUnderTest.panel1 = panel1;
 frameUnderTest.panel2 = panel2;
 frameUnderTest.panel3 = panel3;
 frameUnderTest.panel = panel;
 }

 @Test
 public void addPoint_ValidInput_AddPointToListsAndUpdateTable() {
 final TrigPoint expectedPoint = new TrigPoint(40.7128, 74.006, 45);
 final TrigPoint expectedFox = new TrigPoint(40.7128 + 0.001, 74.0060 + 0.001, 45 + 5);

 when(posModel.getRowCount()).thenReturn(0);

 frameUnderTest.actionPerformed(new ActionEvent(add, 0, "Add It!!!"));

 assertEquals(1, points.size());
 assertEquals(expectedPoint, points.elementAt(0));
 assertEquals(1, fox.size());
 assertEquals(expectedFox, fox.elementAt(0));

 verify(display).addPoint(expectedPoint);
 verify(posModel).fireTableDataChanged();
 }

 @Test(expected = NumberFormatException.class)
 public void addPoint_InvalidLatInput_ThrowNumberFormatException() {
 when(lat.getText()).thenReturn("dd.mm.mmm.ddd");

 frameUnderTest.actionPerformed(new ActionEvent(add, 0, "Add It!!!"));
 }

 @Test(expected = NumberFormatException.class)
 public void addPoint_InvalidLonInput_ThrowNumberFormatException() {
 when(lon.getText()).thenReturn("dddddddd.ddd");

 frameUnderTest.actionPerformed(new ActionEvent(add, 0, "Add It!!!"));
 }

 @Test(expected = NumberFormatException.class)
 public void addPoint_InvalidThetaInput_ThrowNumberFormatException() {
 when(theta.getText()).thenReturn("45s");

 frameUnderTest.actionPerformed(new ActionEvent(add, 0, "Add It!!!"));
 }

 @Test
 public void addPoint_FirstTwoPoints_CalculateFoxCoordinates() {
 final TrigPoint point1 = new TrigPoint(40.7128, 74.006, 45);
 final TrigPoint point2 = new TrigPoint(40.713, 74.0061, 45);
 final TrigPoint expectedFox = new TrigPoint(40.7128 + 0.0009353, 74.006 + 0.0004561, 90 + 0.0004561);

 when(posModel.getRowCount()).thenReturn(1);

 frameUnderTest.actionPerformed(new ActionEvent(add, 0, "Add It!!!"));

 assertEquals(2, points.size());
 assertEquals(point1, points.elementAt(0));
 assertEquals(point2, points.elementAt(1));
 assertEquals(1, fox.size());
 assertEquals(expectedFox, fox.elementAt(0));
 }

 @Test
 public void addPoint_ThirdPoint_AddToFoxVectorAndUpdateTable() {
 final TrigPoint point1 = new TrigPoint(40.7128, 74.006, 45);
 final TrigPoint point2 = new TrigPoint(40.713, 74.0061, 45);
 final TrigPoint point3 = new TrigPoint(40.714, 74.0062, 45);
 final TrigPoint expectedFox = new TrigPoint(40.7128 + 0.0018728, 74.006 + 0.000936, 90 + 0.0018728);

 when(posModel.getRowCount()).thenReturn(2);

 frameUnderTest.actionPerformed(new ActionEvent(add, 0, "Add It!!!"));

 assertEquals(3, points.size());
 assertEquals(point1, points.elementAt(0));
 assertEquals(point2, points.elementAt(1));
 assertEquals(point3, points.elementAt(2));
 assertEquals(2, fox.size());
 assertEquals(expectedFox, fox.elementAt(1));
 }
}