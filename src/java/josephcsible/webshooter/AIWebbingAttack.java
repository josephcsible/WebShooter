package josephcsible.webshooter;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

class AIWebbingAttack extends EntityAIBase
{
	private final static double maxDistance = 256.0D; // 16 is 256, 32 is 1024, 64 is 4096
	private final EntityLiving parentEntity;
	public int attackTimer;

	public AIWebbingAttack(EntityLiving entity)
	{
		this.parentEntity = entity;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute()
	{
		return this.parentEntity.getAttackTarget() != null;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.attackTimer = 0;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask()
	{ }

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask()
	{
		EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();

		if ( entitylivingbase.getDistanceSq(this.parentEntity) < maxDistance &&
				this.parentEntity.canEntityBeSeen(entitylivingbase) )
		{
			++this.attackTimer;

			/*
            // MAYBE play pre-shoot event
            if (this.attackTimer == (reshootTime/2))
            {
                world.playEvent((EntityPlayer)null, effect, new BlockPos(this.parentEntity), 0);
            }
			 */

			if (this.attackTimer == ModConfig.reshootTime)
			{
				// MAYBE source.world.playEvent((EntityPlayer)null, effect, new BlockPos(this.parentEntity), 0);

				EntityWebbing.sling(parentEntity.world, parentEntity);

				double cooldown = ModConfig.reshootTime +
						ModConfig.reshootTime * parentEntity.world.rand.nextFloat() * ModConfig.slingVariance;
				
				this.attackTimer -= cooldown;
				
			}
		}
		else if (this.attackTimer > 0)
		{
			--this.attackTimer;
		}
	}
}