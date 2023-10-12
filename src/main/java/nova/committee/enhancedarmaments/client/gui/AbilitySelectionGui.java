package nova.committee.enhancedarmaments.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import nova.committee.enhancedarmaments.common.config.Config;
import nova.committee.enhancedarmaments.common.network.GuiAbilityPacket;
import nova.committee.enhancedarmaments.common.network.PacketHandler;
import nova.committee.enhancedarmaments.core.Ability;
import nova.committee.enhancedarmaments.core.Experience;
import nova.committee.enhancedarmaments.core.Rarity;
import nova.committee.enhancedarmaments.util.ComponentUtil;
import nova.committee.enhancedarmaments.util.EAUtil;
import nova.committee.enhancedarmaments.util.NBTUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class AbilitySelectionGui extends Screen {
    private final int leftPos, topPos;
    private Button[] weaponAbilities;
    private Button[] armorAbilities;

    public AbilitySelectionGui() {
        super(Component.literal(""));
        this.leftPos = (this.width) / 2;
        this.topPos = (this.height) / 2;

    }


    @Override
    protected void init() {
        Player player = this.minecraft.player;

        if (player != null) {
            ItemStack stack = player.getInventory().getSelected();

            if (stack != ItemStack.EMPTY) {
                if (EAUtil.canEnhanceWeapon(stack.getItem())) {
                    weaponAbilities = new Button[Ability.WEAPON_ABILITIES_COUNT];
                    CompoundTag nbt = stack.getTag();

                    if (nbt != null) {
                        int j = 0;

                        for (int i = 0; i < weaponAbilities.length; i++) {
                            if (Ability.WEAPON_ABILITIES.get(i).getType().equals("active")) {
                                weaponAbilities[i] = new ExtendedButton(width / 2 - 215, 100 + (i * 21), 110, 20,
                                        Component.translatable("enhancedarmaments.ability." + Ability.WEAPON_ABILITIES.get(i).getName()), this::actionPerformed);
                                j++;
                            } else
                                weaponAbilities[i] = new ExtendedButton(width / 2 - 100, 100 + ((i - j) * 21), 110, 20,
                                        Component.translatable("enhancedarmaments.ability." + Ability.WEAPON_ABILITIES.get(i).getName()), this::actionPerformed);

                            this.addRenderableWidget(weaponAbilities[i]);
                            weaponAbilities[i].active = false;
                        }
                    }
                } else if (EAUtil.canEnhanceArmor(stack.getItem())) {
                    armorAbilities = new Button[Ability.ARMOR_ABILITIES_COUNT];
                    CompoundTag nbt = stack.getTag();

                    if (nbt != null) {
                        int j = 0;

                        for (int i = 0; i < armorAbilities.length; i++) {
                            if (Ability.ARMOR_ABILITIES.get(i).getType().equals("active")) {
                                armorAbilities[i] = new ExtendedButton(width / 2 - 215, 100 + (i * 21), 100, 20,
                                        Component.translatable("enhancedarmaments.ability." + Ability.ARMOR_ABILITIES.get(i).getName()), this::actionPerformed);

                                j++;
                            } else
                                armorAbilities[i] = new ExtendedButton(width / 2 - 100, 100 + ((i - j) * 21), 105, 20,
                                        Component.translatable("enhancedarmaments.ability." + Ability.ARMOR_ABILITIES.get(i).getName()), this::actionPerformed);

                            this.addRenderableWidget(armorAbilities[i]);
                            armorAbilities[i].active = false;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);

        Player player = this.minecraft.player;

        if (player != null) {
            ItemStack stack = player.getInventory().getSelected();

            if (stack != ItemStack.EMPTY) {
                if (EAUtil.canEnhance(stack.getItem())) {
                    CompoundTag nbt = NBTUtil.loadStackNBT(stack);

                    {
                        if (EAUtil.canEnhanceWeapon(stack.getItem())) {

                            drawStrings(graphics, stack, Ability.WEAPON_ABILITIES, nbt);
                            displayButtons(graphics, weaponAbilities, Ability.WEAPON_ABILITIES, nbt, player);
                            drawTooltips(graphics, weaponAbilities, Ability.WEAPON_ABILITIES, mouseX, mouseY);
                        } else if (EAUtil.canEnhanceArmor(stack.getItem())) {
                            drawStrings(graphics, stack, Ability.ARMOR_ABILITIES, nbt);
                            displayButtons(graphics, armorAbilities, Ability.ARMOR_ABILITIES, nbt, player);
                            drawTooltips(graphics, armorAbilities, Ability.ARMOR_ABILITIES, mouseX, mouseY);
                        }
                    }
                }
            }
        }
    }


    @OnlyIn(Dist.CLIENT)
    private void actionPerformed(Button button) {
        Player player = minecraft.player;

        if (player != null) {
            ItemStack stack = player.getInventory().getSelected();

            if (stack != ItemStack.EMPTY) {
                CompoundTag nbt = NBTUtil.loadStackNBT(stack);

                {
                    if (Experience.getAbilityTokens(nbt) > 0 || player.experienceLevel > 1 || player.isCreative()) {
                        if (EAUtil.canEnhanceWeapon(stack.getItem())) {
                            for (int i = 0; i < weaponAbilities.length; i++) {
                                if (button == weaponAbilities[i]) {
                                    PacketHandler.INSTANCE.sendToServer(new GuiAbilityPacket(i));
                                }
                            }
                        } else if (EAUtil.canEnhanceArmor(stack.getItem())) {
                            for (int i = 0; i < armorAbilities.length; i++) {
                                if (button == armorAbilities[i]) {
                                    PacketHandler.INSTANCE.sendToServer(new GuiAbilityPacket(i));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 绘制技能选择GUI的字符串。
     *
     * @param stack
     * @param abilities
     * @param nbt
     */
    private void drawStrings(GuiGraphics graphics, ItemStack stack, ArrayList<Ability> abilities, CompoundTag nbt) {
        Rarity rarity = Rarity.getRarity(nbt);

        graphics.drawCenteredString(font, stack.getDisplayName(), width / 2, 20, 0xFFFFFF);
        graphics.drawString(font, I18n.get("enhancedarmaments.misc.rarity") + ": ", width / 2 - 50, 40, 0xFFFFFF);
        graphics.drawString(font, I18n.get("enhancedarmaments.rarity." + rarity.getName()), width / 2 - 15, 40, rarity.getHex());
        graphics.drawCenteredString(font, ChatFormatting.ITALIC + I18n.get("enhancedarmaments.misc.abilities"), width / 2 - 100, 73, 0xFFFFFF);
        graphics.drawCenteredString(font, ChatFormatting.GRAY + I18n.get("enhancedarmaments.misc.abilities.tokens") + ": " + ChatFormatting.DARK_GREEN + Experience.getAbilityTokens(nbt), width / 2 - 100, 86, 0xFFFFFF);
        graphics.drawCenteredString(font, ChatFormatting.GOLD + I18n.get("enhancedarmaments.misc.abilities.purchased"), width / 2 + 112, 100, 0xFFFFFF);
        graphics.drawCenteredString(font, ChatFormatting.BOLD + I18n.get("enhancedarmaments.ability.type.active"), width / 2 + 75, 120, 0xFFFFFF);
        graphics.drawCenteredString(font, ChatFormatting.BOLD + I18n.get("enhancedarmaments.ability.type.passive"), width / 2 + 150, 120, 0xFFFFFF);

        if (Experience.getLevel(nbt) == Config.maxLevel) {
            graphics.drawString(font, I18n.get("enhancedarmaments.misc.level") + ": " + Experience.getLevel(nbt) + ChatFormatting.DARK_RED + " (" + I18n.get("enhancedarmaments.misc.max") + ")", width / 2 - 50, 50, 0xFFFFFF);
            graphics.drawString(font, I18n.get("enhancedarmaments.misc.experience") + ": " + Experience.getExperience(nbt), width / 2 - 50, 60, 0xFFFFFF);
        } else {
            graphics.drawString(font, I18n.get("enhancedarmaments.misc.level") + ": " + Experience.getLevel(nbt), width / 2 - 50, 50, 0xFFFFFF);
            graphics.drawString(font, I18n.get("enhancedarmaments.misc.experience") + ": " + Experience.getExperience(nbt) + " / " + Experience.getMaxLevelExp(Experience.getLevel(nbt)), width / 2 - 50, 60, 0xFFFFFF);
        }

        int j = -1;
        int k = -1;

        for (Ability ability : abilities) {
            if (ability.hasAbility(nbt)) {
                if (ability.getType().equals("active")) {
                    j++;
                    graphics.drawCenteredString(font, I18n.get(ability.getName(nbt)), width / 2 + 75, 135 + (j * 12), ability.getHex());
                } else if (ability.getType().equals("passive")) {
                    k++;
                    graphics.drawCenteredString(font, ability.getName(nbt), width / 2 + 150, 135 + (k * 12), ability.getHex());
                }
            }
        }
    }

    /**
     * 根据可用的能力标记确定需要启用哪些按钮，以及武器是否足够高以启用。
     *
     * @param buttons
     * @param abilities
     * @param nbt
     */
    private void displayButtons(GuiGraphics graphics, Button[] buttons, ArrayList<Ability> abilities, CompoundTag nbt, Player player) {
        for (Button button : buttons) {
            button.active = false;
        }

        for (int i = 0; i < buttons.length; i++) {
            if (!(abilities.get(i).hasAbility(nbt))) {
                if (abilities.get(i).hasEnoughExp(player, nbt))
                    buttons[i].active = true;
            } else if (abilities.get(i).canUpgradeLevel(nbt) && Experience.getAbilityTokens(nbt) >= abilities.get(i).getTier()) {
                buttons[i].active = true;
            } else {
                buttons[i].active = false;
            }
        }
    }


    public void renderComponentTooltip(GuiGraphics graphics,Font font, List<Component> text, int mouseX, int mouseY) {
        graphics.renderComponentTooltip(font, text, mouseX, mouseY);
    }

    private void drawTooltips(GuiGraphics graphics, Button[] buttons, ArrayList<Ability> abilities, int mouseX, int mouseY) {
        Player player = this.minecraft.player;
        ItemStack stack = player.getInventory().getSelected();
        CompoundTag nbt = stack.getOrCreateTag();

        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isHoveredOrFocused()) {
                List<String> list = new ArrayList<>();
                list.add(abilities.get(i).getColor() + I18n.get("enhancedarmaments.ability." + abilities.get(i).getName()) + " (" + abilities.get(i).getTypeName() + abilities.get(i).getColor() + ")");
                list.add("");
                list.add(I18n.get("enhancedarmaments.abilities.info." + abilities.get(i).getName()));
                list.add("");
                if (EAUtil.canEnhanceWeapon(stack.getItem())) {
                    if (i == 0)//FIRE
                    {
                        float chance = (float) (1.0 / (Config.firechance)) * 100;
                        float currentduration = (Ability.FIRE.getLevel(nbt) + Ability.FIRE.getLevel(nbt) * 4) / 4;
                        float nextlevelduration = (Ability.FIRE.getLevel(nbt) + 1 + (Ability.FIRE.getLevel(nbt) + 1) * 4) / 4;
                        int c = (int) chance;

                        if (!(Ability.FIRE.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": 0%" + ChatFormatting.GREEN + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
                                if (!(Ability.FIRE.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }

                    }
                    if (i == 1)//FROST
                    {
                        float chance = (float) (1.0 / (Config.frostchance)) * 100;
                        float currentduration = (Ability.FROST.getLevel(nbt) + Ability.FROST.getLevel(nbt) * 4) / 3;
                        float nextlevelduration = (Ability.FROST.getLevel(nbt) + 1 + (Ability.FROST.getLevel(nbt) + 1) * 4) / 3;
                        int c = (int) chance;

                        if (!(Ability.FROST.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": 0%" + ChatFormatting.GREEN + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
                                if (!(Ability.FROST.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }

                    }
                    if (i == 2)//POISON
                    {
                        float chance = (float) (1.0 / (Config.poisonchance)) * 100;
                        float currentduration = (Ability.POISON.getLevel(nbt) + Ability.POISON.getLevel(nbt) * 4) / 2;
                        float nextlevelduration = (Ability.POISON.getLevel(nbt) + 1 + (Ability.POISON.getLevel(nbt) + 1) * 4) / 2;
                        int c = (int) chance;

                        if (!(Ability.POISON.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": 0%" + ChatFormatting.GREEN + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
                                if (!(Ability.POISON.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }

                    }
                    if (i == 3)//INNATE
                    {
                        float chance = (float) ((1.0 / (Config.innatechance)) * 100);
                        float currentduration = (Ability.INNATE.getLevel(nbt) + Ability.INNATE.getLevel(nbt) * 4) / 3;
                        float nextlevelduration = (Ability.INNATE.getLevel(nbt) + 1 + (Ability.INNATE.getLevel(nbt) + 1) * 4) / 3;
                        float currentbleedingspeed = (Ability.INNATE.getLevel(nbt));
                        float nextlevelbleedingspeed = (Ability.INNATE.getLevel(nbt) + 1);
                        int c = (int) chance;

                        if (!(Ability.INNATE.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": 0%" + ChatFormatting.GREEN + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
                                list.add(I18n.get("enhancedarmaments.abilities.info.bleedingspeed") + ": 0 " + ChatFormatting.GREEN + "+" + nextlevelbleedingspeed);
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
                                list.add(I18n.get("enhancedarmaments.abilities.info.bleedingspeed") + ": " + currentbleedingspeed + " " + ChatFormatting.GREEN + "+" + (nextlevelbleedingspeed - currentbleedingspeed));
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
                                list.add(I18n.get("enhancedarmaments.abilities.info.bleedingspeed") + ": " + currentbleedingspeed);
                                if (!(Ability.INNATE.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }

                    }
                    if (i == 4)//BOMBASTIC
                    {
                        float chance = (float) ((1.0 / (Config.bombasticchance)) * 100);
                        float currentexplosionintensity = (Ability.BOMBASTIC.getLevel(nbt));
                        float nextlevelexplosionintensity = (Ability.BOMBASTIC.getLevel(nbt) + 1);
                        int c = (int) chance;

                        if (!(Ability.BOMBASTIC.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": 0%" + ChatFormatting.GREEN + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.explosionintensity") + ": 0 " + ChatFormatting.GREEN + "+" + nextlevelexplosionintensity);
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.explosionintensity") + ": " + currentexplosionintensity + " " + ChatFormatting.GREEN + "+" + (nextlevelexplosionintensity - currentexplosionintensity));
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.explosionintensity") + ": " + currentexplosionintensity);
                                if (!(Ability.BOMBASTIC.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }

                    }
                    if (i == 5)//CRITICAL_POINT
                    {
                        float chance = (float) ((1.0 / (Config.criticalpointchance)) * 100);
                        float currentdamage = (Ability.CRITICAL_POINT.getLevel(nbt) * 17);
                        float nextleveldamage = ((Ability.CRITICAL_POINT.getLevel(nbt) + 1) * 17);
                        int c = (int) chance;

                        if (!(Ability.CRITICAL_POINT.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": 0%" + ChatFormatting.GREEN + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.healthpercentage") + ": 0%" + ChatFormatting.GREEN + " + " + nextleveldamage + "%");
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.healthpercentage") + " + " + currentdamage + "%" + " " + ChatFormatting.GREEN + "+" + (nextleveldamage - currentdamage) + "%");
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.healthpercentage") + " + " + currentdamage + "%");
                                if (!(Ability.CRITICAL_POINT.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }

                    }
                    if (i == 6)//ILLUMINATION
                    {
                        if (!(Ability.ILLUMINATION.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + 5.0);
                            }
                        } else {
                            list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + 5.0 + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
                        }
                        if (!(Ability.ILLUMINATION.canUpgradeLevel(nbt)) && (!(buttons[i].active)))
                            list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));

                    }
                    if (i == 7)//ETHEREAL
                    {
                        float currentrepair = (Ability.ETHEREAL.getLevel(nbt) * 2);
                        float nextlevelrepair = ((Ability.ETHEREAL.getLevel(nbt) + 1) * 2);

                        if (!(Ability.ETHEREAL.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.durability") + ": 0" + ChatFormatting.GREEN + " +" + 2.0);
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.durability") + ": " + currentrepair + " " + ChatFormatting.GREEN + "+" + (nextlevelrepair - currentrepair));
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.durability") + ": " + currentrepair);
                                if (!(Ability.ETHEREAL.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }

                    }
                    if (i == 8)//BLOODTHIRST
                    {
                        float currentpercentage = (float) (Ability.BLOODTHIRST.getLevel(nbt) * 12);
                        float nextlevelpercentage = (float) ((Ability.BLOODTHIRST.getLevel(nbt) + 1) * 12);

                        if (!(Ability.BLOODTHIRST.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.damagepercentage") + ": 0%" + ChatFormatting.GREEN + " + " + nextlevelpercentage + "%");
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.damagepercentage") + " + " + currentpercentage + "%" + " " + ChatFormatting.GREEN + "+ %" + (nextlevelpercentage - currentpercentage));
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.damagepercentage") + " + " + currentpercentage + "%");
                                if (!(Ability.BLOODTHIRST.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }

                    }
                } else if (EAUtil.canEnhanceArmor(stack.getItem())) {
                    if (i == 0)//MOLTEN
                    {
                        float chance = (float) (1.0 / (Config.moltenchance)) * 100;
                        float currentduration = (Ability.MOLTEN.getLevel(nbt) + Ability.MOLTEN.getLevel(nbt) * 5) / 4;
                        float nextlevelduration = (Ability.MOLTEN.getLevel(nbt) + 1 + (Ability.MOLTEN.getLevel(nbt) + 1) * 5) / 4;
                        int c = (int) chance;

                        if (!(Ability.MOLTEN.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": 0%" + ChatFormatting.GREEN + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
                                if (!(Ability.MOLTEN.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }

                    }
                    if (i == 1)//FROZEN
                    {
                        float chance = (float) (1.0 / (Config.frozenchance)) * 100;
                        float currentduration = (Ability.FROZEN.getLevel(nbt) + Ability.FROZEN.getLevel(nbt) * 5) / 6;
                        float nextlevelduration = (Ability.FROZEN.getLevel(nbt) + 1 + (Ability.FROZEN.getLevel(nbt) + 1) * 5) / 6;
                        int c = (int) chance;

                        if (!(Ability.FROZEN.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": 0%" + ChatFormatting.GREEN + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
                                if (!(Ability.FROZEN.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }

                    }
                    if (i == 2)//TOXIC
                    {
                        float chance = (float) (1.0 / (Config.toxicchance)) * 100;
                        float currentduration = (Ability.TOXIC.getLevel(nbt) + Ability.TOXIC.getLevel(nbt) * 4) / 4;
                        float nextlevelduration = (Ability.TOXIC.getLevel(nbt) + 1 + (Ability.TOXIC.getLevel(nbt) + 1) * 4) / 4;
                        int c = (int) chance;

                        if (!(Ability.TOXIC.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": 0%" + ChatFormatting.GREEN + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
                                if (!(Ability.TOXIC.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }

                    }
                    if (i == 3)//BEASTIAL
                    {
                        if (!(Ability.BEASTIAL.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + 7.0);
                            }
                        } else {
                            list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + 7.0 + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
                        }
                        if (!(Ability.BEASTIAL.canUpgradeLevel(nbt)) && (!(buttons[i].active)))
                            list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));

                    }
                    if (i == 4)//REMEDIAL
                    {
                        float heal = (float) Ability.REMEDIAL.getLevel(nbt);
                        if (!(Ability.REMEDIAL.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                heal = 1f;
                                list.add(I18n.get("enhancedarmaments.abilities.info.heal_amount") + ": 0 " + ChatFormatting.GREEN + "+" + heal);
                            }
                        } else {
                            if (buttons[i].active)
                                list.add(I18n.get("enhancedarmaments.abilities.info.heal_amount") + ": " + heal + ChatFormatting.GREEN + " +" + 1.0);
                            else
                                list.add(I18n.get("enhancedarmaments.abilities.info.heal_amount") + ": " + heal);
                        }
                        if (!(Ability.REMEDIAL.canUpgradeLevel(nbt)) && (!(buttons[i].active)))
                            list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));

                    }
                    if (i == 5)//HARDENED
                    {
                        float chance = (float) ((1.0 / (Config.hardenedchance)) * 100);

                        if (!(Ability.HARDENED.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": 0%" + ChatFormatting.GREEN + " + " + chance + "%");
                            }
                        } else {
                            list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + chance + "%");
                        }
                        if (!(Ability.HARDENED.canUpgradeLevel(nbt)) && (!(buttons[i].active)))
                            list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));

                    }
                    if (i == 6)//ADRENALINE
                    {
                        float chance = (float) (1.0 / (Config.adrenalinechance)) * 100;
                        float currentduration = (Ability.ADRENALINE.getLevel(nbt) + Ability.ADRENALINE.getLevel(nbt) * 5) / 3;
                        float nextlevelduration = (Ability.ADRENALINE.getLevel(nbt) + 1 + (Ability.ADRENALINE.getLevel(nbt) + 1) * 5) / 3;
                        int c = (int) chance;

                        if (!(Ability.ADRENALINE.hasAbility(nbt))) {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": 0%" + ChatFormatting.GREEN + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
                            }
                        } else {
                            if (buttons[i].isActive()) {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
                            } else {
                                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + " + " + c + "%");
                                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
                                if (!(Ability.ADRENALINE.canUpgradeLevel(nbt)))
                                    list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
                            }
                        }


                    }
                }

                int explevel = abilities.get(i).getExpLevel(nbt);
                if (!abilities.get(i).hasAbility(nbt)) {
                    list.add("");
                    if (abilities.get(i).hasEnoughExp(player, nbt))
                        list.add(ChatFormatting.DARK_GREEN + I18n.get("enhancedarmaments.abilities.info.required_exp") + ": " + explevel);
                    else
                        list.add(ChatFormatting.DARK_RED + I18n.get("enhancedarmaments.abilities.info.required_exp") + ": " + explevel);

                } else if (abilities.get(i).canUpgradeLevel(nbt)) {
                    if (Experience.getAbilityTokens(nbt) >= abilities.get(i).getTier())
                        list.add(ChatFormatting.DARK_GREEN + I18n.get("enhancedarmaments.abilities.info.required_token") + ": " + abilities.get(i).getTier());
                    else
                        list.add(ChatFormatting.DARK_RED + I18n.get("enhancedarmaments.abilities.info.required_token") + ": " + abilities.get(i).getTier());

                }
                //System.out.println(list);
                this.renderComponentTooltip(graphics, font, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
            }
        }
    }



    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
