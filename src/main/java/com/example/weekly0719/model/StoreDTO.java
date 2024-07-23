package com.example.weekly0719.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDTO {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;

    public static StoreDTO fromEntity(Store store) {
        return StoreDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .address(store.getAddress())
                .phoneNumber(store.getPhoneNumber())
                .build();
    }

    public static Store toEntity(StoreDTO storeDTO) {
        return Store.builder()
                .id(storeDTO.getId())
                .name(storeDTO.getName())
                .address(storeDTO.getAddress())
                .phoneNumber(storeDTO.getPhoneNumber())
                .build();
    }
}
