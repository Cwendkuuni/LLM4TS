import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import com.oceanbase.odc.service.session.SessionManager;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @Mock
    private SessionManager sessionManager;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void testObjectMapper() throws Exception {
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());

        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);

        assertEquals(JacksonFactory.unsafeJsonMapper(), objectMapper.getFactory());
        assertEquals(1, objectMapper.registeredModuleIds().size());
        assertEquals(JacksonModules.SENSITIVE_INPUT_HANDLING_MODULE,
                objectMapper.registeredModuleIds().stream().findFirst().get());
        assertEquals(1, objectMapper.getSerializerProviders().size());

        ObjectMapper customObjectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));

        assertEquals(customObjectMapper.getSubtypeFor(), objectMapper.getSubtypeFor());
        assertEquals(customObjectMapper.getSubtypeResolver(), objectMapper.getSubtypeResolver());
        assertEquals(customObjectMapper.getDeserializationConfig(), objectMapper.getDeserializationConfig());
        assertEquals(customObjectMapper.getSerializationConfig(), objectMapper.getSerializationConfig());
        assertEquals(customObjectMapper.getSerializerProvider(), objectMapper.getSerializerProvider());
        assertEquals(customObjectMapper.getTypeFactory(), objectMapper.getTypeFactory());
        assertEquals(customObjectMapper.getFeaturePresent(mapper -> mapper.isEnabled(SerializationFeature.INDENT_OUTPUT)),
                objectMapper.getFeaturePresent(SerializationFeature.INDENT_OUTPUT));
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertTrue(defaultHttpFirewall instanceof DefaultHttpFirewall);
        assertTrue(defaultHttpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertTrue(cloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertTrue(cookieSerializer instanceof DefaultCookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertTrue(nullCloudMetadataClient instanceof NullCloudMetadataClient);
    }

    @Test
    public void testNullCloudMetadataClient_supportsCloudMetadata() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
    }

    @Test
    public void testNullCloudMetadataClient_supportsTenantInstance() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
    }

    @Test
    public void testNullCloudMetadataClient_needsOBTenantName() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
    }

    @Test
    public void testNullCloudMetadataClient_needsSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
    }

    @Test
    public void testNullCloudMetadataClient_includeClusterNameForJdbcUsername() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
    }

    @Test
    public void testNullCloudMetadataClient_oceanbaseAccessMode() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());
    }

    @Test
    public void testNullCloudMetadataClient_listInstances() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNull(nullCloudMetadataClient.listInstances());
    }

    @Test
    public void testNullCloudMetadataClient_listTenants() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNull(nullCloudMetadataClient.listTenants(null));
    }

    @Test
    public void testNullCloudMetadataClient_getTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNull(nullCloudMetadataClient.getTenant(null, null));
    }

    @Test
    public void testNullCloudMetadataClient_listDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNull(nullCloudMetadataClient.listDatabaseUsers(null, null));
    }

    @Test
    public void testNullCloudMetadataClient_getTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint(null, null);
        assertEquals(OceanBaseAccessMode.DIRECT, endpoint.getAccessMode());
    }

    @Test
    public void testNullCloudMetadataClient_getSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNull(nullCloudMetadataClient.getSysTenantUser(null));
    }

    @Test(expected = UnsupportedException.class)
    public void testNullCloudMetadataClient_getTenant_UnsupportedException() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        nullCloudMetadataClient.getTenant("instanceId", "tenantId");
    }

    @Test
    public void testNullCloudMetadataClient_listInstances_Empty() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        when(sessionManager.listInstances()).thenReturn(new ArrayList<>());

        List<OBInstance> instances = nullCloudMetadataClient.listInstances();
        assertTrue(instances.isEmpty());
    }

    @Test
    public void testNullCloudMetadataClient_listTenants_Empty() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        when(sessionManager.listTenants("instanceId")).thenReturn(new ArrayList<>());

        List<OBTenant> tenants = nullCloudMetadataClient.listTenants("instanceId");
        assertTrue(tenants.isEmpty());
    }
}