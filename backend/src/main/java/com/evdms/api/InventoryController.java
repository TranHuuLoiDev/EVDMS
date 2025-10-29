package com.evdms.api;

import com.evdms.domain.InventoryItem;
import com.evdms.domain.Product;
import com.evdms.domain.WholesalePrice;
import com.evdms.service.InventoryService;
import com.evdms.service.ProductService;
import com.evdms.service.WholesalePriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    private final ProductService productService;
    private final WholesalePriceService wholesaleService;

    public InventoryController(InventoryService inventoryService,
                               ProductService productService,
                               WholesalePriceService wholesaleService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
        this.wholesaleService = wholesaleService;
    }

    // (các endpoint cũ giữ nguyên)

    @GetMapping("/product/{productId}/wholesale")
    public ResponseEntity<?> getWholesalePrice(@PathVariable Long productId,
                                               @RequestParam int qty) {
        return wholesaleService.getEffectivePrice(productId, qty)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
