import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
    }

    @Test
    public void testObjectMapper() throws Exception {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());

        assertEquals(JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(customOutputSerializer), objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertEquals(DefaultHttpFirewall.class, defaultHttpFirewall.getClass());
        assertEquals(true, defaultHttpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());

        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("decrypted");
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertEquals(JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer())),
                objectMapper);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClientUnsupportedException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.listInstances());

        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.listTenants("instanceId"));

        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.getTenant("instanceId", "tenantId"));

        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));

        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));

        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.getSysTenantUser("instanceId"));

        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.supportsCloudMetadata());

        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.supportsTenantInstance());

        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.needsOBTenantName());

        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.needsSysTenantUser());

        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.includeClusterNameForJdbcUsername());

        assertThrows(UnsupportedException.class,
                () -> nullCloudMetadataClient.oceanbaseAccessMode());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);
        assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
        assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
        assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
        assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
        assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
    }

    @Test
    public void testNullCloudMetadataClientListInstances() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        List<OBInstance> instances = new ArrayList<>();
        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("decrypted");
        assertEquals(instances, nullCloudMetadataClient.listInstances());
    }

    @Test
    public void testNullCloudMetadataClientListTenants() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        List<OBTenant> tenants = new ArrayList<>();
        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("decrypted");
        assertEquals(tenants, nullCloudMetadataClient.listTenants("instanceId"));
    }

    @Test
    public void testNullCloudMetadataClientGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBTenant tenant = new OBTenant();
        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("decrypted");
        assertEquals(tenant, nullCloudMetadataClient.getTenant("instanceId", "tenantId"));
    }

    @Test
    public void testNullCloudMetadataClientListDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        List<OBDatabaseUser> users = new ArrayList<>();
        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("decrypted");
        assertEquals(users, nullCloudMetadataClient.listDatabaseUsers("instanceId", "tenantId"));
    }

    @Test
    public void testNullCloudMetadataClientGetTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBTenantEndpoint endpoint = new OBTenantEndpoint();
        endpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
        assertEquals(endpoint, nullCloudMetadataClient.getTenantEndpoint("instanceId", "tenantId"));
    }

    @Test
    public void testNullCloudMetadataClientGetSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.cloudMetadataClient();
        OBDatabaseUser user = new OBDatabaseUser();
        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("decrypted");
        assertEquals(user, nullCloudMetadataClient.getSysTenantUser("instanceId"));
    }
}