package net.virtualinfinity.atrobots.gui.renderers.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Distance;
import net.virtualinfinity.atrobots.measures.Position;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

import org.junit.Before;
import org.junit.Test;

public class RobotRendererTest {

	private RobotRenderer renderer;
	private RobotSnapshot robotSnapshot;
	private Graphics2D g2d;
	private static final Distance THREE_ZERO = new Distance(30.0);
	private static final Position ZERO = new Position(0.0, 0.0);
	private static final Position THREE_FIFTY = new Position(35.0, 0.0);
	private static final Position FIVE = new Position(5.0, 5.0);
	private static final AbsoluteAngle SIXTY = new AbsoluteAngle(60);
	private static final AbsoluteAngle ONE_SIXTY = new AbsoluteAngle(160);
	private static final AbsoluteAngle TWO_HUNDRED_AND_SIXTY = new AbsoluteAngle(260);
	private static final AbsoluteAngle THREE_HUNDRED = new AbsoluteAngle(300);
	private static final Color COLOR_YELLOW = Color.YELLOW;
	private static final Color COLOR_RED = Color.RED;
	private static final Color COLOR_ORANGE = new Color(128, 64, 0);
	private static final Color COLOR_BLACK = Color.BLACK;
	private static final Color COLOR_GREEN = Color.GREEN;
	private static final Color COLOR_BLUE = Color.BLUE;
	private static final Color COLOR_WHITE = Color.WHITE;
	private static final float[] HEAT_COLOR1_COMPONENTS = { 1.0f, 0.0f, 0.0f, 0.1f };
	private static final Color HEAT_COLOR2 = new Color(1.0f, 1.0f, 0.0f, 0.25f);
	private static final RadialGradientPaint RADIAL_GRADIENT_PAINT = new RadialGradientPaint(ZERO, 15.0f, new float[] { 0.0f, 0.75f, 1.0f }, new Color[] { new Color(0.5f, 0.5f, 0.0f, 0.8f), new Color(0.0f, 0.0f, 0.5f, 0.1f), COLOR_WHITE });
	private static final GradientPaint HEAT_GRADIENT_PAINT = new GradientPaint(THREE_FIFTY, 0.0, COLOR_RED, THREE_FIFTY, 10.0, HEAT_COLOR1);
	 private static final Set<Integer> SELECTED_ROBOT_IDS = new HashSet<Integer>(Arrays.asList(1, 2));

