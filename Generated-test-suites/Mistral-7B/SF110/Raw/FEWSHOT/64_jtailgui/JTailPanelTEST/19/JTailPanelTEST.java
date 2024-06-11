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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers.*;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel panelUnderTest;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 panelUnderTest = new JTailPanel(mockModel);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals("JTailPanel should have the JTailModel as a field", mockModel, panelUnderTest.model);
 assertNotNull("Scroll pane should not be null", panelUnderTest.scrollPane);
 assertNotNull("Table should not be null", panelUnderTest.dataTable);
 assertNotNull("Table model should not be null", panelUnderTest.tableModel);
 assertNotNull("Character height should be set", panelUnderTest.characterHeight);
 assertNotNull("Vertical ScrollBar should not be null", panelUnderTest.verticalScrollBar);
 }

 @Test
 public void testGetCharacterHeight() {
 assertEquals("Character height should be the expected value", panelUnderTest.getCharacterHeight(), panelUnderTest.characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertEquals("Vertical ScrollBar should be the expected value", panelUnderTest.getVerticalScrollBar(), panelUnderTest.verticalScrollBar);
 }

 @Test
 public void testUpdateWithNewModelData() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7";
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7";
 when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
 when(mockModel.getContent()).thenReturn(lines);

 panelUnderTest.update(mockModel, mock(JTailModelEvent.class));

 assertEquals("Number of rows in the table model should be equal to the number of lines", 7, panelUnderTest.tableModel.getRowCount());

 for (int i = 0; i < 7; i++) {
 String expectedLineNumber = "1" + (i + 1);
 String expectedLine = "Line " + (i + 1);
 String[] rowData = (String[]) panelUnderTest.tableModel.getDataVector().get(i);
 assertEquals("Line number should match", expectedLineNumber, rowData[0]);
 assertEquals("Line content should match", expectedLine, rowData[1]);
 }
 }

 @Test
 public void testDisableScrollBarWhenNoLinesToDisplay() {
 final String lineNumbers = "1\n2\n";
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7";
 when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
 when(mockModel.getContent()).thenReturn(lines);

 panelUnderTest.update(mockModel, mock(JTailModelEvent.class));

 assertTrue("ScrollBar should be disabled when no lines can be displayed", !panelUnderTest.verticalScrollBar.isEnabled());
 }

 @Test
 public void testEnableScrollBarWhenLinesToDisplay() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15";
 final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10\nLine 11\nLine 12\nLine 13\nLine 14\nLine 15";
 when(mockModel.getLineNumbers()).thenReturn(lineNumbers);
 when(mockModel.getContent()).thenReturn(lines);

 panelUnderTest.update(mockModel, mock(JTailModelEvent.class));

 assertFalse("ScrollBar should be disabled when all lines can be displayed", !panelUnderTest.verticalScrollBar.isEnabled());
 }
}