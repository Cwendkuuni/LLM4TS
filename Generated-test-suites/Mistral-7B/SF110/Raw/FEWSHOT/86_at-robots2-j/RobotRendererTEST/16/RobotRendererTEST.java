package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.*;

public class RobotRendererTest {

    private static final double DELTA = 0.01;
    private static final double PI_OVER_TWO = Math.PI / 2.0;
    private static final double TWO_PI = 2.0 * Math.PI;
    private RobotRenderer robotRenderer;
    private Graphics2D graphics2D;
    private RobotSnapshot robotSnapshot;
    private Set<Integer> selectedRobotIds;

    @Before
    public void setUp() {
        robotRenderer = new RobotRenderer();
        graphics2d = mock(Graphics2D.class);
        robotSnapshot = new RobotSnapshot(1, 0.0, 0.0, new Angle(PI_OVER_TWO), 100.0, 50.0, 30.0, 1.0, 0.0, 1.0, new Vector2D(1.0, 1.0), "name", false, false, false);
        selectedRobotIds = new HashSet<>();
    }

    @Test
    public void testRenderWithNullGraphics2D() {
        robotRenderer.render(null, robotSnapshot, selectedRobotIds);
        // No action should be taken when graphics2D is null
    }

    @Test
    public void testRenderWithNullRobotSnapshot() {
        robotRenderer.render(graphics2d, null, selectedRobotIds);
        // No action should be taken when robotSnapshot is null
    }

    @Test
    public void testRenderWithNullSelectedRobotIds() {
        robotRenderer.render(graphics2d, robotSnapshot, null);
        // No action should be taken when selectedRobotIds is null
    }

    @Test
    public void testRenderRobotThatIsDead() {
        when(graphics2d.getFontMetrics()).thenReturn(mock(FontMetrics.class));

        robotRenderer.render(graphics2d, robotSnapshot, selectedRobotIds);

        final Stroke stroke = graphics2d.getStroke();
        assertEquals("Stroke is not orange.darker().darker() when robot is dead.", Color.orange.darker().darker(), robotRenderer.getPaint(graphics2d));
        assertEquals("Fill path is not performed when robot is dead.", false, robotRenderer.wasPathFilled(graphics2D));
        assertEquals("Path is drawn when robot is dead.", graphics2d.getStroke(), robotRenderer.getStroke());
    }

    @Test
    public void testRenderRobotThatIsNotDead() {
        when(graphics2d.getFontMetrics()).thenReturn(mock(FontMetrics.class));

        robotRenderer.render(graphics2d, robotSnapshot, selectedRobotIds);

        final Stroke stroke = graphics2d.getStroke();
        assertNotSame("Stroke is not the same after rendering.", stroke, robotRenderer.getStroke());
        assertEquals("Paint is red when robot is not dead.", Color.red, robotRenderer.getPaint(graphics2d));
        assertTrue("Path is filled when robot is not dead.", robotRenderer.wasPathFilled(graphics2D));
    }

    @Test
    public void testRenderWithSelectedRobot() {
        robotRenderer.setRenderDead(false);
        robotRenderer.render(graphics2d, robotSnapshot, selectedRobotIds);
        selectedRobotIds.add(robotSnapshot.getId());
        robotRenderer.render(graphics2d, robotSnapshot, selectedRobotIds);

        final Ellipse2D.Double ellipse = new Ellipse2D.Double();
        ellipse.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 30.0, robotSnapshot.getY() + 30.0);

