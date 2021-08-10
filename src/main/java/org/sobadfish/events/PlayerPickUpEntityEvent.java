package org.sobadfish.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import org.sobadfish.entity.AbstractDropEntity;

/**
 * @author SoBadFish
 */
public class PlayerPickUpEntityEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlers() {
        return HANDLERS;
    }

    private AbstractDropEntity entity;

    public PlayerPickUpEntityEvent(Player player,AbstractDropEntity entity){
        this.player = player;
        this.entity = entity;
    }

    public AbstractDropEntity getEntity() {
        return entity;
    }
}
