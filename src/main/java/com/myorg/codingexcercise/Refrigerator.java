package com.myorg.codingexcercise;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
*
* You are about to build a Refrigerator which has SMALL, MEDIUM, and LARGE sized shelves.
*
* Method signature are given below. You need to implement the logic to
*
*  1. To keep track of items put in to the Refrigerator (add or remove)
*  2. Make sure enough space available before putting it in
*  3. Make sure space is used as efficiently as possible
*  4. Make sure code runs efficiently
*
*
* Created by kamoorr on 7/14/17.
*/
public class Refrigerator {

	/**
	 * Refrigerator Total Cubic Feet (CuFt)
	 */
	private int cubicFt;

	/**
	 * Large size shelf count and size of one shelf
	 */
	private int largeShelfCount;
	private int largeShelfCuFt;

	/**
	 * Medium size shelf count and size of one shelf
	 */
	private int mediumShelfCount;
	private int mediumShelfCuFt;

	/**
	 * Medium size shelf count and size of one shelf
	 */
	private int smallShelfCount;
	private int smallShelfCuFt;

	private ArrayList<Integer> largeShelfLeftSpace = new ArrayList<Integer>();
	private ArrayList<Integer> mediumShelfLeftSpace = new ArrayList<Integer>();
	private ArrayList<Integer> smallShelfLeftSpace = new ArrayList<Integer>();

	private Map<String, Item> items = new HashMap<String, Item>();

	private Map<String, Integer> itemIndex = new HashMap<String, Integer>();

	/**
	 * 
	 * Create a new refrigerator by specifying shelfSize and count for SMALL,
	 * MEDIUM, LARGE shelves
	 * 
	 * @param largeShelfCount
	 * @param largeShelfCuFt
	 * @param mediumShelfCount
	 * @param mediumShelfCuFt
	 * @param smallShelfCount0
	 *            * @param smallShelfCuFt
	 */
	public Refrigerator(int largeShelfCount, int largeShelfCuFt, int mediumShelfCount, int mediumShelfCuFt,
			int smallShelfCount, int smallShelfCuFt) {

		/**
		 * Calculating total cuft as local variable to improve performance. Assuming no
		 * vacant space in the refrigerator
		 * 
		 */
		this.cubicFt = (largeShelfCount * largeShelfCuFt) + (mediumShelfCount * mediumShelfCuFt)
				+ (smallShelfCount * smallShelfCuFt);

		this.largeShelfCount = largeShelfCount;
		this.largeShelfCuFt = largeShelfCuFt;
		init(largeShelfLeftSpace, largeShelfCuFt, largeShelfCount);

		this.mediumShelfCount = mediumShelfCount;
		this.mediumShelfCuFt = mediumShelfCuFt;
		init(mediumShelfLeftSpace, mediumShelfCuFt, mediumShelfCount);

		this.smallShelfCount = smallShelfCount;
		this.smallShelfCuFt = smallShelfCuFt;
		init(smallShelfLeftSpace, smallShelfCuFt, smallShelfCount);

	}

	/**
	 * Implement logic to put an item to this refrigerator. Make sure -- You have
	 * enough vacant space in the refrigerator -- Make this action efficient in a
	 * way to increase maximum utilization of the space, re-arrange items when
	 * necessary
	 * 
	 * Return true if put is successful false if put is not successful, for example,
	 * if you don't have enough space any shelf, even after re-arranging
	 * 
	 * 
	 * @param item
	 */
	public boolean put(Item item) {

		// Large
		if (item.getCubicFt() > mediumShelfCuFt && item.getCubicFt() <= largeShelfCuFt) {
			return putLargeItem(item);
		} else if (item.getCubicFt() > smallShelfCuFt && item.getCubicFt() <= mediumShelfCuFt) {// Medium
			return putMediumItem(item);
		} else if (item.getCubicFt() <= smallShelfCuFt) { // Small
			return putSmalltem(item);
		}

		return false;
	}

	/**
	 * remove and return the requested item Return null when not available
	 * 
	 * @param string
	 * @return
	 */
	public Item get(String string) {
		Item i = items.remove(string);
		if (i != null) {
			int index = itemIndex.get(i.getItemId());
			if (index < largeShelfCount) {
				largeShelfLeftSpace.set(index, largeShelfLeftSpace.get(index) + i.getCubicFt());
			} else if (index < largeShelfCount + mediumShelfCount) {
				int idx = index - largeShelfCount;
				mediumShelfLeftSpace.set(idx, mediumShelfLeftSpace.get(idx) + i.getCubicFt());
			} else if (index < largeShelfCount + mediumShelfCount + smallShelfCount) {
				int idx = index - (largeShelfCount + mediumShelfCount);
				smallShelfLeftSpace.set(idx, smallShelfLeftSpace.get(idx) + i.getCubicFt());
			}
		}
		return i;
	}

	/**
	 * Return current utilization of the space
	 * 
	 * @return
	 */
	public float getUtilizationPercentage() {
		return (getUsedSpace() / cubicFt) * 100;
	}

	/**
	 * Return current utilization in terms of cuft
	 * 
	 * @return
	 */
	public int getUsedSpace() {
		int usedSpace = 0;
		for (Item i : items.values()) {
			usedSpace = usedSpace + i.getCubicFt();
		}
		return usedSpace;
	}

	public boolean putLargeItem(Item item) {
		for (int i = 0; i < largeShelfCount; i++) {
			if (item.getCubicFt() <= largeShelfLeftSpace.get(i)) {
				items.put(item.getItemId(), item);
				largeShelfLeftSpace.set(i, largeShelfLeftSpace.get(i) - item.getCubicFt());
				itemIndex.put(item.getItemId(), i);
				return true;
			}
		}
		return false;
	}

	// Large
	// Med
	// Small

	public boolean putMediumItem(Item item) {
		for (int i = 0; i < mediumShelfCount; i++) {
			if (item.getCubicFt() <= mediumShelfLeftSpace.get(i)) {
				items.put(item.getItemId(), item);
				mediumShelfLeftSpace.set(i, mediumShelfLeftSpace.get(i) - item.getCubicFt());
				itemIndex.put(item.getItemId(), largeShelfCount + i);
				return true;
			}
		}
		return putLargeItem(item);
	}

	public boolean putSmalltem(Item item) {
		for (int i = 0; i < smallShelfCount; i++) {
			if (item.getCubicFt() <= smallShelfLeftSpace.get(i)) {
				items.put(item.getItemId(), item);
				smallShelfLeftSpace.set(i, smallShelfLeftSpace.get(i) - item.getCubicFt());
				itemIndex.put(item.getItemId(), largeShelfCount + mediumShelfCount + i);
				return true;
			}
		}
		return putMediumItem(item);
	}

	private void init(ArrayList<Integer> spaceList, int initialSpace, int count) {
		for (int i = 0; i < count; i++) {
			spaceList.add(initialSpace);
		}
	}
}
