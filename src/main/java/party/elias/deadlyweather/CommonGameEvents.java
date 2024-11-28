package party.elias.deadlyweather;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = DeadlyWeather.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommonGameEvents {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {

    }

}
