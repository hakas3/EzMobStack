package io.edkek.mc.ezmobstack.listener;

import io.edkek.mc.ezmobstack.EzMobStack;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class MobListener implements Listener {
    private transient int stackRadius;

    private HashMap<UUID, Integer> stack = new HashMap<>();
    private HashMap<UUID, Entity> stackCache = new HashMap<>();
    private HashMap<UUID, EntityType> typeCache = new HashMap<>();

    public void init(EzMobStack plugin) {
        stackRadius = plugin.getConfig().getInt("mobs.stackRadius", 30);

        if (stackRadius == 30)
            plugin.getConfig().set("mobs.stackRadius", 30);
    }

    public int getStackRadius() {
        return stackRadius;
    }

    public void setStackRadius(int stackRadius) {
        this.stackRadius = stackRadius;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void mobSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Monster || event.getEntity() instanceof Animals) {
            for (UUID id : stackCache.keySet()) {
                Entity entity = stackCache.get(id);

                if (entity.getType() != event.getEntityType())
                    continue;

                double entityDistance = event.getLocation().distanceSquared(entity.getLocation());

                if (entityDistance <= stackRadius) {

                    if (entity.isDead())
                        return; //Ignore this logic if the owner is dying

                    event.setCancelled(true);

                    int num = stack.get(id) + 1;

                    stack.put(id, num);

                    String name = toProperCase(entity.getType().name());

                    entity.setCustomName(num + " " + name + "s");
                    return;
                }
            }

            //If we get here then start a new stack
            Entity entity = event.getEntity();
            entity.setCustomNameVisible(true);

            String name = toProperCase(entity.getType().name());

            entity.setCustomName("1 " + name);

            stack.put(entity.getUniqueId(), 1);
            stackCache.put(entity.getUniqueId(), entity);
            typeCache.put(entity.getUniqueId(), entity.getType());
        }
    }

    @EventHandler
    public void mobKilled(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        UUID id = entity.getUniqueId();

        if (stack.containsKey(id)) {
            int curStack = stack.get(id) - 1;

            if (curStack > 0) {
                EntityType type = typeCache.get(id);

                String name = toProperCase(type.name());

                Entity newEntity = entity.getWorld().spawnEntity(event.getEntity().getLocation(), type);
                newEntity.setCustomName(curStack + " " + name + (curStack > 1 ? "s" : ""));
                newEntity.setCustomNameVisible(true);
                UUID newId = newEntity.getUniqueId();

                stackCache.put(newId, newEntity);
                stack.put(newId, curStack);
                typeCache.put(newId, type);
            }

            stack.remove(id);
            stackCache.remove(id);
            typeCache.remove(id);
        }
    }

    private static final String toProperCase(String str) {
        str = str.toLowerCase();
        char firstLetter = str.toUpperCase().toCharArray()[0];

        return firstLetter + str.substring(1);
    }

    public void disable() {
        EntitySpawnEvent.getHandlerList().unregister(this);
        EntityDeathEvent.getHandlerList().unregister(this);
    }
}
