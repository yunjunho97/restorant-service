package com.example.weekly0719.model;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDTO {
    private Long id;
    private String name;
    private String category;
    private Long price;
    private String description;

    public static MenuDTO fromEntity(Menu menu) {
        return MenuDTO.builder()
                .id(menu.getId())
                .name(menu.getName())
                .category(menu.getCategory())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .build();
    }

    public static Menu toEntity(MenuDTO menuDTO){
        return Menu.builder()
                .id(menuDTO.getId())
                .name(menuDTO.getName())
                .category(menuDTO.getCategory())
                .price(menuDTO.getPrice())
                .description(menuDTO.getDescription())
                .build();
    }

}
