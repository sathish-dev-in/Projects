package com.shopease.config;

import com.shopease.model.Category;
import com.shopease.model.Product;
import com.shopease.model.User;
import com.shopease.repository.CategoryRepository;
import com.shopease.repository.ProductRepository;
import com.shopease.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Seeds the database with sample data on startup (only if tables are empty).
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public DataInitializer(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        seedAdminUser();
        seedCategories();
        seedProducts();
    }

    private void seedAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setFullName("Shop Administrator");
            admin.setEmail("admin@zynkart.com");
            admin.setPassword(encoder.encode("admin123"));
            admin.setPhone("9876543210");
            admin.setRole(User.Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
        }
        if (!userRepository.existsByUsername("user")) {
            User user = new User();
            user.setUsername("user");
            user.setFullName("Test User");
            user.setEmail("user@zynkart.com");
            user.setPassword(encoder.encode("user123"));
            user.setPhone("9876543211");
            user.setAddress("123 Main Street, Mumbai, Maharashtra 400001");
            user.setRole(User.Role.USER);
            user.setActive(true);
            userRepository.save(user);
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                new Category("Electronics", "Gadgets, phones, laptops and accessories"),
                new Category("Clothing", "Men and women fashion apparel"),
                new Category("Books", "Educational and fiction books"),
                new Category("Home & Kitchen", "Kitchen appliances and home decor"),
                new Category("Sports", "Sports equipment and fitness gear")
            );
            categoryRepository.saveAll(categories);
        }
    }

    private void seedProducts() {
        if (productRepository.count() == 0) {
            Category electronics = categoryRepository.findByName("Electronics").orElse(null);
            Category clothing = categoryRepository.findByName("Clothing").orElse(null);
            Category books = categoryRepository.findByName("Books").orElse(null);
            Category home = categoryRepository.findByName("Home & Kitchen").orElse(null);
            Category sports = categoryRepository.findByName("Sports").orElse(null);

            createProduct("Wireless Headphones", "Premium noise-cancelling wireless headphones with 30hr battery",
                    new BigDecimal("2499.00"), 50, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400", electronics);
            createProduct("Smartphone Pro Max", "Latest 5G smartphone with 108MP camera and 5000mAh battery",
                    new BigDecimal("45999.00"), 30, "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400", electronics);
            createProduct("Laptop Ultra 15", "15.6\" Full HD laptop with Intel i5, 8GB RAM, 512GB SSD",
                    new BigDecimal("54999.00"), 15, "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400", electronics);
            createProduct("Men's Cotton T-Shirt", "Premium 100% cotton round-neck t-shirt, available in multiple colours",
                    new BigDecimal("499.00"), 200, "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400", clothing);
            createProduct("Women's Floral Dress", "Elegant floral print midi dress perfect for all occasions",
                    new BigDecimal("1299.00"), 100, "https://images.unsplash.com/photo-1496747611176-843222e1e57c?w=400", clothing);
            createProduct("Clean Code by Robert Martin", "A handbook of agile software craftsmanship — must-read for developers",
                    new BigDecimal("699.00"), 75, "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=400", books);
            createProduct("Spring Boot in Action", "Comprehensive guide to building enterprise-grade Spring Boot applications",
                    new BigDecimal("799.00"), 60, "https://images.unsplash.com/photo-1532012197267-da84d127e765?w=400", books);
            createProduct("Air Fryer 4L", "Digital air fryer with 8 preset modes and non-stick basket",
                    new BigDecimal("3499.00"), 40, "https://images.unsplash.com/photo-1585515320310-259814833e62?w=400", home);
            createProduct("Yoga Mat Premium", "Non-slip 6mm thick eco-friendly yoga mat with carrying strap",
                    new BigDecimal("899.00"), 80, "https://images.unsplash.com/photo-1599901860904-17e6ed7083a0?w=400", sports);
            createProduct("Resistance Bands Set", "Set of 5 colour-coded resistance bands for home workouts",
                    new BigDecimal("599.00"), 120, "https://images.unsplash.com/photo-1598289431512-b97b0917affc?w=400", sports);
        }
    }

    private void createProduct(String name, String desc, BigDecimal price, int stock, String imgUrl, Category category) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setStock(stock);
        p.setImageUrl(imgUrl);
        p.setCategory(category);
        p.setActive(true);
        productRepository.save(p);
    }
}
