package com.xnfh.cec.repository.specification;

import com.xnfh.entity.SysExpert;
import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserVo;
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
public class PersonalSpecification {

    private static final Sort defaultSort = Sort.by(Sort.Direction.DESC, "createTime");

    public static Specification<SysUser> PersonalSpecification(final SysUserVo sysUserVo) {
        return new Specification<SysUser>() {

            @Override
            public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //用户名
                if (sysUserVo.getAccountName() != null) {
                    Predicate accountName = criteriaBuilder.equal(root.get("accountName"), sysUserVo.getAccountName());
                    predicates.add(accountName);
                }
                //姓名
                if (sysUserVo.getTrueName() != null) {
                    Predicate trueName = criteriaBuilder.equal(root.get("trueName"), sysUserVo.getTrueName());
                    predicates.add(trueName);
                }
                //电话
                if (sysUserVo.getContactPhone() != null) {
                    Predicate contactPhone = criteriaBuilder.equal(root.get("contactPhone"), sysUserVo.getContactPhone());
                    predicates.add(contactPhone);
                }

                Predicate status = criteriaBuilder.equal(root.get("status"),1);
                predicates.add(status);

                Predicate openCloseStatus = criteriaBuilder.equal(root.get("openCloseStatus"),1);
                predicates.add(openCloseStatus);

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static  Sort getSort(SysUserVo sysUserVo){
        if(StringUtils.isEmpty(sysUserVo.getOrderBy())){
            return defaultSort;
        }
        return null;
    }
}
