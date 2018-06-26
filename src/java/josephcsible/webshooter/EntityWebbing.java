package josephcsible.webshooter;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityWebbing extends EntityThrowable
{
	public static final EnumParticleTypes particleType = EnumParticleTypes.SNOWBALL;

	public EntityLivingBase shootingEntity;

	public EntityWebbing(World worldIn)
	{
		super(worldIn);
	}

	public EntityWebbing(World worldIn, EntityLivingBase throwerIn)
	{
		super(worldIn, throwerIn);
	}

	public EntityWebbing(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityWebbing(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
	{
		super(worldIn);
		this.setSize(1.0F, 1.0F);
		this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
		this.setPosition(x, y, z);
	}

	public static void registerFixesWebbing(DataFixer fixer)
	{
		EntityThrowable.registerFixesThrowable(fixer, "webbing");
	}

	/**
	 * Handler for {@link World#setEntityState}
	 */
	 @SideOnly(Side.CLIENT)
	 public void handleStatusUpdate(byte id)
	 {
		 if (id == 3)
		 {
			 for (int i = 0; i < 3; ++i)
			 {
				 this.world.spawnParticle(particleType, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
			 }
		 }
	 }

	 /**
	  * Called when this EntityThrowable hits a block or entity.
	  */
	 protected void onImpact(RayTraceResult result)
	 {
		 boolean doit = true;

		 if (result.entityHit != null)
		 {
			 // 0 damage attack
			 result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
		 }

		 if (result.typeOfHit != RayTraceResult.Type.MISS)
		 {
			 BlockPos pos;

			 if (result.typeOfHit == RayTraceResult.Type.BLOCK)
			 {
				 // if hitting another cobweb, don't create web - to avoid massive web areas
				 if (!ModConfig.webbingOnWeb)
				 {
					 IBlockState state = world.getBlockState(result.getBlockPos());
					 Block oldBlock = state.getBlock();	        		
					 doit &= (oldBlock != Blocks.WEB);
				 }

				 // need to check block immediately before hit, not the hit block itself
				 pos = result.getBlockPos().offset(result.sideHit);
			 }
			 else // RayTraceResult.Type.ENTITY
			 {
				 pos = result.entityHit.getPosition();
			 }

			 doit &= (this.getThrower() != result.entityHit);

			 if (doit)
			 {
				 onHit(world, pos, this.getThrower(), result.entityHit);
			 }
		 }

		 if (doit && !this.world.isRemote)
		 {
			 this.world.setEntityState(this, (byte)3);
			 this.setDead();
		 }
	 }

	 // ----------------------------------------------------------------------

	 public static EntityWebbing sling(World worldIn, EntityLivingBase entityIn)
	 {
		 EntityWebbing entity = null;

		 float pitch = 1.0F / (entityIn.getRNG().nextFloat() * 0.4F + 0.8F);
		 entityIn.playSound(ObjectHandler.WEBBING_SHOOT, 1.0F, pitch);

		 if (!worldIn.isRemote)
		 {
			 entity = new EntityWebbing(worldIn, entityIn);
			 float inaccuracy = (float)ModConfig.slingInaccuracy;
			 entity.shoot(entityIn, entityIn.rotationPitch, entityIn.rotationYaw, 0.0F, 1.1F, inaccuracy);
			 worldIn.spawnEntity(entity);
		 }

		 return entity;
	 }

	 public static void onHit(World world, BlockPos pos, Entity source, Entity target)
	 {
		 IBlockState state = world.getBlockState(pos);
		 Block oldBlock = state.getBlock();
		 boolean stick = true;

		 if (!oldBlock.isReplaceable(world, pos) ||
				 (!ModConfig.allowReplacement && !oldBlock.isAir(state, world, pos)) )
		 {
			 stick = false;
		 }

		 if (!stick)
		 {
			 world.playSound((EntityPlayer)null, pos, ObjectHandler.WEBBING_NONSTICK, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
			 return;
		 }

		 world.playSound((EntityPlayer)null, pos, ObjectHandler.WEBBING_STICK, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));

		 if (world.isRemote)
		 {
			 return;	
		 }

		 world.setBlockState(pos, Blocks.WEB.getDefaultState());

		 if (target != null)
		 {
			 target.setInWeb();
			 if(target instanceof EntityPlayerMP)
			 {
				 // If we don't tell the client about the web ourself, it won't get told until after the
				 // attack resolves. This will result in the client thinking the player got knocked back
				 // further than they really did, which in turn will result in a "player moved wrongly"
				 // message on the server.
				 WebShooter.netWrapper.sendTo(new PlayerInWebMessage(pos), (EntityPlayerMP)target);
			 }
		 }
	 }    
}