package net.hellomouse.photonic.tools;

import net.hellomouse.photonic.Photonic;
import net.hellomouse.photonic.util.Networking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.ChunkRandom;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.List;

// Divining pendulum to find slime chunks
public class SlimeFinderItem extends Item {
	public SlimeFinderItem(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
		tooltip.add(Text.translatable("item." + Photonic.MOD_ID + ".slime_finder.tooltip")
				.formatted(Formatting.ITALIC, Formatting.GRAY));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
		// Ignore if run client side as client doesn't have seed (and shouldn't)
		if (world.isClient()) return super.use(world, playerEntity, hand);

		Long seed = null;
		if (world instanceof StructureWorldAccess && world.getRegistryKey() == World.OVERWORLD)
			seed = ((StructureWorldAccess) world).getSeed();
		if (seed != null) {
			ChunkPos pos = playerEntity.getChunkPos();
			// Found a slime chunk, tell the player
			if (ChunkRandom.getSlimeRandom(pos.x, pos.z, seed, 987234911L).nextInt(10) == 0) {
				// playerEntity.sendSystemMessage(Text.literal("Slime")); // Unimmersive

				PacketByteBuf buf = PacketByteBufs.create();
				buf.writeBoolean(true);
				ServerPlayNetworking.send((ServerPlayerEntity)playerEntity, Networking.SLIME_FINDER_PACKET_ID, buf);

				Vec3d ppos = playerEntity.getPos();
				world.playSound(null, ppos.x, ppos.y, ppos.z, SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.MASTER, 1f, 1f);
			}
		}
		return TypedActionResult.success(playerEntity.getStackInHand(hand));
	}

	public static void onClientWorld(MinecraftClient client, boolean isSlime) {
		if (!isSlime) return; // Not a slime chunk

		// Found a slime chunk, play particles
		World world = client.world;
		Vec3d ppos = client.player.getPos();
		PlayerEntity playerEntity = client.player;

		RandomGenerator rand = playerEntity.getRandom();
		for (int i = 0; i <= 10; i++)
			world.addImportantParticle(ParticleTypes.WAX_ON,
					ppos.x + (rand.nextDouble() - 0.5D) * (double)playerEntity.getWidth(),
					ppos.y + rand.nextDouble() * (double)playerEntity.getHeight() - playerEntity.getHeightOffset(),
					ppos.z + (rand.nextDouble() - 0.5D) * (double)playerEntity.getWidth(),
					(rand.nextDouble() - 0.5D), -0.5, (rand.nextDouble() - 0.5D));
	}
}
