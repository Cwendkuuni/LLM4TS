import de.progra.charting.*;
import de.progra.charting.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoordSystemUtilitiesTest {

 private CoordSystemUtilities utils;
 private ChartDataModelConstraints constraints;
 private ChartDataModelConstraints constraints2;
 private ChartDataModel model;
 private CoordSystem coord;

 @Before
 public void setup() {
 constraints = new ChartDataModelConstraints();
 constraints2 = new ChartDataModelConstraints();
 model = new ChartDataModel();
 coord = new CoordSystem(new Rectangle2D.Double(0, 0, 400, 400), model, constraints, constraints2);
 utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
 }

 @Test
 public void testComputeLeftMargin() {
 double xmin = -10.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 assertEquals(44, utils.computeLeftMargin()); // basic functionality

 xmin = -20.0;
 xmax = 20.0;
 ymin = -10.0;
 ymax = 20.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 assertTrue(utils.computeLeftMargin() > 30); // edge case: wide range

 xmin = 0.0;
 xmax = 10.0;
 ymin = -10.0;
 ymax = 0.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 assertEquals(20, utils.computeLeftMargin()); // edge case: negative y-axis

 xmin = 0.0;
 xmax = 10.0;
 ymin = 0.0;
 ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 assertTrue(utils.computeLeftMargin() <= 44); // error scenario: margin is too small
 }

 @Test
 public void testComputeRightMargin() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 assertEquals(15, utils.computeRightMargin()); // basic functionality

 xmin = -10.0;
 xmax = 10.0;
 ymin = 0.0;
 ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 assertTrue(utils.computeRightMargin() > 15); // edge case: wide range
 }

 @Test
 public void testComputeTopMargin() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = -10.0;
 double ymax = 0.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 assertEquals(15, utils.computeTopMargin()); // basic functionality

 xmin = 0.0;
 xmax = 10.0;
 ymin = 0.0;
 ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 assertTrue(utils.computeTopMargin() > 15); // edge case: positive y-axis
 }

 @Test
 public void testComputeBottomMargin() {
 double xmin = 0.0;
 double xmax = 10.0;
 double ymin = 0.0;
 double ymax = 10.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 assertEquals(44, utils.computeBottomMargin()); // basic functionality

 xmin = 0.0;
 xmax = 10.0;
 ymin = -10.0;
 ymax = 0.0;
 constraints.setMinimumColumnValue(xmin);
 constraints.setMaximumColumnValue(xmax);
 constraints.setMinimumValue(new DoubleValue(ymin));
 constraints.setMaximumValue(new DoubleValue(ymax));
 assertTrue(utils.computeBottomMargin() > 44); // edge case: negative y-axis
 }

 @Test(expected = IllegalArgumentException.class)
 public void testComputeLeftMarginWithInvalidConstraints() {
 constraints = null;
 assertEquals(20, utils.computeLeftMargin()); // error scenario: null constraints
 }
}