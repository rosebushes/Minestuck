package com.mraof.minestuck.computer.editmode;

import com.mraof.minestuck.alchemy.MutableGristSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public final class ClientDeployList
{
	private static final Logger LOGGER = LogManager.getLogger();
	
	static void load(CompoundTag nbt)
	{
		if(entryList == null)
			entryList = new ArrayList<>();
		else entryList.clear();
		ListTag list = nbt.getList("l", Tag.TAG_COMPOUND);
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag tag = list.getCompound(i);
			Entry entry = new Entry();
			entry.item = ItemStack.of(tag);
			entry.index = tag.getInt("i");
			
			entry.cost = MutableGristSet.CODEC.parse(NbtOps.INSTANCE, tag.get("cost")).getOrThrow(false, LOGGER::error);
			entry.category = DeployList.EntryLists.values()[tag.getInt("cat")];
			
			entryList.add(entry);
		}
	}
	
	private static List<Entry> entryList;
	
	public static Entry getEntry(ItemStack stack)
	{
		stack = DeployList.cleanStack(stack);
		for(Entry entry : entryList)
			if(ItemStack.matches(entry.item, stack))
				return entry;
		return null;
	}
	
	public static class Entry
	{
		private ItemStack item;
		private MutableGristSet cost;
		private int index;
		private DeployList.EntryLists category;
		
		public MutableGristSet getCost()
		{
			return cost;
		}
		
		public DeployList.EntryLists getCategory() { return category; }
		
		public int getIndex()
		{
			return index;
		}
		
	}
}