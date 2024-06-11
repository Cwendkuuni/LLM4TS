import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
import com.oceanbase.odc.service.connection.ModelTestUtil;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;

public class BeanConfigurationTest {

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("test");
        assertNotNull(encodedPassword);
    }

    @Test
    public void testObjectMapper() {
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = new JacksonFactory().unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()
                        .addSerializer(Internationalizable.class, new I18nOutputSerializer())));
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
        defaultHttpFirewall.setAllowUrlEncodedSlash(true);
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = new BeanConfiguration.NullCloudMetadataClient();
        assertEquals(false, cloudMetadataClient.supportsCloudMetadata());
        assertEquals(false, cloudMetadataClient.supportsTenantInstance());
        assertEquals(false, cloudMetadataClient.needsOBTenantName());
        assertEquals(false, cloudMetadataClient.needsSysTenantUser());
        assertEquals(true, cloudMetadataClient.includeClusterNameForJdbcUsername());
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
        CookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookieName("JSESSIONID");
        assertNotNull(cookieSerializer);
    }

    private static class TestNullCloudMetadataClient extends BeanConfiguration.NullCloudMetadataClient {

        @Override
        public List<OBInstance> listInstances() {
            List<OBInstance> obInstances = new ArrayList<>();
            OBInstance obInstance = ModelTestUtil.createOBInstance("1", "192.168.0.1");
            obInstances.add(obInstance);
            return obInstances;
        }

        @Override
        public List<OBTenant> listTenants(String instanceId) {
            List<OBTenant> obTenants = new ArrayList<>();
            OBTenant obTenant = ModelTestUtil.createOBTenant("1", "10.0.0.1", "test", "test");
            obTenants.add(obTenant);
            return obTenants;
        }

        @Override
        public OBTenant getTenant(String instanceId, String tenantId) {
            OBTenantEndpoint obTenantEndpoint = new OBTenantEndpoint();
            obTenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
            OBTenant obTenant = ModelTestUtil.createOBTenant("1", "10.0.0.1", "test", "test");
            obTenant.setTenantEndpoint(obTenantEndpoint);
            return obTenant;
        }

        @Override
        public List<OBDatabaseUser> listDatabaseUsers(String instanceId, String tenantId) {
            List<OBDatabaseUser> obDatabaseUsers = new ArrayList<>();
            OBDatabaseUser obDatabaseUser = ModelTestUtil.createOBDatabaseUser("test", "test", "test");
            obDatabaseUsers.add(obDatabaseUser);
            return obDatabaseUsers;
        }

        @Override
        public OBTenantEndpoint getTenantEndpoint(String instanceId, String tenantId) {
            OBTenantEndpoint obTenantEndpoint = new OBTenantEndpoint();
            obTenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
            return obTenantEndpoint;
        }

        @Override
        public OBDatabaseUser getSysTenantUser(String instanceId) {
            OBDatabaseUser obDatabaseUser = ModelTestUtil.createOBDatabaseUser("test", "test", "test");
            return obDatabaseUser;
        }
    }

    @Test
    public void testObjectMapperWithCustomObject() throws Exception {
        SensitivePropertyHandler sensitivePropertyHandler = mock(SensitivePropertyHandler.class);
        ObjectMapper objectMapper = new JacksonFactory().unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()
                        .addSerializer(Internationalizable.class, new I18nOutputSerializer())));

        BeanConfiguration beanConfiguration = new BeanConfiguration();
        beanConfiguration.objectMapper(objectMapper, sensitivePropertyHandler);

        OBInstance obInstance = ModelTestUtil.createOBInstance("1", "192.168.0.1");
        String json = objectMapper.writeValueAsString(obInstance);

        OBInstance deserializedObInstance = objectMapper.readValue(json, OBInstance.class);
        assertEquals(obInstance.getId(), deserializedObInstance.getId());
        assertEquals(obInstance.getIp(), deserializedObInstance.getIp());
    }

    @Test
    public void testCloudMetadataClientWithMock() {
        CloudMetadataClient cloudMetadataClient = mock(CloudMetadataClient.class);

        when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(true);
        when(cloudMetadataClient.supportsTenantInstance()).thenReturn(true);
        when(cloudMetadataClient.needsOBTenantName()).thenReturn(true);
        when(cloudMetadataClient.needsSysTenantUser()).thenReturn(true);
        when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(false);
        when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.PROXY);

        when(cloudMetadataClient.listInstances()).thenAnswer(new Answer<List<OBInstance>>() {
            @Override
            public List<OBInstance> answer(InvocationOnMock invocation) throws Throwable {
                List<OBInstance> obInstances = new ArrayList<>();
                OBInstance obInstance = ModelTestUtil.createOBInstance("1", "192.168.0.1");
                obInstances.add(obInstance);
                return obInstances;
            }
        });

        when(cloudMetadataClient.listTenants(eq("instanceId")))
                .thenReturn(new ArrayList<OBTenant>() {{
                    add(ModelTestUtil.createOBTenant("1", "10.0.0.1", "test", "test"));
                }});

        when(cloudMetadataClient.getTenant(eq("instanceId"), eq("tenantId")))
                .thenReturn(new OBTenant() {{
                    setTenantEndpoint(new OBTenantEndpoint() {{
                        setAccessMode(OceanBaseAccessMode.PROXY);
                    }});
                }});

        when(cloudMetadataClient.listDatabaseUsers(eq("instanceId"), eq("tenantId")))
                .thenReturn(new ArrayList<OBDatabaseUser>() {{
                    add(ModelTestUtil.createOBDatabaseUser("test", "test", "test"));
                }});

        when(cloudMetadataClient.getTenantEndpoint(eq("instanceId"), eq("tenantId")))
                .thenReturn(new OBTenantEndpoint() {{
                    setAccessMode(OceanBaseAccessMode.PROXY);
                }});

        when(cloudMetadataClient.getSysTenantUser(eq("instanceId")))
                .thenReturn(ModelTestUtil.createOBDatabaseUser("test", "test", "test"));

        BeanConfiguration beanConfiguration = new BeanConfiguration();
        beanConfiguration.cloudMetadataClient(cloudMetadataClient);
    }
}