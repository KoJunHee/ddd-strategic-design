package camp.nextstep.edu.kitchenpos.bo;

import camp.nextstep.edu.kitchenpos.dao.ProductDao;
import camp.nextstep.edu.kitchenpos.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductBoTest {
    @Mock
    private ProductDao productDao;
    private ProductBo productBo;

    @BeforeEach
    void setUp() {
        productBo = new ProductBo(productDao);
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        Product product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(1000L));
        product.setName("후라이드");
        given(productDao.save(any())).willReturn(product);

        // when
        Product createdProduct = productBo.create(product);

        // then
        assertAll(
            () -> assertThat(createdProduct.getPrice()).isEqualTo(BigDecimal.valueOf(1000L)),
            () -> assertThat(createdProduct.getName()).isEqualTo("후라이드")
        );
        then(productDao).should().save(any());
    }

    @DisplayName("등록된 상품의 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        Product product01 = new Product();
        product01.setId(1L);
        product01.setPrice(BigDecimal.valueOf(1000L));
        product01.setName("후라이드");

        Product product02 = new Product();
        product02.setId(2L);
        product02.setPrice(BigDecimal.valueOf(2000L));
        product02.setName("양념치킨");

        given(productDao.findAll()).willReturn(Arrays.asList(product01, product02));

        // when
        List<Product> productList = productBo.list();

        // then
        assertThat(productList).containsExactlyInAnyOrderElementsOf(Arrays.asList(product02, product01));
    }
}
