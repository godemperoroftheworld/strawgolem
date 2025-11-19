package com.t2pellet.strawgolem.common.entity.capabilities;

import com.t2pellet.haybale.common.capability.api.registry.IModCapabilities;
import com.t2pellet.strawgolem.common.entity.capabilities.decay.Decay;
import com.t2pellet.strawgolem.common.entity.capabilities.deliverer.Deliverer;
import com.t2pellet.strawgolem.common.entity.capabilities.harvester.Harvester;
import com.t2pellet.strawgolem.common.entity.capabilities.held_item.HeldItem;
import com.t2pellet.strawgolem.common.entity.capabilities.tether.Tether;

public class StrawgolemCapabilities implements IModCapabilities {

    @ICapability(Decay.class)
    public static final HaybaleCapability<Decay> decay = new HaybaleCapability<>(Decay::getInstance);
    @ICapability(HeldItem.class)
    public static final HaybaleCapability<HeldItem> heldItem = new HaybaleCapability<>(HeldItem::getInstance);
    @ICapability(Harvester.class)
    public static final HaybaleCapability<Harvester> harvester = new HaybaleCapability<>(Harvester::getInstance);
    @ICapability(Deliverer.class)
    public static final HaybaleCapability<Deliverer> deliverer = new HaybaleCapability<>(Deliverer::getInstance);
    @ICapability(Tether.class)
    public static final HaybaleCapability<Tether> tether = new HaybaleCapability<>(Tether::getInstance);
}
