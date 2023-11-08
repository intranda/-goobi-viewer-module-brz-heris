/**
 * This file is part of the Goobi viewer - a content presentation and management application for digitized objects.
 *
 * Visit these websites for more information.
 *          - http://www.intranda.com
 *          - http://digiverso.com
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.goobi.viewer.modules.heris;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.event.Event;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.goobi.viewer.controller.AbstractConfiguration;
import io.goobi.viewer.controller.DataManager;

public final class ModuleConfiguration extends AbstractConfiguration {

    private static final Logger logger = LogManager.getLogger(ModuleConfiguration.class);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ModuleConfiguration(String configFilePath) {
        // Load local config file
        File fileLocal = new File(configFilePath);
        builder =
                new ReloadingFileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
                        .configure(new Parameters().properties()
                                .setFileName(fileLocal.getAbsolutePath())
                                .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
                                .setThrowExceptionOnMissing(false));

        if (fileLocal.exists()) {
            builder.addEventListener(ConfigurationBuilderEvent.CONFIGURATION_REQUEST,
                    new EventListener() {

                        @Override
                        public void onEvent(Event event) {
                            if (builder.getReloadingController().checkForReloading(null)) {
                                //
                            }
                        }
                    });
            logger.info("Local configuration file '{}' loaded.", fileLocal.getAbsolutePath());
        } else {
            logger.error("Module configuration file not found: {}", fileLocal.getAbsolutePath());
        }
    }

    /*********************************** direct config results ***************************************/

    public static String getLocalRessourceBundleFile() {
        return DataManager.getInstance().getConfiguration().getConfigLocalPath() + "messages_de.properties";
    }

    /**
     * 
     * @return
     * @should return correct value
     */
    public boolean isModuleEnabled() {
        return getLocalBoolean(("enabled"), false);
    }

    /**
     * 
     * @param type
     * @return
     */
    public List<String> getGuiContributions(String type) {
        return getLocalList("guiContributions." + type + ".url", new ArrayList<>());
    }

    /**
     * 
     * @param host
     * @return
     * @should return correct value
     */
    public String getIndexFieldForHost(String host) {
        List<HierarchicalConfiguration<ImmutableNode>> fieldList = getLocalConfigurationsAt("identifierFields.field");
        if (fieldList != null) {
            for (HierarchicalConfiguration<ImmutableNode> subElement : fieldList) {
                if (subElement.getString("[@host]").equals(host)) {
                    return subElement.getString(".", "");
                }
            }
        }

        return "";
    }
}
