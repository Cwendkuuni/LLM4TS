package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import Joshua.FoxHunt.FoxHuntFrame;
import Joshua.FoxHunt.FoxTableModel;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

@RunWith(MockitoJUnitRunner.class)
public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private HuntDisplay displayMock;
 private FoxTableModel modelMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 displayMock = Mockito.mock(HuntDisplay.class);
 modelMock = Mockito.mock(FoxTableModel.class);
 frame = new FoxHuntFrame() {
 @Override
 public HuntDisplay getDisplay() {
 return displayMock;
 }

 @Override
 public FoxTableModel getPosModel() {
 return modelMock;
 }
 };
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testFoxHuntFrameConstruction() {
 assertNotNull(frame);
 assertEquals("Triangulation", frame.getTitle());
 }

 @Test
 public void testAddPointActionPerformed() {
 final JTextField theta = Mockito.mock(JTextField.class);
 final JTextField lat = Mockito.mock(JTextField.class);
 final JTextField lon = Mockito.mock(JTextField.class);
 final JButton add = Mockito.mock(JButton.class);
 final JTable table = Mockito.mock(JTable.class);
 final FoxTableModel tableModel = Mockito.mock(FoxTableModel.class);

 Mockito.when(lat.getText()).thenReturn("51.5074");
 Mockito.when(lon.getText()).thenReturn("0.1278");
 Mockito.when(theta.getText()).thenReturn("180");
 Mockito.when(frame.getPos()).thenReturn(table);
 Mockito.when(frame.getPosModel()).thenReturn(tableModel);
 Mockito.when(frame.getTheta()).thenReturn(theta);
 Mockito.when(frame.getLat()).thenReturn(lat);
 Mockito.when(frame.getLon()).thenReturn(lon);
 Mockito.when(frame.getAdd()).thenReturn(add);

 frame.addPoint();

 verify(displayMock).addPoint(Mockito.any(TrigPoint.class));
 assertTrue(frame.points.size() == 1);
 assertTrue(frame.fox.size() == 0);
}

 @Test
 public void testAddPointWhenTwoPointsAreEntered() {
 final JTextField theta = Mockito.mock(JTextField.class);
 final JTextField lat1 = Mockito.mock(JTextField.class);
 final JTextField lon1 = Mockito.mock(JTextField.class);
 final JTextField lat2 = Mockito.mock(JTextField.class);
 final JTextField lon2 = Mockito.mock(JTextField.class);
 final JButton add = Mockito.mock(JButton.class);
 final JTable table = Mockito.mock(JTable.class);
 final FoxTableModel tableModel = Mockito.mock(FoxTableModel.class);

 Mockito.when(lat1.getText()).thenReturn("51.5074");
 Mockito.when(lon1.getText()).thenReturn("0.1278");
 Mockito.when(theta.getText()).thenReturn("180");
 Mockito.when(lat2.getText()).thenReturn("51.5174");
 Mockito.when(lon2.getText()).thenReturn("0.1288");
 Mockito.when(frame.getPos()).thenReturn(table);
 Mockito.when(frame.getPosModel()).thenReturn(tableModel);
 Mockito.when(frame.getTheta()).thenReturn(theta);
 Mockito.when(frame.getLat()).thenReturn(lat1);
 Mockito.when(frame.getLon()).thenReturn(lon1);
 Mockito.when(frame.getAdd()).thenReturn(add);

 frame.addPoint();
 frame.addPoint();

 verify(displayMock, times(2)).addPoint(Mockito.any(TrigPoint.class));
 assertTrue(frame.points.size() == 2);
 assertTrue(frame.fox.size() == 1);

 final TrigPoint foxPoint = (TrigPoint) frame.fox.get(0);
 assertEquals(foxPoint.getX(), 51.5074, 0.0001);
 assertEquals(foxPoint.getY(), 51.5174, 0.0001);
}

 @Test
 public void testErrorDialogIsShownWhenInvalidInputIsEntered() {
 final JTextField theta = Mockito.mock(JTextField.class);
 final JTextField lat = Mockito.mock(JTextField.class);
 final JTextField lon = Mockito.mock(JTextField.class);
 final JButton add = Mockito.mock(JButton.class);
 final JFrame frameMock = Mockito.mock(JFrame.class);

 Mockito.when(lat.getText()).thenReturn("51.5074");
 Mockito.when(lon.getText()).thenReturn("0.1278");
 Mockito.when(theta.getText()).thenReturn("abc");
 Mockito.when(frame.getTheta()).thenReturn(theta);
 Mockito.when(frame.getLat()).thenReturn(lat);
 Mockito.when(frame.getLon()).thenReturn(lon);
 Mockito.when(frame.getAdd()).thenReturn(add);
 Mockito.when(frame.getFrame()).thenReturn(frameMock);

 frame.addPoint();

 verify(frameMock, times(1)).setVisible(Mockito.anyBoolean());
 verify(frameMock, times(1)).setDefaultCloseOperation(3);
 verify(frameMock, times(1)).pack();
 verifyZeroInteractions(displayMock);
 verify(tableModel, times(0)).fireTableDataChanged();
}

 @Test
 public void testLoadLandmarks() {
 final String[] iconFile = new String[] { "icon1.gif", "icon2.gif" };
 final double[] lat = new double[] { 51.5074, 51.5174 };
 final double[] lon = new double[] { 0.1278, 0.1288 };
 final Landmark landmark1 = Mockito.mock(Landmark.class);
 final Landmark landmark2 = Mockito.mock(Landmark.class);

 Mockito.when(displayMock.addLandMark(Mockito.any(Landmark.class))).thenAnswer(invocation -> {
 if (invocation.getArgument(0) == landmark1) {
 return;
 }
 landmark2;
 return;
 });

 frame.loadLandmarks();

 verify(displayMock, times(2)).addLandMark(Mockito.any(Landmark.class));
}
}