        final Shape shape = robotRenderer.getSelectionShape(graphics2d, robotSnapshot);
        assertEquals("Selection shape is not an ellipse.", ellipse, shape);
    }

    @Test
    public void testRenderWithoutSelectedRobot() {
        robotRenderer.render(graphics2d, robotSnapshot, selectedRobotIds);

        final Shape shape = robotRenderer.getSelectionShape(graphics2d, robotSnapshot);
        assertNull("Selection shape is not null when no robot is selected.", shape);
    }

    @Test
    public void testPaintSelection() {
        robotRenderer.paintSelection(graphics2d, robotSnapshot);

        final Shape shape = robotRenderer.getSelectionShape(graphics2d, robotSnapshot);
        assertNotNull("Selection shape is null after painting.", shape);
        assertEquals("Selection shape is not an ellipse.", Ellipse2D.class, shape.getClass());
    }

    @Test
    public void testPaintShieldWhenRenderShieldIsFalse() {
        robotRenderer.setRenderDead(false);
        robotRenderer.setFillShield(false);
        robotSnapshot.setActiveShield(true);
        robotRenderer.paintShield(graphics2d, robotSnapshot);

        final Graphics2D graphics2dCopy = (Graphics2D) graphics2d.create();
        robotRenderer.paintShield(graphics2dCopy, robotSnapshot);

        final Shape shape1 = graphics2d.getShape();
        final Shape shape2 = graphics2dCopy.getShape();

        assertNotSame("Shape is the same when renderShield is false.", shape1, shape2);
        assertTrue("Shape is empty when renderShield is false.", shape1.isEmpty());
    }

    @Test
    public void testPaintShieldWhenRenderShieldIsTrue() {
        robotRenderer.setRenderDead(false);
        robotRenderer.setFillShield(true);
        robotSnapshot.setActiveShield(true);
        robotRenderer.paintShield(graphics2d, robotSnapshot);

        final Ellipse2D.Double ellipse = new Ellipse2D.Double();
        ellipse.setFrameFromCenter(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + 15.0, robotSnapshot.getY() + 15.0);

        final Shape shape = graphics2d.getShape();
        assertEquals("Shape is not an ellipse.", ellipse, shape);
        assertEquals("GradientPaint color is not HEAT_COLOR1.", RobotRenderer.HEAT_COLOR1, robotRenderer.getPaint(graphics2d));
    }

    @Test
    public void testPaintName() {
        robotRenderer.setRenderDead(false);
        robotRenderer.setShowName(true);
        robotSnapshot.setName("name");

        robotRenderer.paintName(graphics2d, robotSnapshot);

        final String string = graphics2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2d).getSubstring(0, (int) Math.ceil(graphics2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), graphics2d).getWidth() * 0.5));
        assertEquals("String is not a substring of robotSnapshot name.", robotSnapshot.getName().substring(0, robotSnapshot.getName().length() / 2), string);
    }

    @Test
    public void testPaintHeat() {
        robotRenderer.setRenderDead(false);
        robotSnapshot.setTemperature(new Temperature(100.0));
        robotSnapshot.setDead(false);

        robotRenderer.paintHeat(graphics2d, robotSnapshot);

        final Rectangle2D.Double rectangle = new Rectangle2D.Double();
        rectangle.setFrame(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, 100.0, 10.0);

        final Shape shape = graphics2d.getShape();
        assertEquals("Shape is not a rectangle.", rectangle, shape);
        assertEquals("GradientPaint color is not HEAT_COLOR1.", RobotRenderer.HEAT_COLOR1, robotRenderer.getPaint(graphics2d));
        assertEquals("GradientPaint length is not 10.", 10.0, robotSnapshot.getTemperature().getLogScale() * 0.2);
    }

    @Test
    public void testPaintArmor() {
        robotRenderer.setRenderDead(false);
        robotRenderer.setFillShield(true);
        robotSnapshot.setArmor(50.0);

        robotRenderer.paintArmor(graphics2d, robotSnapshot);

        final Rectangle2D.Double rectangle1 = new Rectangle2D.Double();
        rectangle1.setFrameFromCenter(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, robotSnapshot.getArmor(), 10.0);

        final Shape shape1 = graphics2d.getShape();
        assertEquals("Shape is not a rectangle.", rectangle1, shape1);
        assertEquals("Paint is not the color of the armor.", new Color(0.0f, 0.0f, 1.0f, 0.6f), robotRenderer.getPaint(graphics2d));

        final Rectangle2D.Double rectangle2 = new Rectangle2D.Double();
        rectangle2.setFrameFromCenter(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, 100.0, 10.0);

        final Shape shape2 = graphics2d.getShape();
        assertEquals("Shape is not a rectangle.", rectangle2, shape2);
        assertEquals("Paint is not the color of the background.", new Color(0.0f, 1.0f, 0.0f, 0.6f), robotRenderer.getPaint(graphics2d));
    }

    @Test
    public void testPaintTurret() {
        robotRenderer.setRenderDead(false);
        robotSnapshot.setHeading(new Angle(PI_OVER_TWO));

        robotRenderer.paintTurret(graphics2d, robotSnapshot);

        final Line2D.Double line = new Line2D.Double();
        line.setLine(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0);

        final Shape shape = graphics2d.getShape();
        assertEquals("Shape is not a line.", line, shape);
        assertEquals("Paint is not white.", Color.white, robotRenderer.getPaint(graphics2d));
    }

    @Test
    public void testPaintBody() {
        robotRenderer.setRenderDead(false);

        robotRenderer.paintBody(graphics2d, robotSnapshot);

        final GeneralPath path = new GeneralPath();
        path.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
        path.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().counterClockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
        path.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
        path.lineTo(robotSnapshot.getX() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).cosine() * 9.0, robotSnapshot.getY() + robotSnapshot.getHeading().clockwise(RelativeAngle.fromBygrees(160)).sine() * 9.0);
        path.closePath();

        final Shape shape = graphics2d.getShape();
        assertEquals("Shape is not the same as the body path.", path, shape);
    }

    @After
    public void tearDown() {
        robotRenderer = null;
        graphics2d = null;
        robotSnapshot = null;
        selectedRobotIds = null;
    }
}