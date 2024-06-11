import org.junit.Test;
import static org.junit.Assert.*;
import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.TrigPoint;
import Joshua.FoxHunt.Landmark;
import java.awt.Image;

public class HuntDisplayTest {

 private HuntDisplay huntDisplay;

 @Before
 public void setUp() {
 huntDisplay = new HuntDisplay();
 }

 @Test
 public void testAddPoint() {
 TrigPoint trigPoint = new TrigPoint(1.0, 2.0, 0.0, false);
 huntDisplay.addPoint(trigPoint);
 assertEquals(1, huntDisplay.points.size());
 }

 @Test
 public void testAddMultiplePoints() {
 TrigPoint trigPoint1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint trigPoint2 = new TrigPoint(3.0, 4.0, 0.0, false);
 huntDisplay.addPoint(trigPoint1);
 huntDisplay.addPoint(trigPoint2);
 assertEquals(2, huntDisplay.points.size());
 }

 @Test
 public void testAddLandMark() {
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/icon.png")));
 huntDisplay.addLandMark(landmark);
 assertEquals(1, huntDisplay.landmarks.size());
 }

 @Test
 public void testAddMultipleLandMarks() {
 Landmark landmark1 = new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/icon.png")));
 Landmark landmark2 = new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/icon.png")));
 huntDisplay.addLandMark(landmark1);
 huntDisplay.addLandMark(landmark2);
 assertEquals(2, huntDisplay.landmarks.size());
 }

 @Test
 public void testGetPreferredSize() {
 Dimension preferredSize = huntDisplay.getPreferredSize();
 assertEquals(200, preferredSize.getWidth());
 assertEquals(200, preferredSize.getHeight());
 }

 @Test
 public void testGetMinimumSize() {
 Dimension minimumSize = huntDisplay.getMinimumSize();
 assertEquals(50, minimumSize.getWidth());
 assertEquals(50, minimumSize.getHeight());
 }

 @Test
 public void testPaintComponentWithNoPointsOrLandmarks() {
 HuntDisplay spy = spy(huntDisplay);
 doCallRealMethod().andReturn(true); // return true from repaint
 Captor imageCaptor = ArgumentCaptor.forClass(BufferedImage.class);
 huntDisplay.paintComponent(eq(spy.mock.getMockControl().getMockForConcrete(Graphics.class)));
 verifyZeroInteractions(spy);
 }

 @Test
 public void testPaintComponentWithPoints() {
 HuntDisplay spy = spy(huntDisplay);
 TrigPoint[] trigPoints = {new TrigPoint(1.0, 2.0, 0.0, false), new TrigPoint(3.0, 4.0, 0.0, false), new TrigPoint(5.0, 6.0, 0.0, false)};
 doCallRealMethod().andAnswer(invocation -> {
 trigPoints[0].setX(trigPoints[0].getX() + 0.5);
 trigPoints[0].setY(trigPoints[0].getY() + 0.5);
 return true;
 });
 for (TrigPoint trigPoint : trigPoints) {
 huntDisplay.addPoint(trigPoint);
 }
 doCallRealMethod().andReturn(true); // return true from repaint
 Captor imageCaptor = ArgumentCaptor.forClass(BufferedImage.class);
 huntDisplay.paintComponent(eq(spy.mock.getMockControl().getMockForConcrete(Graphics.class)));
 verify(spy).getBackground(); // verify getBackground call
 for (TrigPoint trigPoint : trigPoints) {
 verify(spy).drawImage(imageCaptor.capture(any(Image.class)), argThat(arg -> arg.intValue() >= 0 && arg.intValue() < huntDisplay.getWidth()), argThat(arg -> arg.intValue() >= 0 && arg.intValue() < huntDisplay.getHeight()), anyInt(), anyInt(), eq(huntDisplay.getBackground()), any(Graphics.class));
 }
 }

 @Test
 public void testPaintComponentWithLandmarks() {
 HuntDisplay spy = spy(huntDisplay);
 Landmark[] landmarks = {new Landmark(1.0, 2.0, new ImageIcon(getClass().getResource("/icon.png"))), new Landmark(3.0, 4.0, new ImageIcon(getClass().getResource("/icon.png"))), new Landmark(5.0, 6.0, new ImageIcon(getClass().getResource("/icon.png")))};
 doCallRealMethod().andAnswer(invocation -> {
 landmarks[0].setLon(landmarks[0].getLon() + 0.5);
 landmarks[0].setLat(landmarks[0].getLat() + 0.5);
 return true;
 });
 for (Landmark landmark : landmarks) {
 huntDisplay.addLandMark(landmark);
 }
 doCallRealMethod().andReturn(true); // return true from repaint
 Captor imageCaptor = ArgumentCaptor.forClass(BufferedImage.class);
 huntDisplay.paintComponent(eq(spy.mock.getMockControl().getMockForConcrete(Graphics.class)));
 verify(spy).getBackground(); // verify getBackground call
 for (Landmark landmark : landmarks) {
 verify(spy).drawImage(imageCaptor.capture(any(Image.class)), argThat(arg -> arg.intValue() >= 0 && arg.intValue() < huntDisplay.getWidth()), argThat(arg -> arg.intValue() >= 0 && arg.intValue() < huntDisplay.getHeight()), anyInt(), anyInt(), any(Image.class), any(Image.class));
 }
 }
}