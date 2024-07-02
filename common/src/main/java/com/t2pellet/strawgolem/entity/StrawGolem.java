package com.t2pellet.strawgolem.entity;

import com.t2pellet.strawgolem.StrawgolemConfig;
import com.t2pellet.strawgolem.entity.animations.StrawgolemArmsController;
import com.t2pellet.strawgolem.entity.animations.StrawgolemHarvestController;
import com.t2pellet.strawgolem.entity.animations.StrawgolemMovementController;
import com.t2pellet.strawgolem.entity.capabilities.decay.Decay;
import com.t2pellet.strawgolem.entity.capabilities.decay.DecayState;
import com.t2pellet.strawgolem.entity.capabilities.deliverer.Deliverer;
import com.t2pellet.strawgolem.entity.capabilities.harvester.Harvester;
import com.t2pellet.strawgolem.entity.capabilities.held_item.HeldItem;
import com.t2pellet.strawgolem.entity.capabilities.tether.Tether;
import com.t2pellet.strawgolem.entity.goals.golem.*;
import com.t2pellet.strawgolem.registry.StrawgolemItems;
import com.t2pellet.strawgolem.registry.StrawgolemParticles;
import com.t2pellet.strawgolem.registry.StrawgolemSounds;
import com.t2pellet.tlib.Services;
import com.t2pellet.tlib.entity.capability.api.CapabilityManager;
import com.t2pellet.tlib.entity.capability.api.ICapabilityHaver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StemGrownBlock;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

// TODO : Fix bug - not always walking fully to destination
public class StrawGolem extends AbstractGolem implements IAnimatable, ICapabilityHaver {

    public static final Item REPAIR_ITEM = BuiltInRegistries.ITEM.get(new ResourceLocation(StrawgolemConfig.Lifespan.repairItem.get()));
    public static final Item BARREL_ITEM = BuiltInRegistries.ITEM.get(new ResourceLocation(StrawgolemConfig.Lifespan.barrelItem.get()));
    private static final double WALK_DISTANCE = 0.00000001D;
    private static final double RUN_DISTANCE = 0.003D;

    // Synched Data
    private static final EntityDataAccessor<Boolean> IS_SCARED = SynchedEntityData.defineId(StrawGolem.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_HAT = SynchedEntityData.defineId(StrawGolem.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> BARREL_HEALTH = SynchedEntityData.defineId(StrawGolem.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HARVESTING_ITEM = SynchedEntityData.defineId(StrawGolem.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HARVESTING_BLOCK = SynchedEntityData.defineId(StrawGolem.class, EntityDataSerializers.BOOLEAN);

    // Capabilities
    CapabilityManager capabilities = CapabilityManager.newInstance(this);
    private final Decay decay;
    private final HeldItem heldItem;
    private final Harvester harvester;
    private final Deliverer deliverer;
    private final Tether tether;

    // Misc
    private boolean isFirstTick = true;

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.23)
                .add(Attributes.MAX_HEALTH, StrawgolemConfig.Lifespan.baseHealth.get());
    }

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public StrawGolem(EntityType<? extends StrawGolem> type, Level level) {
        super(type, level);
        decay = capabilities.addCapability(Decay.class);
        heldItem = capabilities.addCapability(HeldItem.class);
        harvester = capabilities.addCapability(Harvester.class);
        deliverer = capabilities.addCapability(Deliverer.class);
        tether = capabilities.addCapability(Tether.class);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_SCARED, false);
        this.entityData.define(HAS_HAT, false);
        this.entityData.define(BARREL_HEALTH, 0);
        this.entityData.define(HARVESTING_ITEM, false);
        this.entityData.define(HARVESTING_BLOCK, false);
    }

    /* AI */

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new GolemFleeEntityGoal<>(this, Monster.class, 8.0F, true));
        this.goalSelector.addGoal(1, new GolemFleeEntityGoal<>(this, Evoker.class, 12.0F,  true));
        this.goalSelector.addGoal(1, new GolemFleeEntityGoal<>(this, Vindicator.class, 8.0F,  true));
        this.goalSelector.addGoal(1, new GolemFleeEntityGoal<>(this, Vex.class, 8.0F,  true));
        this.goalSelector.addGoal(1, new GolemFleeEntityGoal<>(this, Raider.class, 15.0F,  true));
        this.goalSelector.addGoal(1, new GolemFleeEntityGoal<>(this, Illusioner.class, 12.0F, true));
        this.goalSelector.addGoal(1, new GolemFleeEntityGoal<>(this, Sheep.class, 8.0F,  false));
        this.goalSelector.addGoal(1, new GolemFleeEntityGoal<>(this, Cow.class, 8.0F,  false));
        this.goalSelector.addGoal(1, new GolemPanicGoal(this));
        this.goalSelector.addGoal(2, new GolemTemptGoal(this));
        this.goalSelector.addGoal(2, new GolemBeShyGoal(this));
        this.goalSelector.addGoal(3, new HarvestCropGoal(this));
        this.goalSelector.addGoal(3, new DeliverCropGoal(this));
        this.goalSelector.addGoal(5, new ReturnToTetherGoal(this));
        this.goalSelector.addGoal(6, new GolemWanderGoal(this));
        if (Services.PLATFORM.isModLoaded("animal_feeding_trough")) {
            this.goalSelector.addGoal(6, new GolemRepairSelfGoal(this, 24));
        }
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    /* Base Logic */

