package com.bitespeed.us.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentifiedContactRes extends BaseResponse {

    private ContactDetails contact;

}
