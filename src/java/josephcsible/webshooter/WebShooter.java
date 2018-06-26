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

import org.apache.logging.log4j.Logger;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, dependencies = Reference.DEPENDENCIES, updateJSON=Reference.UPDATEJSON)
public class WebShooter
{
	@Mod.Instance(Reference.MOD_ID)
	public static WebShooter instance;

	public static Logger logger;
	public static SimpleNetworkWrapper netWrapper;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		netWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
		netWrapper.registerMessage(PlayerInWebMessage.Handler.class, PlayerInWebMessage.class, 0, Side.CLIENT);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) 
	{ }

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{ }

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event)
	{ }
}
