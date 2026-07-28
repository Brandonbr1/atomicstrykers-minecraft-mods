package atomicstryker.infernalmobs.common.modifiers;

import javax.annotation.Nullable;

import atomicstryker.infernalmobs.common.InfernalMobsCore;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class MM_Fiery extends MobModifier {

    private static final List<Class<?>> bannedClasses = new ArrayList<>();

    private static final String[] suffix = { "ofConflagration", "thePhoenix", "ofCrispyness" };
    private static final String[] prefix = { "burning", "toasting" };
    private static int fireDuration;

    public MM_Fiery(@Nullable MobModifier next) {
        super("Fiery", next);
    }

    @Override
    public float onHurt(EntityLivingBase mob, DamageSource source, float damage) {
        if (source.getEntity() != null && (source.getEntity() instanceof EntityLivingBase)
            && !InfernalMobsCore.instance().isRangedProjectile(source)) {
            source.getEntity()
                .setFire(fireDuration);
        }

        mob.extinguish();
        return super.onHurt(mob, source, damage);
    }

    @Override
    public float onAttack(EntityLivingBase entity, DamageSource source, float damage) {
        if (entity != null) {
            entity.setFire(fireDuration);
        }

        return super.onAttack(entity, source, damage);
    }

    @Override
    protected String[] getModNameSuffix() {
        return suffix;
    }

    @Override
    protected String[] getModNamePrefix() {
        return prefix;
    }

    @Override
    public Class<?>[] getBlackListMobClasses() {
        return bannedClasses.toArray(new Class<?>[0]);
    }


    public static class Loader extends ModifierLoader<MM_Fiery> {

        public Loader() {
            super(MM_Fiery.class);
        }

        @Override
        public MM_Fiery make(@Nullable MobModifier next) {
            return new MM_Fiery(next);
        }

        @Override
        public void loadConfig(Configuration config) {
            fireDuration = config.get(getModifierClassName(), "fieryDurationSecs", 3L, "Time attacker is set on fire")
                .getInt(3);
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
