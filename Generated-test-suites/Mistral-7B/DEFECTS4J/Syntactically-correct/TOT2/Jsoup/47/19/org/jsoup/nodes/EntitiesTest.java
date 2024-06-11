import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertFalse(Entities.isNamedEntity("gt")); // edge case: not a named entity
        Assert.assertFalse(Entities.isNamedEntity("amppm")); // error scenario: invalid entity name
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("amp")); // edge case: not a base named entity
        Assert.assertFalse(Entities.isBaseNamedEntity("quot")); // error scenario: valid named entity but not in base set
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
        Assert.assertEquals('&', Entities.getCharacterByName("amp"));
        Assert.assertEquals('>', Entities.getCharacterByName("gt"));
        Assert.assertEquals('"', Entities.getCharacterByName("quot")); // edge case: null character
        Assert.assertNull(Entities.getCharacterByName("unknown")); // error scenario: invalid named entity
    }

    
}