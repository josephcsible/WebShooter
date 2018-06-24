package josephcsible.webshooter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder("webshooter")
public class ObjectHandler
{
	private static int entityID = 0;

	//@ObjectHolder("webshooter:webbing")
	public static Item webbing = null;

	public static final SoundEvent WEBBING_SHOOT = SoundEvents.ENTITY_SNOWBALL_THROW;
	public static final SoundEvent WEBBING_STICK = SoundEvents.BLOCK_SNOW_HIT;
	public static final SoundEvent WEBBING_NONSTICK = SoundEvents.BLOCK_SNOW_BREAK;

	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
	public static class RegistrationHandler extends RegistrationHelpers
	{
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();

			// MAYBE webbing = Item.REGISTRY.getObject(new ResourceLocation("minecraft:web"));
			webbing = regHelper(registry, new ItemWebbing());

			registerTileEntities();
		}

		@SubscribeEvent
		public static void registerSoundEvents(RegistryEvent.Register<SoundEvent> event) {
			//esky_open = registerSound(event.getRegistry(), "foodfunk:esky_open");
		}

		public static void registerTileEntities() {
			//registerTileEntity(TileEntityEsky.class, "foodfunk:esky");
		}

		@SideOnly(Side.CLIENT)
		@SubscribeEvent
		public static void registerRenders(ModelRegistryEvent event)
		{
			registerRender(webbing);

			RenderingRegistry.registerEntityRenderingHandler(EntityWebbing.class, manager -> new RenderSnowball<>(manager, webbing, Minecraft.getMinecraft().getRenderItem()));

			//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFreezer.class, new TileEntityFreezerRenderer());
		}

		@SubscribeEvent
		public static void entityRegistration(final RegistryEvent.Register<EntityEntry> event)
		{
			registerEntity(event.getRegistry());
		}

		public static void registerEntity(IForgeRegistry<EntityEntry> registry)
		{
			EntityEntry entry = EntityEntryBuilder.create()
					.entity(EntityWebbing.class)
					.id(new ResourceLocation("webshooter", "webbing"), entityID++)
					.name("webbing")
					.tracker(64, 10, true)
					.build();
			registry.register(entry);
		}
	}
}
