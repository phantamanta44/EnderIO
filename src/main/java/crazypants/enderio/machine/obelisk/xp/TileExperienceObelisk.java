package crazypants.enderio.machine.obelisk.xp;

import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.enderio.core.api.common.util.ITankAccess;
import com.enderio.core.common.util.FluidUtil;

import crazypants.enderio.ModObject;
import crazypants.enderio.config.Config;
import crazypants.enderio.machine.AbstractMachineEntity;
import crazypants.enderio.machine.SlotDefinition;
import crazypants.enderio.network.PacketHandler;
import crazypants.enderio.xp.ExperienceContainer;
import crazypants.enderio.xp.IHaveExperience;
import crazypants.enderio.xp.PacketExperianceContainer;
import crazypants.enderio.xp.XpUtil;

@Storable
public class TileExperienceObelisk extends AbstractMachineEntity implements IFluidHandler, IHaveExperience, ITankAccess {

  @Store
  private ExperienceContainer xpCont = new ExperienceContainer(XpUtil.getExperienceForLevel(Config.xpObeliskMaxXpLevel));

  public TileExperienceObelisk() {
    super(new SlotDefinition(0, 0, 0));
  }

  @Override
  public String getMachineName() {
    return ModObject.blockExperienceObelisk.getUnlocalisedName();
  }

  @Override
  protected boolean isMachineItemValidForSlot(int i, ItemStack itemstack) {
    return false;
  }

  @Override
  public boolean isActive() {
    return true;
  }

  @Override
  protected boolean processTasks(boolean redstoneCheck) {
    if (xpCont.isDirty()) {
      PacketHandler.sendToAllAround(new PacketExperianceContainer(this), this);
      xpCont.setDirty(false);
    }
    return false;
  }

  @Override
  protected boolean doPull(EnumFacing dir) {
    boolean res = super.doPull(dir);
    FluidUtil.doPull(this, dir, Config.fluidConduitMaxIoRate);
    return res;
  }

  @Override
  protected boolean doPush(EnumFacing dir) {
    boolean res = super.doPush(dir);
    FluidUtil.doPush(this, dir, Config.fluidConduitMaxIoRate);
    return res;
  }

  @Override
  public boolean canFill(EnumFacing from, Fluid fluid) {
    return xpCont.canFill(from, fluid);
  }

  @Override
  public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
    return xpCont.fill(from, resource, doFill);
  }

  @Override
  public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
    return xpCont.drain(from, resource, doDrain);
  }

  @Override
  public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
    return xpCont.drain(from, maxDrain, doDrain);
  }

  @Override
  public boolean canDrain(EnumFacing from, Fluid fluid) {
    return xpCont.canDrain(from, fluid);
  }

  @Override
  public FluidTankInfo[] getTankInfo(EnumFacing from) {
    return xpCont.getTankInfo(from);
  }

  @Override
  public ExperienceContainer getContainer() {
    return xpCont;
  }

  @Override
  public FluidTank getInputTank(FluidStack forFluidType) {
    return xpCont;
  }

  @Override
  public FluidTank[] getOutputTanks() {
    return new FluidTank[] { xpCont };
  }

  @Override
  public void setTanksDirty() {
    xpCont.setDirty(true);
  }

}
