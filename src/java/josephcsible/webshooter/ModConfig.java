package josephcsible.webshooter;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID)
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ModConfig {

	@Name("Melee web chance")
	@Comment("Chance per melee attack that a spider will create a web")
	@RangeDouble(min = 0.0, max = 1.0)
	public static double webChance = 0.15;

	@Name("Allow replacement")
	@Comment("Whether webs are able to replace water, lava, fire, snow, vines, and any mod-added blocks declared as replaceable")
	public static boolean allowReplacement = true;

	@Name("Sling webbing")
	@Comment("Spiders can spit webbing from a distance")
	public static boolean slingWebbing = true;

	@Name("Sling cooldown")
	@Comment("Time between web slings")
	@RangeInt(min = 1)
	public static int reshootTime = 45;

	@Name("Webbing on web")
	@Comment("Webbing hitting web creates more web")
	public static boolean webbingOnWeb = false;
	
	@Name("Sling variance")
	@Comment("Time variance between a spider's web slings")
	public static double slingVariance = 2.0F;
	
	@Name("Sling inaccuracy")
	@Comment("Inaccuracy of web slings")
	public static double slingInaccuracy = 6.0F;

	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
	private static class EventHandler {
		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MOD_ID)) {
				ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}