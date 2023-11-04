package nova.committee.enhancedarmaments.util;


import net.minecraft.ChatFormatting;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StringHelper {

    public static String printCommas(int amount) {

        String number = String.valueOf(amount);
        double amountD = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###");

        return formatter.format(amountD);
    }

    public static String[] getArrayFromList(List<String> list) {

        String[] output = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            output[i] = list.get(i);
        }

        return output;
    }

    public static List<String> removeCharFromList(List<String> list, CharSequence... chSeq) {

        for (int i = 0; i < list.size(); i++) {

            int toRemove = 0;

            for (CharSequence charSequence : chSeq) {

                if (list.get(i - toRemove).contains(charSequence)) {
                    list.remove(i - toRemove);
                    return removeCharFromList(list, chSeq);
                }
            }
        }

        return list;
    }

    public static List<String> removeNullsFromList(List<String> list) {

        for (int i = 0; i < list.size(); i++) {

            if (list.get(0) == null) {
                list.remove(0);
            }
        }

        return list;
    }

    public static List<ChatFormatting> getFormatsFromString(String str) {

        List<ChatFormatting> formats = new ArrayList<>();

        for (ChatFormatting format : ChatFormatting.values()) {

            if (str.contains(format.toString())) {
                formats.add(format);
            }
        }

        return formats;
    }

    public static String addAllFormats(List<ChatFormatting> formats) {

        StringBuilder builder = new StringBuilder();

        for (ChatFormatting format : formats) {
            builder.append(format);
        }

        return builder.toString();
    }
}
