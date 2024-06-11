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
 public void testUpdateWithNoChanges() {
 // Given
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3");
 when(mockModel.getContent()).thenReturn("foo\nbar\nbaz");

 // When
 panelUnderTest.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.NONE));

 // Then
 assertEquals("1, foo\n2, bar\n3, baz", panelUnderTest.tableModel.asString());
 }

 @Test
 public void testUpdateWithResized() {
 // Given
 final int maxWidth = 100;
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3");
 when(mockModel.getContent()).thenReturn("foo\nbar\nbaz");
 doNothing().when(panelUnderTest).updateDataTable(argThat(isA(String[].class)), argThat(isA(String.class)));

 // When
 panelUnderTest.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, maxWidth));

 // Then
 verify(panelUnderTest, times(1)).updateDataTable(anyString(), anyString());
 }

 @Test
 public void testUpdateWithScrolled() {
 // Given
 when(mockModel.getLineNumbers()).thenReturn("1\n2\n3");
 when(mockModel.getContent()).thenReturn("foo\nbar\nbaz");
 final int firstLine = 2;
 final int maxLine = 3;
 final int nbDisplayedLines = 3;
 doNothing().when(panelUnderTest).updateDataTable(argThat(isA(String[].class)), argThat(isA(String.class)));

 // When
 panelUnderTest.update(mockModel, new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, firstLine, maxLine, nbDisplayedLines));

 // Then
 verify(panelUnderTest, times(1)).updateDataTable(anyString(), anyString());
 }

 @Test
 public void testUpdateDataTableWithNoChanges() {
 // Given
 final String lineNumbers = "1\n2\n3";
 final String content = "foo\nbar\nbaz";

 // When
 panelUnderTest.updateDataTable(lineNumbers, content);

 // Then
 assertEquals("1, foo\n2, bar\n3, baz", panelUnderTest.tableModel.asString());
 }

 @Test
 public void testUpdateDataTableWithChangedModel() {
 // Given
 final String lineNumbers = "1\n2\n3";
 final String content = "foo\nbar\nbaz";
 final String newLineNumbers = "1\n3\n4";
 final String newContent = "qux\nqux\nbaz";

 // When
 panelUnderTest.updateDataTable(lineNumbers, content);

 // Then
 assertEquals("1, foo\n2, bar\n3, baz", panelUnderTest.tableModel.asString());

 // When
 panelUnderTest.updateDataTable(newLineNumbers, newContent);

 // Then
 assertEquals("1, qux\n3, qux\n4, baz", panelUnderTest.tableModel.asString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableWithNullLineNumbers() {
 // Given
 final String lineNumbers = null;
 final String content = "foo\nbar\nbaz";

 // When
 panelUnderTest.updateDataTable(lineNumbers, content);

 // Then
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableWithEmptyLineNumbers() {
 // Given
 final String lineNumbers = "";
 final String content = "foo\nbar\nbaz";

 // When
 panelUnderTest.updateDataTable(lineNumbers, content);
 // Then
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableWithNullContent() {
 // Given
 final String lineNumbers = "1\n2\n3";
 final String content = null;
 // When
 panelUnderTest.updateDataTable(lineNumbers, content);
 // Then
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableWithEmptyContent() {
 // Given
 final String lineNumbers = "1\n2\n3";
 final String content = "";
 // When
 panelUnderTest.updateDataTable(lineNumbers, content);
 // Then
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testUpdateTableWidths() {
 // Given
 final int width = 100;
 final int maxCharacterWidth = 10;
 final int characterHeight = 10;
 final int[] lineNumbers = { "1", "2", "3" };
 final String[] lines = { "foo", "bar", "baz" };
 final TableColumn numbersColumn = mock(TableColumn.class);
 final TableColumn linesColumn = mock(TableColumn.class);

 // When
 panelUnderTest.updateDataTable(lineNumbers, lines);

 // Then
 verify(panelUnderTest, times(1)).updateTableWidths(width, maxCharacterWidth, characterHeight, numbersColumn, linesColumn);
 }

 @Test
 public void testUpdateTableWidthsWithEmptyModel() {
 // Given
 final int width = 100;
 final int maxCharacterWidth = 10;
 final int characterHeight = 10;
 final TableColumn numbersColumn = mock(TableColumn.class);
 final TableColumn linesColumn = mock(TableColumn.class);

 // When
 panelUnderTest.updateTableWidths(width, maxCharacterWidth, characterHeight, numbersColumn, linesColumn);

 // Then
 verifyNoInteractions(panelUnderTest);
 }
}