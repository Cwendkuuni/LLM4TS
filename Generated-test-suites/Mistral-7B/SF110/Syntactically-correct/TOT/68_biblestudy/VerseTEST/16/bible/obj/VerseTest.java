import org.junit.BeforeClass;
import org.junit.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class VerseTest {

 private static Verse verse1;
 private static Verse verse2;
 private static Verse verse3;
 private static Verse verseNull;
 private static Verse verseNegativeId;
 private static Translation translation1;
 private static Translation translation2;
 private static Book book1;
 private static Book book2;
 private static Reference reference1;
 private static Reference reference2;
 private static int id1;
 private static int id2;
 private static int id3;
 private static int invalidId;

 @BeforeClass
 public static void setup() throws SQLException {
 // Initialize test data
 translation1 = new Translation("English", "ENG");
 translation2 = new Translation("Spanish", "ESP");
 book1 = new Book("Book1", 1);
 book2 = new Book("Book2", 2);
 reference1 = new Reference(book1.getId(), 1, 1);
 reference2 = new Reference(book2.getId(), 2, 2);
 id1 = Verse.GetId(translation1, book1, 1, 1);
 id2 = Verse.GetId(translation1, book1, 1, 2);
 id3 = Verse.GetId(translation2, book2, 2, 2);
 // Create test verses
 verse1 = Verse.New(id1);
 verse2 = Verse.New(id2);
 verse3 = Verse.New(id3);
 verseNull = Verse.New((Integer) null);
 verseNegativeId = new Verse(-1);
 // Test initialization of verseCache and translations hashmap
 assertEquals("Unexpected number of verses in verseCache", 3, Verse.verseCache.size());
 assertEquals("Unexpected number of translations in translations hashmap", 2, Verse.translations.size());
 }

 @Test
 public void testgetId() {
 // Test getting id of existing verse
 assertEquals("Unexpected id for verse1", id1, verse1.getId());
 // Test getting id of null verse
 assertEquals("Unexpected id for null verse", 0, verseNull.getId());
 // Test getting id of verse with negative id
 assertThrows("Unexpected exception for verse with negative id", SQLException.class, () -> verseNegativeId.getId());
 }

 @Test
 public void testgetText() {
 // Test getting text of existing verse
 assertEquals("Unexpected text for verse1", "Expected text1", verse1.getText());
 // Test getting text of null verse
 assertNull("Unexpected text for null verse", verseNull.getText());
 }

 @Test
 public void testGetTranslationReference() {
 // Test getting translation reference of existing verse
 TranslationReference expectedTransRef1 = new TranslationReference(translation1, reference1);
 TranslationReference expectedTransRef2 = new TranslationReference(translation2, reference2);
 TranslationReference actualTransRef1 = verse1.getTranslationReference();
 TranslationReference actualTransRef2 = verse2.getTranslationReference();
 assertEquals("Unexpected translation reference for verse1", expectedTransRef1, actualTransRef1);
 assertEquals("Unexpected translation reference for verse2", expectedTransRef2, actualTransRef2);
 // Test getting translation reference of null verse
 assertNull("Unexpected translation reference for null verse", verseNull.getTranslationReference());
 }

 @Test
 public void testGetReference() {
 // Test getting reference of existing verse
 Reference expectedRef1 = reference1;
 Reference actualRef1 = verse1.getReference();
 assertEquals("Unexpected reference for verse1", expectedRef1, actualRef1);
 // Test getting reference of null verse
 assertNull("Unexpected reference for null verse", verseNull.getReference());
 }

 @Test
 public void testToString() {
 // Test toString method of existing verse
 String expectedToString1 = "bible.obj.Verse:[1 Eng [Book1 1 1]]Expected text1";
 String actualToString1 = verse1.toString();
 assertEquals("Unexpected toString for verse1", expectedToString1, actualToString1);
 // Test toString method of null verse
 String actualToStringNull = verseNull.toString();
 assertTrue("Unexpected toString for null verse", actualToStringNull.contains("null"));
 }

 @Test
 public void testNew() {
 // Test New method with valid id
 Verse expectedVerse1 = verse1;
 Verse actualVerse1 = Verse.New(id1);
 assertEquals("Unexpected verse created with New method", expectedVerse1, actualVerse1);
 // Test New method with null id
 assertNull("Unexpected verse created with New method with null id", Verse.New((Integer) null));
 }

 @Test
 public void testGet() {
 // Test getting verses for all translations for given book and chapter
 List<Reference> references = new ArrayList<Reference>() {{
 add(new Reference(book1.getId(), 1, 1));
 add(new Reference(book1.getId(), 1, 2));
 add(new Reference(book2.getId(), 2, 2));
 }};
 List<Verse> expectedVerses = new ArrayList<Verse>() {{
 add(verse1);
 add(verse2);
 add(verse3);
 }};
 List<Verse> actualVerses = Verse.Get(new Translation[] {translation1, translation2}, book1, new Reference(book1.getId(), 1, 1), new Reference(book1.getId(), 1, 2));
 assertEquals("Unexpected number of verses returned", 3, actualVerses.size());
 IntSummaryStatistics stats = IntStream.range(0, actualVerses.size())
 .mapToInt(i -> actualVerses.get(i).getId())
 .summaryStatistics();
 assertEquals("Unexpected ids of verses", stats.getSum(), 3);
 assertEquals("Unexpected verses", expectedVerses, actualVerses);
 }

 @Test
 public void testGetWithInvalidTranslationId() {
 List<Verse> actualVerses = Verse.Get(new Translation[] {translation1, translation2}, book1, new Reference(book1.getId(), 1, 1), new Reference(book1.getId(), 1, 3));
 assertEquals("Unexpected number of verses returned", 0, actualVerses.size());
 }

 @Test
 public void testGetWithInvalidBookId() {
 List<Verse> actualVerses = Verse.Get(new Translation[] {translation1, translation2}, new Book("InvalidBook", 0), new Reference(book1.getId(), 1, 1), new Reference(book1.getId(), 1, 2));
 assertEquals("Unexpected number of verses returned", 0, actualVerses.size());
 }

 @Test
 public void testGetWithInvalidChapterId() {
 List<Verse> actualVerses = Verse.Get(new Translation[] {translation1, translation2}, book1, new Reference(book1.getId(), 0, 1), new Reference(book1.getId(), 1, 2));
 assertEquals("Unexpected number of verses returned", 0, actualVerses.size());
 }

 @Test
 public void testGetWithInvalidVerseId() {
 List<Verse> actualVerses = Verse.Get(new Translation[] {translation1, translation2}, book1, new Reference(book1.getId(), 1, 1), new Reference(book1.getId(), 1, 0));
 assertEquals("Unexpected number of verses returned", 0, actualVerses.size());
 }

 @Test
 public void testGetIdStatic() {
 // Test getting id of verse with valid data
 int expectedId = id1;
 int actualId = Verse.GetId(translation1, book1, 1, 1);
 assertEquals("Unexpected id", expectedId, actualId);
 // Test getting id of verse with invalid translation id
 int invalidTranslationId = 999;
 int actualIdInvalidTranslationId = Verse.GetId(new Translation("InvalidTranslation", "INVALID"), book1, 1, 1);
 assertEquals("Unexpected id", -1, actualIdInvalidTranslationId);
 // Test getting id of verse with invalid book id
 int invalidBookId = 999;
 int actualIdInvalidBookId = Verse.GetId(translation1, new Book("InvalidBook", invalidBookId), 1, 1);
 assertEquals("Unexpected id", -1, actualIdInvalidBookId);
 // Test getting id of verse with invalid chapter id
 int invalidChapterId = 999;
 int actualIdInvalidChapterId = Verse.GetId(translation1, book1, invalidChapterId, 1);
 assertEquals("Unexpected id", -1, actualIdInvalidChapterId);
 // Test getting id of verse with invalid verse id
 int invalidVerseId = 999;
 int actualIdInvalidVerseId = Verse.GetId(translation1, book1, 1, invalidVerseId);
 assertEquals("Unexpected id", -1, actualIdInvalidVerseId);
 }
}