    @Override
    public void baseTick() {
        super.baseTick();
        if (level.isClientSide) baseClientTick();
        else baseServerTick();
        baseCommonTick();
    }



    private void baseClientTick() {
    }

    private void baseServerTick() {
        getDecay().decay();
        if (isInWaterOrRain()) {
            if (isInWater()) {
                if (StrawgolemConfig.Lifespan.waterAcceleratesDecay.get()) getDecay().decay();
            } else if (!hasHat()) {
                if (StrawgolemConfig.Lifespan.rainAcceleratesDecay.get()) getDecay().decay();
            }
        }
        // When first loaded scan the area
        if (isFirstTick) {
            getHarvester().findHarvestables();
            isFirstTick = false;
        }
    }

    private void baseCommonTick() {
        if (getDecay().getState() == DecayState.DYING && getRandom().nextInt(StrawgolemConfig.Visual.dyingGolemFlyChance.get()) == 0) {
            spawnFlyParticle();
        }
    }

    @Override
    public void teleportTo(double x, double y, double z) {
        super.teleportTo(x, y, z);
        getTether().update(new BlockPos(x, y, z));
        getHarvester().clearHarvest();
        getHarvester().clearQueue();
        getHarvester().findHarvestables();
    }

    @Override
    public void moveTo(double $$0, double $$1, double $$2, float $$3, float $$4) {
        super.moveTo($$0, $$1, $$2, $$3, $$4);
        // Initially set tether
        if (!tether.exists()) {
            tether.update();
        }
    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (item.getItem() == REPAIR_ITEM && decay.getState() != DecayState.NEW) {
            boolean success = decay.repair();
            if (success) {
                spawnHappyParticle();
                item.shrink(1);
                playSound(StrawgolemSounds.GOLEM_HEAL.get());
            }
            return InteractionResult.SUCCESS;
        } else if (item.getItem() == StrawgolemItems.strawHat.get() && !hasHat()) {
            this.entityData.set(HAS_HAT, true);
            item.shrink(1);
            return InteractionResult.SUCCESS;
        } else if (item.getItem() == Items.BARREL && !hasBarrel()) {
            entityData.set(BARREL_HEALTH, StrawgolemConfig.Lifespan.barrelDurability.get());
            item.shrink(1);
            return InteractionResult.SUCCESS;
        } else if (item.getItem() == BARREL_ITEM && hasBarrel()) {
            boolean success = repairBarrel();
            if (success) {
                item.shrink(1);
                playSound(SoundEvents.ARMOR_EQUIP_LEATHER);
            }
        } else if (hand == InteractionHand.MAIN_HAND && item.isEmpty() && player.isCrouching()) {
            StrawGolemOrderer orderer = (StrawGolemOrderer) (Object) player;
            if (orderer.getOrderedGolem().isPresent() && orderer.getOrderedGolem().get().getId() == getId()) {
                player.displayClientMessage(Component.translatable("strawgolem.ordering.stop"), true);
                orderer.setOrderedGolem(null);
            } else {
                player.displayClientMessage(Component.translatable("strawgolem.ordering.start"), true);
                orderer.setOrderedGolem(this);
            }
        }
        return super.mobInteract(player, hand);
    }

    /* Damage */

    @Override
    public boolean isDamageSourceBlocked(DamageSource source) {
        if (source == DamageSource.SWEET_BERRY_BUSH) return true;
        if (hasBarrel()) return true;
        return super.isDamageSourceBlocked(source);
    }

