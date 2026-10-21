package wly.tweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Durability implements ModInitializer {
    /** How much longer everything lasts. Keep this equal to mc-26.2. */
    static final double MULTIPLIER = 2.0;

    private static final Logger LOG = LoggerFactory.getLogger("wly_tweaks");

    @Override
    public void onInitialize() {
        DefaultItemComponentEvents.MODIFY.register(context -> {
            int[] changed = {0};
            context.modify(
                item -> item.getComponents().contains(DataComponentTypes.MAX_DAMAGE),
                (builder, item) -> {
                    int max = item.getComponents().getOrDefault(DataComponentTypes.MAX_DAMAGE, 0);
                    builder.add(DataComponentTypes.MAX_DAMAGE, scaled(item, max));
                    changed[0]++;
                });
            LOG.info("wly: durability x{} on {} items", MULTIPLIER, changed[0]);
        });
    }

    /** Wooden tools get the old stone-equivalent durability; everything else keeps the x2 rule. */
    static int scaled(Object item, int max) {
        if (isWoodenTool(item)) {
            return Math.max(1, (int) Math.round(max * (131.0 / 59.0)));
        }
        return Math.max(1, (int) Math.round(max * MULTIPLIER));
    }

    private static boolean isWoodenTool(Object item) {
        if (item == null) return false;
        return item == Items.WOODEN_AXE
            || item == Items.WOODEN_HOE
            || item == Items.WOODEN_PICKAXE
            || item == Items.WOODEN_SHOVEL
            || item == Items.WOODEN_SWORD;
    }
}
