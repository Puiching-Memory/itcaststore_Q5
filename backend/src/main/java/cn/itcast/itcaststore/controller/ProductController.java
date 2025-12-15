package cn.itcast.itcaststore.controller;

import cn.itcast.itcaststore.domain.Product;
import cn.itcast.itcaststore.repository.ProductRepository;
import cn.itcast.itcaststore.util.ResponseResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseResult<Page<Product>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products;
        
        if (keyword != null && !keyword.isEmpty()) {
            products = productRepository.searchProducts(keyword, pageable);
        } else if (category != null && !category.isEmpty()) {
            products = productRepository.findByCategory(category, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }
        
        return ResponseResult.success(products);
    }

    @GetMapping("/{id}")
    public ResponseResult<Product> getProduct(@PathVariable String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        return ResponseResult.success(product);
    }

    @GetMapping("/hot")
    public ResponseResult<List<Product>> getHotProducts() {
        List<Product> products = productRepository.findTop10ByOrderByPnumDesc();
        return ResponseResult.success(products);
    }
}
