package atomicstryker.infernalmobs.common.modifiers;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class MM_Unyielding extends MobModifier {
    private static final List<Class<?>> bannedClasses = new ArrayList<>();

    private static final String[] suffix = { "ofRelentlessness", "theUnYielding", "theUnstoppable" };
    private static final String[] prefix = { "relentless", "unyielding", "unstoppable" };

    public MM_Unyielding(@Nullable MobModifier next) {
        super("Unyielding", next);
    }

    @Override
    public float onHurt(EntityLivingBase mob, DamageSource source, float amount) {
        mob.getEntityAttribute(SharedMonsterAttributes.knockbackResistance)
            .setBaseValue(Double.MAX_VALUE);

        return super.onHurt(mob, source, amount);
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


    public static class Loader extends ModifierLoader<MM_Unyielding> {

        public Loader() {
            super(MM_Unyielding.class);
        }

        @Override
        public MM_Unyielding make(@Nullable MobModifier next) {
            return new MM_Unyielding(next);
        }

        @Override
        public void loadConfig(Configuration config) {
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
