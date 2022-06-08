package com.xnfh.cec.repository.specification;

import com.xnfh.entity.BusCharacterManager;
import com.xnfh.entity.BusPlantResource;
import com.xnfh.enums.AuditStatus;
import com.xnfh.vo.CancelRegisterVo;
import com.xnfh.vo.CharacterManagerVo;
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
 * @Author wanghaohao ON 2022/6/7
 */
public class CancelInputNumSpecification {


    private static final Sort defaultSort = Sort.by(Sort.Direction.ASC, "createTime");



    public static Specification<BusPlantResource> cancelInputNumSpecification(final CancelRegisterVo cancelRegisterVo) {
        return new Specification<BusPlantResource>() {

            @Override
            public Predicate toPredicate(Root<BusPlantResource> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //登记单位
                if (!StringUtils.isEmpty(cancelRegisterVo.getBusinessName())) {
                    Predicate businessName = criteriaBuilder.equal(root.get("businessName"), cancelRegisterVo.getBusinessName());
                    predicates.add(businessName);
                }

                //登记号
                if (cancelRegisterVo.getInputNum() != null) {
                    Predicate inputNum = criteriaBuilder.equal(root.get("inputNum"), cancelRegisterVo.getInputNum());
                    predicates.add(inputNum);
                }

                //作物id
                if(cancelRegisterVo.getPlantId()!=null){
                    Predicate plantId = criteriaBuilder.equal(root.get("plantId"), cancelRegisterVo.getPlantId());
                    predicates.add(plantId);
                }

                //登记联系人
                if(!StringUtils.isEmpty(cancelRegisterVo.getContactId())){
                    Predicate contactId = criteriaBuilder.equal(root.get("contactId"), cancelRegisterVo.getContactId());
                    predicates.add(contactId);
                }

                //登记批号
                if(!StringUtils.isEmpty(cancelRegisterVo.getInputNum())){
                    Predicate inputNum = criteriaBuilder.equal(root.get("inputNum"), cancelRegisterVo.getInputNum());
                    predicates.add(inputNum);
                }

                //撤销任务号
                if(!StringUtils.isEmpty(cancelRegisterVo.getCancelInputNum())){
                    Predicate cancelInputNum = criteriaBuilder.equal(root.get("cancelInputNum"), cancelRegisterVo.getCancelInputNum());
                    predicates.add(cancelInputNum);
                }

                //审核状态和撤销时间
//                if(cancelRegisterVo.getStatus()!=null){
//                    criteriaBuilder.equal(root.get(""),cancelRegisterVo.getStatus())
//                }


                //1代表逻辑删除
                Predicate auditStatus = criteriaBuilder.equal(root.get("auditStatus"),0);
                predicates.add(auditStatus);

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static  Sort getSort( CancelRegisterVo cancelRegisterVo){
        if(StringUtils.isEmpty(cancelRegisterVo.getOrderBy())){
            return defaultSort;
        }
        return null;
    }
}
