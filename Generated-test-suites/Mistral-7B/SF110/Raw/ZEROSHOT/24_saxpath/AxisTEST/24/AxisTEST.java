package org.saxpath.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class AxisTest {

 @Test
 public void testLookupAxisNumber() {
 int axisNumber = Axis.CHILD;
 String expectedAxisName = "child";
 String actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.DESCENDANT;
 expectedAxisName = "descendant";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.PARENT;
 expectedAxisName = "parent";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.ANCESTOR;
 expectedAxisName = "ancestor";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.FOLLOWING_SIBLING;
 expectedAxisName = "following-sibling";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.PRECEDING_SIBLING;
 expectedAxisName = "preceding-sibling";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.FOLLOWING;
 expectedAxisName = "following";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.PRECEDING;
 expectedAxisName = "preceding";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.ATTRIBUTE;
 expectedAxisName = "attribute";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.NAMESPACE;
 expectedAxisName = "namespace";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.SELF;
 expectedAxisName = "self";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.DESCENDANT_OR_SELF;
 expectedAxisName = "descendant-or-self";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = Axis.ANCESTOR_OR_SELF;
 expectedAxisName = "ancestor-or-self";
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertEquals(expectedAxisName, actualAxisName);

 axisNumber = 14; // invalid axis number
 expectedAxisName = null;
 actualAxisName = Axis.lookup(Axis.lookup(axisNumber));
 assertNull(actualAxisName);
 }

 @Test
 public void testLookupAxisName() {
 String axisName = "child";
 int expectedAxisNumber = Axis.CHILD;
 int actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "descendant";
 expectedAxisNumber = Axis.DESCENDANT;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "parent";
 expectedAxisNumber = Axis.PARENT;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "ancestor";
 expectedAxisNumber = Axis.ANCESTOR;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "following-sibling";
 expectedAxisNumber = Axis.FOLLOWING_SIBLING;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "preceding-sibling";
 expectedAxisNumber = Axis.PRECEDING_SIBLING;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "following";
 expectedAxisNumber = Axis.FOLLOWING;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "preceding";
 expectedAxisNumber = Axis.PRECEDING;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "attribute";
 expectedAxisNumber = Axis.ATTRIBUTE;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "namespace";
 expectedAxisNumber = Axis.NAMESPACE;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "self";
 expectedAxisNumber = Axis.SELF;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "descendant-or-self";
 expectedAxisNumber = Axis.DESCENDANT_OR_SELF;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "ancestor-or-self";
 expectedAxisNumber = Axis.ANCESTOR_OR_SELF;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);

 axisName = "invalidAxisName"; // invalid axis name
 expectedAxisNumber = 0;
 actualAxisNumber = Axis.lookup(axisName);
 assertEquals(expectedAxisNumber, actualAxisNumber);
 }
}