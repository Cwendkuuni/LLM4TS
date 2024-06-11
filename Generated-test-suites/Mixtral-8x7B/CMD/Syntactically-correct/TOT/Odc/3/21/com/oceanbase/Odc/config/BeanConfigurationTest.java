import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.CloudMetadataClient.OceanBaseAccessMode;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String encodedPassword = passwordEncoder.encode("testPassword");
        assertNotNull(encodedPassword);

        assertTrue(passwordEncoder.matches("testPassword", encodedPassword));
    }

    @Test
    public void testObjectMapper() throws Exception {
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer().addSerializer(
                Internationalizable.class, new I18nOutputSerializer());

        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);

        assertEquals(JacksonFactory.unsafeJsonMapper().registerModule(
                JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(customOutputSerializer), objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertEquals(DefaultHttpFirewall.class, httpFirewall.getClass());
        assertEquals(true, httpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());

        assertFalse(cloudMetadataClient.supportsCloudMetadata());
        assertFalse(cloudMetadataClient.supportsTenantInstance());
        assertFalse(cloudMetadataClient.needsOBTenantName());
        assertFalse(cloudMetadataClient.needsSysTenantUser());
        assertTrue(cloudMetadataClient.includeClusterNameForJdbcUsername());

        assertEquals(OceanBaseAccessMode.DIRECT, cloudMetadataClient.oceanbaseAccessMode());

        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants("instanceId"));
        assertThrows(UnsupportedException.class,
                () -> cloudMetadataClient.getTenant("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser("instanceId"));
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer serializer = beanConfiguration
                .cookieSerializer();
        assertEquals(DefaultCookieSerializer.class, serializer.getClass());
        assertEquals("JSESSIONID", serializer.getCookieName());

        CookieSerializer serializedCookie = serializer.serialize("sessionId");
        assertNotSame(serializedCookie, serializer.serialize("sessionId"));
    }
}