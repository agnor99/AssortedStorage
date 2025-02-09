package com.grim3212.assorted.storage.client.screen;

import com.grim3212.assorted.storage.AssortedStorage;
import com.grim3212.assorted.storage.common.inventory.LocksmithWorkbenchContainer;
import com.grim3212.assorted.storage.common.network.PacketHandler;
import com.grim3212.assorted.storage.common.network.SetLockPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LocksmithWorkbenchScreen extends AbstractContainerScreen<LocksmithWorkbenchContainer> implements MenuAccess<LocksmithWorkbenchContainer>, ContainerListener {

	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(AssortedStorage.MODID, "textures/gui/container/locksmith_workbench.png");
	private EditBox lockField;

	public LocksmithWorkbenchScreen(LocksmithWorkbenchContainer container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);

		this.passEvents = false;
	}

	@Override
	protected void init() {
		super.init();

		this.lockField = new EditBox(this.font, this.leftPos + 67, this.topPos + 39, 42, 12, Component.translatable(AssortedStorage.MODID + ".container.keycode"));
		this.lockField.setCanLoseFocus(false);
		this.lockField.setTextColor(-1);
		this.lockField.setTextColorUneditable(-1);
		this.lockField.setBordered(false);
		this.lockField.setMaxLength(10);
		this.lockField.setResponder(this::onNameChanged);
		this.addRenderableWidget(this.lockField);
		this.setInitialFocus(this.lockField);
	}

	private void onNameChanged(String lock) {
		String s = lock;
		this.menu.updateLock(s);
		PacketHandler.sendToServer(new SetLockPacket(s));
	}

	/**
	 * Sends the contents of an inventory slot to the client-side Container. This
	 * doesn't have to match the actual contents of that slot.
	 */
	public void slotChanged(AbstractContainerMenu containerToSend, int slotInd, ItemStack stack) {
		if (slotInd == 0) {
			String code = stack.getTag().contains("Storage_Lock", 8) ? stack.getTag().getString("Storage_Lock") : "";

			this.lockField.setValue(stack.isEmpty() ? "" : code);
			this.lockField.setEditable(!stack.isEmpty());
			this.setFocused(this.lockField);
		}

	}

	public void refreshContainer(AbstractContainerMenu containerToSend, NonNullList<ItemStack> itemsList) {
		this.slotChanged(containerToSend, 0, containerToSend.getSlot(0).getItem());
	}

	@Override
	public void resize(Minecraft minecraft, int width, int height) {
		String s = this.lockField.getValue();
		this.init(minecraft, width, height);
		this.lockField.setValue(s);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 256) {
			this.minecraft.player.closeContainer();
		}

		return !this.lockField.keyPressed(keyCode, scanCode, modifiers) && !this.lockField.canConsumeInput() ? super.keyPressed(keyCode, scanCode, modifiers) : true;
	}

	@Override
	public void removed() {
		super.removed();
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		RenderSystem.disableBlend();
		this.lockField.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
		RenderSystem.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, GUI_TEXTURE);
		int i = this.leftPos;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}

	@Override
	public void dataChanged(AbstractContainerMenu containerIn, int varToUpdate, int newValue) {
	}
}
