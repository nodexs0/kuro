package com.node.kuro;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.EventPriority;

import org.lwjgl.glfw.GLFW;
import net.minecraftforge.client.event.InputEvent;

import java.util.List;


@Mod(KuroMod.MODID)
public class KuroMod {
    public static final String MODID = "kuro";

    private static final Logger LOGGER = LogManager.getLogger();

    private boolean keyPressed = false;

    private String crop = "Wheat Crops";

    private int shortcut = GLFW.GLFW_KEY_N;

    private Player player;

    public KuroMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        player = event.getEntity();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onKeyRelease(InputEvent.Key event) {
        if (event.getAction() == GLFW.GLFW_RELEASE && event.getKey() == shortcut) {
            keyPressed = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onKeyPress(InputEvent.Key event) {
        if (event.getKey() == shortcut && !keyPressed) {

            Level world = player.level();
            BlockPos playerPos = player.blockPosition();

            int posX = playerPos.getX();
            int posY = playerPos.getY();
            int posZ = playerPos.getZ();


            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int zOffset = -1; zOffset <= 1; zOffset++) {
                    BlockPos blockPos = new BlockPos(playerPos.getX() + xOffset, Mth.floor(player.getY()) + 1, playerPos.getZ() + zOffset);

                    BlockState blockState = world.getBlockState(blockPos);
                    Block block = blockState.getBlock();

                    if (block.getName().getString().equalsIgnoreCase(crop)) {
                        world.destroyBlock(blockPos, true);
                    }
                }
            }

            keyPressed = true;
        }
    }
}