	@Before
	public void setup() {
		renderer = new RobotRenderer();
		robotSnapshot = new RobotSnapshot(1, new Position(10.0, 10.0), new Position(10.0, 10.0), new Distance(50.0), new Distance(15.0), new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), new AbsoluteAngle(0), 10.0, 15.0, 50.0, 10.0, 10.0, true, true, true, true);
		g2d = new StubGraphics2D();
	}

	@Test
	public void testRender() {
		renderer.render(g2d, robotSnapshot, new HashSet<Integer>());

		// Check if robot is not rendered when dead and renderer is set to not render dead robots
		assertFalse(g2d.isDrawing());

		// Check if robot is rendered when alive and renderer is set to render dead robots
		renderer.setRenderDead(false);
		renderer.render(g2d, robotSnapshot, new HashSet<Integer>());
		assertTrue(g2d.isDrawing());

		// Test rendering body
		renderer.setRenderDead(true);
		renderer.render(g2d, robotSnapshot, new HashSet<Integer>());
		assertEquals(g2d.getColor(), COLOR_ORANGE.darker().darker());
		assertEquals(g2d.getStroke(), new BasicStroke(1.0f, 1, 0, 10.0f, new float[] { 5.0f, 5.0f }, 0.0f));
		assertEquals(g2d.getPaint(), COLOR_ORANGE.darker().darker());
		assertEquals(g2d.getShape(), getBodyPath(robotSnapshot));

		// Test rendering body when alive
		renderer.setRenderDead(false);
		renderer.render(g2d, robotSnapshot, new HashSet<Integer>());
		assertEquals(g2d.getColor(), COLOR_RED);
		assertEquals(g2d.getStroke(), new BasicStroke());
		assertEquals(g2d.getPaint(), COLOR_RED);
		assertEquals(g2d.getShape(), getBodyPath(robotSnapshot));

		// Test rendering shield
		renderer.render(g2d, robotSnapshot, new HashSet<Integer>());
		assertTrue(g2d.isDrawing());
		assertEquals(g2d.getPaint(), RobotRenderer.HEAT_COLOR1);
		assertEquals(g2d.getShape(), getShieldPath(robotSnapshot));

		// Test rendering shield with fill
		renderer.setFillShield(true);
		renderer.render(g2d, robotSnapshot, new HashSet<Integer>());
		assertTrue(g2d.isDrawing());
		assertEquals(g2d.getPaint(), RADIAL_GRADIENT_PAINT);
		assertEquals(g2d.getShape(), getShieldPath(robotSnapshot));

		// Test rendering name
		renderer.setShowName(true);
		renderer.render(g2d, robotSnapshot, new HashSet<Integer>());
		assertEquals(g2d.getFont(), g2d.getFont());
		assertEquals(g2d.getColor(), COLOR_YELLOW);
		assertEquals(g2d.getString(), robotSnapshot.getName());
		assertEquals(g2d.getX(), (float)(robotSnapshot.getX() - g2d.getFontMetrics().getStringBounds(robotSnapshot.getName(), g2d).getWidth() / 2.0));
		assertEquals(g2d.getY(), (float)(robotSnapshot.getY() - 10.0));

		// Test rendering heat
		renderer.setShowStatusBars(true);
		renderer.render(g2d, robotSnapshot, new HashSet<Integer>());
		assertEquals(g2d.getPaint(), HEAT_GRADIENT_PAINT);
		assertEquals(g2d.getShape(), new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0, robotSnapshot.getTemperature().getLogScale() * 0.2, 10.0));

		// Test rendering armor
		renderer.render(g2d, robotSnapshot, new HashSet<Integer>());
		assertEquals(g2d.getPaint(), COLOR_BLACK);
		assertEquals(g2d.getShape(), new Rectangle2D.Double(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 20.0, robotSnapshot.getArmor(), 10.0));

		// Test rendering turret
		renderer.render(g2d, robotSnapshot, new HashSet<Integer>());
		assertEquals(g2d.getPaint(), COLOR_WHITE);
		assertEquals(g2d.getShape(), new Line2D.Double(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0));
	}

	@Test
	public void testIsRenderDead() {
		assertTrue(renderer.isRenderDead());
		renderer.setRenderDead(false);
		assertFalse(renderer.isRenderDead());
	}

	@Test
	public void testSetRenderDead() {
		renderer.setRenderDead(true);
		assertTrue(renderer.isRenderDead());
		renderer.setRenderDead(false);
		assertFalse(renderer.isRenderDead());
	}

	@Test
	public void testIsShowStatusBars() {
		assertTrue(renderer.isShowStatusBars());
		renderer.setShowStatusBars(false);
		assertFalse(renderer.isShowStatusBars());
	}

	@Test
	public void testSetShowStatusBars() {
		renderer.setShowStatusBars(true);
		assertTrue(renderer.isShowStatusBars());
		renderer.setShowStatusBars(false);
		assertFalse(renderer.isShowStatusBars());
	}

	@Test
	public void testIsShowName() {
		assertTrue(renderer.isShowName());
		renderer.setShowName(false);
		assertFalse(renderer.isShowName());
	}

	@Test
	public void testSetShowName() {
		renderer.setShowName(true);
		assertTrue(renderer.isShowName());
		renderer.setShowName(false);
		assertFalse(renderer.isShowName());
	}

	@Test
	public void testIsFillShield() {
		assertTrue(renderer.isFillShield());
		renderer.setFillShield(false);
		assertFalse(renderer.isFillShield());
	}

	@Test
	public void testSetFillShield() {
		renderer.setFillShield(true);
		assertTrue(renderer.isFillShield());
		renderer.setFillShield(false);
		assertFalse(renderer.isFillShield());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRenderWithNullGraphics2D() {
		renderer.render(null, robotSnapshot, new HashSet<Integer>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRenderWithNullRobotSnapshot() {
		renderer.render(g2d, null, new HashSet<Integer>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRenderWithNullSelectedRobotIds() {
		renderer.render(g2d, robotSnapshot, null);
	}

	@Test
	public void testPaintSelectionWhenRobotIsSelected() {
		renderer.setRenderDead(false);
		renderer.setShowName(false);
		renderer.setShowStatusBars(false);
		renderer.setFillShield(false);
		renderer.render(g2d, robotSnapshot, SELECTED_ROBOT_IDS);
		assertTrue(g2d.isDrawing());
		assertEquals(g2d.getPaint(), new Color(1.0f, 1.0f, 0.0f, 0.25f));
		assertEquals(g2d.getShape(), new Ellipse2D.Double(robotSnapshot.getX() - 15.0, robotSnapshot.getY() - 15.0, 30.0, 30.0));
	}

	@Test
	public void testPaintSelectionWhenRobotIsNotSelected() {
		renderer.setRenderDead(false);
		renderer.setShowName(false);
		renderer.setShowStatusBars(false);
		renderer.setFillShield(false);
		renderer.render(g2d, robotSnapshot, new HashSet<Integer>());
		assertFalse(g2d.isDrawing());
	}

	private static GeneralPath getBodyPath(RobotSnapshot robotSnapshot) {
		GeneralPath path = new GeneralPath();
		path.moveTo(robotSnapshot.getX() + robotSnapshot.getHeading().cosine() * 15.0, robotSnapshot.getY() + robotSnapshot.getHeading().sine() * 15.0);
		path.lineTo(robotSnapshot.getX(), robotSnapshot.getY());
		path.closePath();
		path.moveTo(robotSnapshot.getX(), robotSnapshot.getY());
		path.lineTo(robotSnapshot.getX() + ONE_SIXTY.cosine() * 9.0, robotSnapshot.getY() + ONE_SIXTY.sine() * 9.0);
		path.lineTo(robotSnapshot.getX() + SIXTY.cosine() * 9.0, robotSnapshot.getY() + SIXTY.sine() * 9.0);
		path.lineTo(robotSnapshot.getX() + TWO_HUNDRED_AND_SIXTY.cosine() * 9.0, robotSnapshot.getY() + TWO_HUNDRED_AND_SIXTY.sine() * 9.0);
		path.lineTo(robotSnapshot.getX() + THREE_HUNDRED.cosine() * 9.0, robotSnapshot.getY() + THREE_HUNDRED.sine() * 9.0);
		path.closePath();
		return path;
	}

	private static GeneralPath getShieldPath(RobotSnapshot robotSnapshot) {
		GeneralPath path = new GeneralPath();
		path.moveTo(robotSnapshot.getX() - 50.0 + FIVE.cosine() * 7.5, robotSnapshot.getY() + 35.0 + FIVE.sine() * 7.5);
		path.lineTo(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 35.0);
		path.lineTo(robotSnapshot.getX() - 50.0, robotSnapshot.getY() + 45.0);
		path.lineTo(robotSnapshot.getX() - 50.0 + FIVE.cosine() * 7.5, robotSnapshot.getY() + 45.0);
		path.closePath();
		return path;
	}

}