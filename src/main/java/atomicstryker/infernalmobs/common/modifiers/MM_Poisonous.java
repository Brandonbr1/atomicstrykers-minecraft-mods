package atomicstryker.infernalmobs.common.modifiers;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.common.config.Configuration;

import atomicstryker.infernalmobs.common.InfernalMobsCore;

import java.util.ArrayList;
import java.util.List;

public class MM_Poisonous extends MobModifier {

    private static final String[] suffix = { "ofVenom", "thedeadlyChalice" };
    private static final String[] prefix = { "poisonous", "stinging", "despoiling" };
    private static int potionDuration;
    private static final List<Class<?>> bannedClasses = new ArrayList<>();

    public MM_Poisonous(@Nullable MobModifier next) {
        super("Poisonous", next);
    }

    @Override
    public float onHurt(EntityLivingBase mob, DamageSource source, float damage) {
        if (source.getEntity() != null && (source.getEntity() instanceof EntityLivingBase)
            && InfernalMobsCore.instance()
                .getIsEntityAllowedTarget(source.getEntity())) {
            EntityLivingBase ent = (EntityLivingBase) source.getEntity();
            if (!ent.isPotionActive(Potion.poison) && !InfernalMobsCore.instance().isRangedProjectile(source)) {
                ent.addPotionEffect(new PotionEffect(Potion.poison.id, potionDuration, 0));
            }
        }

        return super.onHurt(mob, source, damage);
    }

    @Override
    public float onAttack(EntityLivingBase entity, DamageSource source, float damage) {
        if (entity != null && InfernalMobsCore.instance()
            .getIsEntityAllowedTarget(entity) && !entity.isPotionActive(Potion.poison)) {
            entity.addPotionEffect(new PotionEffect(Potion.poison.id, potionDuration, 0));
        }

        return super.onAttack(entity, source, damage);
    }

    @Override
    public Class<?>[] getBlackListMobClasses() {
        return bannedClasses.toArray(new Class<?>[0]);
    }

    @Override
    protected String[] getModNameSuffix() {
        return suffix;
    }

    @Override
    protected String[] getModNamePrefix() {
        return prefix;
    }

    public static class Loader extends ModifierLoader<MM_Poisonous> {

        public Loader() {
            super(MM_Poisonous.class);
        }

        @Override
        public MM_Poisonous make(@Nullable MobModifier next) {
            return new MM_Poisonous(next);
        }

        @Override
        public void loadConfig(Configuration config) {
            potionDuration = config
                .get(getModifierClassName(), "poisonDurationTicks", 120L, "Time attacker is poisoned")
                .getInt(120);
            String[] bannedClassString = config.getStringList("Disallowed Mob Classes", getModifierClassName(), new String[]{""}, "Fully Qualified Mob classes which can not have this effect.");
            try {
                for (int i = 0; i < bannedClassString.length; i++) {
                    Class<?> clazz = Class.forName(bannedClassString[i]);
                    bannedClasses.add(clazz);
                }
            } catch (Exception e) {

            }
        }
    }
}
