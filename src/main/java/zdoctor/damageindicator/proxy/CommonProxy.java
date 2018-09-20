package zdoctor.damageindicator.proxy;

import java.util.Optional;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zdoctor.damageindicator.optional.ZCore;

public class CommonProxy {

	public Logger log;

	public Optional<ZCore> zcore;

	public void preInit(FMLPreInitializationEvent e) {
		log = e.getModLog();
	}

	public void init(FMLInitializationEvent e) {
	}

	public void postInit(FMLPostInitializationEvent e) {
		zcore = (Optional<ZCore>) e.buildSoftDependProxy("zcoremod", ZCore.class.getName(), new Object[0]);
	}

}