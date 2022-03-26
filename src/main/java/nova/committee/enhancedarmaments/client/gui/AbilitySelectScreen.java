//package nova.committee.enhancedarmaments.client.gui;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.ChatFormatting;
//import net.minecraft.client.gui.components.Button;
//import net.minecraft.client.resources.language.I18n;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.TextComponent;
//import net.minecraft.network.chat.TranslatableComponent;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import nova.committee.enhancedarmaments.EnhancedArmaments;
//import nova.committee.enhancedarmaments.client.widgets.ButtonRect;
//import nova.committee.enhancedarmaments.client.widgets.SkillButton;
//import nova.committee.enhancedarmaments.client.widgets.TypeTab;
//import nova.committee.enhancedarmaments.common.config.Config;
//import nova.committee.enhancedarmaments.common.network.GuiAbilityPacket;
//import nova.committee.enhancedarmaments.core.Ability;
//import nova.committee.enhancedarmaments.core.Experience;
//import nova.committee.enhancedarmaments.core.Rarity;
//import nova.committee.enhancedarmaments.util.ComponentUtil;
//import nova.committee.enhancedarmaments.util.EAUtil;
//import nova.committee.enhancedarmaments.util.NBTUtil;
//import nova.committee.enhancedarmaments.util.ScreenHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Description:
// * Author: cnlimiter
// * Date: 2022/3/25 19:41
// * Version: 1.0
// */
//public class AbilitySelectScreen extends GuiBase{
//    private TypeTab zhuTab, beiTab;
//    private TypeTab activeTab;
//
//    private int selectZhuIndex, selectBeiIndex;
//
//    private ButtonRect buyBtn;
//
//    public AbilitySelectScreen() {
//        super();
//    }
//
//
//    @Override
//    protected void init() {
//        super.init();
//        if(minecraft != null){
//            zhuTab = new TypeTab(getScreenX() - 53, getScreenY() - 52, I18n.get(""), getScreenX() - 90, getScreenY() - 38);
//            beiTab = new TypeTab(getScreenX() + 5, getScreenY() - 52, I18n.get(""), getScreenX() - 90, getScreenY() - 38);
//            for (int index = 0; index < Math.min(Ability.WEAPON_ABILITIES_COUNT, 20); index++) {
//                int selectZhuIndex = index;
//                SkillButton button = zhuTab.addButton(index, (btn) -> setSelectZhuIndex(selectZhuIndex));
//                addRenderableWidget(button);
//            }
//
//            for (int index = 0; index < Math.min(Ability.ARMOR_ABILITIES_COUNT, 50); index++) {
//                int selectBeiIndex = index;
//                SkillButton button = beiTab.addButton(index, (btn) -> setSelectBeiIndex(selectBeiIndex));
//                addRenderableWidget(button);
//            }
//
//            buyBtn = addRenderableWidget(new ButtonRect(getScreenX(), getGuiSizeY(), 56, "", this::actionPerformed));
//
//        }
//    }
//
//    @Override
//    protected void drawGuiBackground(PoseStack matrixStack, int mouseX, int mouseY) {
//        if (minecraft == null) {
//            return;
//        }
//        matrixStack.pushPose();
//        ScreenHelper.bindGuiTextures();
//        ScreenHelper.drawRect(matrixStack, 0, getScreenY() - 76, 0, 1, 50, width, 18);
//
//    }
//
//    @Override
//    protected void drawGuiForeground(PoseStack matrixStack, int mouseX, int mouseY) {
//        Player player = this.minecraft.player;
//        if (player != null)
//        {
//            ItemStack stack = player.getInventory().getSelected();
//            if (stack != ItemStack.EMPTY)
//            {
//                if (EAUtil.canEnhance(stack.getItem()))
//                {
//                    CompoundTag nbt = NBTUtil.loadStackNBT(stack);
//                    {
//                        if (EAUtil.canEnhanceWeapon(stack.getItem()))
//                        {
//                            drawStrings(matrixStack, stack, Ability.WEAPON_ABILITIES, nbt);
//                            drawWeaponTooltips(matrixStack, selectZhuIndex, Ability.WEAPON_ABILITIES, mouseX, mouseY);
//                        }
//                        else if (EAUtil.canEnhanceArmor(stack.getItem()))
//                        {
//                            drawStrings(matrixStack,stack, Ability.ARMOR_ABILITIES, nbt);
//                            drawArmorTooltips(matrixStack, selectBeiIndex, Ability.ARMOR_ABILITIES, mouseX, mouseY);
//                        }
//
//                    }
//                }
//            }
//        }
//    }
//
//
//    public void setSelectBeiIndex(int selectBeiIndex) {
//        this.selectBeiIndex = selectBeiIndex;
//    }
//
//    public void setSelectZhuIndex(int selectZhuIndex) {
//        this.selectZhuIndex = selectZhuIndex;
//    }
//
//    private void drawStrings(PoseStack poseStack , ItemStack stack, ArrayList<Ability> abilities, CompoundTag nbt)
//    {
//        Rarity rarity = Rarity.getRarity(nbt);
//
//        drawCenteredString(poseStack ,font, stack.getDisplayName().getString(), width / 2, 20, 0xFFFFFF);
//        drawString(poseStack, font, I18n.get("enhancedarmaments.misc.rarity") + ": ", width / 2 - 50, 40, 0xFFFFFF);
//        drawString(poseStack,font, I18n.get("enhancedarmaments.rarity." + rarity.getName()), width / 2 - 15, 40, rarity.getHex());
//        drawCenteredString(poseStack,font, ChatFormatting.ITALIC + I18n.get("enhancedarmaments.misc.abilities"), width / 2 - 100, 73, 0xFFFFFF);
//        drawCenteredString(poseStack,font, ChatFormatting.GRAY + I18n.get("enhancedarmaments.misc.abilities.tokens") + ": " + ChatFormatting.DARK_GREEN + Experience.getAbilityTokens(nbt), width / 2 - 100, 86, 0xFFFFFF);
//        drawCenteredString(poseStack,font, ChatFormatting.GOLD + I18n.get("enhancedarmaments.misc.abilities.purchased"), width / 2 + 112, 100, 0xFFFFFF);
//        drawCenteredString(poseStack,font, ChatFormatting.BOLD + I18n.get("enhancedarmaments.ability.type.active"), width / 2 + 75, 120, 0xFFFFFF);
//        drawCenteredString(poseStack,font, ChatFormatting.BOLD + I18n.get("enhancedarmaments.ability.type.passive"), width / 2 + 150, 120, 0xFFFFFF);
//
//        if (Experience.getLevel(nbt) == Config.maxLevel)
//        {
//            drawString(poseStack,font, I18n.get("enhancedarmaments.misc.level") + ": " + Experience.getLevel(nbt) + ChatFormatting.DARK_RED +" (" + I18n.get("enhancedarmaments.misc.max") + ")", width / 2 - 50, 50, 0xFFFFFF);
//            drawString(poseStack,font, I18n.get("enhancedarmaments.misc.experience") + ": " + Experience.getExperience(nbt), width / 2 - 50, 60, 0xFFFFFF);
//        }
//        else
//        {
//            drawString(poseStack,font, I18n.get("enhancedarmaments.misc.level") + ": " + Experience.getLevel(nbt), width / 2 - 50, 50, 0xFFFFFF);
//            drawString(poseStack,font, I18n.get("enhancedarmaments.misc.experience") + ": " + Experience.getExperience(nbt) + " / " + Experience.getMaxLevelExp(Experience.getLevel(nbt)), width / 2 - 50, 60, 0xFFFFFF);
//        }
//
//        int j = -1;
//        int k = -1;
//
//        for (Ability ability : abilities) {
//            if (ability.hasAbility(nbt)) {
//                if (ability.getType().equals("active")) {
//                    j++;
//                    drawCenteredString(poseStack, font, I18n.get(ability.getName(nbt)), width / 2 + 75, 135 + (j * 12), ability.getHex());
//                } else if (ability.getType().equals("passive")) {
//                    k++;
//                    drawCenteredString(poseStack, font, ability.getName(nbt), width / 2 + 150, 135 + (k * 12), ability.getHex());
//                }
//            }
//        }
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    private void actionPerformed(Button button)
//    {
//        Player player = minecraft.player;
//
//        if (player != null)
//        {
//            ItemStack stack = player.getInventory().getSelected();
//
//            if (stack != ItemStack.EMPTY)
//            {
//                CompoundTag nbt = NBTUtil.loadStackNBT(stack);
//
//                {
//                    if (Experience.getAbilityTokens(nbt) > 0 || player.experienceLevel > 1 || player.isCreative())
//                    {
//                        if (EAUtil.canEnhanceWeapon(stack.getItem()))
//                        {
//
//                                if (button == zhuTab.getButton(selectZhuIndex))
//                                {
//                                    EnhancedArmaments.network.sendToServer(new GuiAbilityPacket(selectZhuIndex));
//                                }
//
//                        }
//                        else if (EAUtil.canEnhanceArmor(stack.getItem()))
//                        {
//
//                                if (button == beiTab.getButton(selectBeiIndex))
//                                {
//                                    EnhancedArmaments.network.sendToServer(new GuiAbilityPacket(selectBeiIndex));
//                                }
//
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void drawWeaponTooltips(PoseStack poseStack, int i, ArrayList<Ability> abilities, int mouseX, int mouseY){
//        Player player = this.minecraft.player;
//        ItemStack stack = player.getInventory().getSelected();
//        CompoundTag nbt = stack.getOrCreateTag();
//        List<String> list = new ArrayList<>();
//        list.add(abilities.get(i).getColor() + I18n.get("enhancedarmaments.ability." + abilities.get(i).getName()) + " (" + abilities.get(i).getTypeName() + abilities.get(i).getColor() + ")");
//        list.add("");
//        list.add(I18n.get("enhancedarmaments.abilities.info." + abilities.get(i).getName()));
//        list.add("");
//        if (i == 0)//FIRE
//        {
//            float chance = (float) (1.0 / (Config.firechance)) * 100;
//            float currentduration = (Ability.FIRE.getLevel(nbt) + Ability.FIRE.getLevel(nbt) * 4) / 4;
//            float nextlevelduration = (Ability.FIRE.getLevel(nbt) + 1 + (Ability.FIRE.getLevel(nbt) + 1) * 4) / 4;
//            int c = (int) chance;
//
//            if (!(Ability.FIRE.hasAbility(nbt))) {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %0" + ChatFormatting.GREEN + " + %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
//                }
//            } else {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
//                    if (!(Ability.FIRE.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 1)//FROST
//        {
//            float chance = (float) (1.0 / (Config.frostchance)) * 100;
//            float currentduration = (Ability.FROST.getLevel(nbt) + Ability.FROST.getLevel(nbt) * 4) / 3;
//            float nextlevelduration = (Ability.FROST.getLevel(nbt) + 1 + (Ability.FROST.getLevel(nbt) + 1) * 4) / 3;
//            int c = (int) chance;
//
//            if (!(Ability.FROST.hasAbility(nbt))) {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %0" + ChatFormatting.GREEN + " + %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
//                }
//            } else {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
//                    if (!(Ability.FROST.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 2)//POISON
//        {
//            float chance = (float) (1.0 / (Config.poisonchance)) * 100;
//            float currentduration = (Ability.POISON.getLevel(nbt) + Ability.POISON.getLevel(nbt) * 4) / 2;
//            float nextlevelduration = (Ability.POISON.getLevel(nbt) + 1 + (Ability.POISON.getLevel(nbt) + 1) * 4) / 2;
//            int c = (int) chance;
//
//            if (!(Ability.POISON.hasAbility(nbt))) {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %0" + ChatFormatting.GREEN + " + %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
//                }
//            } else {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
//                    if (!(Ability.POISON.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 3)//INNATE
//        {
//            float chance = (float) ((1.0 / (Config.innatechance)) * 100);
//            float currentduration = (Ability.INNATE.getLevel(nbt) + Ability.INNATE.getLevel(nbt) * 4) / 3;
//            float nextlevelduration = (Ability.INNATE.getLevel(nbt) + 1 + (Ability.INNATE.getLevel(nbt) + 1) * 4) / 3;
//            float currentbleedingspeed = (Ability.INNATE.getLevel(nbt));
//            float nextlevelbleedingspeed = (Ability.INNATE.getLevel(nbt) + 1);
//            int c = (int) chance;
//
//            if (!(Ability.INNATE.hasAbility(nbt))) {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %0" + ChatFormatting.GREEN + " + %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.bleedingspeed") + ": 0 " + ChatFormatting.GREEN + "+" + nextlevelbleedingspeed);
//                }
//            } else {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
//                    list.add(I18n.get("enhancedarmaments.abilities.info.bleedingspeed") + ": " + currentbleedingspeed + " " + ChatFormatting.GREEN + "+" + (nextlevelbleedingspeed - currentbleedingspeed));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
//                    list.add(I18n.get("enhancedarmaments.abilities.info.bleedingspeed") + ": " + currentbleedingspeed);
//                    if (!(Ability.INNATE.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 4)//BOMBASTIC
//        {
//            float chance = (float) ((1.0 / (Config.bombasticchance)) * 100);
//            float currentexplosionintensity = (Ability.BOMBASTIC.getLevel(nbt));
//            float nextlevelexplosionintensity = (Ability.BOMBASTIC.getLevel(nbt) + 1);
//            int c = (int) chance;
//
//            if (!(Ability.BOMBASTIC.hasAbility(nbt))) {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %0" + ChatFormatting.GREEN + " + %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.explosionintensity") + ": 0 " + ChatFormatting.GREEN + "+" + nextlevelexplosionintensity);
//                }
//            } else {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.explosionintensity") + ": " + currentexplosionintensity + " " + ChatFormatting.GREEN + "+" + (nextlevelexplosionintensity - currentexplosionintensity));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.explosionintensity") + ": " + currentexplosionintensity);
//                    if (!(Ability.BOMBASTIC.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 5)//CRITICAL_POINT
//        {
//            float chance = (float) ((1.0 / (Config.criticalpointchance)) * 100);
//            float currentdamage = (Ability.CRITICAL_POINT.getLevel(nbt) * 17);
//            float nextleveldamage = ((Ability.CRITICAL_POINT.getLevel(nbt) + 1) * 17);
//            int c = (int) chance;
//
//            if (!(Ability.CRITICAL_POINT.hasAbility(nbt))) {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %0" + ChatFormatting.GREEN + " + %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.healthpercentage") + ": %0" + ChatFormatting.GREEN + " + %" + nextleveldamage);
//                }
//            } else {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.healthpercentage") + ": %" + currentdamage + " " + ChatFormatting.GREEN + "+ %" + (nextleveldamage - currentdamage));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.healthpercentage") + ": %" + currentdamage);
//                    if (!(Ability.CRITICAL_POINT.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 6)//ILLUMINATION
//        {
//            if (!(Ability.ILLUMINATION.hasAbility(nbt))) {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + 5.0);
//                }
//            } else {
//                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + 5.0 + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
//            }
//            if (!(Ability.ILLUMINATION.canUpgradeLevel(nbt)) && (!(zhuTab.getButton(i).isActive())))
//                list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 7)//ETHEREAL
//        {
//            float currentrepair = (Ability.ETHEREAL.getLevel(nbt) * 2);
//            float nextlevelrepair = ((Ability.ETHEREAL.getLevel(nbt) + 1) * 2);
//
//            if (!(Ability.ETHEREAL.hasAbility(nbt))) {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.durability") + ": 0" + ChatFormatting.GREEN + " +" + 2.0);
//                }
//            } else {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.durability") + ": " + currentrepair + " " + ChatFormatting.GREEN + "+" + (nextlevelrepair - currentrepair));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.durability") + ": " + currentrepair);
//                    if (!(Ability.ETHEREAL.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 8)//BLOODTHIRST
//        {
//            float currentpercentage = (float) (Ability.BLOODTHIRST.getLevel(nbt) * 12);
//            float nextlevelpercentage = (float) ((Ability.BLOODTHIRST.getLevel(nbt) + 1) * 12);
//
//            if (!(Ability.BLOODTHIRST.hasAbility(nbt))) {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.damagepercentage") + ": %0" + ChatFormatting.GREEN + " + %" + nextlevelpercentage);
//                }
//            } else {
//                if (zhuTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.damagepercentage") + ": %" + currentpercentage + " " + ChatFormatting.GREEN + "+ %" + (nextlevelpercentage - currentpercentage));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.damagepercentage") + ": %" + currentpercentage);
//                    if (!(Ability.BLOODTHIRST.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        int explevel = abilities.get(i).getExpLevel(nbt);
//        if (!abilities.get(i).hasAbility(nbt)) {
//            list.add("");
//            if (abilities.get(i).hasEnoughExp(player, nbt))
//                list.add(ChatFormatting.DARK_GREEN + I18n.get("enhancedarmaments.abilities.info.required_exp") + ": " + explevel);
//            else
//                list.add(ChatFormatting.DARK_RED + I18n.get("enhancedarmaments.abilities.info.required_exp") + ": " + explevel);
//
//        } else if (abilities.get(i).canUpgradeLevel(nbt)) {
//            if (Experience.getAbilityTokens(nbt) >= abilities.get(i).getTier())
//                list.add(ChatFormatting.DARK_GREEN + I18n.get("enhancedarmaments.abilities.info.required_token") + ": " + abilities.get(i).getTier());
//            else
//                list.add(ChatFormatting.DARK_RED + I18n.get("enhancedarmaments.abilities.info.required_token") + ": " + abilities.get(i).getTier());
//
//        }
//        this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//    }
//
//    private void drawArmorTooltips(PoseStack poseStack, int i, ArrayList<Ability> abilities, int mouseX, int mouseY){
//        Player player = this.minecraft.player;
//        ItemStack stack = player.getInventory().getSelected();
//        CompoundTag nbt = stack.getOrCreateTag();
//        List<String> list = new ArrayList<>();
//        list.add(abilities.get(i).getColor() + I18n.get("enhancedarmaments.ability." + abilities.get(i).getName()) + " (" + abilities.get(i).getTypeName() + abilities.get(i).getColor() + ")");
//        list.add("");
//        list.add(I18n.get("enhancedarmaments.abilities.info." + abilities.get(i).getName()));
//        list.add("");
//        if (i == 0)//MOLTEN
//        {
//            float chance = (float) (1.0 / (Config.moltenchance)) * 100;
//            float currentduration = (Ability.MOLTEN.getLevel(nbt) + Ability.MOLTEN.getLevel(nbt) * 5) / 4;
//            float nextlevelduration = (Ability.MOLTEN.getLevel(nbt) + 1 + (Ability.MOLTEN.getLevel(nbt) + 1) * 5) / 4;
//            int c = (int) chance;
//
//            if (!(Ability.MOLTEN.hasAbility(nbt))) {
//                if (beiTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %0" + ChatFormatting.GREEN + " + %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
//                }
//            } else {
//                if (beiTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
//                    if (!(Ability.MOLTEN.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 1)//FROZEN
//        {
//            float chance = (float) (1.0 / (Config.frozenchance)) * 100;
//            float currentduration = (Ability.FROZEN.getLevel(nbt) + Ability.FROZEN.getLevel(nbt) * 5) / 6;
//            float nextlevelduration = (Ability.FROZEN.getLevel(nbt) + 1 + (Ability.FROZEN.getLevel(nbt) + 1) * 5) / 6;
//            int c = (int) chance;
//
//            if (!(Ability.FROZEN.hasAbility(nbt))) {
//                if (beiTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %0" + ChatFormatting.GREEN + " + %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
//                }
//            } else {
//                if (beiTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
//                    if (!(Ability.FROZEN.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 2)//TOXIC
//        {
//            float chance = (float) (1.0 / (Config.toxicchance)) * 100;
//            float currentduration = (Ability.TOXIC.getLevel(nbt) + Ability.TOXIC.getLevel(nbt) * 4) / 4;
//            float nextlevelduration = (Ability.TOXIC.getLevel(nbt) + 1 + (Ability.TOXIC.getLevel(nbt) + 1) * 4) / 4;
//            int c = (int) chance;
//
//            if (!(Ability.TOXIC.hasAbility(nbt))) {
//                if (beiTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %0" + ChatFormatting.GREEN + " + %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
//                }
//            } else {
//                if (beiTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
//                    if (!(Ability.TOXIC.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 3)//BEASTIAL
//        {
//            if (!(Ability.BEASTIAL.hasAbility(nbt))) {
//                if (beiTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + 7.0);
//                }
//            } else {
//                list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + 7.0 + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
//            }
//            if (!(Ability.BEASTIAL.canUpgradeLevel(nbt)) && (!(beiTab.getButton(i).isActive())))
//                list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 4)//REMEDIAL
//        {
//            float heal = (float) Ability.REMEDIAL.getLevel(nbt);
//            if (!(Ability.REMEDIAL.hasAbility(nbt))) {
//                if (beiTab.getButton(i).isActive()) {
//                    heal = 1f;
//                    list.add(I18n.get("enhancedarmaments.abilities.info.heal_amount") + ": 0 " + ChatFormatting.GREEN + "+" + heal);
//                }
//            } else {
//                if (beiTab.getButton(i).isActive())
//                    list.add(I18n.get("enhancedarmaments.abilities.info.heal_amount") + ": " + heal + ChatFormatting.GREEN + " +" + 1.0);
//                else
//                    list.add(I18n.get("enhancedarmaments.abilities.info.heal_amount") + ": " + heal);
//            }
//            if (!(Ability.REMEDIAL.canUpgradeLevel(nbt)) && (!(beiTab.getButton(i).isActive())))
//                list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 5)//HARDENED
//        {
//            float chance = (float) ((1.0 / (Config.hardenedchance)) * 100);
//
//            if (!(Ability.HARDENED.hasAbility(nbt))) {
//                if (beiTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %0" + ChatFormatting.GREEN + " + %" + chance);
//                }
//            } else {
//                list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + chance);
//            }
//            if (!(Ability.HARDENED.canUpgradeLevel(nbt)) && (!(beiTab.getButton(i).isActive())))
//                list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        if (i == 6)//ADRENALINE
//        {
//            float chance = (float) (1.0 / (Config.adrenalinechance)) * 100;
//            float currentduration = (Ability.ADRENALINE.getLevel(nbt) + Ability.ADRENALINE.getLevel(nbt) * 5) / 3;
//            float nextlevelduration = (Ability.ADRENALINE.getLevel(nbt) + 1 + (Ability.ADRENALINE.getLevel(nbt) + 1) * 5) / 3;
//            int c = (int) chance;
//
//            if (!(Ability.ADRENALINE.hasAbility(nbt))) {
//                if (beiTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %0" + ChatFormatting.GREEN + " + %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": 0 " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + nextlevelduration);
//                }
//            } else {
//                if (beiTab.getButton(i).isActive()) {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds") + ChatFormatting.GREEN + " +" + (nextlevelduration - currentduration));
//                } else {
//                    list.add(I18n.get("enhancedarmaments.abilities.info.chance") + ": %" + c);
//                    list.add(I18n.get("enhancedarmaments.abilities.info.duration") + ": " + currentduration + " " + I18n.get("enhancedarmaments.abilities.info.seconds"));
//                    if (!(Ability.ADRENALINE.canUpgradeLevel(nbt)))
//                        list.add(ChatFormatting.RED + I18n.get("enhancedarmaments.misc.max") + " " + I18n.get("enhancedarmaments.misc.level"));
//                }
//            }
//
//            this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//            list.clear();
//        }
//        int explevel = abilities.get(i).getExpLevel(nbt);
//        if (!abilities.get(i).hasAbility(nbt)) {
//            list.add("");
//            if (abilities.get(i).hasEnoughExp(player, nbt))
//                list.add(ChatFormatting.DARK_GREEN + I18n.get("enhancedarmaments.abilities.info.required_exp") + ": " + explevel);
//            else
//                list.add(ChatFormatting.DARK_RED + I18n.get("enhancedarmaments.abilities.info.required_exp") + ": " + explevel);
//
//        } else if (abilities.get(i).canUpgradeLevel(nbt)) {
//            if (Experience.getAbilityTokens(nbt) >= abilities.get(i).getTier())
//                list.add(ChatFormatting.DARK_GREEN + I18n.get("enhancedarmaments.abilities.info.required_token") + ": " + abilities.get(i).getTier());
//            else
//                list.add(ChatFormatting.DARK_RED + I18n.get("enhancedarmaments.abilities.info.required_token") + ": " + abilities.get(i).getTier());
//
//        }
//        this.renderComponentTooltip(poseStack, ComponentUtil.stringToComponent(list), mouseX + 3, mouseY + 3);
//    }
//
//
//    @Override
//    protected String getGuiTextureName() {
//        return null;
//    }
//
//    @Override
//    protected int getGuiSizeX() {
//        return 0;
//    }
//
//    @Override
//    protected int getGuiSizeY() {
//        return 0;
//    }
//
//    @Override
//    protected boolean canCloseWithInvKey() {
//        return true;
//    }
//}
