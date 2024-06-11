import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Token resetDoctype = doctype.reset();
        Assert.assertEquals(TokenType.Doctype, doctype.type);
        Assert.assertEquals(TokenType.Doctype, resetDoctype.type);

        doctype.name("testDoctype").pubSysKey("testPubSysKey").publicIdentifier("testPublicIdentifier")
                .systemIdentifier("testSystemIdentifier").forceQuirks(true);

        Assert.assertEquals("testDoctype", doctype.getName());
        Assert.assertEquals("testPubSysKey", doctype.getPubSysKey());
        Assert.assertEquals("testPublicIdentifier", doctype.getPublicIdentifier());
        Assert.assertEquals("testSystemIdentifier", doctype.getSystemIdentifier());
        Assert.assertEquals(true, doctype.isForceQuirks());

        Token resetDoctypeAfterSettingValues = doctype.reset();
        Assert.assertEquals("testDoctype", resetDoctypeAfterSettingValues.getName());
        Assert.assertNull(resetDoctypeAfterSettingValues.getPubSysKey());
        Assert.assertNull(resetDoctypeAfterSettingValues.getPublicIdentifier());
        Assert.assertNull(resetDoctypeAfterSettingValues.getSystemIdentifier());
        Assert.assertEquals(false, resetDoctypeAfterSettingValues.isForceQuirks());
    }
}