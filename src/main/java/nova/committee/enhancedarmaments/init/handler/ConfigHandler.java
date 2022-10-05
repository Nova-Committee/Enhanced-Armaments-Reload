package nova.committee.enhancedarmaments.init.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import nova.committee.enhancedarmaments.Static;
import nova.committee.enhancedarmaments.common.config.Config;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/27 22:15
 * Version: 1.0
 */
public class ConfigHandler {
    private Config config;
    public Config getConfig() {
        return config;
    }
    private final File file;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public Gson GSON;

    public ConfigHandler(){
        this.file = new File(FabricLoader.getInstance().getConfigDir().toFile(), Static.MOD_ID + ".json");
        this.GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        load(false);
    }


    public void load(boolean async) {
        Runnable task = () -> {
            try {
                //read if exists
                if (file.exists()) {
                    String fileContents = FileUtils.readFileToString(file, Charset.defaultCharset());
                    config = GSON.fromJson(fileContents, Config.class);

                } else { //write new if no config file exists
                    writeNewConfig();
                }

            } catch (Exception e) {
                e.printStackTrace();
                writeNewConfig();
            }
        };

        if (async) executor.execute(task);
        else task.run();
    }
    public void writeNewConfig() {
        config = new Config();
        save(false);
    }
    public void save(boolean async) {
        Runnable task = () -> {
            try {
                if (config != null) {
                    String serialized = GSON.toJson(config);
                    FileUtils.writeStringToFile(file, serialized, Charset.defaultCharset());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if (async) executor.execute(task);
        else task.run();
    }

    private static List<Item> parseItemList(List<String> lst) {
        List<Item> exp = new ArrayList<>(lst.size());
        for (String s : lst) {
            Item i = Registry.ITEM.get(new ResourceLocation(s));
            if (i == Items.AIR) {
                Static.LOGGER.error("Invalid config entry {} will be ignored from blacklist.", s);
                continue;
            }
            exp.add(i);
        }
        return exp;
    }
}
