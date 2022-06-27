package nova.committee.enhancedarmaments.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/3/25 18:09
 * Version: 1.0
 */
public class ComponentUtil {


    public static List<ITextComponent> stringToComponent(List<String> list) {
        List<ITextComponent> text = new ArrayList<>();

        for (String s : list) {
            text.add(new StringTextComponent(s));
        }

        return text;
    }

}
