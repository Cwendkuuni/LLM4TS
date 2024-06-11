package fr.pingtimeout.jtail.gui.test;

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.util.JTailLogger;

@RunWith(org.mockito.jUnit.MockitoJUnitRunner.class)
public class JTailPanelTest {

 private JTailPanel jTailPanel;
 private JTailModel jTailModel;
 private JScrollBar scrollBar;

 @Before
 public void setup() {
 JTailLogger.init();
 jTailModel = Mockito.spy(new JTailModel());
 scrollBar = Mockito.mock(JScrollBar.class);
 jTailPanel = new JTailPanel(jTailModel);
 jTailPanel.verticalScrollBar = scrollBar;
 }

 @Test
 public void testJTailPanelConstruction() {
 assertNotNull(jTailPanel);
 assertNotNull(jTailPanel.model);
 assertNotNull(jTailPanel.scrollPane);
 assertNotNull(jTailPanel.verticalScrollBar);
 assertNotNull(jTailPanel.dataTable);
 assertNotNull(jTailPanel.tableModel);
 assertNotNull(jTailPanel.characterHeight);
 assertNull(jTailPanel.maxCharacterWidth);
 }

 @Test
 public void testGetCharacterHeight() {
 final int expectedCharacterHeight = 15; // Assuming a FontMetrics instance returns this height
 assertEquals(expectedCharacterHeight, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertSame(scrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWithResizeEvent() {
 final int minLine = 5;
 final int firstLine = 10;
 final int nbDisplayedLines = 15;
 final int maxLine = 20;
 final JTailModelEvent event = new JTailModelEvent(jTailModel, JTailModelEvent.Type.RESIZED, minLine, firstLine, nbDisplayedLines, maxLine);

 jTailModel.notifyObservers(event);

 final BoundedRangeModel rangeModel = Mockito.mock(BoundedRangeModel.class);
 Mockito.when(scrollBar.getModel()).thenReturn(rangeModel);

 jTailPanel.update(jTailModel, event);

 Mockito.verify(rangeModel).setValueIsAdjusting(true);
 Mockito.verify(rangeModel).setMinimum(minLine);
 Mockito.verify(rangeModel).setValue(firstLine);
 Mockito.verify(rangeModel).setExtent(nbDisplayedLines);
 Mockito.verify(rangeModel).setMaximum(maxLine);
 Mockito.verify(rangeModel).setValueIsAdjusting(false);
 Mockito.verify(jTailPanel).updateDataTable(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"), Arrays.asList("Line 1", "Line 2", "Line 3", "Line 4", "Line 5", "Line 6", "Line 7", "Line 8", "Line 9", "Line 10", "Line 11", "Line 12", "Line 13", "Line 14", "Line 15", "Line 16"));
 }

 @Test
 public void testUpdateWithScrollEvent() {
 final JTailModelEvent event = new JTailModelEvent(jTailModel, JTailModelEvent.Type.SCROLLED, null, null, null, null);

 jTailModel.notifyObservers(event);

 jTailPanel.update(jTailModel, event);

 jTailPanel.updateDataTable(jTailModel.getLineNumbers(), jTailModel.getContent());
 }
}