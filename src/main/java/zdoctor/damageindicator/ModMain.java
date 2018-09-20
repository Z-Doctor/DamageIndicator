package zdoctor.damageindicator;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zdoctor.damageindicator.proxy.CommonProxy;

@Mod(modid = ModMain.MODID, name = ModMain.NAME, version = ModMain.VERSION, clientSideOnly = true)
public class ModMain
{
    public static final String MODID = "damageindicator";
    public static final String NAME = "Damage Indicator Mod";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "zdoctor.damageindicator.proxy.ClientProxy", serverSide = "zdoctor.damageindicator.proxy.CommonProxy")
	public static CommonProxy proxy;

    
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

}
