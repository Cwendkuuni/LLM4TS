package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import Joshua.FoxHunt.*;

@RunWith(MockitoJUnitRunner.class)
public class FoxHuntFrameTest {

 private FoxHuntFrame frame;
 private HuntDisplay display;
 private FoxTableModel tableModel;

 @Before
 public void setUp() {
 frame = new FoxHuntFrame();
 display = Mockito.mock(HuntDisplay.class);
 tableModel = new FoxTableModel();
 frame.display = display;
 frame.posModel = tableModel;
 }

 @Test
 public void testAddPoint() {
 final double lat = 40.7128;
 final double lon = -74.0060;
 final int angle = 90;
 frame.theta.setText(String.valueOf(angle));
 frame.lat.setText(String.valueOf(lat));
 frame.lon.setText(String.valueOf(lon));
 frame.addActionPerformed(Mockito.mock(ActionEvent.class));
 assertEquals(1, frame.points.size());
 assertEquals(1, frame.fox.size());
 final TrigPoint expectedPoint = new TrigPoint(lat, lon, angle);
 assertEquals(expectedPoint, frame.points.lastElement());
 final TrigPoint expectedFox = FoxCalc.calcFox(expectedPoint, expectedPoint);
 assertEquals(expectedFox, frame.fox.lastElement());
 }

 @Test
 public void testLoadLandmarks() {
 final int numLandmarks = 3;
 final String[] iconFiles = new String[numLandmarks];
 final double[] lat = new double[numLandmarks];
 final double[] lon = new double[numLandmarks];
 final Landmark[] landmarks = new Landmark[numLandmarks];
 for (int i = 0; i < numLandmarks; i++) {
 lat[i] = i * 0.01;
 lon[i] = i * 0.02;
 iconFiles[i] = "icon" + i + ".gif";
 landmarks[i] = new Landmark(lat[i], lon[i], new ImageIcon(iconFiles[i]));
 }
 final File landmarksFile = Mockito.mock(File.class);
 final FileInputStream inputStream = Mockito.mock(FileInputStream.class);
 final BufferedReader reader = Mockito.mock(BufferedReader.class);
 try {
 when(landmarksFile.exists()).thenReturn(true);
 when(landmarksFile.isFile()).thenReturn(true);
 when(landmarksFile.length()).thenReturn(1024L);
 when(inputStream.available()).thenReturn(1024);
 when(reader.readLine()).thenAnswer(i -> i.equals("") ? null : i);
 when(reader.readLine()).thenAnswer(i -> {
 String line = i.answer();
 if (line == null) {
 return null;
 }
 final int startIndex = line.indexOf(":");
 if (startIndex == -1) {
 throw new IndexOutOfBoundsException();
 }
 final String key = line.substring(0, startIndex);
 final String value = line.substring(startIndex + 1).trim();
 if (key.equalsIgnoreCase("FILE")) {
 iconFiles[0] = value;
 } else if (key.equalsIgnoreCase("LAT")) {
 lat[0] = Double.parseDouble(value);
 } else if (key.equalsIgnoreCase("LON")) {
 lon[0] = Double.parseDouble(value);
 }
 return null;
 });
 frame.loadLandmarks(landmarksFile);
 verify(display, times(1)).addLandMark(landmarks[0]);
 } catch (IOException e) {
 fail();
 }
 }

 @Test
 public void testError() {
 final String errorMessage = "An error occurred.";
 frame.error(errorMessage);
 verify(display, times(1)).showErrorDialog(eq(frame), eq(errorMessage));
 }
}