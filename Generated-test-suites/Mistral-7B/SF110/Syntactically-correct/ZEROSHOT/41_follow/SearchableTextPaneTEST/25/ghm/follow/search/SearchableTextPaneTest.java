package ghm.follow.search;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.*;

public class SearchableTextPaneTest {

 private SearchableTextPane searchableTextPane;
 private List<LineResult> lineResults;

 @Before
 public void setUp() {
 searchableTextPane = new SearchableTextPane(new Font("Arial", Font.PLAIN, 12), 4);
 }

 @Test
 public void testConstructor() {
 assertNotNull(searchableTextPane);
 }

 @Test
 public void testGetScrollableTracksViewportWidth() {
 assertFalse(searchableTextPane.getScrollableTracksViewportWidth());
 }

 @Test
 public void testHighlight_EmptyString() {
 lineResults = searchableTextPane.highlight( "", 0 );
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlight_NullTerm() {
 lineResults = searchableTextPane.highlight( null, 0 );
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlight_EmptyDocument() {
 searchableTextPane.setText( "" );
 lineResults = searchableTextPane.highlight( "test", 0 );
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testHighlight_OneTerm() {
 searchableTextPane.setText( "This is a test text." );
 lineResults = searchableTextPane.highlight( "test", 0 );
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(0, lineResult.getLineNumber());
 assertEquals(0, lineResult.getLineStartOffset());
 assertEquals(11, lineResult.getLineEndOffset());
 assertFalse(lineResult.isEmpty());
 }

 @Test
 public void testHighlight_MultipleTerms() {
 searchableTextPane.setText( "This is a test text. This is a SearchableTextPane." );
 lineResults = searchableTextPane.highlight( "test SearchableTextPane", 0 );
 assertNotNull(lineResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(1, lineResult1.getLineNumber());
 assertEquals(42, lineResult1.getLineStartOffset());
 assertEquals(48, lineResult1.getLineEndOffset());
 assertFalse(lineResult1.isEmpty());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(2, lineResult2.getLineNumber());
 assertEquals(53, lineResult2.getLineStartOffset());
 assertEquals(62, lineResult2.getLineEndOffset());
 assertFalse(lineResult2.isEmpty());
 }

 @Test
 public void testHighlight_Flags() {
 searchableTextPane.setText( "This is a test text. This is a SearchableTextPane." );
 lineResults = searchableTextPane.highlight( "SearchableTextPane", 0 );
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult = lineResults.get(0);
 assertEquals(2, lineResult.getLineNumber());
 assertEquals(53, lineResult.getLineStartOffset());
 assertEquals(62, lineResult.getLineEndOffset());
 assertFalse(lineResult.isEmpty());
 lineResults = searchableTextPane.highlight( "SearchableTextPane", 0, SearchEngine.FLAG_MATCH_CASE );
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 lineResults = searchableTextPane.highlight( "text", 0 );
 assertNotNull(lineResults);
 assertEquals(1, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getLineStartOffset());
 assertEquals(11, lineResult1.getLineEndOffset());
 assertFalse(lineResult1.isEmpty());
 }

 @Test
 public void testRemoveHighlights() {
 // Set text and perform a search to add some highlights
 searchableTextPane.setText( "This is a test text. This is a SearchableTextPane." );
 lineResults = searchableTextPane.highlight( "SearchableTextPane", 0 );
 // Assert that there are some highlights
 assertFalse(searchableTextPane.getHighlighter().hasHighlightAt(0, 0));
 assertFalse(searchableTextPane.getHighlighter().hasHighlightAt(1, 1));
 assertFalse(searchableTextPane.getHighlighter().hasHighlightAt(11, 11));
 assertFalse(searchableTextPane.getHighlighter().hasHighlightAt(12, 12));
 assertFalse(searchableTextPane.getHighlighter().hasHighlightAt(13, 13));
 // Remove highlights
 searchableTextPane.removeHighlights();
 // Assert that there are no highlights anymore
 assertFalse(searchableTextPane.getHighlighter().hasHighlightAt(0, 0));
 assertFalse(searchableTextPane.getHighlighter().hasHighlightAt(1, 1));
 assertFalse(searchableTextPane.getHighlighter().hasHighlightAt(11, 11));
 assertFalse(searchableTextPane.getHighlighter().hasHighlightAt(12, 12));
 assertFalse(searchableTextPane.getHighlighter().hasHighlightAt(13, 13));
 }

 @Test
 public void testSearch_EmptyString() {
 int position = searchableTextPane.search( "" );
 assertEquals(0, position);
 }

 @Test
 public void testSearch_NullTerm() {
 int position = searchableTextPane.search( null );
 assertEquals(-1, position);
 }

 @Test
 public void testSearch_EmptyDocument() {
 searchableTextPane.setText( "" );
 int position = searchableTextPane.search( "test" );
 assertEquals(-1, position);
 }

 @Test
 public void testSearch_OneTerm() {
 searchableTextPane.setText( "This is a test text." );
 int position = searchableTextPane.search( "test" );
 assertEquals(7, position);
 }

 @Test
 public void testSearch_MultipleTerms() {
 searchableTextPane.setText( "This is a test text. This is a SearchableTextPane." );
 int position = searchableTextPane.search( "test SearchableTextPane" );
 assertEquals(53, position);
 }

 @Test
 public void testSearch_Flags() {
 searchableTextPane.setText( "This is a test text. This is a SearchableTextPane." );
 int position = searchableTextPane.search( "SearchableTextPane", SearchEngine.FLAG_MATCH_CASE );
 assertEquals(-1, position);
 int position1 = searchableTextPane.search( "text" );
 assertEquals(0, position1);
 }

 @Test
 public void testConvertWords2Lines_EmptyList() {
 List<WordResult> words = new ArrayList<WordResult>();
 lineResults = searchableTextPane.convertWords2Lines( words );
 assertNotNull(lineResults);
 assertEquals(0, lineResults.size());
 }

 @Test
 public void testConvertWords2Lines() {
 List<WordResult> words = Arrays.asList( new WordResult( "test", 0, 4 ), new WordResult( "SearchableTextPane", 42, 48 ) );
 lineResults = searchableTextPane.convertWords2Lines( words );
 assertNotNull(lineResults);
 assertEquals(2, lineResults.size());
 LineResult lineResult1 = lineResults.get(0);
 assertEquals(0, lineResult1.getLineNumber());
 assertEquals(0, lineResult1.getLineStartOffset());
 assertEquals(11, lineResult1.getLineEndOffset());
 assertFalse(lineResult1.isEmpty());
 LineResult lineResult2 = lineResults.get(1);
 assertEquals(1, lineResult2.getLineNumber());
 assertEquals(42, lineResult2.getLineStartOffset());
 assertEquals(48, lineResult2.getLineEndOffset());
 assertFalse(lineResult2.isEmpty());
 }
}