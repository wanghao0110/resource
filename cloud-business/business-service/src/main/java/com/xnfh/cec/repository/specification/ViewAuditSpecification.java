package com.xnfh.cec.repository.specification;

import com.xnfh.entity.BusCharacterType;
import com.xnfh.entity.BusPlantResource;
import com.xnfh.vo.BusCharacterTypeVo;
import com.xnfh.vo.ViewAuditVo;
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
 * @Author wanghaohao ON 2022/6/6
 */
public class ViewAuditSpecification {

    private static final Sort defaultSort = Sort.by(Sort.Direction.ASC, "createTime");



    public static Specification<BusPlantResource> viewAuditSpecification(final ViewAuditVo viewAuditVo) {
        return new Specification<BusPlantResource>() {

            @Override
            public Predicate toPredicate(Root<BusPlantResource> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //登记号
                if (viewAuditVo.getInputNum() != null) {
                    Predicate inputNum = criteriaBuilder.equal(root.get("inputNum"), viewAuditVo.getInputNum());
                    predicates.add(inputNum);
                }
                //种子名称
                if (!StringUtils.isEmpty(viewAuditVo.getName())) {
                    Predicate name = criteriaBuilder.equal(root.get("name"), viewAuditVo.getName());
                    predicates.add(name);
                }
                //资源类型
                if (viewAuditVo.getResourceId() != null) {
                    Predicate resourceId = criteriaBuilder.equal(root.get("resourceId"), viewAuditVo.getResourceId());
                    predicates.add(resourceId);
                }
                //RID
                if (viewAuditVo.getRID() != null) {
                    Predicate rId = criteriaBuilder.equal(root.get("rId"), viewAuditVo.getRID());
                    predicates.add(rId);
                }
                //保存资源类型
                if (viewAuditVo.getSaveResourceTypes() != null && viewAuditVo.getSaveResourceTypes().contains(",")) {
                    List<String> busSaveResourceTypeId = Arrays.asList(StringUtils.split(viewAuditVo.getSaveResourceTypes(), ","));
                    busSaveResourceTypeId.forEach(id -> {
                        predicates.add(criteriaBuilder.like(root.get("saveResourceId"), addLike(dealDatabaseLike(id))));
                    });
                } else if(viewAuditVo.getSaveResourceTypes() != null &&!(viewAuditVo.getSaveResourceTypes().contains(","))) {
                    predicates.add(criteriaBuilder.like(root.get("saveResourceId"), addLike(dealDatabaseLike(viewAuditVo.getSaveResourceTypes()))));
                }


                Predicate isDelete = criteriaBuilder.equal(root.get("isDelete"),0);
                predicates.add(isDelete);

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
    public static  Sort getSort(ViewAuditVo viewAuditVo){
        if(StringUtils.isEmpty(viewAuditVo.getOrderBy())){
            return defaultSort;
        }
        return null;
    }
}
