package com.grim3212.assorted.storage.common.util;

import net.minecraft.nbt.CompoundNBT;

public class StorageLockCode {
	public static final StorageLockCode EMPTY_CODE = new StorageLockCode("");
	private final String lock;

	public StorageLockCode(String code) {
		this.lock = code;
	}

	public String getLockCode() {
		return lock;
	}

	public void write(CompoundNBT nbt) {
		if (!this.lock.isEmpty()) {
			nbt.putString("Storage_Lock", this.lock);
		}

	}

	public static StorageLockCode read(CompoundNBT nbt) {
		return nbt.contains("Storage_Lock", 8) ? new StorageLockCode(nbt.getString("Storage_Lock")) : EMPTY_CODE;
	}
}
