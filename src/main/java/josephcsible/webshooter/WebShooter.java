/*
WebShooter Minecraft Mod
Copyright (C) 2016 Joseph C. Sible

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package josephcsible.webshooter;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = WebShooter.MODID, version = WebShooter.VERSION, guiFactory = "josephcsible.webshooter.WebShooterGuiFactory")
public class WebShooter
{
	// XXX duplication with mcmod.info and build.gradle
	public static final String MODID = "webshooter";
	public static final String VERSION = "1.0.0";

	public static Configuration config;
	public static SimpleNetworkWrapper netWrapper;
	protected double webChance;
	protected boolean allowReplacement;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		netWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		netWrapper.registerMessage(PlayerInWebMessage.Handler.class, PlayerInWebMessage.class, 0, Side.CLIENT);
		config = new Configuration(event.getSuggestedConfigurationFile());
		syncConfig();
	}

	protected void syncConfig() {
		webChance = config.get(Configuration.CATEGORY_GENERAL, "webChance", 0.15, "The chance per attack that a spider will create a web on an entity it attacks", 0.0, 1.0).getDouble();
		allowReplacement = config.get(Configuration.CATEGORY_GENERAL, "allowReplacement", true, "Whether webs are able to replace water, lava, fire, snow, vines, and any mod-added blocks declared as replaceable").getBoolean();
		if(config.hasChanged())
			config.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent eventArgs) {
		if(eventArgs.getModID().equals(MODID))
			syncConfig();
	}

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		// Mod authors: If your mod adds custom spiders, and you want them to work with
		// this mod, make them subclass EntitySpider (like vanilla cave spiders do).
		if(!(event.getSource().getEntity() instanceof EntitySpider))
			return;

		Entity target = event.getEntity();
		World world = target.worldObj;
		BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);
		IBlockState state = world.getBlockState(pos);
		Block oldBlock = state.getBlock();

		if(!oldBlock.isReplaceable(world, pos))
			return;

		if(!allowReplacement && !oldBlock.isAir(state, world, pos))
			return;

		if(webChance < world.rand.nextDouble())
			return;

		world.setBlockState(pos, Blocks.WEB.getDefaultState());
		target.setInWeb();
		if(target instanceof EntityPlayerMP) {
			// If we don't tell the client about the web ourself, it won't get told until after the
			// attack resolves. This will result in the client thinking the player got knocked back
			// further than they really did, which in turn will result in a "player moved wrongly"
			// message on the server.
			netWrapper.sendTo(new PlayerInWebMessage(pos), (EntityPlayerMP)target);
		}
	}
}
