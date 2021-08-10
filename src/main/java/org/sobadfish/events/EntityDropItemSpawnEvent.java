package org.sobadfish.events;

import cn.nukkit.event.Cancellable;
import cn.nukkit.event.Event;
import cn.nukkit.event.HandlerList;
import org.sobadfish.entity.AbstractDropEntity;

/**
 * @author SoBadFish
 */
public class EntityDropItemSpawnEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlers() {
        return HANDLERS;
    }

    private AbstractDropEntity entity;

    public EntityDropItemSpawnEvent(AbstractDropEntity entity){
        this.entity = entity;
    }

    public AbstractDropEntity getEntity() {
        return entity;
    }

    public void setEntity(AbstractDropEntity entity) {
        this.entity = entity;
    }
}
