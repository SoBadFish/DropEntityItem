package org.sobadfish.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import org.sobadfish.events.EntityItemCloseEvent;
import org.sobadfish.events.PlayerPickUpEntityEvent;

import java.util.Random;

/**
 * @author SoBadFish
 */
public abstract class AbstractDropEntity extends Entity {

    protected AbstractDropEntity(FullChunk chunk,Position position) {
        super(chunk, getEntityDefaultCompoundTag(position));
        this.setScale(0.3f);
        this.onGround = true;
    }

    protected AbstractDropEntity(FullChunk chunk,CompoundTag tag) {
        super(chunk, tag);
        this.setScale(0.3f);
        this.onGround = true;

    }

    private static CompoundTag getEntityDefaultCompoundTag(Position e){
        Vector3 motion = new Vector3((new Random()).nextDouble() * 0.2D - 0.1D, 0.2D, (new Random()).nextDouble() * 0.2D - 0.1D);
        Entity.getDefaultNBT(e, motion, (new Random()).nextFloat() * 360.0F, 0.0F);
        return Entity.getDefaultNBT(e, new Vector3((new Random()).nextDouble() * 0.2D - 0.1D, 0.2D, (new Random()).nextDouble() * 0.2D - 0.1D));
    }



    @Override
    public float getWidth() {
        return 0.25F;
    }

    @Override
    public float getLength() {
        return 0.25F;
    }

    @Override
    public float getHeight() {
        return 0.25F;
    }

    @Override
    public float getGravity() {
        return 0.04F;
    }

    @Override
    public float getDrag() {
        return 0.02F;
    }

    @Override
    protected float getBaseOffset() {
        return 0.125F;
    }

    @Override
    public boolean canCollide() {
        return false;
    }

    private int pickupDelay = 10;

    /**
     * 通过重写生物id实现
     * @return 生物id
     * */
    @Override
    abstract public int getNetworkId();

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        } else {
            int tickDiff = currentTick - this.lastUpdate;
            if (tickDiff <= 0 && !this.justCreated) {
                return true;
            } else {
                this.yaw += 5;
                if(this.yaw >= 355){
                    this.yaw = 0;
                }
                double minSize = 1.5;
                Player p = null;
                double dis;
                for (Player player : this.level.getPlayers().values()) {
                    dis = player.distance(this);
                    if (dis <= minSize) {
                        p = player;
                        minSize = dis;
                    }
                }
                if (p != null) {
                    if(pickupDelay == 0) {
                        PlayerPickUpEntityEvent event = new PlayerPickUpEntityEvent(p, this);
                        Server.getInstance().getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            this.close();
                        }
                    }
                }
            }
            return onGravity(currentTick);
        }
    }

    private boolean onGravity(int currentTick){
        int tickDiff = currentTick - this.lastUpdate;
        this.lastUpdate = currentTick;
        this.timing.startTiming();
        int bid;
        boolean hasUpdate = this.entityBaseTick(tickDiff);
        if (!this.fireProof && this.isInsideOfFire()) {
            this.kill();
        }

        if (this.isAlive()) {
            if (this.pickupDelay > 0 && this.pickupDelay < 32767) {
                this.pickupDelay -= tickDiff;
                if (this.pickupDelay < 0) {
                    this.pickupDelay = 0;
                }
            }

            bid = this.level.getBlockIdAt(this.getFloorX(), this.getFloorY(), this.getFloorZ());
            if (bid != 8 && bid != 9) {
                if (!this.isOnGround()) {
                    this.motionY -= (double)this.getGravity();
                }
            } else {
                this.motionY = (double)this.getGravity() - 0.06D;
            }

            if (this.checkObstruction(this.x, this.y, this.z)) {
                hasUpdate = true;
            }

            this.move(this.motionX, this.motionY, this.motionZ);
            double friction = (double)(1.0F - this.getDrag());
            if ((Math.abs(this.motionX) > 1.0E-5D || Math.abs(this.motionZ) > 1.0E-5D)) {
                if(onGround){
                    friction *= this.getLevel().getBlock(this.temporalVector.setComponents((double)((int)Math.floor(this.x)), (double)((int)Math.floor(this.y - 1.0D)), (double)((int)Math.floor(this.z) - 1))).getFrictionFactor();
                }
            }
            this.motionX *= friction;
            this.motionY *= (double)(1.0F - this.getDrag());
            this.motionZ *= friction;
            if (this.onGround) {
                this.motionY *= -0.5D;
            }

            this.updateMovement();
            if (this.age > 6000) {
                EntityItemCloseEvent ev = new EntityItemCloseEvent(this);
                this.server.getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    this.age = 0;
                } else {
                    this.kill();
                    hasUpdate = true;
                }
            }
        }

        this.timing.stopTiming();
        return hasUpdate || !this.onGround || Math.abs(this.motionX) > 1.0E-5D || Math.abs(this.motionY) > 1.0E-5D || Math.abs(this.motionZ) > 1.0E-5D;
    }


}
