package org.sobadfish;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityDeathEvent;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import org.sobadfish.entity.AbstractDropEntity;
import org.sobadfish.events.EntityDropItemSpawnEvent;

/**
 * @author SoBadFish
 */
public class DropEntityItem extends PluginBase implements Listener {

    private static final String CONFIG_KEY_OPEN = "击杀生物是否掉落";

    @Override
    public void onEnable() {
        this.getLogger().info("欢迎使用某吃瓜咸鱼的击杀生物掉落生物插件");
        this.getLogger().info("这个插件 也就实现了击杀生物掉落迷你的一些东西");
        this.getLogger().info("可当前置api使用");
        saveDefaultConfig();
        reloadConfig();
        this.getServer().getPluginManager().registerEvents(this,this);
    }

    /**防止掉落生物被击杀*/
    @EventHandler
    public void onDamageEntityEvent(EntityDamageEvent event){
        if(event.getEntity() instanceof AbstractDropEntity){
            if(event.getCause() == EntityDamageEvent.DamageCause.LAVA || event.getCause() == EntityDamageEvent.DamageCause.FIRE){
                event.getEntity().close();
                return;
            }
            event.setCancelled();
        }
    }


    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event){
        Entity e = event.getEntity();
        if(getConfig().getBoolean(CONFIG_KEY_OPEN)){
            dropEntityItem(e,e.getNetworkId());
        }


    }

    public static void dropEntityItem(AbstractDropEntity entity){
        EntityDropItemSpawnEvent event = new EntityDropItemSpawnEvent(entity);
        Server.getInstance().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            entity.close();
            return;
        }
        entity.spawnToAll();
    }


    public static void dropEntityItem(Position e,int entityId){
        AbstractDropEntity abstractDropEntity = new AbstractDropEntity(e.getChunk(),e) {
            @Override
            public int getNetworkId() {
                return entityId;
            }
        };
        dropEntityItem(abstractDropEntity);

    }
}
