import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import bible.obj.Verse;
import bible.obj.Translation;
import bible.obj.Reference;
import java.util.*;

public class VerseTest {

 private static final int ID = 1;
 private static final int TRANSLATION_ID = 1;
 private static final int BOOK_ID = 1;
 private static final int CHAPTER = 1;
 private static final int VERSE_ID = 1;
 private static final int VERSE_ID_NOT_FOUND = -1;
 private static final Translation TRANSLATION = new Translation(TRANSLATION_ID);
 private static final Reference REFERENCE = new Reference(BOOK_ID, CHAPTER, VERSE_ID);
 private static final Verse VERSE = new Verse(ID);
 private static final DbResult DUMMY_RESULT = new DbResult() {{
     try {
         addColumnName("Verse_ID");
         addColumnName("Text");
         addColumnName("Translation_ID");
         addColumnName("Book_ID");
         addColumnName("Chapter");
         addColumnName("Verse_No");
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }};

 @Test
 public void testNew() {
     Verse verse1 = Verse.New(ID);
     assertNotNull(verse1);
     assertEquals(ID, verse1.getId());
 }

 @Test
 public void testGetId() {
     assertEquals(ID, Verse.GetId(TRANSLATION, BOOK_ID, CHAPTER, VERSE_ID));
 }

 @Test
 public void testGet() {
     Translation[] translations = new Translation[1];
     translations[0] = TRANSLATION;
     ArrayList verses = Verse.Get(translations, new Reference(BOOK_ID, CHAPTER, VERSE_ID), new Reference(BOOK_ID, CHAPTER, VERSE_ID));
     assertNotNull(verses);
     assertEquals(1, verses.size());
     Verse verse2 = (Verse) verses.get(0);
     assertNotNull(verse2);
     assertEquals(ID, verse2.getId());
 }

 @Test
 public void testGetWithEmptyTranslationArray() {
     ArrayList verses = Verse.Get(new Translation[0], new Reference(BOOK_ID, CHAPTER, VERSE_ID), new Reference(BOOK_ID, CHAPTER, VERSE_ID));
     assertNotNull(verses);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithNullTranslationArray() {
     ArrayList verses = Verse.Get(null, new Reference(BOOK_ID, CHAPTER, VERSE_ID), new Reference(BOOK_ID, CHAPTER, VERSE_ID));
     assertNotNull(verses);
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetWithNullBeginReference() {
     ArrayList verses = Verse.Get(TRANSLATIONS_ARRAY, null, new Reference(BOOK_ID, CHAPTER, VERSE_ID));
     assertNotNull(verses);
     assertEquals(0, verses.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithNullEndReference() {
     ArrayList verses = Verse.Get(TRANSLATIONS_ARRAY, new Reference(BOOK_ID, CHAPTER, VERSE_ID), null);
 }

 @Test
 public void testGetWithEmptyQueryResult() {
     DbResult result = new DbResult() {{
         try {
             addColumnName("Verse_ID");
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }};
     ArrayList verses = Verse.Get(TRANSLATIONS_ARRAY, new Reference(BOOK_ID, CHAPTER, VERSE_ID), new Reference(BOOK_ID, CHAPTER, VERSE_ID));
     assertEquals(0, verses.size());
 }

 @Test
 public void testGetTranslationFromDb() {
     ArrayList verses = Verse.Get(TRANSLATIONS_ARRAY, new Reference(BOOK_ID, CHAPTER, VERSE_ID), new Reference(BOOK_ID, CHAPTER, VERSE_ID));
     assertEquals(1, verses.size());
     Verse verse = (Verse) verses.get(0);
     TranslationReference transRef = verse.getTranslationReference();
     assertNotNull(transRef);
     assertEquals(TRANSLATION_ID, transRef.getTranslation().getId());
     assertEquals(REFERENCE, transRef.getReference());
 }

 @Test
 public void testGetIdFromDb() {
     assertEquals(ID, Verse.GetId(TRANSLATION_ID, BOOK_ID, CHAPTER, VERSE_ID));
 }

 @Test
 public void testGetIdWithTranslationNotFound() {
     assertEquals(VERSE_ID_NOT_FOUND, Verse.GetId(-1, BOOK_ID, CHAPTER, VERSE_ID));
 }

 @Test
 public void testNewWithIdAlreadyInCache() {
     Verse verse1 = Verse.New(ID);
     Verse verse2 = Verse.New(ID);
     assertEquals(verse1, verse2);
 }

 @Test
 public void testNewWithIdNotInCache() {
     Verse verse1 = Verse.New(ID);
     assertNotNull(verse1);
     assertEquals(ID, verse1.getId());
     Verse verse2 = Verse.New(ID);
     assertNotSame(verse1, verse2);
 }

 @Test
 public void testNewWithIdAlreadyInResult() {
     DbResult result = new DbResult() {{
         try {
             addColumnName("Verse_ID");
             addColumnName("Text");
             addColumnName("Translation_ID");
             addColumnName("Book_ID");
             addColumnName("Chapter");
             addColumnName("Verse_No");
             setNext(true);
             setInt("Verse_ID", ID);
             setString("Text", VERSE.getText());
             setInt("Translation_ID", TRANSLATION_ID);
             setInt("Book_ID", BOOK_ID);
             setInt("Chapter", CHAPTER);
             setInt("Verse_No", VERSE_ID);
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }};
     Verse verse1 = new Verse(result);
     Verse verse2 = new Verse(ID);
     assertEquals(verse1, verse2);
 }

 @Test(expected = SQLException.class)
 public void testNewWithIdNotInDb() {
     DbResult result = new DbResult() {{
         try {
             addColumnName("Verse_ID");
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }};
     Verse verse = null;
     try {
         verse = new Verse(-1);
     } catch (SQLException e) {
         // expected
     }
     assertNull(verse);
 }

 @Test
 public void testComplete() throws SQLException {
     Verse verse = new Verse(-1);
     DbResult result = DUMMY_RESULT;
     verse.complete(result);
     assertEquals(ID, verse.getId());
     assertEquals(VERSE.getText(), verse.getText());
     TranslationReference transRef = verse.getTranslationReference();
     assertNotNull(transRef);
     assertEquals(TRANSLATION_ID, transRef.getTranslation().getId());
     assertEquals(REFERENCE, transRef.getReference());
 }

 @Test(expected = SQLException.class)
 public void testCompleteWithNullDbResult() {
     Verse verse = new Verse(-1);
     verse.complete(null);
 }

 @Test(expected = SQLException.class)
 public void testCompleteWithEmptyDbResult() {
     Verse verse = new Verse(-1);
     DbResult result = new DbResult() {{
         try {
             addColumnName("Verse_ID");
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }};
     try {
         verse.complete(result);
     } catch (SQLException e) {
         // expected
     }
 }

 @Test(expected = SQLException.class)
 public void testCompleteWithMissingColumns() {
     Verse verse = new Verse(-1);
     DbResult result = DUMMY_RESULT;
     result.removeColumn("Verse_ID");
     try {
         verse.complete(result);
     } catch (SQLException e) {
         // expected
     }
 }

 @Test(expected = SQLException.class)
 public void testGetIdWithNullTranslationId() {
     assertEquals(VERSE_ID_NOT_FOUND, Verse.GetId(null, BOOK_ID, CHAPTER, VERSE_ID));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdWithNullBookId() {
     assertEquals(VERSE_ID_NOT_FOUND, Verse.GetId(TRANSLATION_ID, null, CHAPTER, VERSE_ID));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdWithNullChapter() {
     assertEquals(VERSE_ID_NOT_FOUND, Verse.GetId(TRANSLATION_ID, BOOK_ID, null, VERSE_ID));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetIdWithNullVerseId() {
     assertEquals(VERSE_ID_NOT_FOUND, Verse.GetId(TRANSLATION_ID, BOOK_ID, CHAPTER, null));
 }

 @Test
 public void testReadId() {
     assertEquals(ID, Verse.ReadId(TRANSLATION_ID, BOOK_ID, CHAPTER, VERSE_ID));
 }

 @Test
 public void testReadIdWithTranslationNotFound() {
     assertEquals(VERSE_ID_NOT_FOUND, Verse.ReadId(-1, BOOK_ID, CHAPTER, VERSE_ID));
 }

 @Test(expected = SQLException.class)
 public void testReadIdWithEmptyResult() {
     assertEquals(VERSE_ID_NOT_FOUND, Verse.ReadId(TRANSLATION_ID, BOOK_ID, CHAPTER, VERSE_ID));
 }

 @Test(expected = SQLException.class)
 public void testReadIdWithTranslationIdMismatch() {
     assertEquals(VERSE_ID_NOT_FOUND, Verse.ReadId(TRANSLATION_ID + 1, BOOK_ID, CHAPTER, VERSE_ID));
 }
}