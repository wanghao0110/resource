package com.xnfh.cec.repository.specification;

import com.xnfh.entity.BusPlant;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserUnitVo;
import com.xnfh.vo.PlantVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

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
public class PlantSpecification {

    private static final Sort defaultSort = Sort.by(Sort.Direction.DESC, "createTime");



    public static Specification<BusPlant> plantSpecification(final PlantVo plantVo) {
        return new Specification<BusPlant>() {

            @Override
            public Predicate toPredicate(Root<BusPlant> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (!org.apache.commons.lang3.StringUtils.isEmpty(plantVo.getPlantName())) {
                    Predicate plantName = criteriaBuilder.equal(root.get("plantName"), plantVo.getPlantName());
                    predicates.add(plantName);
                }
                if (!StringUtils.isEmpty(plantVo.getPlantCode())) {
                    Predicate plantCode = criteriaBuilder.equal(root.get("plantCode"), plantVo.getPlantCode());
                    predicates.add(plantCode);
                }
                if (plantVo.getPlantTypeId()!=null) {
                    Predicate plantTypeId = criteriaBuilder.equal(root.get("plantTypeId"), plantVo.getPlantTypeId());
                    predicates.add(plantTypeId);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static  Sort getSort( ){
       return defaultSort;
    }
}
