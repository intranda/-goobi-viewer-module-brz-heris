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
package io.goobi.viewer;

import java.io.File;

import org.junit.BeforeClass;

import io.goobi.viewer.controller.DataManager;
import io.goobi.viewer.modules.HerisModule;
import io.goobi.viewer.modules.heris.ModuleConfiguration;

public abstract class AbstractModuleTest extends AbstractTest {

    public static final String TEST_CONFIG_PATH = new File("src/test/resources/config_viewer-module-heris.test.xml").getAbsolutePath();

    @BeforeClass
    public static void setUpClass() throws Exception {
        AbstractTest.setUpClass();

        // Register module in the core
        HerisModule module = new HerisModule();

        DataManager.getInstance().registerModule(module);

        // Inject custom module config
        module.injectConfiguration(new ModuleConfiguration(TEST_CONFIG_PATH));
    }
}
