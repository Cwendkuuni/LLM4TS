package Joshua.FoxHunt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class FoxHuntFrameTest {

 private FoxHuntFrame frame;

 @Before
 public void setUp() {
 this.frame = new FoxHuntFrame();
 }

 @Test
 public void testAddPointWithValidValues() {
 // Given: Valid values for theta, lat, and lon
 this.frame.theta.setText("35");
 this.frame.lat.setText("51.5074");
 this.frame.lon.setText("-0.1278");

 // When: Add point button is clicked
 this.frame.add.doClick();

 // Then: Point is added to the points vector and table, and fox point is calculated
 assertEquals(2, this.frame.points.size());
 assertEquals(1, this.frame.fox.size());
 }

 @Test
 public void testAddPointWithInvalidValues() {
 // Given: Invalid value for theta
 this.frame.theta.setText("invalid");
 this.frame.lat.setText("51.5074");
 this.frame.lon.setText("-0.1278");

 // When: Add point button is clicked
 this.frame.add.doClick();

 // Then: Error message is displayed and no points are added
 assertEquals(0, this.frame.points.size());
 assertEquals(0, this.frame.fox.size());
 }

 @Test
 public void testAddPointWithEmptyFields() {
 // Given: Empty fields for theta, lat, and lon
 this.frame.theta.setText("");
 this.frame.lat.setText("");
 this.frame.lon.setText("");

 // When: Add point button is clicked
 this.frame.add.doClick();

 // Then: Error message is displayed and no points are added
 assertEquals(0, this.frame.points.size());
 assertEquals(0, this.frame.fox.size());
 }

 @Test
 public void testAddPointWithInvalidLatLongFormat() {
 // Given: Invalid format for lat or lon
 this.frame.theta.setText("45");
 this.frame.lat.setText("51.5074 dd");
 this.frame.lon.setText("-0.1278 ddd");

 // When: Add point button is clicked
 this.frame.add.doClick();

 // Then: Error message is displayed and no points are added
 assertEquals(0, this.frame.points.size());
 assertEquals(0, this.frame.fox.size());
 }
}