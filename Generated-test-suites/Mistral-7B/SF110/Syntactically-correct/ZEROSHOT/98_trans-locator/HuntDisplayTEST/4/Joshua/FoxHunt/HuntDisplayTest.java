package Joshua.FoxHunt.Test;

import static org.junit.Assert.*;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

 private HuntDisplay display;

 @Test
 public void testHuntDisplayConstruction() {
 // Test constructing an instance of the HuntDisplay
 display = new HuntDisplay();
 assertNotNull(display);
 }

 @Test
 public void testGetPreferredSize() {
 // Test getting the preferred size of the HuntDisplay
 display = new HuntDisplay();
 assertEquals(200, display.getPreferredSize().getHeight());
 assertEquals(200, display.getPreferredSize().getWidth());
 }

 @Test
 public void testGetMinimumSize() {
 // Test getting the minimum size of the HuntDisplay
 display = new HuntDisplay();
 assertEquals(50, display.getMinimumSize().getHeight());
 assertEquals(50, display.getMinimumSize().getWidth());
 }

 @Test
 public void testAddPoint() {
 // Test adding a point to the HuntDisplay
 display = new HuntDisplay();
 TrigPoint point = new TrigPoint(1.0, 2.0, 0.0, false);
 display.addPoint(point);
 // Test that the point was added to the display
 assertEquals(1, display.points.size());
 // Test that the point was drawn in the correct location
 display.repaint();
 // Adding some stub code to check the drawing here is not feasible,
 // as we don't have a way to test the JComponent drawing logic directly
 // in JUnit.
 }

 @Test
 public void testAddLandMark() {
 // Test adding a landmark to the HuntDisplay
 display = new HuntDisplay();
 Landmark landmark = new Landmark(1.0, 2.0, new ImageIcon("path/to/image.png"));
 display.addLandMark(landmark);
 // Test that the landmark was added to the display
 assertEquals(1, display.landmarks.size());
 // Test that the image was drawn in the correct location
 display.repaint();
 // Adding some stub code to check the drawing here is not feasible,
 // as we don't have a way to test the JComponent drawing logic directly
 }

 @Test
 public void testPaintComponent() {
 // Test painting the HuntDisplay with no points or landmarks
 display = new HuntDisplay();
 Graphics2D g = (Graphics2D)display.createGraphic();
 display.paintComponent(g);
 // Adding some stub code to check the drawing here is not feasible,
 // as we don't have a way to test the JComponent drawing logic directly

 // Test painting the HuntDisplay with points
 TrigPoint p1 = new TrigPoint(1.0, 2.0, 0.0, false);
 TrigPoint p2 = new TrigPoint(3.0, 4.0, 0.0, false);
 display.addPoint(p1);
 display.addPoint(p2);
 display.repaint();
 // Adding some stub code to check the drawing here is not feasible,
 // as we don't have a way to test the JComponent drawing logic directly

 // Test painting the HuntDisplay with points and landmarks
 Landmark l1 = new Landmark(1.5, 2.5, new ImageIcon("path/to/image.png"));
 display.addLandMark(l1);
 display.addPoint(p1);
 display.addPoint(p2);
 display.repaint();
 // Adding some stub code to check the drawing here is not feasible,
 // as we don't have a way to test the JComponent drawing logic directly
 }

}