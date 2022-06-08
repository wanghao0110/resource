package com.xnfh.cec.repository.specification;

import com.xnfh.entity.SysUser;
import com.xnfh.entity.vo.SysUserUnitVo;
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
 * @Author wanghaohao ON 2022/5/31
 */
public class UnitSpecification {

    private static final Sort defaultSort = Sort.by(Sort.Direction.DESC, "createTime");

    private static final Sort openCloseStatus = Sort.by(Sort.Direction.DESC, "openCloseStatus");

    public static Specification<SysUser> unitSpecification(final SysUserUnitVo sysUserUnitVo) {
        return new Specification<SysUser>() {

            @Override
            public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //用户名
                if (sysUserUnitVo.getAccountName() != null) {
                    Predicate accountName = criteriaBuilder.equal(root.get("accountName"), sysUserUnitVo.getAccountName());
                    predicates.add(accountName);
                }
                //姓名
                if (sysUserUnitVo.getBusinessName() != null) {
                    Predicate bussinessName = criteriaBuilder.equal(root.get("businessName"), sysUserUnitVo.getBusinessName());
                    predicates.add(bussinessName);
                }
                //状态
                if (sysUserUnitVo.getOpenCloseStatus() != null) {
                    Predicate openCloseStatus = criteriaBuilder.equal(root.get("openCloseStatus"), sysUserUnitVo.getOpenCloseStatus());
                    predicates.add(openCloseStatus);
                }



                Predicate status = criteriaBuilder.equal(root.get("status"),0);
                predicates.add(status);

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static  Sort getSort(SysUserUnitVo sysUserUnitVo){
        if(StringUtils.isEmpty(sysUserUnitVo.getOrderBy())){
            return defaultSort;

        }else {
            return openCloseStatus;
        }
    }
}
