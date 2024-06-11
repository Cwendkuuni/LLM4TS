import org.junit.Assert;
import org.junit.Test;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {
    @Test
    public void testSimpleSectorConstructor() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
        Assert.assertEquals(5, sector.getHeight());
        Assert.assertTrue(sector.isDefenderSpawn());
        Assert.assertTrue(sector.isAttackerSpawn());
        Assert.assertEquals(2, sector.getAttackBonus(CharacterClass.ARCHER));
        Assert.assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
        Assert.assertTrue(sector.isFlag());
        Assert.assertEquals(4, sector.getClassBonus());
        Assert.assertEquals(CharacterClass.ARCHER, sector.getClassType());
    }
}