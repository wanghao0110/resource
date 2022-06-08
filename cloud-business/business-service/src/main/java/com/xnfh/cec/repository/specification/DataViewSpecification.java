package com.xnfh.cec.repository.specification;

import com.xnfh.entity.BusCharacterType;
import com.xnfh.entity.BusPlantResource;
import com.xnfh.entity.BusSaveResourceType;
import com.xnfh.vo.BusCharacterTypeVo;
import com.xnfh.vo.DataViewVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/6/6
 */
@Slf4j
public class DataViewSpecification {

    private static final Sort defaultSort = Sort.by(Sort.Direction.ASC, "plantTypeId");



    public static Specification<BusPlantResource> dataViewSpecification(final DataViewVo dataViewVo) {
        return new Specification<BusPlantResource>() {

            @Override
            public Predicate toPredicate(Root<BusPlantResource> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //作物
                if (dataViewVo.getPlantId() != null) {
                    Predicate plantId = criteriaBuilder.equal(root.get("plantId"), dataViewVo.getPlantId());
                    predicates.add(plantId);
                }
                //种质名称
                if (!StringUtils.isEmpty(dataViewVo.getPlantId())) {
                    Predicate name = criteriaBuilder.equal(root.get("name"), dataViewVo.getName());
                    predicates.add(name);
                }
                //RN
                if (!StringUtils.isEmpty(dataViewVo.getRn())) {
                    Predicate rn = criteriaBuilder.equal(root.get("rn"), dataViewVo.getRn());
                    predicates.add(rn);
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //2022-02-02 12:12:12
                if (dataViewVo.getCreateTime() != null) {
                    String createTime = dataViewVo.getCreateTime();
                    createTime = new StringBuilder(createTime).insert(createTime.length(), "-01-01 00:00:00").toString();
                    Date createTimeDate = null;
                    try {
                        createTimeDate = simpleDateFormat.parse(createTime);
                    } catch (ParseException e) {
                        log.error("transform string to date error.e :{}", e);
                    }
                    Predicate create = criteriaBuilder.greaterThan(root.get("createTime"), createTimeDate);
                    predicates.add(create);
                }

                if (dataViewVo.getCreateTime() != null) {
                    String createTime = dataViewVo.getCreateTime();
                    createTime = new StringBuilder(createTime).insert(createTime.length(), "-12-31 23:59:59").toString();
                    Date createTimeDate = null;
                    try {
                        createTimeDate = simpleDateFormat.parse(createTime);
                    } catch (ParseException e) {
                        log.error("transform string to date error.e :{}", e);
                    }
                    Predicate create = criteriaBuilder.lessThan(root.get("createTime"), createTimeDate);
                    predicates.add(create);
                }
                //保存资源类型
                if (dataViewVo.getSaveResourceTypes() != null && dataViewVo.getSaveResourceTypes().contains(",")) {
                    List<String> busSaveResourceTypeId = Arrays.asList(StringUtils.split(dataViewVo.getSaveResourceTypes(), ","));
                    busSaveResourceTypeId.forEach(id -> {
                        predicates.add(criteriaBuilder.like(root.get("saveResourceId"), addLike(dealDatabaseLike(id))));
                    });
                } else if(dataViewVo.getSaveResourceTypes() != null &&!(dataViewVo.getSaveResourceTypes().contains(","))) {
                    predicates.add(criteriaBuilder.like(root.get("saveResourceId"), addLike(dealDatabaseLike(dataViewVo.getSaveResourceTypes()))));
                }

                //资源类型
                if (!StringUtils.isEmpty(dataViewVo.getResourceId())) {
                    Predicate resourceId = criteriaBuilder.equal(root.get("resourceId"), dataViewVo.getResourceId());
                    predicates.add(resourceId);
                }
                //审核状态为1
                Predicate auditStatus = criteriaBuilder.equal(root.get("auditStatus"),1);
                predicates.add(auditStatus);

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

    public static  Sort getSort(DataViewVo dataViewVo){
        if(StringUtils.isEmpty(dataViewVo.getOrderBy())){
            return defaultSort;
        }
        return null;
    }
}
