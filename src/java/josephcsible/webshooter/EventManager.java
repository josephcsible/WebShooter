package josephcsible.webshooter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class EventManager
{

	/*
	 * Mod authors: If your mod adds custom spiders, and you want them to work with
	 * this mod, make them subclass EntitySpider (like vanilla cave spiders do).
	 */

	/*
	 * Add ranged webbing attack AI to spiders
	 */
	@SubscribeEvent
	public static void onEntitySpawn(EntityJoinWorldEvent event)
	{ 
		if (event.getEntity() instanceof EntitySpider)
		{
			addAIToSpider((EntitySpider)event.getEntity());
		}
	}

	/*
	 * Handle melee webbing attack for spiders
	 */
	@SubscribeEvent
	public static void onLivingAttack(LivingAttackEvent event)
	{
		Entity immediateSource = event.getSource().getImmediateSource();
		Entity trueSource = event.getSource().getTrueSource();
		Entity target = event.getEntity();

		// if web is shot, let EntityWebbing handle it - so immediateSource and trueSource must be spider 
		if ((immediateSource instanceof EntitySpider) && (immediateSource == trueSource))
		{
			tryAttack(immediateSource, trueSource, target);
		}
	}

	// ----------------------------------------------------------------------

	private static void addAIToSpider(EntitySpider entity)
	{
		if (ModConfig.slingWebbing == true)
		{
			entity.tasks.addTask(7, new AIWebbingAttack(entity));
		}
	}

	public static void tryAttack(Entity immediateSource, Entity source, Entity target) 
	{
		World world = target.world;
		BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);

		if(ModConfig.webChance <= world.rand.nextDouble())
		{
			return;
		}

		if ((target != null) && (immediateSource != null))
		{
			double distance = immediateSource.getDistanceSq(target);

			// some mods somehow make LivingAttackEvent fire when spider is still far away
			// so make it so spider only does webbing if very close
			if (distance > 2)
			{
				return;
			}
		}

		EntityWebbing.onHit(world, pos, source, target);
	}
}
