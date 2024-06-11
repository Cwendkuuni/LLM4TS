import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.config.BeanConfiguration;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.session.SessionConfiguration;

@SpringBootTest(classes = BeanConfiguration.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class BeanConfigurationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HttpFirewall httpFirewall;

    @Autowired
    private CloudMetadataClient cloudMetadataClient;

    @Autowired
    private CookieSerializer cookieSerializer;

    @Autowired
    private SessionConfiguration sessionConfiguration;

    @Autowired
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Test
    public void testPasswordEncoder() {
        String password = "test123";
        String encodedPassword = passwordEncoder.encode(password);
        assertNotNull(encodedPassword);
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        assertEquals(true, matches);
    }

    @Test
    public void testObjectMapper() {
        String input = "{\"key\":\"value\"}";
        ObjectMapper objectMapper = new BeanConfiguration().objectMapper(sensitivePropertyHandler);
        try {
            Object obj = objectMapper.readValue(input, Object.class);
            assertNotNull(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHttpFirewall() {
        String url = "http://test.com/url?param=%25";
        assertEquals(httpFirewall.getAllowedHttpMethods().size(), 9);
        assertEquals(httpFirewall.getAllowedUrlEncodedSlash(), true);
        assertEquals(httpFirewall.getAllowedRelativePaths(".").size(), 1);
        httpFirewall.setAllowUrlEncodedSlash(false);
        assertEquals(httpFirewall.getAllowedUrlEncodedSlash(), false);
    }

    @Test
    public void testCloudMetadataClient() {
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants("123"));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant("123", "456"));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers("123", "456"));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenantEndpoint("123", "456"));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser("123"));
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = new BeanConfiguration().cookieSerializer();
        String cookieName = serializer.getCookieName();
        assertEquals(cookieName, "JSESSIONID");
    }

    @Test
    public void testSessionConfiguration() {
        assertEquals(sessionConfiguration.getTimeout(), 1800);
    }

}