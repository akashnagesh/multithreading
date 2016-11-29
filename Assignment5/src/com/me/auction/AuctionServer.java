package com.me.auction;

/**
 *  @author akash
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AuctionServer {
	/**
	 * Singleton: the following code makes the server a Singleton. You should
	 * not edit the code in the following noted section.
	 * 
	 * For test purposes, we made the constructor protected.
	 */
	final static Logger logger = Logger.getLogger(AuctionServer.class.getSimpleName());

	/* Singleton: Begin code that you SHOULD NOT CHANGE! */
	protected AuctionServer() {
	}

	private static AuctionServer instance = new AuctionServer();

	public static AuctionServer getInstance() {
		return instance;
	}

	/* Singleton: End code that you SHOULD NOT CHANGE! */

	/*
	 * Statistic variables and server constants: Begin code you should likely
	 * leave alone.
	 */

	/**
	 * Server statistic variables and access methods:
	 */
	private int soldItemsCount = 0;
	private int revenue = 0;

	public int soldItemsCount() {
		return this.soldItemsCount;
	}

	public int revenue() {
		return this.revenue;
	}

	/**
	 * Server restriction constants:
	 */
	public static final int maxBidCount = 10; // The maximum number of bids at
												// any given time for a buyer.
	public static final int maxSellerItems = 20; // The maximum number of items
													// that a seller can submit
													// at any given time.
	public static final int serverCapacity = 80; // The maximum number of active
													// items at a given time.

	/*
	 * Statistic variables and server constants: End code you should likely
	 * leave alone.
	 */

	/**
	 * Some variables we think will be of potential use as you implement the
	 * server...
	 */

	// List of items currently up for bidding (will eventually remove things
	// that have expired).
	private List<Item> itemsUpForBidding = new ArrayList<Item>();

	// The last value used as a listing ID. We'll assume the first thing added
	// gets a listing ID of 0.
	private int lastListingID = -1;

	// List of item IDs and actual items. This is a running list with everything
	// ever added to the auction.
	final private HashMap<Integer, Item> itemsAndIDs = new HashMap<Integer, Item>();

	// List of itemIDs and the highest bid for each item. This is a running list
	// with everything ever added to the auction.
	final private HashMap<Integer, Integer> highestBids = new HashMap<Integer, Integer>();

	// List of itemIDs and the person who made the highest bid for each item.
	// This is a running list with everything ever bid upon.
	final private HashMap<Integer, String> highestBidders = new HashMap<Integer, String>();

	// List of sellers and how many items they have currently up for bidding.
	final private HashMap<String, Integer> itemsPerSeller = new HashMap<String, Integer>();

	// List of buyers and how many items on which they are currently bidding.
	private HashMap<String, Integer> itemsPerBuyer = new HashMap<String, Integer>();

	private Map<String, Integer> itemsPerSellerOverPriced = new HashMap<>();

	private Map<String, Integer> itemsPerBidderUnderPriced = new HashMap<>();

	// Object used for instance synchronization if you need to do it at some
	// point
	// since as a good practice we don't use synchronized (this) if we are doing
	// internal
	// synchronization.
	//
	// private Object instanceLock = new Object();

	final private Object lockForSubmitItem = new Object();
	final private Object lockForSubmitBid = new Object();

	/*
	 * The code from this point forward can and should be changed to correctly
	 * and safely implement the methods as needed to create a working
	 * multi-threaded server for the system. If you need to add Object instances
	 * here to use for locking, place a comment with them saying what they
	 * represent. Note that if they just represent one structure then you should
	 * probably be using that structure's intrinsic lock.
	 */

	/**
	 * Attempt to submit an <code>Item</code> to the auction
	 * 
	 * @param sellerName
	 *            Name of the <code>Seller</code>
	 * @param itemName
	 *            Name of the <code>Item</code>
	 * @param lowestBiddingPrice
	 *            Opening price
	 * @param biddingDurationMs
	 *            Bidding duration in milliseconds
	 * @return A positive, unique listing ID if the <code>Item</code> listed
	 *         successfully, otherwise -1
	 * 
	 *         preconditions: the lowestBiddingPrice cannot be a negative number
	 *         The biddingDuration cannot be a negative number there should be
	 *         space in the {@code AuctionServer} The Seller should not have
	 *         reached his maximum limit. postconditions:The size of the auction
	 *         server is incremented by 1 The Seller is added to the list(for
	 *         new sellers) or the number of items for this seller is
	 *         incremented by 1 Invariants :The
	 */
	public int submitItem(String sellerName, String itemName, int lowestBiddingPrice, int biddingDurationMs) {
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		// Make sure there's room in the auction site.
		// If the seller is a new one, add them to the list of sellers.
		// If the seller has too many items up for bidding, don't let them add
		// this one.
		// Don't forget to increment the number of things the seller has
		// currently listed.

		synchronized (lockForSubmitItem) {
			if (itemsPerSellerOverPriced.containsKey(sellerName) && itemsPerSellerOverPriced.get(sellerName) >= 3) {
				return -1;
			}
			if(lowestBiddingPrice<0 && lowestBiddingPrice >99)
			{
				if (itemsPerSellerOverPriced.containsKey(sellerName)) {
					final int numberOfItmes = itemsPerSellerOverPriced.get(sellerName);
					itemsPerSellerOverPriced.put(sellerName, numberOfItmes + 1);
				} else {
					itemsPerSellerOverPriced.put(sellerName, 1);
				}
				return -1;
			}
			if (itemsUpForBidding.size() >= serverCapacity) {
				// logger.log(Level.WARNING, "Server size full. Cannot submit
				// items.");
				return -1;
			}
			if (itemsPerSeller.containsKey(sellerName)) {
				final int numberOfItems = itemsPerSeller.get(sellerName);
				if (numberOfItems >= maxSellerItems) {
					// logger.log(Level.WARNING, "Seller capacity reached.
					// Cannot submit items.");
					return -1;
				}
			}
			++lastListingID;

			final Item item = new Item(sellerName, itemName, lastListingID, lowestBiddingPrice, biddingDurationMs);
			itemsUpForBidding.add(item);
			if (itemsPerSeller.containsKey(sellerName)) {
				final int itemsForthisSeller = itemsPerSeller.get(sellerName);
				itemsPerSeller.put(sellerName, itemsForthisSeller + 1);
			} else {
				itemsPerSeller.put(sellerName, 1);
			}

			itemsAndIDs.put(lastListingID, item);
			// logger.log(Level.FINE, "Item submitted for bid");
			return lastListingID;
		}
	}

	/**
	 * Get all <code>Items</code> active in the auction
	 * 
	 * @return A copy of the <code>List</code> of <code>Items</code>
	 * @throws InterruptedException
	 */
	public List<Item> getItems() throws InterruptedException {
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		// Don't forget that whatever you return is now outside of your control.
		/**
		 * Create a new ARRAYLIST
		 * 
		 * AcquireLock<lockForitemsUpForBidding> Add the items in this list to
		 * new arraylist Release lock return the new arraylist
		 */
		synchronized (lockForSubmitItem) {
			return new ArrayList<Item>(itemsUpForBidding);
		}
	}

	/**
	 * Attempt to submit a bid for an <code>Item</code>
	 * 
	 * @param bidderName
	 *            Name of the <code>Bidder</code>
	 * @param listingID
	 *            Unique ID of the <code>Item</code>
	 * @param biddingAmount
	 *            Total amount to bid
	 * @return True if successfully bid, false otherwise
	 */
	public boolean submitBid(String bidderName, int listingID, int biddingAmount) {
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		// See if the item exists.
		// See if it can be bid upon.
		// See if this bidder has too many items in their bidding list.
		// Get current bidding info.
		// See if they already hold the highest bid.
		// See if the new bid isn't better than the existing/opening bid floor.
		// Decrement the former winning bidder's count
		// Put your bid in place

		synchronized (lockForSubmitItem) {
			boolean isItemInBid = itemsUpForBidding.stream().filter(i -> i.listingID() == listingID).findAny()
					.isPresent();
			Item item = itemsAndIDs.get(listingID);
			if (!isItemInBid) {
				// logger.log(Level.WARNING, "Item not present");
				return false;
			}
		}
		synchronized (lockForSubmitBid) {
			if (itemsPerBidderUnderPriced.containsKey(bidderName) && itemsPerBidderUnderPriced.get(bidderName) >= 3) {
				return false;
			}
			Item item;

			item = itemsAndIDs.get(listingID);
			if (item != null) {
				if (!item.biddingOpen()) {
					// logger.log(Level.WARNING, "Item not open for bidding");
					return false;
				}
				if (item.lowestBiddingPrice() > biddingAmount) {
					if (itemsPerBidderUnderPriced.containsKey(bidderName)) {
						int itemCount = itemsPerBidderUnderPriced.get(bidderName);
						itemsPerBidderUnderPriced.put(bidderName, itemCount + 1);
					} else {
						itemsPerBidderUnderPriced.put(bidderName, 1);
					}
					return false;
				}
				if (itemsPerBuyer.get(bidderName) != null && itemsPerBuyer.get(bidderName) >= maxBidCount) {
					// logger.log(Level.WARNING, "Bidder max capacity
					// reached.");
					return false;
				}
				if (highestBids.get(item.listingID()) != null && highestBids.get(item.listingID()) >= biddingAmount) {
					// logger.log(Level.WARNING, "Bidding amount is less than
					// the current highest bids.");
					return false;
				}
				if (highestBidders.get(item.listingID()) != null
						&& highestBidders.get(item.listingID()).equals(bidderName)) {
					// logger.log(Level.WARNING, "Bidder already owns the
					// highest bid");
					return false;
				}
				if (highestBids.containsKey(listingID)) {
					String formerBidderName = highestBidders.get(item.listingID());
					int formerBidderItemCount = itemsPerBuyer.get(formerBidderName);
					itemsPerBuyer.put(formerBidderName, formerBidderItemCount - 1);
				}

				highestBidders.put(item.listingID(), bidderName);
				highestBids.put(item.listingID(), biddingAmount);
				if (highestBidders.containsKey(bidderName)) {
					int numberOfBidsForBuyer = itemsPerBuyer.get(bidderName);
					itemsPerBuyer.put(bidderName, numberOfBidsForBuyer + 1);
					// logger.log(Level.INFO, "Bid placed successfully");
					return true;
				} else {
					itemsPerBuyer.put(bidderName, 1);
					return true;
				}

			}
		}
		return false;
	}

	/**
	 * Check the status of a <code>Bidder</code>'s bid on an <code>Item</code>
	 * 
	 * @param bidderName
	 *            Name of <code>Bidder</code>
	 * @param listingID
	 *            Unique ID of the <code>Item</code>
	 * @return 1 (success) if bid is over and this <code>Bidder</code> has
	 *         won<br>
	 *         2 (open) if this <code>Item</code> is still up for auction<br>
	 *         3 (failed) If this <code>Bidder</code> did not win or the
	 *         <code>Item</code> does not exist
	 */
	public int checkBidStatus(String bidderName, int listingID) {
		// TODO: IMPLEMENT CODE HERE
		// Some reminders:
		// If the bidding is closed, clean up for that item.
		// Remove item from the list of things up for bidding.
		// Decrease the count of items being bid on by the winning bidder if
		// there was any...
		// Update the number of open bids for this seller

		synchronized (lockForSubmitItem) {
			Item item = itemsAndIDs.get(listingID);
			if (item == null)
				return 3;
			if (item.biddingOpen())
				return 2;
			itemsUpForBidding.remove(item);
		}
		synchronized (lockForSubmitBid) {
			if (highestBidders.get(listingID) == null || !highestBidders.get(listingID).equals(bidderName)) {
				return 3;
			}
			String winner = highestBidders.get(listingID);
			int itemsForBidder = itemsPerBuyer.get(winner);
			itemsPerBuyer.put(winner, itemsForBidder - 1);
			synchronized (lockForSubmitItem) {
				Item item = itemsAndIDs.get(listingID);
				String seller = item.seller();
				int numberOfItemsForThisSeller = itemsPerSeller.get(seller);
				itemsPerSeller.put(seller, numberOfItemsForThisSeller - 1);
				soldItemsCount = soldItemsCount + 1;
				int soldPrice = highestBids.get(listingID);
				revenue = revenue + soldPrice;
				return 1;
			}
		}
	}

	/**
	 * Check the current bid for an <code>Item</code>
	 * 
	 * @param listingID
	 *            Unique ID of the <code>Item</code>
	 * @return The highest bid so far or the opening price if no bid has been
	 *         made, -1 if no <code>Item</code> exists
	 */
	public int itemPrice(int listingID) {

		synchronized (lockForSubmitItem) {
			if (!itemsAndIDs.containsKey(listingID))
				return -1;
		}
		synchronized (lockForSubmitBid) {
			if (!highestBids.containsKey(listingID)) {
				synchronized (lockForSubmitItem) {
					Item item = itemsAndIDs.get(listingID);
					return item.lowestBiddingPrice();
				}
			} else {
				return highestBids.get(listingID);
			}
		}
	}

	/**
	 * Check whether an <code>Item</code> has been bid upon yet
	 * 
	 * @param listingID
	 *            Unique ID of the <code>Item</code>
	 * @return True if there is no bid or the <code>Item</code> does not exist,
	 *         false otherwise
	 */
	public Boolean itemUnbid(int listingID) {
		// TODO: IMPLEMENT CODE HERE
		/**
		 * AcquireLock<lockForhighestBids> IF listingID not in highestBids
		 * ARRAYLIST return true release lock
		 */
		synchronized (lockForSubmitBid) {
			if (!highestBids.containsKey(listingID))
				return true;
		}
		synchronized (lockForSubmitItem) {
			if (!itemsAndIDs.containsKey(listingID))
				return true;
		}
		return false;
	}

}
