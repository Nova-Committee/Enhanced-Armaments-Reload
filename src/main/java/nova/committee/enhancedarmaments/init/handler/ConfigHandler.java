package nova.committee.enhancedarmaments.init.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import nova.committee.enhancedarmaments.Static;
import nova.committee.enhancedarmaments.common.config.Config;
import nova.committee.enhancedarmaments.util.JSONFormat;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/27 22:15
 * Version: 1.0
 */
public class ConfigHandler {
    private static File file;

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static void prepareConfigFile() {
        if (file != null) {
            return;
        }
        file = FabricLoader.getInstance().getConfigDir().resolve(Static.MOD_ID + ".json").toFile();
    }


    public static Config load() {
        prepareConfigFile();
        Config config = new Config();


        try {
            if (!file.exists()) {
                save(config);
            }
            if (file.exists()){

                config = GSON.fromJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8),
                        Config.class);

            }
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load Login Toast configuration file; reverting to defaults");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }

    public static void save(Config config) {
        prepareConfigFile();

        try  {
            FileUtils.write(file, JSONFormat.formatJson(GSON.toJson(config, Config.class)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Couldn't save Login Toast configuration file");
            e.printStackTrace();
        }
    }

}
