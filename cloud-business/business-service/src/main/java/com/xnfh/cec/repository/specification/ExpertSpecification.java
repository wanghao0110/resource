package com.xnfh.cec.repository.specification;

import com.xnfh.entity.SysExpert;
import com.xnfh.entity.vo.SysExpertVo;
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
 * @Author wanghaohao ON 2022/5/30
 */
public class ExpertSpecification {

    private static final Sort defaultSort = Sort.by(Sort.Direction.DESC, "createTime");

    public static Specification<SysExpert> sysExpertSpecification(final SysExpertVo sysExpertVo) {
        return new Specification<SysExpert>() {

            @Override
            public Predicate toPredicate(Root<SysExpert> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //用户名
                if (sysExpertVo.getUserName() != null) {
                    Predicate userName = criteriaBuilder.equal(root.get("userName"), sysExpertVo.getUserName());
                    predicates.add(userName);
                }
                //姓名
                if (sysExpertVo.getExpertName() != null) {
                    Predicate expertName = criteriaBuilder.equal(root.get("expertName"), sysExpertVo.getExpertName());
                    predicates.add(expertName);
                }
                //电话
                if (sysExpertVo.getPhone() != null) {
                    Predicate phone = criteriaBuilder.equal(root.get("phone"), sysExpertVo.getPhone());
                    predicates.add(phone);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static  Sort getSort(SysExpertVo sysExpertVo){
        if(StringUtils.isEmpty(sysExpertVo.getOrderBy())){
            return defaultSort;
        }
        return null;
    }
}
