package wly.tweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.component.DataComponentTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tools and armour last longer, across the board, as in Raspberry Flavoured.
 * The pack makes the early game longer; a longer early game should not mean
 * more time at a crafting table replacing pickaxes.
 *
 * Every item with a max damage gets it multiplied, modded items included,
 * because Fabric runs this over the whole item registry once it is complete.
 * A datapack cannot do this on 1.21.1: max damage is a default component, and
 * default components come from code.
 *
 * It must run on both sides. The server decides when an item breaks, the
 * client draws the durability bar, and default components are not synced, so
 * each side computes its own.
 */
public final class Durability implements ModInitializer {
    /** How much longer everything lasts. Tune by playing. */
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
                    builder.add(DataComponentTypes.MAX_DAMAGE, scaled(max));
                    changed[0]++;
                });
            LOG.info("wly: durability x{} on {} items", MULTIPLIER, changed[0]);
        });
    }

    /** At least 1, so nothing becomes unbreakable by accident. */
    static int scaled(int max) {
        return Math.max(1, (int) Math.round(max * MULTIPLIER));
    }
}
