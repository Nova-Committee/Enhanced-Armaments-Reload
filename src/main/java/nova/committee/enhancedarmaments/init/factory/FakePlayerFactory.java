package nova.committee.enhancedarmaments.init.factory;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import nova.committee.enhancedarmaments.common.entity.FakePlayer;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/6/27 22:54
 * Version: 1.0
 */
public class FakePlayerFactory {
    private static final GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");
    private static final Map<GameProfile, FakePlayer> fakePlayers = Maps.newHashMap();
    private static WeakReference<FakePlayer> MINECRAFT_PLAYER = null;

    public static FakePlayer getMinecraft(ServerLevel world)
    {
        FakePlayer ret = MINECRAFT_PLAYER != null ? MINECRAFT_PLAYER.get() : null;
        if (ret == null)
        {
            ret = FakePlayerFactory.get(world,  MINECRAFT);
            MINECRAFT_PLAYER = new WeakReference<>(ret);
        }
        return ret;
    }


    public static FakePlayer get(ServerLevel world, GameProfile username)
    {
        if (!fakePlayers.containsKey(username))
        {
            FakePlayer fakePlayer = new FakePlayer(null, world, username, null);
            fakePlayers.put(username, fakePlayer);
        }

        return fakePlayers.get(username);
    }

    public static void unloadWorld(ServerLevel world)
    {
        fakePlayers.entrySet().removeIf(entry -> entry.getValue().getCommandSenderWorld() == world);
        if (MINECRAFT_PLAYER != null && MINECRAFT_PLAYER.get() != null && MINECRAFT_PLAYER.get().getCommandSenderWorld() == world)
        {
            FakePlayer mc = MINECRAFT_PLAYER.get();
            if (mc != null && mc.getCommandSenderWorld() == world)
            {
                MINECRAFT_PLAYER = null;
            }
        }
    }
}
