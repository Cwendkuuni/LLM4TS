import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import net.virtualinfinity.atrobots.gui.renderers.RobotRenderer;
import net.virtualinfinity.atrobots.measures.Length;
import net.virtualinfinity.atrobots.measures.Temperature;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import java.awt.*;
import java.awt.geom.*;

public class RobotRendererTest {

 private RobotRenderer renderer;
 private Graphics2D g2dMock;
 private RobotSnapshot robotSnapshot;

 @Before
 public void setup() {
 renderer = new RobotRenderer();
 g2dMock = mock(Graphics2D.class);
 robotSnapshot = new RobotSnapshot(1, new Length(10.0), new Length(10.0), new Temperature(100.0), new Temperature(50.0), new Vector2D(0.0, 0.0), new Vector2D(0.0, 0.0), new Angle(0.0), new Angle(0.0), new Angle(0.0), new Angle(0.0), true, false, false, false, true, true, true, true);
 }

 @Test
 public void testRenderWhenRobotIsDead() {
 // Given
 when(g2dMock.getFontMetrics()).thenReturn(Mockito.mock(FontMetrics.class));

 // When
 renderer.render(g2dMock, robotSnapshot, Mockito.Collections.singleton(1));

 // Then
 Mockito.verify(g2dMock, Mockito.times(1)).setPaint(Mockito.eq(Mockito.any(Color.class)));
 Mockito.verify(g2dMock, Mockito.times(1)).setStroke(Mockito.any(Stroke.class));
 Mockito.verify(g2dMock, Mockito.never()).fill(Mockito.any(Shape.class));
 Mockito.verify(g2dMock, Mockito.never()).draw(Mockito.any(Shape.class));
 }

 @Test
 public void testRenderWhenRobotIsAlive() {
 // Given
 when(g2dMock.getFontMetrics()).thenReturn(Mockito.mock(FontMetrics.class));

 // When
 renderer.render(g2dMock, robotSnapshot, Mockito.Collections.singleton(1));

 // Then
 Mockito.verify(g2dMock, Mockito.times(1)).setPaint(Mockito.any(Color.class));
 Mockito.verify(g2dMock, Mockito.times(1)).setStroke(Mockito.any(Stroke.class));
 Mockito.verify(g2dMock, Mockito.atLeastOnce()).fill(Mockito.any(Shape.class));
 Mockito.verify(g2dMock, Mockito.atLeastOnce()).draw(Mockito.any(Shape.class));
 }

 @Test
 public void testIsRenderDead() {
 // Given

 // When
 boolean result = renderer.isRenderDead();

 // Then
 assertTrue(result);
 }

 @Test
 public void testSetRenderDead() {
 // Given

 // When
 renderer.setRenderDead(false);

 // Then
 assertFalse(renderer.isRenderDead());
 }

 @Test
 public void testRenderRobotWithSelection() {
 // Given

 // When
 renderer.render(g2dMock, robotSnapshot, Mockito.Collections.singleton(1));

 // Then
 Mockito.verify(g2dMock, Mockito.times(1)).setPaint(Mockito.eq(new Color(1.0f, 1.0f, 0.0f, 0.25f)));
 }

 @Test
 public void testPaintSelectionOutsideOfRobotBounds() {
 // Given
 final RobotSnapshot robotSnapshotOutsideBounds = new RobotSnapshot(1, new Length(10.0), new Length(10.0), new Temperature(100.0), new Temperature(50.0), new Vector2D(-10.0, -10.0), new Vector2D(0.0, 0.0), new Angle(0.0), new Angle(0.0), new Angle(0.0), new Angle(0.0), false, false, false, false, true, true, true, true);
 renderer.paintSelection(g2dMock, robotSnapshotOutsideBounds);

 // Then
 Mockito.verify(g2dMock, Mockito.never()).setPaint(Mockito.any(Color.class));
 }
}