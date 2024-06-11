import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod. Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test simple constructor
        SubLine subLine = new SubLine(new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0)),
                new IntervalsSet(new Interval(0.0, 1.0))), null);

        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        Assert.assertEquals(new Vector2D(0.0, 0.0), segment.getStart());
        Assert.assertEquals(new Vector2D(1.0, 0.0), segment.getEnd());

        // Test constructor with remaining region
        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0)),
                new IntervalsSet(new Interval(0.5, 1.0)));
        SubLine subLineWithRegion = new SubLine(hyperplane, new IntervalsSet(new Interval(0.0, 0.5)));

        segments = subLineWithRegion.getSegments();
        Assert.assertEquals(1, segments.size());
        segment = segments.get(0);
        Assert.assertEquals(new Vector2D(0.0, 0.0), segment.getStart());
        Assert.assertEquals(new Vector2D(0.5, 0.0), segment.getEnd());
    }

    @Test
    public void testIntersection() {
        Hyperplane<Euclidean2D> hp1 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0)),
                new IntervalsSet(new Interval(0.5, 1.0)));
        Hyperplane<Euclidean2D> hp2 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(2.0, 0.0), new Vector2D(3.0, 0.0)),
                new IntervalsSet(new Interval(1.5, 2.5)));

        SubLine subLine1 = new SubLine(hp1, null);
        SubLine subLine2 = new SubLine(hp2, null);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertEquals(new Vector2D(1.5, 0.0), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        Assert.assertNull(intersectionPoint);
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        IntervalsSet iset = new IntervalsSet(new Interval(0.2, 0.5), new Interval(0.8, 1.0));
        Hyperplane<Euclidean2D> hp = new Hyperplane<Euclidean2D>(line, iset);
        SubLine subLine = new SubLine(hp, null);
        List<Segment> segments = subLine.getSegments();

        Assert.assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        Segment segment2 = segments.get(1);

        Assert.assertEquals(new Vector2D(0.2, 0.0), segment1.getStart());
        Assert.assertEquals(new Vector2D(0.5, 0.5), segment1.getEnd());
        Assert.assertEquals(new Vector2D(0.8, 0.0), segment2.getStart());
        Assert.assertEquals(new Vector2D(1.0, 1.0), segment2.getEnd());
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(0.5, 1.0), new Vector2D(1.5, 1.0));
        Hyperplane<Euclidean2D> hp1 = new Hyperplane<Euclidean2D>(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        Hyperplane<Euclidean2D> hp2 = new Hyperplane<Euclidean2D>(line2, new IntervalsSet(new Interval(1.0, 2.0)));
        SubLine subLine1 = new SubLine(hp1, null);
        SubLine subLine2 = new SubLine(hp2, null);

        Side side = subLine1.side(subLine2.getHyperplane());
        Assert.assertEquals(Side.MINUS, side);

        side = subLine2.side(subLine1.getHyperplane());
        Assert.assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(0.5, 1.0), new Vector2D(1.5, 1.0));
        Hyperplane<Euclidean2D> hp1 = new Hyperplane<Euclidean2D>(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        Hyperplane<Euclidean2D> hp2 = new Hyperplane<Euclidean2D>(line2, new IntervalsSet(new Interval(1.0, 2.0)));
        SubLine subLine1 = new SubLine(hp1, null);
        SubLine subLine2 = new SubLine(hp2, null);

        Hyperplane<Euclidean2D> hp3 = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.2, 0.0), new Vector2D(0.6, 0.2)),
                new IntervalsSet(new Interval(0.0, 1.0)));

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hp3);
        SubLine subLineSplit1 = (SubLine) split.getLeft();
        SubLine subLineSplit2 = (SubLine) split.getRight();

        List<Segment> segments1 = subLineSplit1.getSegments();
        List<Segment> segments2 = subLineSplit2.getSegments();

        Assert.assertEquals(1, segments1.size());
        Assert.assertEquals(1, segments2.size());

        Segment segment1 = segments1.get(0);
        Segment segment2 = segments2.get(0);

        Assert.assertEquals(new Vector2D(0.0, 0.0), segment1.getStart());
        Assert.assertEquals(new Vector2D(0.5, 0.0), segment1.getEnd());
        Assert.assertEquals(new Vector2D(0.6, 0.2), segment2.getStart());
        Assert.assertEquals(new Vector2D(1.0, 0.0), segment2.getEnd());
    }

    @Test
    public void testBuildNew() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(0.5, 1.0), new Vector2D(1.5, 1.0));
        IntervalsSet iset1 = new IntervalsSet(new Interval(0.0, 1.0));
        IntervalsSet iset2 = new IntervalsSet(new Interval(1.0, 2.0));
        Hyperplane<Euclidean2D> hp1 = new Hyperplane<Euclidean2D>(line1, iset1);
        Hyperplane<Euclidean2D> hp2 = new Hyperplane<Euclidean2D>(line2, iset2);
        SubLine subLine1 = new SubLine(hp1, iset1);

        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5)),
                new IntervalsSet(new Interval(1.2, 1.7)));

        SubLine expectedSubLine = new SubLine(hyperplane, new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.7, 2.0)));
        SubLine actualSubLine = SubLine.buildNew(hp1, iset1, hyperplane, iset2);

        List<Segment> segmentsExpected = expectedSubLine.getSegments();
        List<Segment> segmentsActual = actualSubLine.getSegments();

        Assert.assertEquals(segmentsExpected.size(), segmentsActual.size());

        for (int i = 0; i < segmentsExpected.size(); i++) {
            Segment segmentExpected = segmentsExpected.get(i);
            Segment segmentActual = segmentsActual.get(i);

            Assert.assertEquals(segmentExpected.getStart(), segmentActual.getStart());
            Assert.assertEquals(segmentExpected.getEnd(), segmentActual.getEnd());
        }
    }

}