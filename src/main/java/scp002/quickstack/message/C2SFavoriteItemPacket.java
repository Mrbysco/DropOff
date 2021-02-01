package scp002.quickstack.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SFavoriteItemPacket {

  /**
   * Leave public default constructor for Netty.
   */
  public C2SFavoriteItemPacket() {}

  public C2SFavoriteItemPacket(PacketBuffer buf){
    slotId = buf.readInt();
  }

  public C2SFavoriteItemPacket(int slotId) {this.slotId = slotId;}

  public void encode(PacketBuffer buf) {
    buf.writeInt(slotId);
  }


  int slotId;

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    ServerPlayerEntity player = ctx.get().getSender();
    Slot slot = player.openContainer.getSlot(slotId);
    ItemStack stack = slot.getStack();
    boolean alreadyFavorited = stack.hasTag() && stack.getTag().contains("favorite");
    if (alreadyFavorited) {
      //kill empty tags
      stack.getTag().remove("favorite");
      if (stack.getTag().isEmpty()) {
        stack.setTag(null);
      }
    } else {
      stack.getOrCreateTag().putBoolean("favorite",true);
    }
    ctx.get().setPacketHandled(true);
  }
}

