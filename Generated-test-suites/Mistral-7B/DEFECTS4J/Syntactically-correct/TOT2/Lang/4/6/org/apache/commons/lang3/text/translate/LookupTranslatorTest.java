import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[][] lookup = {{"hello", "hi"}, {"world", "earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.shortest);
        assertEquals(6, translator.longest);
    }
}