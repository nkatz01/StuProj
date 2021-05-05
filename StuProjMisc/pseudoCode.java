public Optional<Lot> placeBid(bid){
	assure bid is not null
	round the amount of bid to nearest bidIncrement//in a real application we'd want to relate back to the bidder the modified amount
if (bid.bidder.equals(owner of lot)
	throw exception(“can’t bid on own lot”);
else if (there is a leadingBidder on this lot AND the leadingBidder.equals(the bidder of this bid) AND the amount of this bid is higher than the highestBid but lower or equals to the pendingAutoBid (if there's one))
	throw exception("you already control the lot"); 
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
			log a message explaining that although the bis was accepted, the amount of this bid equals an existing autobid;
		}
		else {
			set the highestBid to the pendingAutoBid amount + one biddingIncreement;
			if( the leadingBidder.equals(the bidder of this bid)
				log a message saying that their pendingAutoBid increase was accepted;
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

private void addBid(bid){
now = the current wall clock time;
try{
	checkBidHighEnough(bid.amount);
	if now < extendedEndTime  
		Add the bid to bidSet;
	else
		Throw exception("lot has ended");
	if isInTriggerPeriod(now) 
		set extendedEndTime to the extendedEndTime + autoExtendDuration;
}
catch (a bid is too low exception) {
	rethrow it;
}
}


private void checkBidHighEnough(bid){
	set isHigher = false;
	If (highestBid > 0.0)
		isHigher = the amount of bid is more or equals to the highestBid + one biddingIncrement;
	else{// no bids placed yet
				// there's startingPrice and bid is less OR there's isn't startingPrice and bid
				// is lower than 1 increment
		if (the startingPrice > 0.0 and the amount of  bid is less than the startingPrice)
			throw a bidTooLow exception explaining that the amount is lower than the startingPrice;
		else if (the startingPrice == 0.0 AND the amount of  bid is less than (one biddingIncrement)
			throw a bidTooLow exception explaining that the amount is lower than the biddingIncreement;
		else 
			Set isHigher to true;
		}
		
	If (!isHigher)
		throw a bidTooLow exception explaining that the amount is lower thatn the highestBid + one biddingIncrement); 
}

public String persistEntity(Lot lot){
	StringBuilder = new StringBuilder();
	if (lot is not yet associated with lot.getUser())
		add lot to the user's lotsCreatedSet;
	if(lot.getUser() is not yet in the database){
		save the lot's user to the database;
		append to the stringBuilder the database id of the user; 
	}
	else{
		save just the lot to the database;
	}
	append to the stringBuilder the id of the newly created lot; 
	return stringBuilder.toString();
}

public String persistEntity(Bid bid){
	StringBuilder = new StringBuilder();
	if(bid's lot is not yet in the database)
		if (bids' lot is not yet associated with its user)
			add bid's lot to the user's lotsCreatedSet;//doesn't add lot to the database; just associates the two
	if(bid's lot and lot's user are both not in the database){
		save the lot and user to the database;
		append to the stringBuilder the database id of the user and the lot;
	}
	else if (only the lot is not in the database){
		save the lot to the database;
		append to the stringBuilder the database id of the lot;
	}
	if(bid's bidder is not yet in the database){
		save the lot's bidder (as a user) to the database;
		append to the stringBuilder the database id of the bidder; 
	}
	add this bid to the bidsBadeSet of the bidder;
	else{
		save just the lot to the database;
	}
	append to the stringBuilder the id of the newly created lot; 
	save this bid to the database;
	append to the stringBuilder the id of the newly created bid; 
	return stringBuilder.toString();
}

