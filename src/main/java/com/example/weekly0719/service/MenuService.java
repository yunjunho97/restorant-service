package com.example.weekly0719.service;

import com.example.weekly0719.repository.MenuRepository;
import com.example.weekly0719.model.Menu;
import com.example.weekly0719.model.MenuDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public MenuDTO createMenu(MenuDTO menuDTO) {
        Menu menu = MenuDTO.toEntity(menuDTO);
        Menu savedMenu = menuRepository.save(menu);
        return MenuDTO.fromEntity(savedMenu);
    }

    public List<MenuDTO> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<MenuDTO> getById(Long id) {
        return menuRepository.findById(id).map(MenuDTO::fromEntity);
    }

    public Optional<MenuDTO> getByName(String name) {
        return menuRepository.findByName(name)
                .map(MenuDTO::fromEntity);
    }

    public Optional<MenuDTO> updateMenu(Long id, MenuDTO menuDTO) {
        return menuRepository.findById(id)
                .map(o -> {
                    o.setName(menuDTO.getName());
                    o.setCategory(menuDTO.getCategory());
                    o.setPrice(menuDTO.getPrice());
                    o.setDescription(menuDTO.getDescription());
                    return MenuDTO.fromEntity(menuRepository.save(o));
                });
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }
}