    @Override
    public void hurtCurrentlyUsedShield(float amount) {
        int durability = entityData.get(BARREL_HEALTH);
        int newDurability = Math.round(Math.max(durability - amount, 0));
        entityData.set(BARREL_HEALTH, newDurability);
        playSound(newDurability <= 0 ? SoundEvents.SHIELD_BREAK : SoundEvents.SHIELD_BLOCK);
    }

    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        super.actuallyHurt(source, amount);
        decay.setFromHealth();
    }

    /* Items */

    @Override
    protected void dropCustomDeathLoot(DamageSource $$0, int $$1, boolean $$2) {
        spawnAtLocation(getHeldItem().get().copy());
        getHeldItem().get().setCount(0);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) return heldItem.get();
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            heldItem.set(stack);
        }
    }

    /* Animations */

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new StrawgolemMovementController(this));
        data.addAnimationController(new StrawgolemArmsController(this));
        data.addAnimationController(new StrawgolemHarvestController(this));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private double getSqrMovement() {
        double xDiff = getX() - xOld;
        double zDiff = getZ() - zOld;
        return xDiff * xDiff + zDiff * zDiff;
    }

    public boolean isRunning() {
        return getSqrMovement() >= RUN_DISTANCE;
    }

    public boolean isMoving() {
        return getSqrMovement() >= WALK_DISTANCE;
    }

    public boolean isPickingUpItem() {
        return entityData.get(HARVESTING_ITEM);
    }

    public boolean isPickingUpBlock() {
        return entityData.get(HARVESTING_BLOCK);
    }

    public void setPickingUpItem(boolean isPickingUpItem) {
        entityData.set(HARVESTING_ITEM, isPickingUpItem);
    }

    public void setPickingUpBlock(boolean isPickingUpBlock) {
        entityData.set(HARVESTING_BLOCK, isPickingUpBlock);
    }

    public boolean isInCold() {
        return level.getBiome(blockPosition()).value().getBaseTemperature() < 0.15F;
    }

    public boolean isScared() {
        return entityData.get(IS_SCARED);
    }

    public void setScared(boolean isScared) {
        this.entityData.set(IS_SCARED, isScared);
    }

    public boolean hasBarrel() {
     return entityData.get(BARREL_HEALTH) > 0;
    }

    public int getBarrelHealth() {
        return entityData.get(BARREL_HEALTH);
    }

    public boolean repairBarrel() {
        int health = entityData.get(BARREL_HEALTH);
        int amount = StrawgolemConfig.Lifespan.barrelRepairAmount.get();
        if (health >= StrawgolemConfig.Lifespan.barrelDurability.get()) {
            return false;
        }
        if (amount == 0) {
            return false;
        }
        int newHealth = Math.min(health + amount, StrawgolemConfig.Lifespan.barrelDurability.get());
        entityData.set(BARREL_HEALTH, newHealth);
        return true;
    }

    /* Capabilities */

    @Override
    public CapabilityManager getCapabilityManager() {
        return capabilities;
    }

    public Decay getDecay() {
        return decay;
    }

    public HeldItem getHeldItem() {
        return heldItem;
    }

    public Harvester getHarvester() {
        return harvester;
    }

    public Deliverer getDeliverer() {
        return deliverer;
    }

    public Tether getTether() {
        return tether;
    }

    public boolean hasHat() {
        return this.entityData.get(HAS_HAT);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        // Hat!
        this.entityData.set(HAS_HAT, tag.getBoolean("hasHat"));
        // Barrel!
        this.entityData.set(BARREL_HEALTH, tag.getInt("barrelHealth"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("hasHat", this.hasHat());
        tag.putInt("barrelHealth", this.entityData.get(BARREL_HEALTH));
        super.addAdditionalSaveData(tag);
    }

    /* Ambience */

    @Override
    protected SoundEvent getAmbientSound() {
        if (isRunningGoal(PanicGoal.class, AvoidEntityGoal.class)) return StrawgolemSounds.GOLEM_SCARED.get();
        if (isHoldingBlock()) return StrawgolemSounds.GOLEM_STRAINED.get();
        return StrawgolemSounds.GOLEM_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return StrawgolemSounds.GOLEM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return StrawgolemSounds.GOLEM_DEATH.get();
    }

    /* Helpers */

    public boolean shouldHoldAboveHead() {
        if (hasBarrel() && !getMainHandItem().isEmpty()) return true;
        return isHoldingBlock();
    }

    public boolean isHoldingBlock() {
        Item item = heldItem.get().getItem();
        return item instanceof BlockItem blockItem && blockItem.getBlock() instanceof StemGrownBlock;
    }

    @SafeVarargs
    public final boolean isRunningGoal(Class<? extends Goal>... classes) {
        return goalSelector.getRunningGoals().anyMatch(goal -> {
            for (Class<? extends Goal> clazz : classes) {
                if (clazz.isInstance(goal.getGoal())) return true;
            }
            return false;
        });
    }

    private void spawnFlyParticle() {
        Vec3 pos = position();
        Vec3 movement = getDeltaMovement();
        level.addParticle(StrawgolemParticles.FLY_PARTICLE.get(), pos.x, pos.y + 0.15F, pos.z, movement.x, movement.y + 0.15F, movement.z);
    }

    private void spawnHappyParticle() {
        Vec3 pos = position();
        Vec3 movement = getDeltaMovement();
        double x = random.nextFloat() + pos.x - 0.5F;
        double z = random.nextFloat() + pos.z - 0.5F;
        level.addParticle(ParticleTypes.HAPPY_VILLAGER, x, pos.y + 0.85F, z, movement.x, movement.y, movement.z);
    }

}
