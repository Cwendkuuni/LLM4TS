import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
import com.oceanbase.odc.service.connection.ModelTests.OBInstanceTest;
import com.oceanbase.odc.service.connection.ModelTests.OBTenantTest;
import com.oceanbase.odc.service.connection.ModelTests.OBTenantEndpointTest;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() throws Exception {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        assertEquals(customOutputSerializer, objectMapper.getSerializerProvider().getSerializerModifiers().get(1));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
        assertTrue(defaultHttpFirewall.getAllowedUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);

        when(sensitivePropertyHandler.decrypt("test")).thenReturn("decrypted-test");

        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(null));

        assertEquals(objectMapper, nullCloudMetadataClient.objectMapper);

        // Test edge cases for NullCloudMetadataClient
        NullCloudMetadataClient.testEdgeCases();
    }

}

class NullCloudMetadataClientTest {

    @Test
    public void testEdgeCases() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();

        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());

        OceanBaseAccessMode oceanbaseAccessMode = nullCloudMetadataClient.oceanbaseAccessMode();
        assertNotNull(oceanbaseAccessMode);
        assertEquals(OceanBaseAccessMode.DIRECT, oceanbaseAccessMode);

        List<OBInstance> listInstances = nullCloudMetadataClient.listInstances();
        assertNotNull(listInstances);
        assertEquals(0, listInstances.size());

        List<OBTenant> listTenants = nullCloudMetadataClient.listTenants("instanceId");
        assertNotNull(listTenants);
        assertEquals(0, listTenants.size());

        OBTenant getTenant = nullCloudMetadataClient.getTenant("instanceId", "tenantId");
        assertNull(getTenant);

        List<OBDatabaseUser> listDatabaseUsers = nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId");
        assertNotNull(listDatabaseUsers);
        assertEquals(0, listDatabaseUsers.size());

        OBTenantEndpoint getTenantEndpoint = nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId");
        assertNotNull(getTenantEndpoint);
        assertEquals(OceanBaseAccessMode.DIRECT, getTenantEndpoint.getAccessMode());

        OBDatabaseUser getSysTenantUser = nullCloudMetadataClient.getSysTenantUser("instanceId");
        assertNull(getSysTenantUser);
    }

}

class ModelTests {

    public static class OBInstanceTest {
        public OBInstance instance() {
            return new OBInstance();
        }
    }

    public static class OBTenantTest {
        public OBTenant tenant() {
            return new OBTenant();
        }
    }

    public static class OBTenantEndpointTest {
        public OBTenantEndpoint endpoint() {
            return new OBTenantEndpoint();
        }
    }

}