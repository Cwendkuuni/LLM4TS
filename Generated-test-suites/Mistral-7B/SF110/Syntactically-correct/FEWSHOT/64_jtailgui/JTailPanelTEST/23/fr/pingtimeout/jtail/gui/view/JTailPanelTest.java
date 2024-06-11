package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel panelUnderTest;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 panelUnderTest = new JTailPanel(mockModel);
 }

 @Test
 public void testUpdateTableWhenModelIsResized() {
 // Given
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
 when(mockModel.getContent()).thenReturn("line1\nline2\nline3\nline4\nline5");
 // When
 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, "newSize"));
 // Then
 verify(mockModel, times(1)).getLineNumbers();
 verify(mockModel, times(1)).getContent();

 TableModel tableModel = panelUnderTest.tableModel;
 int numRows = tableModel.getRowCount();
 assertEquals(5, numRows);

 String[] lineNumbers = new String[numRows];
 for (int i = 0; i < numRows; i++) {
 lineNumbers[i] = (String)tableModel.getValueAt(i, 0);
 }
 assertArrayEquals(new String[]{"1", "2", "3", "4", "5"}, lineNumbers);

 String[] lines = new String[numRows];
 for (int i = 0; i < numRows; i++) {
 lines[i] = (String)tableModel.getValueAt(i, 1);
 }
 assertArrayEquals(new String[]{"line1", "line2", "line3", "line4", "line5"}, lines);
 }

 @Test
 public void testUpdateTableWhenModelIsScrolled() {
 // Given
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3\n4\n5");
 when(mockModel.getContent()).thenReturn("line1\nline2\nline3\nline4\nline5");
 // When
 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 2));
 // Then
 verify(mockModel, times(1)).getLineNumbers();
 verify(mockModel, times(1)).getContent();

 TableModel tableModel = panelUnderTest.tableModel;
 int numRows = tableModel.getRowCount();
 assertEquals(5, numRows);

 String[] lineNumbers = new String[numRows];
 for (int i = 0; i < numRows; i++) {
 lineNumbers[i] = (String)tableModel.getValueAt(i, 0);
 }
 assertArrayEquals(new String[]{"1", "2", "3", "4", "5"}, lineNumbers);

 String[] lines = new String[numRows];
 for (int i = 0; i < numRows; i++) {
 lines[i] = (String)tableModel.getValueAt(i, 1);
 }
 assertArrayEquals(new String[]{"line1", "line2", "line3", "line4", "line5"}, lines);
 }
}