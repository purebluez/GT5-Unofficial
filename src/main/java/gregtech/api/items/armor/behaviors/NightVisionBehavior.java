package gregtech.api.items.armor.behaviors;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.items.armor.ArmorHelper;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

import static gregtech.api.items.armor.ArmorKeybinds.NIGHT_VISION_KEY;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

public class NightVisionBehavior implements IArmorBehavior {

    public static final NightVisionBehavior INSTANCE = new NightVisionBehavior();

    protected NightVisionBehavior() {/**/}

    @Override
    public void onKeyPressed(@NotNull ItemStack stack, @NotNull EntityPlayer player) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        boolean wasEnabled = tag.getBoolean(ArmorHelper.NIGHT_VISION_KEY);
        tag.setBoolean(ArmorHelper.NIGHT_VISION_KEY, !wasEnabled);

        if (wasEnabled) {
            player.removePotionEffect(Potion.nightVision.id);
            PlayerUtils
                .messagePlayer(player, StatCollector.translateToLocal("GT5U.armor.message.nightvision.disabled"));
        } else {
            PlayerUtils.messagePlayer(player, StatCollector.translateToLocal("GT5U.armor.message.nightvision.enabled"));
        }
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys() {
        return Collections.singleton(NIGHT_VISION_KEY);
    }

    // TODO: we should have our own electric item wrapper
    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (world.isRemote) return;
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (tag.getBoolean(ArmorHelper.NIGHT_VISION_KEY)) {
            //ElectricItem.manager.discharge(stack, 5, 1, true, true, false);
            player.removePotionEffect(Potion.blindness.id);
            player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 999999, 0, true));
        }
    }

    @Override
    public void onArmorUnequip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        player.removePotionEffect(Potion.nightVision.id);
    }

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.NIGHT_VISION_KEY, false); // disabled by default
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (tag.getBoolean(ArmorHelper.NIGHT_VISION_KEY)) {
            tooltip.add(StatCollector.translateToLocal("GT5U.armor.message.nightvision.enabled"));
        } else {
            tooltip.add(StatCollector.translateToLocal("GT5U.armor.message.nightvision.disabled"));
        }
    }
}
