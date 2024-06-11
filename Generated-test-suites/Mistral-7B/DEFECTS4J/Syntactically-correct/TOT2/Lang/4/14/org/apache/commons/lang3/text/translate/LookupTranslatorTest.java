import org.junit.Test;
import org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[][] lookup = new CharSequence[][]{{"abc", "xyz"}, {"def", "pqr"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        Assert.assertNotNull(translator);
        Assert.assertEquals(2, translator.lookupMap.size());
    }
}