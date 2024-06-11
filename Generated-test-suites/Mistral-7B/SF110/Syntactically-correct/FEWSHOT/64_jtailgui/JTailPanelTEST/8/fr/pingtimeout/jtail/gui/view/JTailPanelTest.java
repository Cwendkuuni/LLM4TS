package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.gui.view.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel panelUnderTest;

 @Before
 public void setup() {
 mockModel = mock(JTailModel.class);
 panelUnderTest = new JTailPanel(mockModel);
 }

 @Test
 public void testConstructor() {
 assertEquals("Building a new JTailPanel", JTailLogger.DEBUG_MESSAGES.get(0));
 assertNotNull(panelUnderTest.model);
 assertNotNull(panelUnderTest.scrollPane);
 assertNotNull(panelUnderTest.dataTable);
 assertNotNull(panelUnderTest.verticalScrollBar);
 assertNotNull(panelUnderTest.tableModel);
 assertNotNull(panelUnderTest.characterHeight);
 assertNull(panelUnderTest.maxCharacterWidth);
 }

 @Test
 public void testGetCharacterHeight() {
 assertEquals(panelUnderTest.characterHeight, 1); // Assuming default font
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertSame(panelUnderTest.verticalScrollBar, panelUnderTest.getVerticalScrollBar());
 }

 @Test
 public void testUpdate() {
 JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 1, 1, 5, 5);
 panelUnderTest.update(mockModel, event);

 verify(mockModel, times(1)).getLineNumbers();
 verify(mockModel, times(1)).getContent();

 TableModel newTableModel = new DefaultTableModel(new String[]{"#", ""}, 0) {
 @Override
 public boolean isCellEditable(final int row, final int col) {
 return false;
 }
 };
 verify(panelUnderTest.tableModel, times(1)).setModelDataVector(anyArray(), anyInt());
 assertEquals("Resizing column widths", JTailLogger.DEBUG_MESSAGES.get(panelUnderTest.JTailLogger.DEBUG_MESSAGES.size() - 1));
 }

 @Test
 public void testUpdateWithScrollBarDisabled() {
 JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 1, 1, 5, 5);
 panelUnderTest.update(mockModel, event);

 verifyNoMoreInteractions(panelUnderTest.verticalScrollBar);
 }

 @Test
 public void testUpdateWithScrollBarEnabled() {
 JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 1, 1, 5, 5);
 panelUnderTest.update(mockModel, event);

 verify(panelUnderTest.verticalScrollBar, times(1)).setEnabled(true);
 }

 @Test
 public void testUpdateWithSameData() {
 JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, 1, 1, 5, 5);
 panelUnderTest.update(mockModel, event);

 verifyNoMoreInteractions(panelUnderTest.dataTable);
 }
}