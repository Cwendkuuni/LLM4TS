package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.gui.view.*;
import fr.pingtimeout.jtail.util.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 JTailLogger.setLevel(java.util.logging.Level.FINEST);
 model = new JTailModel(new String[]{"line1", "line2", "line3", "line4", "line5"});
 jTailPanel = new JTailPanel(model);
 }

 @Test
 public void testConstruction() {
 // Test if the JTailPanel is initialized properly
 assertNotNull(jTailPanel);
 assertEquals("JTailPanel size should be 230 by default", new Dimension(230, 180), jTailPanel.getSize());
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertNotNull(jTailPanel.dataTable);
 assertNotNull(jTailPanel.scrollPane);
 }

 @Test
 public void testGetCharacterHeight() {
 int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals("Character height should be 15", 15, characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertNotNull(verticalScrollBar);
 }

 @Test
 public void testUpdate() {
 // Simulate a model event
 model.fireEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 5, Arrays.asList( Arrays.asList(1, "line1"), Arrays.asList(2, "line2"), Arrays.asList(3, "line3"), Arrays.asList(4, "line4"), Arrays.asList(5, "line5")), Arrays.asList("line6", "line7", "line8", "line9", "line10")));

 // Test if the vertical scroll bar is updated correctly
 assertEquals("First line displayed should be 1", 1, jTailPanel.model.getFirstLine());
 assertEquals("Number of displayed lines should be 5", 5, jTailPanel.model.getNbDisplayedLines());
 assertEquals("Maximum line should be 10", 10, jTailPanel.model.getMaxLine());
 assertEquals("Number of lines should be 10", 10, jTailPanel.model.getSize());

 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 AdjustmentEvent adjustmentEvent = new AdjustmentEvent(verticalScrollBar, AdjustmentEvent.ADJUSTING, 0, 5, 5);
 verticalScrollBar.processAdjustmentEvent(adjustmentEvent);

 // Test if the data table is updated correctly
 String[] lines = {"line1", "line2", "line3", "line4", "line5", "line6", "line7", "line8", "line9", "line10"};
 String[] lineNumbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
 assertArrayEquals("Line numbers should be updated correctly", lineNumbers, jTailPanel.model.getLineNumbers().toArray());
 assertArrayEquals("Lines should be updated correctly", lines, jTailPanel.model.getContent().toArray());
 }

}