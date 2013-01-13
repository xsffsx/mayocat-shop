package org.mayocat.shop.service.internal;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;

import org.mayocat.shop.configuration.Configuration;
import org.mayocat.shop.configuration.shop.ShopConfiguration;
import org.mayocat.shop.context.Execution;
import org.mayocat.shop.service.ConfigurationService;
import org.slf4j.Logger;
import org.xwiki.component.annotation.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.Multimap;

/**
 * @version $Id$
 */
@Component
public class DefaultConfigurationService implements ConfigurationService
{
    @Inject
    private Map<String, Configuration> configurations;

    @Inject
    private Execution execution;

    @Inject
    private Logger logger;

    @Override
    public Map<String, Object> getConfiguration()
    {
        Multimap<String, Object> tenantConfiguration = execution.getContext().getTenant().getConfiguration();
        ShopConfiguration gestaltConfiguration = new ShopConfiguration();

        return this.multimapFromConfigurations();
    }

    @Override
    public Map<String, Object> getConfiguration(String name)
    {
        return null;
    }

    private Map<String, Object> multimapFromConfigurations()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new GuavaModule());
        try {
            String json = mapper.writeValueAsString(this.configurations);
            Map<String, Object> result = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
            return result;
        } catch (JsonProcessingException e) {
            this.logger.error("Failed to convert configurations to map", e);
        } catch (IOException e) {
            this.logger.error("Failed to convert configurations to map", e);
        }
        return Collections.<String, Object>emptyMap();
    }
}
