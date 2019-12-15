package camp.nextstep.edu.kitchenpos.bo;

import camp.nextstep.edu.kitchenpos.dao.MenuGroupDao;
import camp.nextstep.edu.kitchenpos.model.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class MenuGroupBoTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    private MenuGroupBo menuGroupBo;

    @BeforeEach
    void setUp() {
        menuGroupBo = new MenuGroupBo(menuGroupDao);
    }

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("두마리메뉴");
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        // when
        MenuGroup createdMenuGroup = menuGroupBo.create(menuGroup);

        // then
        assertThat(createdMenuGroup.getName()).isEqualTo("두마리메뉴");
        then(menuGroupDao).should().save(any());
    }

    @DisplayName("등록된 메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        MenuGroup menuGroup01 = new MenuGroup();
        menuGroup01.setId(1L);
        menuGroup01.setName("두마리메뉴");

        MenuGroup menuGroup02 = new MenuGroup();
        menuGroup02.setId(2L);
        menuGroup02.setName("한마리메뉴");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup01, menuGroup02));

        // when
        List<MenuGroup> menuGroupList = menuGroupBo.list();

        // then
        assertThat(menuGroupList).containsExactlyInAnyOrderElementsOf(Arrays.asList(menuGroup02, menuGroup01));
    }
}
