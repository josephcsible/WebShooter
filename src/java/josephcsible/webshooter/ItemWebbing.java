package josephcsible.webshooter;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWebbing extends Item
{
	public ItemWebbing()
	{
		this.maxStackSize = 16;
		this.setCreativeTab(CreativeTabs.MISC);

		ObjectHandler.RegistrationHandler.nameHelper(this, "webshooter:webbing");
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (!playerIn.capabilities.isCreativeMode)
		{
			itemstack.shrink(1);
		}

		EntityWebbing.sling(worldIn, playerIn);

		playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
	}
}
