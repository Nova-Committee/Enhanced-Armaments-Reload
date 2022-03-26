package nova.committee.enhancedarmaments.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/25 18:09
 * Version: 1.0
 */
public class ComponentUtil {


    public static List<Component> stringToComponent(List<String> list) {
        List<Component> text = new ArrayList<>();

        for (String s : list) {
            text.add(new TextComponent(s));
        }

        return text;
    }

}
