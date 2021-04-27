placeBid(bid){
	assure bid is not null
if (bid.bidder.equals(owner of lot)
	throw exception(“can’t bid on own lot”);
round the amount of bid to nearest bidIncrement
try{
	addBid(bid)
	if there is no pendingAutoBid{
		bump the highestBid up by one biddingIncrement;
	if (the amount of bid is higher than the new highestBid)
			set the pendingAutoBid to this bid;
		set the leading  bidder to the bidder of this bid;
	} 
	else {
		if the amount of bid is less than the pendingAutoBid
			set the highestBid to the amount of this bid + one bidding increment
		else if the amount of this bid is equal to the pendingAutoBid
		{	set the highestBid to the amount of the pendingAutoBid;
			remove the pendingAutoBid;
		}
		else {
			set the highestBid to the pendingAutoBid amount + one biddingIncreement;
			set the leadingBidder to the bidder of this bid;
			if (the amount of this bid is less or equal to the new highestBid)
				remove the pendingAutoBid;
			else
				set the pendingAutoBid to this bid;
		}
		
		}

	}
	Catch (a lot has ended exception)
		rethrow it;
	Catch (a bid is too low exception)
		rethrow it;
	Catch (a general exception)
		Return an empty optional of type lot;
Return an optional of this lot //(if this point is reached - all was successful)
}

addBid(bid){
now = the current wall clock time;
if now < extendedEndTime AND isBidHighEnough(bid.amount)
	Add the bid to bidSet;
else
	Throw exception("lot has ended");
if isInTriggerPeriod(now) 
	set extendedEndTime to the extendedEndTime + autoExtendDuration;
}

isBidHighenough(bid){
	set isHigher = false;
	If (highestBid > 0.0)
		isHigher = the amount of bid is more or equals to the highestBid + one biddingIncrement;
	else{// no bids placed yet
				// there's startingPrice and bid is less OR there's isn't startingPrice and bid
				// is lower than 1 increment
		if (the startingPrice > 0.0 and the amount of  bid is less than the startingPrice)
			throw a bidTooLow exception(startingPrice);
		else if (the startingPrice == 0.0 AND the amount of  bid is less than (the startingPrice + one biddingIncrement)
			throw a bidTooLow exception(startingPrice + biddingIncrement);
		else 
			Set isHigher to true;
		}
		
	If (!isHigher)
		throw a bidTooLow exception(bid.amount, highestBid + biddingIncrement); 
	return true;
}

