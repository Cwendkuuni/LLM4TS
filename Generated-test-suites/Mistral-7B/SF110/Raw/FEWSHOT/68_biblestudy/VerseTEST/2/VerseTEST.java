package bible.obj;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.util.*;
import java.sql.*;
import bible.util.*;

public class VerseTest {

 private static final Translation[] translations = new Translation[3];
 private static final Reference begin = new Reference(1, 1, 1);
 private static final Reference end = new Reference(1, 1, 2);

 static {
 for (int i = 0; i < translations.length; ++i) {
 try {
 translations[i] = new Translation(i + 1);
 } catch (SQLException e) {
 e.printStackTrace();
 }
 }
 }

 @Test
 public void testNew() {
 Verse verse1 = Verse.New(1);
 assertEquals(new Integer(1), new Integer(verse1.getId()));
 }

 @Test(expected = NullPointerException.class)
 public void testNewNullId() {
 Verse.New(null);
 }

 @Test
 public void testGetIdTranslationBookChapterVerse() {
 int id = Verse.GetId(translations[0], new Book(1), 1, 1);
 assertEquals(new Integer(1), new Integer(id));
 }

 @Test
 public void testGetEmpty() {
 ArrayList verses = Verse.Get(translations, null, null);
 assertEquals(0, verses.size());
 }

 @Test
 public void testGetTranslation() {
 ArrayList verses = Verse.Get(translations, begin, end);
 for (int i = 0; i < verses.size(); ++i) {
 Verse verse = (Verse) verses.get(i);
 assertEquals(new Integer(1), new Integer(verse.getId()));
 }
 }

 @Test
 public void testGetDatabase() {
 ArrayList verses = Verse.Get(translations, begin, end);
 for (int i = 0; i < verses.size(); ++i) {
 Verse verse = (Verse) verses.get(i);
 assertNotNull(verse.getText());
 assertNotNull(verse.getTranslationReference());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetInvalidArguments() {
 Verse.Get(null, new Book(1), begin, end);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetNullTranslations() {
 Verse.Get(null, null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBeginNull() {
 Verse.Get(translations, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEndNull() {
 Verse.Get(translations, begin, null);
 }

 @Test
 public void testToString() {
 Verse verse = new Verse(1);
 assertEquals("bible.obj.Verse:[1 [bible.util.TranslationReference:[1 bible.obj.Reference:[1 1 1]] bible.obj.Text:foo]]", verse.toString());
 }

 @Test
 public void testStripLeadingHyphensAndGetId() {
 assertEquals(1, Verse.GetId(translations[0], new Book(1), 1, "-1"));
 assertEquals(1, Verse.GetId(translations[0], new Book(1), 1, "1"));
 assertEquals(2, Verse.GetId(translations[0], new Book(1), 1, "2"));
 }

 @Test
 public void testGetIdInvalidArguments() {
 assertNull(Verse.GetId(0, null, null, null));
 assertNull(Verse.GetId(translations[0].getId(), null, null, null));
 assertNull(Verse.GetId(translations[0].getId(), new Book(1), null, null));
 assertNull(Verse.GetId(translations[0].getId(), new Book(1), 1, null));
 }

 @Test
 public void testGet() throws SQLException {
 ArrayList verses = Verse.Get(translations, begin, end);
 for (int i = 0; i < verses.size(); ++i) {
 Verse verse = (Verse) verses.get(i);
 assertEquals(new Integer(1), new Integer(verse.getId()));
 assertEquals("foo", verse.getText());
 assertEquals(translations[0], verse.getTranslationReference().getTranslation());
 }
 }
}