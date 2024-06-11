package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.AdjustmentEvent;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.event.JTailModelEvent;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.gui.view.JTailPanelModel;

public class JTailPanelTest {

 private JTailPanel panel;
 private JTailModel mockModel;
 private ScrollBarAdjustmentController mockScrollBarAdjustmentController;
 private JTailSizeController mockJTailSizeController;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 panel = new JTailPanel(mockModel);
 mockScrollBarAdjustmentController = mock(ScrollBarAdjustmentController.class);
 mockJTailSizeController = mock(JTailSizeController.class);
 panel.setVerticalScrollBar(mockScrollBarAdjustmentController);
 panel.addComponentListener(mockJTailSizeController);
 }

 @Test
 public void testInitialization() {
 assertEquals(mockModel, panel.getModel());
 assertNull(panel.getVerticalScrollBar());
 assertTrue(panel.isOpaque());
 }

 @Test
 public void testGetCharacterHeight() {
 final FontMetrics metrics = mock(FontMetrics.class);
 when(panel.getDataTable().getFontMetrics(any(Font.class))).thenReturn(metrics);
 when(metrics.getHeight()).thenReturn(20);
 assertEquals(19, panel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertSame(mockScrollBarAdjustmentController, panel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateObserverWithResizedEvent() {
 final int firstLine = 1;
 final int maxLine = 10;
 final int minLine = 5;
 final int nbDisplayedLines = 3;
 final JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, firstLine, maxLine, minLine, nbDisplayedLines);
 panel.update(mockModel, event);

 ArgumentCaptor<AdjustmentEvent> scrollBarAdjustmentEventCaptor = ArgumentCaptor.forClass(AdjustmentEvent.class);
 verify(mockScrollBarAdjustmentController).valueChanged(scrollBarAdjustmentEventCaptor.capture());
 AdjustmentEvent scrollBarAdjustmentEvent = scrollBarAdjustmentEventCaptor.getValue();
 assertEquals(firstLine, scrollBarAdjustmentEvent.getValue());
 assertEquals(nbDisplayedLines, scrollBarAdjustmentEvent.getExtent());
 assertEquals(maxLine, scrollBarAdjustmentEvent.getMaximum());
 assertTrue(scrollBarAdjustmentEvent.isAdjusting());
 }

 @Test
 public void testUpdateObserverWithScrolledEvent() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10";
 panel.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, 5, lineNumbers, lines));

 ArgumentCaptor<ComponentEvent> componentEventCaptor = ArgumentCaptor.forClass(ComponentEvent.class);
 verify(mockJTailSizeController).componentResized(componentEventCaptor.capture());
 ComponentEvent componentEvent = componentEventCaptor.getValue();
 assertEquals(ComponentEvent.COMPONENT_RESIZED, componentEvent.getID());
 }

 @Test
 public void testUpdateDataTable() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10";
 panel.updateDataTable(lineNumbers, lines);
 assertEquals(10, panel.getTableModel().getRowCount());
 assertArrayEquals(new String[] { "1", "Line 1", "2", "Line 2", "3", "Line 3", "4", "Line 4", "5", "Line 5" }, panel.getTableModel().getColumns());
 }

 @Test
 public void testUpdateDataTableWithLongerLines() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11";
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10\nLine 11 with longer length";
 panel.updateDataTable(lineNumbers, lines);
 assertEquals(11, panel.getTableModel().getRowCount());
 assertArrayEquals(new String[] { "1", "Line 1", "2", "Line 2", "3", "Line 3", "4", "Line 4", "5", "Line 5", "6" }, panel.getTableModel().getColumns());
 }
}