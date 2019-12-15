package camp.nextstep.edu.kitchenpos.bo;

import camp.nextstep.edu.kitchenpos.dao.MenuDao;
import camp.nextstep.edu.kitchenpos.dao.MenuGroupDao;
import camp.nextstep.edu.kitchenpos.dao.MenuProductDao;
import camp.nextstep.edu.kitchenpos.dao.ProductDao;
import camp.nextstep.edu.kitchenpos.model.Menu;
import camp.nextstep.edu.kitchenpos.model.MenuProduct;
import camp.nextstep.edu.kitchenpos.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuBoTest {
    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private MenuBo menuBo;


    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given
        Menu menu = createMenu(5000L);

        // when
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.ofNullable(createProduct(5000L)));
        given(menuDao.save(menu)).willReturn(menu);

        // when
        Menu createdMenu = menuBo.create(menu);

        // then
        assertAll(
            () -> assertThat(createdMenu.getName()).isEqualTo("후라이드치킨"),
            () -> assertThat(createdMenu.getMenuGroupId()).isEqualTo(1L),
            () -> assertThat(createdMenu.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴 가격은 0원 이상이다.")
    @Test
    void menu_price_more_than_zero() {
        // given
        Menu menu = createMenu(-1000);

        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> menuBo.create(menu));
    }

    @DisplayName("메뉴 가격은 메뉴상품 가격의 합 이하이어야한다.")
    @Test
    void menu_price_less_than_menu_products_price() {
        // given
        Menu menu = createMenu(4000);
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.ofNullable(createProduct(3000)));

        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> menuBo.create(menu));
    }

    @DisplayName("메뉴가 속한 메뉴 그룹이 있어야한다.")
    @Test
    void menu_group() {
        // given
        Menu menu = createMenu(4000);
        given(menuGroupDao.existsById(any())).willReturn(false);

        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> menuBo.create(menu));
    }

    @DisplayName("메뉴를 구성하는 메뉴 상품이 있어야한다.")
    @Test
    void menu_products() {
        // given
        Menu menu = createMenu(4000);
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willThrow(new IllegalArgumentException());

        // when

        // then
        assertThrows(IllegalArgumentException.class, () -> menuBo.create(menu));
    }

    @DisplayName("등록된 메뉴의 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        Menu menu01 = createMenu(1000);
        Menu menu02 = createMenu(2000);
        given(menuDao.findAll()).willReturn(Arrays.asList(menu01, menu02));
        given(menuProductDao.findAllByMenuId(menu01.getId())).willReturn(menu01.getMenuProducts());
        given(menuProductDao.findAllByMenuId(menu02.getId())).willReturn(menu02.getMenuProducts());

        // when
        List<Menu> menuList = menuBo.list();

        // then
        assertThat(menuList).containsExactlyInAnyOrderElementsOf(Arrays.asList(menu01, menu02));
    }

    private Menu createMenu(long price) {
        Menu menu = new Menu();
        menu.setName("후라이드치킨");
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuProducts(Arrays.asList(createMenuProduct(1)));

        return menu;

    }

    private MenuProduct createMenuProduct(long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    private Product createProduct(long price) {
        Product product = new Product();
        product.setId(1L);
        product.setName("후라이드");
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }
}
