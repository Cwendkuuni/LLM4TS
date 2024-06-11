package org.apache.commons.math3.geometry.euclidean.twod.test;

import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.test.AbstractMathTestCase;

/**
 * Test cases for {@link SubLine}.
 */
public class SubLineTest extends AbstractMathTestCase {

    /** Test constructor with hyperplane and remaining region. */
    public void testConstructorHyperplaneAndRegion() {
        final Euclidean2D space = new Euclidean2D();
        final Line line         = new Line(new Vector2D(0.0, 1.0), new Vector2D(0.0, 2.0));
        final IntervalsSet set   = new IntervalsSet(new Interval(1.0, 3.0));
        final SubLine subLine   = new SubLine(line, set);
        assertEquals(line, subLine.getHyperplane());
        assertEquals(set, subLine.getRemainingRegion());
    }

    /** Test constructor with two endpoints. */
    public void testConstructorTwoEndpoints() {
        final Euclidean2D space = new Euclidean2D();
        final Vector2D start    = new Vector2D(0.0, 1.0);
        final Vector2D end      = new Vector2D(3.0, 4.0);
        final SubLine subLine   = new SubLine(start, end);
        final Line line         = new Line(start, end);
        assertEquals(line, subLine.getHyperplane());
        final IntervalsSet set   = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        assertEquals(set, subLine.getRemainingRegion());
    }

    /** Test constructor with a segment. */
    public void testConstructorSegment() {
        final Euclidean2D space = new Euclidean2D();
        final Vector2D start    = new Vector2D(0.0, 1.0);
        final Vector2D end      = new Vector2D(3.0, 4.0);
        final Segment segment    = new Segment(start, end, new Line(start, end));
        final SubLine subLine   = new SubLine(segment);
        final Line line         = segment.getLine();
        assertEquals(line, subLine.getHyperplane());
        final IntervalsSet set   = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        assertEquals(set, subLine.getRemainingRegion());
    }

    /** Test getSegments() method. */
    public void testGetSegments() {
        final Euclidean2D space = new Euclidean2D();
        final Line line         = new Line(new Vector2D(0.0, 1.0), new Vector2D(10.0, 11.0));
        final IntervalsSet set   = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0), new Interval(5.0, 6.0));
        final SubLine subLine   = new SubLine(line, set);
        final List<Segment> segments = subLine.getSegments();
        assertNotNull(segments);
        assertEquals(3, segments.size());
        final Segment segment1 = segments.get(0);
        final Vector2D start1  = new Vector2D(line.toSpace(new Vector1D(1.0)).add(line.getStart()));
        final Vector2D end1    = new Vector2D(line.toSpace(new Vector1D(2.0)).add(line.getStart()));
        assertEquals(start1, segment1.getStart());
        assertEquals(end1, segment1.getEnd());
        final Segment segment2 = segments.get(1);
        final Vector2D start2  = new Vector2D(line.toSpace(new Vector1D(3.0)).add(line.getStart()));
        final Vector2D end2    = new Vector2D(line.toSpace(new Vector1D(4.0)).add(line.getStart()));
        assertEquals(start2, segment2.getStart());
        assertEquals(end2, segment2.getEnd());
        final Segment segment3 = segments.get(2);
        final Vector2D start3  = new Vector2D(line.toSpace(new Vector1D(5.0)).add(line.getStart()));
        final Vector2D end3    = new Vector2D(line.toSpace(new Vector1D(6.0)).add(line.getStart()));
        assertEquals(start3, segment3.getStart());
        assertEquals(end3, segment3.getEnd());
    }

    /** Test intersection() method with intersection. */
    public void testIntersectionIntersection() {
        final Euclidean2D space = new Euclidean2D();
        final Line line1        = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        final Line line2        = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        final SubLine subLine1  = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5)));
        final SubLine subLine2  = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5)));
        final Vector2D expected  = new Vector2D(1.0, 1.0);
        final Vector2D result    = subLine1.intersection(subLine2, true);
        assertNotNull(result);
        assertEquals(expected, result);
    }

    /** Test intersection() method with no intersection. */
    public void testIntersectionNoIntersection() {
        final Euclidean2D space = new Euclidean2D();
        final Line line1        = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        final Line line2        = new Line(new Vector2D(3.0, 3.0), new Vector2D(5.0, 5.0));
        final SubLine subLine1  = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5)));
        final SubLine subLine2  = new SubLine(line2, new IntervalsSet(new Interval(2.0, 4.0)));
        final Vector2D result    = subLine1.intersection(subLine2, true);
        assertNull(result);
    }

    /** Test intersection() method with no intersection (endpoints). */
    public void testIntersectionNoIntersectionEndpoints() {
        final Euclidean2D space = new Euclidean2D();
        final Line line1        = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        final Line line2        = new Line(new Vector2D(3.0, 3.0), new Vector2D(5.0, 5.0));
        final SubLine subLine1  = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5), new Interval(3.5, 4.5)));
        final SubLine subLine2  = new SubLine(line2, new IntervalsSet(new Interval(2.0, 4.0)));
        final Vector2D result    = subLine1.intersection(subLine2, false);
        assertNull(result);
    }

    /** Test intersection() method with intersection (endpoints). */
    public void testIntersectionIntersectionEndpoints() {
        final Euclidean2D space = new Euclidean2D();
        final Line line1        = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        final Line line2        = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        final SubLine subLine1  = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5), new Interval(3.5, 4.5)));
        final SubLine subLine2  = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5), new Interval(2.5, 3.5)));
        final Vector2D expected  = new Vector2D(1.5, 1.5);
        final Vector2D result    = subLine1.intersection(subLine2, false);
        assertNotNull(result);
        assertEquals(expected, result);
    }

    /** Test getEndpoints() method. */
    public void testGetEndpoints() {
        final Euclidean2D space = new Euclidean2D();
        final Line line         = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        final SubLine subLine   = new SubLine(line, new IntervalsSet(new Interval(0.5, 1.5), new Interval(3.5, 4.5)));
        final List<Segment> segments = subLine.getSegments();
        final Vector2D start1  = new Vector2D(line.toSpace(new Vector1D(0.5)).add(line.getStart()));
        final Vector2D end1    = new Vector2D(line.toSpace(new Vector1D(1.5)).add(line.getStart()));
        final Vector2D start2  = new Vector2D(line.toSpace(new Vector1D(3.5)).add(line.getStart()));
        final Vector2D end2    = new Vector2D(line.toSpace(new Vector1D(4.5)).add(line.getStart()));
        final List<Vector2D> expected = new ArrayList<>();
        expected.add(start1);
        expected.add(end1);
        expected.add(start2);
        expected.add(end2);
        assertEquals(expected, subLine.getEndpoints());
    }

}