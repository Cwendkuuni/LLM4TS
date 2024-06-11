package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.SizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent.Type;
import fr.pingtimeout.jtail.util.JTailLogger;
import fr.pingtimeout.jtail.util.LineNumbers;

public class JTailPanelTest {

 private JTailPanel jtailPanel;
 private JTailModel model;
 private ScrollBarAdjustmentController scrollBarAdjustmentController;
 private SizeController sizeController;

 @Before
 public void setUp() {
 JTailLogger.init();
 model = mock(JTailModel.class);
 jtailPanel = new JTailPanel(model);
 scrollBarAdjustmentController = mock(ScrollBarAdjustmentController.class);
 sizeController = mock(SizeController.class);
 jtailPanel.verticalScrollBar.setAdjustmentListener(scrollBarAdjustmentController);
 jtailPanel.addComponentListener(sizeController);
 }

 @After
 public void tearDown() {
 jtailPanel = null;
 model = null;
 scrollBarAdjustmentController = null;
 sizeController = null;
 }

 @Test
 public void testJTailPanelConstruction() {
 assertNotNull(jtailPanel);
 assertEquals(BorderLayout.class, jtailPanel.getLayoutClass());
 }

 @Test
 public void testCharacterHeight() {
 final FontMetrics metrics = jtailPanel.dataTable.getFontMetrics(jtailPanel.dataTable.getFont());
 final int characterHeight = metrics.getHeight() - 1;
 assertEquals(characterHeight, jtailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertNotNull(jtailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWithEmptyContent() {
 String lineNumbers = "";
 String lines = "";
 jtailPanel.updateDataTable(lineNumbers, lines);
 assertTrue(jtailPanel.dataTable.getRowCount() == 0);
 }

 @Test
 public void testUpdateWithContent() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "line1\nline2\nline3\nline4\nline5";
 jtailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(lineNumbers.split("\n").length, jtailPanel.dataTable.getRowCount());
 for (int i = 0; i < jtailPanel.dataTable.getRowCount(); i++) {
 final String lineNumber = jtailPanel.dataTable.getValueAt(i, 0).toString();
 final String line = jtailPanel.dataTable.getValueAt(i, 1).toString();
 assertEquals(lineNumber, "1" + (i + 1));
 assertEquals(line, "line" + (i + 1));
 }
 }

 @Test
 public void testUpdateWithMinMaxLines() {
 when(model.getMinLine()).thenReturn(1);
 when(model.getMaxLine()).thenReturn(5);
 when(model.getNbDisplayedLines()).thenReturn(3);
 jtailPanel.update(model, new JTailModelEvent(Type.RESIZED, model));
 int minLine = jtailPanel.verticalScrollBar.getModel().getMinimum();
 int firstLine = jtailPanel.verticalScrollBar.getModel().getValue();
 int nbDisplayedLines = jtailPanel.verticalScrollBar.getModel().getExtent();
 int maxLine = jtailPanel.verticalScrollBar.getModel().getMaximum();
 assertEquals(1, minLine);
 assertEquals(1, firstLine);
 assertEquals(3, nbDisplayedLines);
 assertEquals(5, maxLine);
 assertFalse(jtailPanel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdateWithEnabledScrollBar() {
 when(model.getMinLine()).thenReturn(1);
 when(model.getMaxLine()).thenReturn(5);
 when(model.getNbDisplayedLines()).thenReturn(3);
 when(model.getFirstLine()).thenReturn(3);
 when(model.getMaxLine()).thenReturn(3);
 jtailPanel.update(model, new JTailModelEvent(Type.RESIZED, model));
 int minLine = jtailPanel.verticalScrollBar.getModel().getMinimum();
 int firstLine = jtailPanel.verticalScrollBar.getModel().getValue();
 int nbDisplayedLines = jtailPanel.verticalScrollBar.getModel().getExtent();
 int maxLine = jtailPanel.verticalScrollBar.getModel().getMaximum();
 assertEquals(1, minLine);
 assertEquals(3, firstLine);
 assertEquals(3, nbDisplayedLines);
 assertEquals(5, maxLine);
 assertTrue(jtailPanel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdateWithDisabledScrollBar() {
 when(model.getMinLine()).thenReturn(1);
 when(model.getMaxLine()).thenReturn(5);
 when(model.getNbDisplayedLines()).thenReturn(3);
 when(model.getFirstLine()).thenReturn(1);
 when(model.getMaxLine()).thenReturn(3);
 jtailPanel.update(model, new JTailModelEvent(Type.RESIZED, model));
 int minLine = jtailPanel.verticalScrollBar.getModel().getMinimum();
 int firstLine = jtailPanel.verticalScrollBar.getModel().getValue();
 int nbDisplayedLines = jtailPanel.verticalScrollBar.getModel().getExtent();
 int maxLine = jtailPanel.verticalScrollBar.getModel().getMaximum();
 assertEquals(1, minLine);
 assertEquals(1, firstLine);
 assertEquals(3, nbDisplayedLines);
 assertEquals(3, maxLine);
 assertFalse(jtailPanel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdateWithWrongContentSize() {
 when(model.getMinLine()).thenReturn(1);
 when(model.getMaxLine()).thenReturn(5);
 when(model.getNbDisplayedLines()).thenReturn(3);
 when(model.getFirstLine()).thenReturn(1);
 when(model.getMaxLine()).thenReturn(3);
 when(model.getContent()).thenReturn("line1\nline2\nline3\nline4\nline5\nline6");
 jtailPanel.update(model, new JTailModelEvent(Type.RESIZED, model));
 int minLine = jtailPanel.verticalScrollBar.getModel().getMinimum();
 int firstLine = jtailPanel.verticalScrollBar.getModel().getValue();
 int nbDisplayedLines = jtailPanel.verticalScrollBar.getModel().getExtent();
 int maxLine = jtailPanel.verticalScrollBar.getModel().getMaximum();
 assertEquals(1, minLine);
 assertEquals(1, firstLine);
 assertEquals(3, nbDisplayedLines);
 assertEquals(5, maxLine);
 assertTrue(jtailPanel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdateWithWrongNumberOfLines() {
 when(model.getMinLine()).thenReturn(1);
 when(model.getMaxLine()).thenReturn(5);
 when(model.getNbDisplayedLines()).thenReturn(3);
 when(model.getContent()).thenReturn("line1\nline2");
 jtailPanel.update(model, new JTailModelEvent(Type.RESIZED, model));
 int minLine = jtailPanel.verticalScrollBar.getModel().getMinimum();
 int firstLine = jtailPanel.verticalScrollBar.getModel().getValue();
 int nbDisplayedLines = jtailPanel.verticalScrollBar.getModel().getExtent();
 int maxLine = jtailPanel.verticalScrollBar.getModel().getMaximum();
 assertEquals(1, minLine);
 assertEquals(1, firstLine);
 assertEquals(2, nbDisplayedLines);
 assertEquals(2, maxLine);
 assertFalse(jtailPanel.verticalScrollBar.isEnabled());
 }
}