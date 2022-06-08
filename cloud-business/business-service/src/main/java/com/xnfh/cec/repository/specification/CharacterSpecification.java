package com.xnfh.cec.repository.specification;

import com.xnfh.entity.BusCharacterType;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserUnitVo;
import com.xnfh.vo.BusCharacterTypeVo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/31
 */
public class CharacterSpecification {

    private static final Sort defaultSort = Sort.by(Sort.Direction.ASC, "orderNum");



    public static Specification<BusCharacterType> characterSpecification(final BusCharacterTypeVo busCharacterTypeVo) {
        return new Specification<BusCharacterType>() {

            @Override
            public Predicate toPredicate(Root<BusCharacterType> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //形状类型名称
                if (busCharacterTypeVo.getCharacterTypeName() != null) {
                    Predicate characterTypeName = criteriaBuilder.equal(root.get("characterTypeName"), busCharacterTypeVo.getCharacterTypeName());
                    predicates.add(characterTypeName);
                }

                Predicate isDelete = criteriaBuilder.equal(root.get("isDelete"),0);
                predicates.add(isDelete);

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static  Sort getSort(BusCharacterTypeVo busCharacterTypeVo){
        if(StringUtils.isEmpty(busCharacterTypeVo.getOrderBy())){
            return defaultSort;
        }
        return null;
    }
}
