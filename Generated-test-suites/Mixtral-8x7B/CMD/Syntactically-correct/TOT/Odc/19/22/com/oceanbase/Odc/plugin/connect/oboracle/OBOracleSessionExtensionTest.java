import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.junit.AbstractExtensionTest;
import org.pf4j.junit.InstanceExtension;
import org.pf4j.junit.SpringExtension;
import org.pf4j.spring.SpringPluginManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {TestConfig.class})
public class OBOracleSessionExtensionTest extends AbstractExtensionTest {

    @InstanceExtension
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        PluginManager pluginManager = getPluginManager();
        if (pluginManager instanceof SpringPluginManager) {
            ((SpringPluginManager) pluginManager).initialize();
        }
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1234";
        obOracleSessionExtension.killQuery(null, connectionId);
    }
}