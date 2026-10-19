package wly.tweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The 26.x spelling of mc-1.21.1's Durability, which carries the reasoning.
 * Same idea and same multiplier, in Mojang's names: DataComponents and set().
 */
public final class Durability implements ModInitializer {
    /** How much longer everything lasts. Keep equal to mc-1.21.1's. */
    static final double MULTIPLIER = 2.0;

    private static final Logger LOG = LoggerFactory.getLogger("wly_tweaks");

    @Override
    public void onInitialize() {
        DefaultItemComponentEvents.MODIFY.register(context -> {
            int[] changed = {0};
            context.modify(
                item -> item.components().has(DataComponents.MAX_DAMAGE),
                (builder, item) -> {
                    int max = item.components().getOrDefault(DataComponents.MAX_DAMAGE, 0);
                    builder.set(DataComponents.MAX_DAMAGE, scaled(max));
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
