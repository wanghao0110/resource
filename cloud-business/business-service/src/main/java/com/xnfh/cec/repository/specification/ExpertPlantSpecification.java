package com.xnfh.cec.repository.specification;

import com.xnfh.entity.BusExpertPlant;
import com.xnfh.entity.SysExpert;
import com.xnfh.entity.vo.SysExpertVo;
import com.xnfh.vo.BusExpertPlantVo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/30
 */
public class ExpertPlantSpecification {

    private static final Sort defaultSort = Sort.by(Sort.Direction.DESC, "createTime");

    public static Specification<BusExpertPlant> busExpertPlantSpecification(final BusExpertPlantVo busExpertPlantVo) {
        return new Specification<BusExpertPlant>() {

            @Override
            public Predicate toPredicate(Root<BusExpertPlant> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //专家名称
                if (busExpertPlantVo.getTrueName() != null) {
                    Predicate trueName = criteriaBuilder.equal(root.get("trueName"), busExpertPlantVo.getTrueName());
                    predicates.add(trueName);
                }
                //电话
                if (busExpertPlantVo.getPhone() != null) {
                    Predicate phone = criteriaBuilder.equal(root.get("phone"), busExpertPlantVo.getPhone());
                    predicates.add(phone);
                }
                //作物列表 采用顿号分割，传给后端时时顿号
                if(busExpertPlantVo.getPlantIds()!=null){
                    if ( busExpertPlantVo.getPlantIds().contains("、")) {
                        List<String> plantIds = Arrays.asList(StringUtils.split(busExpertPlantVo.getPlantIds(), "、"));
                        plantIds.forEach(id -> {
                            predicates.add(criteriaBuilder.like(root.get("plantId"), addLike(dealDatabaseLike(id))));
                        });
                    }else{
                        predicates.add(criteriaBuilder.like(root.get("plantId"), addLike(dealDatabaseLike(busExpertPlantVo.getPlantIds()))));
                    }
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static String addLike(String text) {
        return "%" + text + "%";
    }

    public static String dealDatabaseLike(String text) {
        String formatParam = org.apache.commons.lang3.StringUtils.replaceEach(text,
                new String[]{"_", "%", "'", "\\"}, new String[]{"\\_", "\\%", "\\'", "\\\\"});
        return formatParam;
    }

    public static  Sort getSort(BusExpertPlantVo busExpertPlantVo){
        if(StringUtils.isEmpty(busExpertPlantVo.getOrderBy())){
            return defaultSort;
        }
        return null;
    }
}
