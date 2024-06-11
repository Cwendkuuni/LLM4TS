import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = XmlTreeBuilder.defaultSettings();
        assertThat(settings).isInstanceOf(ParseSettings.class);
        assertThat(settings.getPreserveCase()).isTrue();
    }
}