
package wly.tweaks;

import java.util.List;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MiningTier implements ModInitializer {
    private static final Logger LOG =
        LoggerFactory.getLogger("wly_tweaks");

    private static final List<Item> WOODEN_TOOLS = List.of(
        Items.WOODEN_PICKAXE,
        Items.WOODEN_AXE,
        Items.WOODEN_SHOVEL,
        Items.WOODEN_HOE
    );

    @Override
    public void onInitialize() {
        DefaultItemComponentEvents.MODIFY.register(context -> {
            int[] changed = {0};

            context.modify(
                WOODEN_TOOLS::contains,
                (builder, item) -> {
                    Tool original =
                        item.components().get(DataComponents.TOOL);

                    if (original == null) {
                        return;
                    }

                    List<Block> stoneTierBlocks =
                        BuiltInRegistries.BLOCK.stream()
                            .filter(block ->
                                block.defaultBlockState().is(
                                    BlockTags.INCORRECT_FOR_STONE_TOOL
                                )
                            )
                            .toList();

                    HolderSet<Block> stoneTierHolders =
                        HolderSet.direct(
                            stoneTierBlocks.stream()
                                .map(Block::builtInRegistryHolder)
                                .toList()
                        );

                    List<Tool.Rule> rules = original.rules()
                        .stream()
                        .map(rule -> {
                            boolean woodenTierRule = rule.blocks()
                                .unwrapKey()
                                .filter(
                                    BlockTags.INCORRECT_FOR_WOODEN_TOOL::equals
                                )
                                .isPresent();

                            if (!woodenTierRule) {
                                return rule;
                            }

                            return Tool.Rule.deniesDrops(
                                stoneTierHolders
                            );
                        })
                        .toList();

                    builder.set(
                        DataComponents.TOOL,
                        new Tool(
                            rules,
                            original.defaultMiningSpeed(),
                            original.damagePerBlock(),
                            original.canDestroyBlocksInCreative()
                        )
                    );

                    changed[0]++;
                }
            );

            LOG.info(
                "wly: stone-equivalent harvest tier applied to {} wooden tools, durability and speed stays the same",
                changed[0]
            );
        });
    }
}
