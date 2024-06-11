import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.session.SessionConfig;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

    private HttpFirewall httpFirewall;

    @Before
    public void setup() {
        passwordEncoder = new BCryptPasswordEncoder();
        objectMapper = JacksonFactory.unsafeJsonMapper();
        httpFirewall = mock(DefaultHttpFirewall.class);

        ReflectionTestUtils.setField(beanConfiguration, "passwordEncoder", passwordEncoder);
    }

    @Test
    public void testPasswordEncoder() {
        String password = "testPassword";
        String encodedPassword = passwordEncoder.encode(password);

        assertNotNull(encodedPassword);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

    @Test
    public void testObjectMapper() throws Exception {
        OBDatabaseUser user = new OBDatabaseUser();
        user.setUserName("testUser");

        ObjectMapper beanMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        String json = beanMapper.writeValueAsString(user);

        assertEquals(json, "{\"userName\":\"testUser\"}");
    }

    @Test
    public void testDefaultHttpFirewall() {
        when(httpFirewall.getAllowUrlEncodedSlash()).thenReturn(true);

        ReflectionTestUtils.setField(beanConfiguration, "httpFirewall", httpFirewall);

        HttpFirewall configuredHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertTrue(configuredHttpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = beanConfiguration.cookieSerializer();
        assertEquals("JSESSIONID", serializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        List<OBInstance> emptyInstances = new ArrayList<>();
        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("");
        ObjectMapper mapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()));
        ReflectionTestUtils.setField(nullCloudMetadataClient, "objectMapper", mapper);

        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants(anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenantEndpoint(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser(anyString()));
    }
}