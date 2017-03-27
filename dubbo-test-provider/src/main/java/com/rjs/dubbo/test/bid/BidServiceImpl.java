package com.rjs.dubbo.test.bid;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.dubbo.demo.bid.BidRequest;
import com.alibaba.dubbo.demo.bid.BidResponse;
import com.alibaba.dubbo.demo.bid.BidService;
import com.alibaba.dubbo.demo.bid.SeatBid;

public class BidServiceImpl implements BidService {

    public BidResponse bid(BidRequest request) {
        BidResponse response = new BidResponse();

        response.setId("abc");

        SeatBid seatBid = new SeatBid();
        seatBid.setGroup("group");
        seatBid.setSeat("seat");
        List<SeatBid> seatBids = new ArrayList<SeatBid>(1);
        seatBids.add(seatBid);

        response.setSeatBids(seatBids);

        return response;
    }

    public void throwNPE() throws NullPointerException {
        throw new NullPointerException();
    }

}