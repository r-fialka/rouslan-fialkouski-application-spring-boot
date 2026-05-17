package com.openclassrooms.safetynet.model;

import lombok.Data;

@Data
public class FireStation {
    private String address;
    private String station;  // Note: `station` is a `String`, not an `int`
}