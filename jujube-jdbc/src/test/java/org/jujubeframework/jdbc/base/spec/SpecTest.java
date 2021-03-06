package org.jujubeframework.jdbc.base.spec;

import com.google.common.collect.Lists;
import org.jujubeframework.exception.DaoQueryException;
import org.jujubeframework.jdbc.base.util.Sqls;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecTest {

    @Test
    public void testEq() {
        Spec spec = new Spec().eq("name", "abc");
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`name`= ?");
        assertThat(spec.getFilterParams()).hasSize(1).contains("abc");
    }

    @Test
    public void testEqEx() {
        org.junit.jupiter.api.Assertions.assertThrows(DaoQueryException.class, () -> new Spec().eq("name", ""));
    }

    @Test
    public void testLike() {
        Spec spec = new Spec().like("name", Sqls.leftLikeWrap("abc"));
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`name` like ?");
        assertThat(spec.getFilterParams()).hasSize(1).contains("%abc");
    }

    @Test
    public void testLikeEx() {
        org.junit.jupiter.api.Assertions.assertThrows(DaoQueryException.class, () -> new Spec().like("name", ""));
    }

    @Test
    public void testNotlike() {
        Spec spec = new Spec().notlike("name", "abc");
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`name` not like ?");
        assertThat(spec.getFilterParams()).hasSize(1).contains("abc");
    }

    @Test
    public void testGt() {
        Spec spec = new Spec().gt("age", "12");
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`age` > ?");
        assertThat(spec.getFilterParams()).hasSize(1).contains("12");
    }

    @Test
    public void testLt() {
        Spec spec = new Spec().lt("age", "12");
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`age` < ?");
        assertThat(spec.getFilterParams()).hasSize(1).contains("12");
    }

    @Test
    public void testGte() {
        Spec spec = new Spec().gte("age", "12");
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`age` >= ?");
        assertThat(spec.getFilterParams()).hasSize(1).contains("12");
    }

    @Test
    public void testLte() {
        Spec spec = new Spec().lte("age", "12");
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`age` <= ?");
        assertThat(spec.getFilterParams()).hasSize(1).contains("12");
    }

    @Test
    public void testNot() {
        Spec spec = new Spec().not("age", "12");
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`age` <> ?");
        assertThat(spec.getFilterParams()).hasSize(1).contains("12");
    }

    @Test
    public void testIsNull() {
        Spec spec = new Spec().isNull("name");
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`name` is null");
        assertThat(spec.getFilterParams()).isEmpty();
    }

    @Test
    public void testIsNotNull() {
        Spec spec = new Spec().isNotNull("name");
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`name` is not null");
        assertThat(spec.getFilterParams()).isEmpty();
    }

    @Test
    public void testIsNotEmpty() {
        Spec spec = new Spec().isNotEmpty("name");
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`name` <> ''");
        assertThat(spec.getFilterParams()).isEmpty();

    }

    @Test
    public void testIn() {
        List<Long> list = Lists.newArrayList(1L, 2L, 3L);
        Spec spec = new Spec().in("name", list);
        System.out.println(spec.getFilterSql());
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`name` in(1,2,3)");
        assertThat(spec.getFilterParams());
    }

    @Test
    public void testNotin() {
        List<Long> list = Lists.newArrayList(1L, 2L, 3L);
        Spec spec = new Spec().notin("name", list);
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`name` not in(1,2,3)");
        assertThat(spec.getFilterParams());
    }

    @Test
    public void testOr() {
        Spec rule = new Spec().eq("age", 12);
        Spec rule2 = new Spec().like("age", 14);
        Spec spec = new Spec().or(rule, rule2);
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("(`age`= ? or `age` like ?)");
        assertThat(spec.getFilterParams()).contains(12).contains(14);
    }

    @Test
    public void testAnd() {
        Spec rule = new Spec().eq("age", 12);
        Spec rule2 = new Spec().like("age", 14);
        Spec spec = new Spec().and(rule, rule2);
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("(`age`= ? and `age` like ?)");
        assertThat(spec.getFilterParams()).contains(12).contains(14);
    }

    @Test
    public void testBetween() {
        Spec spec = new Spec().between("age", 18, 30);
        assertThat(spec.getSpecMap()).hasSize(1);
        assertThat(spec.getFilterSql()).isEqualTo("`age` between ? and ?");
        System.out.println(spec.getFilterSql());
        assertThat(spec.getFilterParams()).contains(18).contains(30);
    }

    @Test
    public void testSort() {
        Spec spec = new Spec().sort().desc("age").end();
        assertThat(spec.sort().buildSqlSort()).isEqualTo(" order by age desc");
        assertThat(spec.getFilterParams()).isEmpty();
    }

    @Test
    public void testClone() {
        Spec spec = new Spec().eq("name", "abc").limit(10).sort().asc("age").desc("number").end();
        Spec spec2 = spec.clone();
        assertThat(spec == spec2).isFalse();
        assertThat(spec.getClass().equals(spec2.getClass())).isTrue();
        assertThat(spec.equals(spec2));
    }

    @Test
    public void testNewS() {
        Spec spec = new Spec();
        assertThat(spec).isNotNull();
    }

}
