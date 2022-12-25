package ru.alov.market.core.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import ru.alov.market.api.dto.CartDto;
import ru.alov.market.api.dto.CartItemDto;
import ru.alov.market.api.dto.OrderDetailsDto;
import ru.alov.market.api.dto.RecalculateCartRequestDto;
import ru.alov.market.core.entities.Category;
import ru.alov.market.core.entities.Product;
import ru.alov.market.core.integrations.CartServiceIntegration;
import ru.alov.market.core.integrations.PromotionServiceIntegration;
import ru.alov.market.core.repositories.OrderRepository;
import ru.alov.market.core.services.OrderService;
import ru.alov.market.core.services.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = OrderService.class)
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private CartServiceIntegration cartServiceIntegration;

    @MockBean
    private PromotionServiceIntegration promotionServiceIntegration;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ProductService productService;

    @Test
    public void createNewOrderTest() {
        String username = "Vasya Pupkin";
        String email = "email";

        Mono<CartDto> invalidCartDto = Mono.just(new CartDto(Collections.EMPTY_LIST, BigDecimal.ZERO));
        Mockito.doReturn(invalidCartDto)
               .when(cartServiceIntegration)
               .getCurrentUserCart(username);
        Assertions.assertThrows(IllegalStateException.class, () -> orderService.createNewOrder(username, email, new OrderDetailsDto("sdsada", "2121", "email")).block());

        Category category = new Category();
        category.setId(1L);
        category.setTitle("Еда");
        category.setProducts(Collections.emptyList());

        Product productButter = new Product();
        productButter.setId(1L);
        productButter.setTitle("Масло");
        productButter.setPrice(BigDecimal.valueOf(50));
        productButter.setCategory(category);

        Product productBread = new Product();
        productBread.setId(2L);
        productBread.setTitle("Хлеб");
        productBread.setPrice(BigDecimal.valueOf(25));
        productBread.setCategory(category);

        CartDto testCartDto = new CartDto(List.of(new CartItemDto(productButter.getId(), productButter.getTitle(), 3, productButter.getPrice(), productButter.getPrice().multiply(BigDecimal.valueOf(3))), new CartItemDto(productBread.getId(), productBread.getTitle(), 2, productBread.getPrice(), productBread.getPrice().multiply(BigDecimal.valueOf(2)))), BigDecimal.valueOf(200));

        Mockito.doReturn(Mono.just(testCartDto))
               .when(cartServiceIntegration)
               .getCurrentUserCart(username);

        Mockito.doReturn(Mono.just(testCartDto))
               .when(promotionServiceIntegration)
               .getRecalculateCart(new RecalculateCartRequestDto(testCartDto, LocalDateTime.now()));

        Mockito.doReturn(Optional.of(productButter))
               .when(productService)
               .findById(productButter.getId());

        Mockito.doReturn(Optional.of(productBread))
               .when(productService)
               .findById(productBread.getId());

        orderService.createNewOrder(username, email, new OrderDetailsDto("sdsada", "2121", "email")).subscribe(order -> {
                    Mockito.verify(orderRepository, Mockito.times(1)).save(ArgumentMatchers.any());
                    Mockito.verify(cartServiceIntegration, Mockito.times(1)).clearCart(username);
                }
        );

    }

}