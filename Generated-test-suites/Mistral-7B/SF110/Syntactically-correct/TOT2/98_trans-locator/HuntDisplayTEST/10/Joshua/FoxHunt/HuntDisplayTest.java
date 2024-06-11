package Joshua.FoxHunt.test;

import org.junit.*;
import static org.junit.Assert.*;
import Joshua.FoxHunt.*;

public class HuntDisplayTest {

@Before
public void setUp() {
}

@After
public void tearDown() {
}

@Test
public void testHuntDisplayConstructor() {
    HuntDisplay display = new HuntDisplay();
    assertNotNull(display);
}

@Test
public void testGetPreferredSize() {
    HuntDisplay display = new HuntDisplay();
    Dimension size = display.getPreferredSize();
    assertEquals(200, size.getHeight());
    assertEquals(200, size.getWidth());
}

@Test
public void testGetMinimumSize() {
    HuntDisplay display = new HuntDisplay();
    Dimension size = display.getMinimumSize();
    assertEquals(50, size.getHeight());
    assertEquals(50, size.getWidth());
}

@Test
public void testAddPoint() {
    HuntDisplay display = new HuntDisplay();
    TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
    display.addPoint(point);
    assertEquals(1, display.points.size());

    // Test with null point
    HuntDisplay displayNullPoint = new HuntDisplay();
    displayNullPoint.addPoint(null);

    // Test with the same point added multiple times
    HuntDisplay displaySamePoint = new HuntDisplay();
    TrigPoint pointSame = new TrigPoint(1.0, 2.0, 0.0, false);
    displaySamePoint.addPoint(pointSame);
    displaySamePoint.addPoint(pointSame);
    assertEquals(1, displaySamePoint.points.size());
}

@Test
public void testAddLandMark() {
    HuntDisplay display = new HuntDisplay();
    Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
    display.addLandMark(landmark);
    assertEquals(1, display.landmarks.size());

    // Test with null landmark
    HuntDisplay displayNullLandmark = new HuntDisplay();
    displayNullLandmark.addLandMark(null);

    // Test with the same landmark added multiple times
    HuntDisplay displaySameLandmark = new HuntDisplay();
    Landmark landmarkSame = new Landmark(1.0, 2.0, new ImageIcon("icon.png"));
    displaySameLandmark.addLandMark(landmarkSame);
    displaySameLandmark.addLandMark(landmarkSame);
    assertEquals(1, displaySameLandmark.landmarks.size());
}

@Test
public void testPaintComponent() {
    HuntDisplay display = new HuntDisplay();
    Graphics2D g = mock(Graphics2D.class);
    display.paintComponent(g);
    verify(g).setColor(display.getBackground());
    verify(g).fillRect(0, 0, display.getWidth(), display.getHeight());
}

}