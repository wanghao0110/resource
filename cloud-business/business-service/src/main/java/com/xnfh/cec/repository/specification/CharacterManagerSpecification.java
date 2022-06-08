package com.xnfh.cec.repository.specification;

import com.xnfh.entity.BusCharacterManager;
import com.xnfh.entity.BusCharacterType;
import com.xnfh.vo.BusCharacterTypeVo;
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
 * @Author wanghaohao ON 2022/5/31
 */
public class CharacterManagerSpecification {

    private static final Sort defaultSort = Sort.by(Sort.Direction.ASC, "orderNum");



    public static Specification<BusCharacterManager> characterManagerSpecification(final CharacterManagerVo characterManagerVo) {
        return new Specification<BusCharacterManager>() {

            @Override
            public Predicate toPredicate(Root<BusCharacterManager> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //形状类型名称
                if (!StringUtils.isEmpty(characterManagerVo.getCharacterName())) {
                    Predicate characterName = criteriaBuilder.equal(root.get("characterName"), characterManagerVo.getCharacterName());
                    predicates.add(characterName);
                }

                //性状类型
                if(characterManagerVo.getCharacterTypeId() != null){
                    Predicate characterTypeId = criteriaBuilder.equal(root.get("characterTypeId"), characterManagerVo.getCharacterTypeId());
                    predicates.add(characterTypeId);
                }

                //1代表逻辑删除
                Predicate isDelete = criteriaBuilder.equal(root.get("isDelete"),0);
                predicates.add(isDelete);

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public static  Sort getSort( CharacterManagerVo characterManagerVo){
        if(StringUtils.isEmpty(characterManagerVo.getOrderBy())){
            return defaultSort;
        }
        return null;
    }
}
