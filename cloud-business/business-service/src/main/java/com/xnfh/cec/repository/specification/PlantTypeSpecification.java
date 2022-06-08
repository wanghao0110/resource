package com.xnfh.cec.repository.specification;

import com.xnfh.entity.BusPlantType;
import com.xnfh.entity.SysExpert;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserUnitVo;
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
 * @Author wanghaohao ON 2022/6/1
 */
public class PlantTypeSpecification {

    private static final Sort defaultSort = Sort.by(Sort.Direction.ASC, "orderNum");

    public static Specification<BusPlantType> plantTypeSpecification(final String plantTypeName) {
        return new Specification<BusPlantType>() {

            @Override
            public Predicate toPredicate(Root<BusPlantType> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //用户名
                //输入作物类型名称
                if(!StringUtils.isEmpty(plantTypeName)){
                    Predicate name = criteriaBuilder.equal(root.get("typeName"),plantTypeName);
                    predicates.add(name);
                }


                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static  Sort getSort( ){

            return defaultSort;

    }
}
