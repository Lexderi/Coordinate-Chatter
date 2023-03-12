package de.lexderi.coordinatechatter.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.EnumSet;


public class CoordinateChatterClient implements ClientModInitializer
{
    private static KeyBinding keyBinding;
    @Override
    public void onInitializeClient()
    {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.coordinatechatter.sendCoordinatesInChat",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F4,
                "category.coordinatechatter.coordinateChatter"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                if(client.player == null || client.player.world.getRegistryKey() == World.END) break;

                Boolean inNether = client.player.world.getRegistryKey() == World.NETHER;
                Vec3d playerPos = client.player.getPos();
                Vec3d playerPosOverworld = (inNether? new Vec3d(playerPos.x * 8, playerPos.y, playerPos.z * 8) : playerPos).floorAlongAxes(EnumSet.allOf(Direction.Axis.class));
                Vec3d playerPosNether = (inNether? playerPos : new Vec3d(playerPos.x / 8, playerPos.y, playerPos.z / 8)).floorAlongAxes(EnumSet.allOf(Direction.Axis.class));
                String text = String.format("Overworld: %d, %d, %d | Nether: %d, %d, %d", (int)playerPosOverworld.x, (int)playerPosOverworld.y, (int)playerPosOverworld.z, (int)playerPosNether.x, (int)playerPosNether.y, (int)playerPosNether.z);
                client.player.sendMessage(new LiteralText(text), false);
            }
        });
    }
}
