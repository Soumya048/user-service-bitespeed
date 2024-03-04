package com.bitespeed.us.service;

import com.bitespeed.us.model.IdentifyContactReq;
import com.bitespeed.us.response.IdentifiedContactRes;

public interface ContactService {

    public IdentifiedContactRes identifyContact(IdentifyContactReq request);



}
