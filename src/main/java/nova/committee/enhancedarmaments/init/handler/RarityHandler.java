package nova.committee.enhancedarmaments.init.handler;

import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.fml.loading.FMLPaths;
import nova.committee.enhancedarmaments.Static;
import nova.committee.enhancedarmaments.core.Rarity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/9/3 15:51
 * Version: 1.0
 */
public class RarityHandler {
    private static final RarityHandler INSTANCE = new RarityHandler();

    public static RarityHandler getInstance() {
        return INSTANCE;
    }

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private static final File dir = FMLPaths.CONFIGDIR.get().resolve("enhancedarmaments/rarity/").toFile();

    public void loadRarities() {
        var stopwatch = Stopwatch.createStarted();

        this.writeDefaultRarityFiles();

        clear();

        if (!dir.mkdirs() && dir.isDirectory()) {
            this.loadFiles();
        }

        stopwatch.stop();

        Static.LOGGER.info("Loaded {} custom rarity(s) in {} ms", Rarity.RARITIES.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public void writeDefaultRarityFiles() {
        if (!dir.exists() && dir.mkdirs()) {
            var BASIC = new JsonObject();
            BASIC.addProperty("name", "basic");
            BASIC.addProperty("color", "white");
            BASIC.addProperty("hex", 0xFFFFFF);
            BASIC.addProperty("weight", 0.5D);
            BASIC.addProperty("effect", 0D);
            BASIC.addProperty("enabled", true);
            var UNCOMMON = new JsonObject();
            UNCOMMON.addProperty("name", "uncommon");
            UNCOMMON.addProperty("color", "dark_green");
            UNCOMMON.addProperty("hex", 0x00AA00);
            UNCOMMON.addProperty("weight", 0.18D);
            UNCOMMON.addProperty("effect", 0.155D);
            UNCOMMON.addProperty("enabled", true);
            var RARE = new JsonObject();
            RARE.addProperty("name", "rare");
            RARE.addProperty("color", "aqua");
            RARE.addProperty("hex", 0x55FFFF);
            RARE.addProperty("weight", 0.1D);
            RARE.addProperty("effect", 0.305D);
            RARE.addProperty("enabled", true);
            var ULTRA_RARE = new JsonObject();
            ULTRA_RARE.addProperty("name", "ultra_rare");
            ULTRA_RARE.addProperty("color", "dark_purple");
            ULTRA_RARE.addProperty("hex", 0xAA00AA);
            ULTRA_RARE.addProperty("weight", 0.05D);
            ULTRA_RARE.addProperty("effect", 0.38D);
            ULTRA_RARE.addProperty("enabled", true);
            var LEGENDARY = new JsonObject();
            LEGENDARY.addProperty("name", "legendary");
            LEGENDARY.addProperty("color", "gold");
            LEGENDARY.addProperty("hex", 0xFFAA00);
            LEGENDARY.addProperty("weight", 0.02D);
            LEGENDARY.addProperty("effect", 0.57D);
            LEGENDARY.addProperty("enabled", true);
            var ARCHAIC = new JsonObject();
            ARCHAIC.addProperty("name", "archaic");
            ARCHAIC.addProperty("color", "red");
            ARCHAIC.addProperty("hex", 0xFF55FF);
            ARCHAIC.addProperty("weight", 0.01D);
            ARCHAIC.addProperty("effect", 0.81D);
            ARCHAIC.addProperty("enabled", true);

            JsonObject[] list = new JsonObject[]{BASIC, UNCOMMON, RARE, ULTRA_RARE, LEGENDARY, ARCHAIC };

            FileWriter writer = null;

            for (JsonObject json : list){
                try {
                    var file = new File(dir, GsonHelper.getAsString(json, "name") + ".json");
                    writer = new FileWriter(file);

                    GSON.toJson(json, writer);
                    writer.close();
                } catch (Exception e) {
                    Static.LOGGER.error("An error occurred while generating default custom rarity", e);
                } finally {
                    IOUtils.closeQuietly(writer);
                }
            }


        }
    }

    private void loadFiles() {
        var files = dir.listFiles((FileFilter) FileFilterUtils.suffixFileFilter(".json"));
        if (files == null)
            return;

        for (var file : files) {
            JsonObject json;
            InputStreamReader reader = null;
            Rarity rarity = null;

            try {
                reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
                json = JsonParser.parseReader(reader).getAsJsonObject();

                rarity = Rarity.loadFromJson(json);

                reader.close();
            } catch (Exception e) {
                Static.LOGGER.error("An error occurred while loading rarity", e);
            } finally {
                IOUtils.closeQuietly(reader);
            }

            if (rarity != null && rarity.isEnabled()) {
                if (rarity.getWeight() > 0.0D) {
                    Rarity.RANDOM_RARITIES.add(rarity.getWeight(), rarity);
                }
                Rarity.RARITIES.add(rarity);
            }
        }

    }
    public void clear(){
        Rarity.RARITIES.clear();
    }

}
