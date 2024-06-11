package net.virtualinfinity.atrobots.gui.renderers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.snapshots.*;
import org.junit.*;
import org.mockito.*;

public class RobotRendererTest {

private RobotRenderer robotRenderer;
private Graphics2D graphics2DMock;
private RobotSnapshot robotSnapshot;

@Before
public void setUp() {
robotRenderer = new RobotRenderer();
graphics2DMock = mock(Graphics2D.class);
robotSnapshot = new RobotSnapshot(1L, new PositionVector(10.0, 10.0), new AbsoluteAngle(0), 50.0, 100.0, 50.0, true, 1.0f, new Color("Red"), new Color("Blue"), false, false, false, false);
}

@Test
public void testRenderRobotSnapshot_isRenderDeadFalseAndNotDead() {
when(graphics2DMock.getColor()).thenReturn(Color.RED);
robotRenderer.render(graphics2DMock, robotSnapshot, Collections.emptySet());

verify(graphics2DMock, times(1)).draw(argThat(new ArgumentMatcher<Shape>() {
@Override
public boolean matches(Shape argument) {
Path2D path = (Path2D) argument;
GeneralPath generalPath = (GeneralPath) path.getPath2D();
return generalPath.getWindingRule() == Path2D.WIND_EVEN_ODD
&& generalPath.getElements().length == 4;
}
}));

verify(graphics2DMock, times(1)).fill(argThat(new ArgumentMatcher<Shape>() {
@Override
public boolean matches(Shape argument) {
Path2D path = (Path2D) argument;
GeneralPath generalPath = (GeneralPath) path.getPath2D();
return generalPath.getWindingRule() == Path2D.WIND_EVEN_ODD
&& generalPath.getElements().length == 4;
}
}));

verify(graphics2DMock, times(1)).drawOval((int) (robotSnapshot.getX() - 30.0), (int) robotSnapshot.getY(), 60, 60);
verify(graphics2DMock, times(1)).setPaint(argThat(new ArgumentMatcher<Paint>() {
@Override
public boolean matches(Paint argument) {
return argument instanceof RadialGradientPaint;
}
}));

verify(graphics2DMock, times(1)).fill(argThat(new ArgumentMatcher<Shape>() {
@Override
public boolean matches(Shape argument) {
Ellipse2D.Double ellipse = (Ellipse2D.Double) argument;
return ellipse.getFrame().getX() == robotSnapshot.getX()
&& ellipse.getFrame().getY() == robotSnapshot.getY()
&& ellipse.getWidth() == 15.0
&& ellipse.getHeight() == 15.0;
}
}));

verify(graphics2DMock, times(1)).setPaint(argThat(new ArgumentMatcher<Paint>() {
@Override
public boolean matches(Paint argument) {
return argument instanceof Color;
}
}));

verify(graphics2DMock, times(1)).draw(argThat(new ArgumentMatcher<Shape>() {
@Override
public boolean matches(Shape argument) {
Ellipse2D.Double ellipse = (Ellipse2D.Double) argument;
return ellipse.getFrame().getX() == robotSnapshot.getX()
&& ellipse.getFrame().getY() == robotSnapshot.getY()
&& ellipse.getWidth() == 100.0
&& ellipse.getHeight() == 10.0;
}
}));

verify(graphics2DMock, times(1)).setPaint(Color.YELLOW);
verify(graphics2DMock, times(1)).drawString(eq(robotSnapshot.getName()), anyFloat(), anyFloat());
}

@Test
public void testRenderRobotSnapshot_isRenderDeadFalseAndDead() {
when(graphics2DMock.getColor()).thenReturn(Color.ORANGE.darker().darker());
robotRenderer.render(graphics2DMock, robotSnapshot, Collections.emptySet());

verify(graphics2DMock, times(1)).setStroke(argThat(new ArgumentMatcher<Stroke>() {
@Override
public boolean matches(Stroke argument) {
return argument instanceof BasicStroke;
}
}));

verify(graphics2DMock, times(1)).setPaint(argThat(new ArgumentMatcher<Paint>() {
@Override
public boolean matches(Paint argument) {
return argument instanceof Color;
}
}));

verify(graphics2DMock, times(1)).draw(eq(new Line2D.Double(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0)));

verify(graphics2DMock, never()).drawOval((int) (robotSnapshot.getX() - 30.0), (int) robotSnapshot.getY(), 60, 60);
verify(graphics2DMock, never()).fill(argThat(new ArgumentMatcher<Shape>() {
@Override
public boolean matches(Shape argument) {
Path2D path = (Path2D) argument;
GeneralPath generalPath = (GeneralPath) path.getPath2D();
return generalPath.getWindingRule() == Path2D.WIND_EVEN_ODD
&& generalPath.getElements().length == 4;
}
}));
}

@Test
public void testRenderRobotSnapshot_isRenderDeadTrueAndNotDead() {
robotRenderer.setRenderDead(true);
robotRenderer.render(graphics2DMock, robotSnapshot, Collections.emptySet());

verify(graphics2DMock, never()).drawOval((int) (robotSnapshot.getX() - 30.0), (int) robotSnapshot.getY(), 60, 60);
verify(graphics2DMock, never()).fill(argThat(new ArgumentMatcher<Shape>() {
@Override
public boolean matches(Shape argument) {
Path2D path = (Path2D) argument;
GeneralPath generalPath = (GeneralPath) path.getPath2D();
return generalPath.getWindingRule() == Path2D.WIND_EVEN_ODD
&& generalPath.getElements().length == 4;
}
}));
}

@Test
public void testRenderRobotSnapshot_isRenderDeadTrueAndDead() {
robotRenderer.setRenderDead(true);
when(graphics2DMock.getColor()).thenReturn(Color.ORANGE.darker().darker());
robotRenderer.render(graphics2DMock, robotSnapshot, Collections.emptySet());

verify(graphics2DMock, times(1)).setStroke(argThat(new ArgumentMatcher<Stroke>() {
@Override
public boolean matches(Stroke argument) {
return argument instanceof BasicStroke;
}
}));

verify(graphics2DMock, times(1)).setPaint(argThat(new ArgumentMatcher<Paint>() {
@Override
public boolean matches(Paint argument) {
return argument instanceof Color;
}
}));

verify(graphics2DMock, times(1)).draw(eq(new Line2D.Double(robotSnapshot.getX(), robotSnapshot.getY(), robotSnapshot.getX() + robotSnapshot.getTurretHeading().cosine() * 5.0, robotSnapshot.getY() + robotSnapshot.getTurretHeading().sine() * 5.0)));

verify(graphics2DMock, never()).drawOval((int) (robotSnapshot.getX() - 30.0), (int) robotSnapshot.getY(), 60, 60);
verify(graphics2DMock, never()).fill(argThat(new ArgumentMatcher<Shape>() {
@Override
public boolean matches(Shape argument) {
Path2D path = (Path2D) argument;
GeneralPath generalPath = (GeneralPath) path.getPath2D();
return generalPath.getWindingRule() == Path2D.WIND_EVEN_ODD
&& generalPath.getElements().length == 4;
}
}));
}

@Test
public void testPaintSelection() {
when(graphics2DMock.getColor()).thenReturn(Color.YELLOW.withAlpha(0.25f));
robotRenderer.paintSelection(graphics2DMock, robotSnapshot);

verify(graphics2DMock, times(1)).setPaint(argThat(new ArgumentMatcher<Paint>() {
@Override
public boolean matches(Paint argument) {
return argument instanceof Color;
}
}));

verify(graphics2DMock, times(1)).fill(argThat(new ArgumentMatcher<Shape>() {
@Override
public boolean matches(Shape argument) {
Ellipse2D.Double ellipse = (Ellipse2D.Double) argument;
return ellipse.getFrame().getX() == robotSnapshot.getX()
&& ellipse.getFrame().getY() == robotSnapshot.getY()
&& ellipse.getWidth() == 60
&& ellipse.getHeight() == 60;
}
}));
}

@Test
public void testPaintShield() {
when(graphics2DMock.getColor()).thenReturn(Color.CYAN);
when(graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
robotRenderer.setFillShield(false);
robotRenderer.paintShield(graphics2DMock, robotSnapshot);

verify(graphics2DMock, times(1)).setPaint(argThat(new ArgumentMatcher<Paint>() {
@Override
public boolean matches(Paint argument) {
return argument instanceof Color;
}
}));

verify(graphics2DMock, times(1)).draw(argThat(new ArgumentMatcher<Shape>() {
@Override
public boolean matches(Shape argument) {
Ellipse2D.Double ellipse = (Ellipse2D.Double) argument;
return ellipse.getFrame().getX() == robotSnapshot.getX()
&& ellipse.getFrame().getY() == robotSnapshot.getY()
&& ellipse.getWidth() == 30.0
&& ellipse.getHeight() == 30.0;
}
}));

verify(graphics2DMock, never()).setPaint(argThat(new ArgumentMatcher<Paint>() {
@Override
public boolean matches(Paint argument) {
return argument instanceof RadialGradientPaint;
}
}));
}

@Test
public void testPaintShieldWithFill() {
when(graphics2DMock.getColor()).thenReturn(Color.CYAN);
when(graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
robotRenderer.setFillShield(true);
robotRenderer.paintShield(graphics2DMock, robotSnapshot);

verify(graphics2DMock, times(1)).setPaint(argThat(new ArgumentMatcher<Paint>() {
@Override
public boolean matches(Paint argument) {
return argument instanceof RadialGradientPaint;
}
}));

verify(graphics2DMock, times(1)).fill(argThat(new ArgumentMatcher<Shape>() {
@Override
public boolean matches(Shape argument) {
Ellipse2D.Double ellipse = (Ellipse2D.Double) argument;
return ellipse.getFrame().getX() == robotSnapshot.getX()
&& ellipse.getFrame().getY() == robotSnapshot.getY()
&& ellipse.getWidth() == 30.0
&& ellipse.getHeight() == 30.0;
}
}));

verify(graphics2DMock, times(1)).fill(argThat(new ArgumentMatcher<Shape>() {
@Override
public boolean matches(Shape argument) {
Ellipse2D.Double ellipse = (Ellipse2D.Double) argument;
return ellipse.getFrame().getX() == robotSnapshot.getX()
&& ellipse.getFrame().getY() == robotSnapshot.getY()
&& ellipse.getWidth() == 30.0
&& ellipse.getHeight() == 30.0;
}
}));
}

@Test
public void testPaintName() {
when(graphics2DMock.getColor()).thenReturn(Color.YELLOW);
when(graphics2DMock.getFontMetrics()).thenReturn(mock(FontMetrics.class));
robotRenderer.paintName(graphics2DMock, robotSnapshot);

verify(graphics2DMock, times(1)).setPaint(argThat(new ArgumentMatcher<Paint>() {
@Override
public boolean matches(Paint argument) {
return argument instanceof Color;
}
}));

verify(graphics2DMock, times(1)).drawString(robotSnapshot.getName(), anyFloat(), anyFloat());
}
}