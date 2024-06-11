import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {
     @Test
     public void testIsEmpty() {
         CharacterReader reader = new CharacterReader("");
         Assert.assertTrue(reader.isEmpty());

         reader = new CharacterReader("abc");
         Assert.assertFalse(reader.isEmpty());
     }
}