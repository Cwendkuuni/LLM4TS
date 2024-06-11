package org.apache.commons.math3.geometry.euclidean.twod.tests;

import junit.framework.Assert;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {

        // Test construction from hyperplane and remaining region
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 2.0), new Vector2D(3.0, 4.0)).getImplicitForm();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(1.0, 2.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        Assert.assertEquals("Incorrect hyperplane.", hyperplane, subLine.getHyperplane());
        Assert.assertEquals("Incorrect remaining region.", remainingRegion, subLine.getRemainingRegion());

        // Test construction from two points
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        SubLine subLine2 = new SubLine(start, end);

        Line line = (Line) subLine2.getHyperplane();
        List<Segment> segments = subLine2.getSegments();

        Assert.assertEquals("Incorrect number of segments.", 1, segments.size());
        Assert.assertEquals("Incorrect start point of segment 0.", start, segments.get(0).getStart());
        Assert.assertEquals("Incorrect end point of segment 0.", end, segments.get(0).getEnd());
        Assert.assertEquals("Incorrect line for sub-line.", line, segments.get(0).getLine());

        // Test construction from a segment
        Segment segment = new Segment(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0), line);
        SubLine subLine3 = new SubLine(segment);

        List<Segment> segments3 = subLine3.getSegments();

        Assert.assertEquals("Incorrect number of segments.", 1, segments3.size());
        Assert.assertEquals("Incorrect start point of segment 0.", segment.getStart(), segments3.get(0).getStart());
        Assert.assertEquals("Incorrect end point of segment 0.", segment.getEnd(), segments3.get(0).getEnd());
        Assert.assertEquals("Incorrect line for sub-line.", line, segments3.get(0).getLine());

    }

    @Test
    public void testGetSegments() {

        // Test getSegments() method for different cases

        // Case 1: Line with infinite endpoints
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(0.0, Double.POSITIVE_INFINITY)));

        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals("Incorrect number of segments.", 1, segments.size());
        Assert.assertEquals("Incorrect start point of segment 0.", new Vector2D(0.0, 0.0), segments.get(0).getStart());
        Assert.assertEquals("Incorrect end point of segment 0.", new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), segments.get(0).getEnd());

        // Case 2: Line with finite endpoints
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        Line line2 = new Line(start, end);
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));

        List<Segment> segments2 = subLine2.getSegments();

        Assert.assertEquals("Incorrect number of segments.", 2, segments2.size());
        Assert.assertEquals("Incorrect start point of segment 0.", start, segments2.get(0).getStart());
        Assert.assertEquals("Incorrect end point of segment 0.", new Vector2D(2.0, 2.0), segments2.get(0).getEnd());
        Assert.assertEquals("Incorrect start point of segment 1.", new Vector2D(3.0, 4.0), segments2.get(1).getStart());
        Assert.assertEquals("Incorrect end point of segment 1.", end, segments2.get(1).getEnd());

    }

    @Test
    public void testIntersection() {

        // Test intersection() method for different cases

        // Case 1: Intersection of two lines with no intersection
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        Vector2D intersection
            = subLine1.intersection(subLine2, true); // includeEndPoints = true

        Assert.assertNull("Incorrect intersection.", intersection);

        // Case 2: Intersection of two lines with intersection
        Line line3 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line4 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine3 = new SubLine(line3, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine4 = new SubLine(line4, new IntervalsSet(new Interval(1.0, 2.0)));

        Vector2D intersection2
            = subLine3.intersection(subLine4, true); // includeEndPoints = true

        Assert.assertEquals("Incorrect intersection.", new Vector2D(1.0, 1.0), intersection2);

        // Case 3: Intersection of two lines with no intersection (parallel lines)
        Line line5 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line6 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine5 = new SubLine(line5, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine6 = new SubLine(line6, new IntervalsSet(new Interval(1.0, 2.0)));

        Vector2D intersection3
            = subLine5.intersection(subLine6, true); // includeEndPoints = true

        Assert.assertNull("Incorrect intersection.", intersection3);

    }

}