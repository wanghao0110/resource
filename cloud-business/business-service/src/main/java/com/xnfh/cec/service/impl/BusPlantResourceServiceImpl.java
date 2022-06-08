package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.*;
import com.xnfh.cec.repository.specification.CharacterSpecification;
import com.xnfh.cec.repository.specification.DataViewSpecification;
import com.xnfh.cec.repository.specification.ExpertPlantSpecification;
import com.xnfh.cec.repository.specification.ViewAuditSpecification;
import com.xnfh.cec.service.BusPlantResourceService;
import com.xnfh.cec.service.BusPlantService;
import com.xnfh.cec.service.BusPlantTypeService;
import com.xnfh.cec.service.SysUserService;
import com.xnfh.data.ResourceAuditStatusData;
import com.xnfh.entity.*;
import com.xnfh.enums.AuditStatus;
import com.xnfh.enums.ResourceStatus;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import com.xnfh.vo.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Predicate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/25
 */
@Service
@Slf4j
public class BusPlantResourceServiceImpl implements BusPlantResourceService {

    @Autowired
    private BusPlantResourceRepository busPlantResourceRepository;

    @Autowired
    private BusPlantTypeService busPlantTypeService;

    @Autowired
    private BusPlantService busPlantService;

    @Autowired
    private BusVerietyRepository busVerietyRepository;

    @Autowired
    private BusSaveResourceTypeRepository busSaveResourceTypeRepository;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private BusResourceTypeRepository busResourceTypeRepository;

    @Autowired
    private BusPlantRepository busPlantRepository;

    @Autowired
    private BusCharacterManagerRespository busCharacterManagerRespository;

    /**
     * 添加在线资源登记
     *
     * @param busPlantResource
     */
    @Override
    public void addPlantResource(BusPlantResource busPlantResource) {
        log.info("current operate data busPlantResource", busPlantResource);
        if (busPlantResource.getResourceId() == null) {
            throw new ApiException(ExceptionDefinition.GET_PLANT_RESOURCE_RESOURCE_ID_NOT_EXIST_4020);
        } else {
            Integer resourceId = busPlantResource.getResourceId();
            switch (resourceId) {
                case 0:
                    addCommonResource(busPlantResource);
                    break;
                case 1:
                    addTableGoodResource(busPlantResource);
                    break;
                case 2:
                    addTakeNewGeneResource(busPlantResource);
                    break;
                case 3:
                    addImporvedResource(busPlantResource);
                    break;
                case 4:
                    addOtherInnovateResource(busPlantResource);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 其它创新种质
     *
     * @param busPlantResource
     */

    private void addOtherInnovateResource(BusPlantResource busPlantResource) {
        checkParams(busPlantResource);
        AboutImportData();
        saveResource(busPlantResource);
        DateInsert(busPlantResource);
    }

    /**
     * 品种间杂交改良种质
     *
     * @param busPlantResource
     */
    private void addImporvedResource(BusPlantResource busPlantResource) {
        checkParams(busPlantResource);
        AboutImportData();
        saveResource(busPlantResource);
        DateInsert(busPlantResource);
    }

    /**
     * 携带新基因的优异品种
     *
     * @param busPlantResource
     */
    private void addTakeNewGeneResource(BusPlantResource busPlantResource) {
        checkParams(busPlantResource);
        AboutImportData();
        saveResource(busPlantResource);
        DateInsert(busPlantResource);
    }

    /**
     * 表型鉴定优异品种
     *
     * @param busPlantResource
     */
    private void addTableGoodResource(BusPlantResource busPlantResource) {
        checkParams(busPlantResource);
        AboutImportData();
        saveResource(busPlantResource);
        DateInsert(busPlantResource);
    }

    /**
     * 添加公共资源
     *
     * @param busPlantResource
     */
    private void addCommonResource(BusPlantResource busPlantResource) {
        //参数校验
        checkParams(busPlantResource);
        AboutImportData();
        saveResource(busPlantResource);
        DateInsert(busPlantResource);

    }

    private void DateInsert(BusPlantResource busPlantResource) {
        setDateData(busPlantResource);
        //设置登记批号
        busPlantResource.setInputNum(UUID.randomUUID().toString());
        busPlantResourceRepository.saveAndFlush(busPlantResource);
    }

    private void setDateData(BusPlantResource busPlantResource) {
        busPlantResource.setAuditStatus(0);
        busPlantResource.setCreateTime(new Date());
        busPlantResource.setUpdateTime(new Date());
    }

    //TODO
    private void AboutImportData() {
        //图片上传 TODO
        //性状配置下载 TODO
        //性状信息上传 TODO
    }

    private void saveResource(BusPlantResource busPlantResource) {
        List<Integer> saveResourceIds = busPlantResource.getSaveResourceIds();
        String saveResourceIdString = org.apache.commons.lang3.StringUtils.join(saveResourceIds, ",");
        busPlantResource.setSaveResourceId(saveResourceIdString);

        List<Integer> saveMethodIds = busPlantResource.getSaveMethodIds();
        String saveMethodIdString = org.apache.commons.lang3.StringUtils.join(saveMethodIds, ",");
        busPlantResource.setSaveMethodId(saveMethodIdString);

        List<Integer> saveExtIds = busPlantResource.getSaveExtIds();
        String saveExtString = org.apache.commons.lang3.StringUtils.join(saveExtIds, ",");
        busPlantResource.setSaveExtId(saveExtString);
    }

    private void checkParams(BusPlantResource busPlantResource) {
        if (busPlantResource.getPlantTypeId() == null || busPlantResource.getPlantId() == null) {
            throw new ApiException(ExceptionDefinition.GET_PLANT_RESOURCE_PLANT_TYPE_ID_AND_PLANT_ID_IS_NULL_4021);
        }
        if (StringUtils.isEmpty(busPlantResource.getName()) || StringUtils.isEmpty(busPlantResource.getCode()) || StringUtils.isEmpty(busPlantResource.getSourceArea())) {
            throw new ApiException(ExceptionDefinition.GET_PLANT_RESOURCE_NAME_AND_CODE_IS_NULL_4022);
        }
        if (StringUtils.isEmpty(busPlantResource.getLatinName()) || StringUtils.isEmpty(busPlantResource.getLatinGenusName()) ||
                StringUtils.isEmpty(busPlantResource.getLatinFamilyName()) || StringUtils.isEmpty(busPlantResource.getLatinPrunusPersica())) {
            throw new ApiException(ExceptionDefinition.GET_PLANT_RESOURCE_LATIN_NAME_IS_NULL_4023);
        }
        if (StringUtils.isEmpty(busPlantResource.getChineseName()) || StringUtils.isEmpty(busPlantResource.getChineseGenusName()) ||
                StringUtils.isEmpty(busPlantResource.getChineseFamilyName()) || StringUtils.isEmpty(busPlantResource.getChinesePrunusName())) {
            throw new ApiException(ExceptionDefinition.GET_PLANT_RESOURCE_CHINESE_MESSAGE_IS_NULL_4024);
        }
        if (busPlantResource.getSaveResourceIds().size() == 0 || busPlantResource.getSaveMethodIds().size() == 0 || busPlantResource.getSaveExtIds().size() == 0) {
            throw new ApiException(ExceptionDefinition.GET_PLANT_SAVE_RESOURCE_IS_NULL_4025);
        }
        Boolean isCommonOpen = busPlantResource.getIsCommonOpen();
        if (isCommonOpen) {
            if (StringUtils.isEmpty(busPlantResource.getContactId()) || StringUtils.isEmpty(busPlantResource.getPhone())) {
                throw new ApiException(ExceptionDefinition.GET_PLANT_IS_OPEN_CONTACT_PHONE_IS_NULL_4026);
            }
        }
        if (busPlantResource.getOtherCere().length() > 500 || busPlantResource.getRemark().length() > 500) {
            throw new ApiException(ExceptionDefinition.GET_PLANT_OTHER_CERE_AND_REMARK_LENGTH_MORE_THAN_4027);
        }

    }

    /**
     * 查看用户端的批次资源
     *
     * @param plantResourceVo
     * @param pageable
     * @return
     */
    @Override
    public Page<BusPlantResource> viewBatchResource(PlantResourceVo plantResourceVo, Pageable pageable) {

        Specification<BusPlantResource> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            //输入登记批号
            if (!org.apache.commons.lang3.StringUtils.isEmpty(plantResourceVo.getInputNum())) {
                Predicate inputNum = criteriaBuilder.equal(root.get("inputNum"), plantResourceVo.getInputNum());
                predicates.add(inputNum);
            }

            //选择作物名称
            if (plantResourceVo.getPlantId() != null) {
                Predicate plantId = criteriaBuilder.equal(root.get("plantId"), plantResourceVo.getPlantId());
                predicates.add(plantId);
            }

            //输入作物名称
            String plantName = plantResourceVo.getPlantName();
            if (!StringUtils.isEmpty(plantName)) {
                BusPlant busPlant = busPlantService.findPlantByPlantName(plantName);
                if (busPlant == null) {
                    Predicate plantId = criteriaBuilder.equal(root.get("plantId"), 9999);
                    predicates.add(plantId);
                } else {
                    Predicate plantId = criteriaBuilder.equal(root.get("plantId"), busPlant.getPlantId());
                    predicates.add(plantId);
                }
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //登记年份
            //2022
            //2022-02-02 12:12:12
            if (plantResourceVo.getCreateTime() != null) {
                String createTime = plantResourceVo.getCreateTime();
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

            if (plantResourceVo.getCreateTime() != null) {
                String createTime = plantResourceVo.getCreateTime();
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

            //判断审核状态
            if (plantResourceVo.getAuditStatus() != null) {
                Predicate auditStatus = criteriaBuilder.equal(root.get("auditStatus"), plantResourceVo.getAuditStatus());
                predicates.add(auditStatus);
            }

            Predicate[] predicatees = new Predicate[predicates.size()];
            return criteriaQuery.where(predicates.toArray(predicatees)).getRestriction();

        };
        //如果遇到findAll(specification,pageable);是红色，记住把repsoitory里面继承-JpaSpecificationExecutor<BusPlantResource>即可
        Page<BusPlantResource> busPlantResourcePage = busPlantResourceRepository.findAll(specification, pageable);
        //设置作物类型和作物名称
        if (busPlantResourcePage.getContent() != null && busPlantResourcePage.getContent().size() > 0) {
            busPlantResourcePage.getContent().forEach(plantResource -> {
                //查询类型id得到名称
                String typeName = busPlantTypeService.getPlantTypeForName(plantResource.getPlantTypeId());
                BusPlant busPlant = busPlantService.findPlantByPlantId(plantResource.getPlantId());
                plantResource.setPlantType(typeName);
                plantResource.setPlantName(busPlant.getPlantName());
                viewStatusName(plantResource);
            });
        }
        //共计的个数
        return new PageImpl<BusPlantResource>(busPlantResourcePage.getContent(), pageable, busPlantResourcePage.getTotalElements());
    }

    /**
     * 根据登记批号查询详情
     *
     * @param inputNum
     * @return
     */
    @Override
    public BusPlantResource viewBatchResourceByInputNum(String inputNum) {
        if (!StringUtils.isEmpty(inputNum)) {
            return busPlantResourceRepository.findBusPlantResourceByInputNum(inputNum);
        }
        return null;
    }

    /**
     * 查看用户端的批次资源审核状态
     *
     * @return
     */
    @Override
    public ResourceAuditStatusData viewResourceByAuditStatus() {
        ResourceAuditStatusData resourceAuditStatusData = new ResourceAuditStatusData();
        Integer successResource = busPlantResourceRepository.viewResourceByAuditStatusWithSuccess();
        Integer noSuccessResource = busPlantResourceRepository.viewResourceByAuditStatusWithNoSuccess();
        Integer auditStatusToAudit = busPlantResourceRepository.viewResourceByAuditStatusToAudit();
        Integer auditStatusCancel = busPlantResourceRepository.viewResourceByAuditStatusCancel();
        if (successResource == null) {
            resourceAuditStatusData.setSuccessResource(0);
        } else {
            resourceAuditStatusData.setSuccessResource(successResource);
        }
        if (noSuccessResource == null) {
            resourceAuditStatusData.setNosuccessResource(0);
        } else {
            resourceAuditStatusData.setNosuccessResource(noSuccessResource);
        }
        if (auditStatusToAudit == null) {
            resourceAuditStatusData.setToAuditResource(0);
        } else {
            resourceAuditStatusData.setToAuditResource(auditStatusToAudit);
        }
        if (auditStatusCancel == null) {
            resourceAuditStatusData.setCancelResource(0);
        } else {
            resourceAuditStatusData.setCancelResource(auditStatusCancel);
        }
        return resourceAuditStatusData;
    }

    /**
     * 点击‘登记成功资源XXX个’按钮查看登记成功的资源的信息
     *
     * @return
     */
    @Override
    public List<BusPlantResource> viewResourceByAuditStatusSuccess() {
        return busPlantResourceRepository.findListByAuditSuccess();
    }

    /**
     * 查看用户端的登记成功的种质资源
     *
     * @param plantResourceVerietyVo
     * @param pageable
     * @return
     */

    @Override
    public Page<BusPlantResource> viewResourceVaruety(PlantResourceVerietyVo plantResourceVerietyVo, Pageable pageable) {

        Specification<BusPlantResource> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            //输入登记批号
            if (!org.apache.commons.lang3.StringUtils.isEmpty(plantResourceVerietyVo.getInputNum())) {
                Predicate inputNum = criteriaBuilder.equal(root.get("inputNum"), plantResourceVerietyVo.getInputNum());
                predicates.add(inputNum);
            }

            //输入种质名称
            String verietyVoVarietyName = plantResourceVerietyVo.getVarietyName();
            if (!StringUtils.isEmpty(verietyVoVarietyName)) {
                Predicate name = criteriaBuilder.equal(root.get("name"), plantResourceVerietyVo.getVarietyName());
                predicates.add(name);
            }

            //种质类型名称.下拉选择框  这个给我的是id就可以
            if (plantResourceVerietyVo.getVarietyTypeId() != null) {
                Predicate plantVerietyTypeId = criteriaBuilder.equal(root.get("varietyTypeId"), plantResourceVerietyVo.getVarietyTypeId());
                predicates.add(plantVerietyTypeId);
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //登记年份
            //2022
            //2022-02-02 12:12:12
            if (plantResourceVerietyVo.getCreateTime() != null) {
                String createTime = plantResourceVerietyVo.getCreateTime();
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

            if (plantResourceVerietyVo.getCreateTime() != null) {
                String createTime = plantResourceVerietyVo.getCreateTime();
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

            //选择作物名称 传过来是id形式-
            if (plantResourceVerietyVo.getPlantId() != null) {
                Predicate plantId = criteriaBuilder.equal(root.get("plantId"), plantResourceVerietyVo.getPlantId());
                predicates.add(plantId);
            }
            //确定审核状态是已经登记成功的
            Predicate auditStatus = criteriaBuilder.equal(root.get("auditStatus"), 1);
            predicates.add(auditStatus);

            //选择保存资源类型进行查询  //查询所有的保存资源类型
            if (plantResourceVerietyVo.getSaveResourceTypes() != null && plantResourceVerietyVo.getSaveResourceTypes().contains(",")) {
                List<String> busSaveResourceTypeId = Arrays.asList(StringUtils.split(plantResourceVerietyVo.getSaveResourceTypes(), ","));
                busSaveResourceTypeId.forEach(id -> {
                    predicates.add(criteriaBuilder.like(root.get("saveResourceId"), addLike(dealDatabaseLike(id))));
                });
            } else {
                predicates.add(criteriaBuilder.like(root.get("saveResourceId"), addLike(dealDatabaseLike(plantResourceVerietyVo.getSaveResourceTypes()))));
            }

            Predicate[] predicatees = new Predicate[predicates.size()];
            return criteriaQuery.where(predicates.toArray(predicatees)).getRestriction();
        };

        //如果遇到findAll(specification,pageable);是红色，记住把repsoitory里面继承-JpaSpecificationExecutor<BusPlantResource>即可
        Page<BusPlantResource> busPlantResourcePage = busPlantResourceRepository.findAll(specification, pageable);
        //设置作物类型和作物名称
        if (busPlantResourcePage.getContent() != null && busPlantResourcePage.getContent().size() > 0) {
            busPlantResourcePage.getContent().forEach(plantResource -> {
                //查询类型id得到名称
                String typeName = busPlantTypeService.getPlantTypeForName(plantResource.getPlantTypeId());
                plantResource.setPlantType(typeName);
                //查询作物名称
                BusPlant busPlant = busPlantService.findPlantByPlantId(plantResource.getPlantId());
                plantResource.setPlantName(busPlant.getPlantName());
                //查询种质类型
                BusVarietyType busVarietyType = busVerietyRepository.findBusVarietyTypeByVarietyId(plantResource.getVarietyTypeId());
                if (busVarietyType != null) {
                    plantResource.setPlantVerietyName(busVarietyType.getVarietyName());
                }
                //优化审核状态
                viewStatusName(plantResource);
                //查询资源类型
                String resourceId = plantResource.getSaveResourceId();
                if (resourceId.contains(",")) {
                    String[] split = resourceId.split(",");
                    String sb = "";
                    String saveResourceName = null;
                    for (String id : split) {
                        BusSaveResourceType busSaveResourceType = busSaveResourceTypeRepository.findBusSaveResourceTypeBySaveResourceId(Integer.valueOf(id));
                        if (busSaveResourceType != null) {
                            saveResourceName = busSaveResourceType.getSaveResourceName();
                        }
                        sb += saveResourceName + ",";
                    }
                    plantResource.setSaveResourceName(sb.substring(0, sb.length() - 1));
                } else {
                    BusSaveResourceType busSaveResourceType = busSaveResourceTypeRepository.findBusSaveResourceTypeBySaveResourceId(Integer.valueOf(resourceId));
                    if (busSaveResourceType != null) {
                        String saveResourceName = busSaveResourceType.getSaveResourceName();
                        plantResource.setSaveResourceName(saveResourceName);
                    }
                }
            });
        }
        //共计的个数
        return new PageImpl<BusPlantResource>(busPlantResourcePage.getContent(), pageable, busPlantResourcePage.getTotalElements());
    }

    public static String addLike(String text) {
        return "%" + text + "%";
    }

    public static String dealDatabaseLike(String text) {
        String formatParam = org.apache.commons.lang3.StringUtils.replaceEach(text,
                new String[]{"_", "%", "'", "\\"}, new String[]{"\\_", "\\%", "\\'", "\\\\"});
        return formatParam;
    }

    private void viewStatusName(BusPlantResource plantResource) {
        Integer auditStatus = plantResource.getAuditStatus();
        if (auditStatus != null) {
            switch (auditStatus) {
                case 0:
                    plantResource.setAuditStatusName(AuditStatus.toAuditResource.getDesc());
                    break;
                case 1:
                    plantResource.setAuditStatusName(AuditStatus.successResource.getDesc());
                    break;
                case 2:
                    plantResource.setAuditStatusName(AuditStatus.NosuccessResource.getDesc());
                    break;
                case 3:
                    plantResource.setAuditStatusName(AuditStatus.cancelResource.getDesc());
                    break;
                case 4:
                    plantResource.setAuditStatusName(AuditStatus.cancelToAutitResource.getDesc());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 查看用户端的登记成功的种质资源详情
     *
     * @param inputNum
     * @return
     */
    @Override
    public BusPlantResource viewSuccessPlantResource(String inputNum) {
        if (StringUtils.isEmpty(inputNum)) {
            log.error("inputNum is not exist,please input inputNUm");
        }
        log.info("current operate inputNum :{}", inputNum);
        return busPlantResourceRepository.findBusPlantResourceByInputNum(inputNum);
    }

    /**
     * 用户端撤销登记信息，不可物理删除
     *
     * @param inputNum
     */
    @Override
    public void CancelPlantResource(String inputNum) {
        //先根据登记号查询是否已经审核成功了
        if (StringUtils.isEmpty(inputNum)) {
            throw new ApiException(ExceptionDefinition.GET_PLANT_CANCEL_PLANT_RESOURCE_INPUTMUN_NOT_EXIST_4028);
        }
        BusPlantResource busPlantResourceByInputNumDb = busPlantResourceRepository.findBusPlantResourceByInputNum(inputNum);
        if (busPlantResourceByInputNumDb == null) {
            throw new ApiException(ExceptionDefinition.GET_PLANT_CANCEL_PLANT_RESOURCE_DB_NOT_EXIST_4029);
        }
        if (busPlantResourceByInputNumDb.getAuditStatus() != 1) {
            //如果还没审核  执行逻辑删除,此情况下不需要管理员审核
            BusPlantResource busPlantResource = new BusPlantResource();
            busPlantResource.setIsDelete(1);
            busPlantResource.setAuditStatus(0);
            busPlantResource.setCancelTime(new Date());
            busPlantResource.setUpdateTime(new Date());
            busPlantResourceRepository.saveAndFlush(busPlantResource);
        }
    }

    /**
     * 查询管理员端的资源登记
     *
     * @param dataAuditResourceVo
     * @param pageable
     * @return
     */
    @Override
    public Page<BusPlantResource> dataAuditResource(DataAuditResourceVo dataAuditResourceVo, Pageable pageable) {

        Specification<BusPlantResource> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            //输入登记批号
            if (!org.apache.commons.lang3.StringUtils.isEmpty(dataAuditResourceVo.getInputNum())) {
                Predicate inputNum = criteriaBuilder.equal(root.get("inputNum"), dataAuditResourceVo.getInputNum());
                predicates.add(inputNum);
            }

            //输入作物
            Integer plantId = dataAuditResourceVo.getPlantId();
            if (plantId != null) {
                Predicate plantIdSearch = criteriaBuilder.equal(root.get("plantId"), dataAuditResourceVo.getPlantId());
                predicates.add(plantIdSearch);
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //登记年份
            //2022
            //2022-02-02 12:12:12
            if (dataAuditResourceVo.getCreateTime() != null) {
                String createTime = dataAuditResourceVo.getCreateTime();
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

            if (dataAuditResourceVo.getCreateTime() != null) {
                String createTime = dataAuditResourceVo.getCreateTime();
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

            //确定审核状态是已经登记成功的
            Predicate auditStatus = criteriaBuilder.equal(root.get("auditStatus"), 1);
            predicates.add(auditStatus);

            //登记单位
            if (!StringUtils.isEmpty(dataAuditResourceVo.getAddress())) {
                SysUser sysUser = sysUserRepository.findSysUserByBusiness(dataAuditResourceVo.getAddress());
                if (sysUser != null) {
                    Predicate userId = criteriaBuilder.lessThan(root.get("userId"), sysUser.getUserId());
                    predicates.add(userId);
                }
            }

            //审核状态
            if (plantId != null) {
                Predicate auditStatu = criteriaBuilder.equal(root.get("auditStatus"), dataAuditResourceVo.getAuditStatus());
                predicates.add(auditStatu);
            }

            Predicate[] predicatees = new Predicate[predicates.size()];
            return criteriaQuery.where(predicates.toArray(predicatees)).getRestriction();
        };

        //如果遇到findAll(specification,pageable);是红色，记住把repsoitory里面继承-JpaSpecificationExecutor<BusPlantResource>即可
        Page<BusPlantResource> busPlantResourcePage = busPlantResourceRepository.findAll(specification, pageable);
        //设置作物类型和作物名称
        if (busPlantResourcePage.getContent() != null && busPlantResourcePage.getContent().size() > 0) {
            busPlantResourcePage.getContent().forEach(plantResource -> {
                //查询类型id得到名称
                String typeName = busPlantTypeService.getPlantTypeForName(plantResource.getPlantTypeId());
                plantResource.setPlantType(typeName);
                //查询作物名称
                BusPlant busPlant = busPlantService.findPlantByPlantId(plantResource.getPlantId());
                plantResource.setPlantName(busPlant.getPlantName());
                //优化审核状态
                viewStatusName(plantResource);
                //查询登记单位
                SysUser sysUser = sysUserRepository.getOne(plantResource.getUserId());
                if (sysUser != null) {
                    plantResource.setAddress(sysUser.getBusinessName());
                }
            });
        }
        //共计的个数
        return new PageImpl<BusPlantResource>(busPlantResourcePage.getContent(), pageable, busPlantResourcePage.getTotalElements());
    }

    /**
     * 资源审核不通过，需要填写原因
     * @param busPlantResource
     */
    @Override
    public void addResult(BusPlantResource busPlantResource) {
        if(StringUtils.isEmpty(busPlantResource.getReason())||busPlantResource.getReason().length()>200){
            throw new ApiException(ExceptionDefinition.GET_PLANT_RESOURCE_REASON_IS_NULL_OR_LENGTH_TOO_LONG_4089);
        }
        BusPlantResource busPlantResourceDb = busPlantResourceRepository.getOne(busPlantResource.getPlantResourceId());
        if(busPlantResourceDb!=null){
            busPlantResourceDb.setAuditStatus(2);
            busPlantResourceDb.setAuditTime(new Date());
            busPlantResourceRepository.saveAndFlush(busPlantResourceDb);
        }
    }

    @Override
    public Page<BusPlantResource> viewAudit(ViewAuditVo viewAuditVo, Pageable pageable) {
        Page<BusPlantResource> busPlantResourcePage = busPlantResourceRepository.findAll(ViewAuditSpecification.viewAuditSpecification(viewAuditVo),pageable);
        if(busPlantResourcePage.getContent()!=null){
            busPlantResourcePage.getContent().forEach(busPlantResource -> {
                //资源状态
                Integer resourceStatus = busPlantResource.getResourceStatus();
                if(resourceStatus!=null){
                    switch (resourceStatus){
                        case 0:
                            busPlantResource.setResourceStatusName(ResourceStatus.successResource.getDesc());
                            break;
                        case 1:
                            busPlantResource.setResourceStatusName(ResourceStatus.NocereResource.getDesc());
                            break;
                        default:
                            break;
                    }
                }
                //资源类型
                Integer resourceId = busPlantResource.getResourceId();
                if(resourceId!=null){
                    BusResourceType busResourceType = busResourceTypeRepository.findBusResourceTypeByResourceId(resourceId);
                    if(busResourceType!=null){
                        busPlantResource.setResourceTypeName(busResourceType.getResourceName());
                    }
                }
                //保存资源类型
                String saveResourceType = busPlantResource.getSaveResourceId();
                if (saveResourceType.contains(",")) {
                    String[] split = saveResourceType.split(",");
                    String sb = "";
                    String saveResourceName = null;
                    for (String id : split) {
                        BusSaveResourceType busSaveResourceType = busSaveResourceTypeRepository.findBusSaveResourceTypeBySaveResourceId(Integer.valueOf(id));
                        if (busSaveResourceType != null) {
                            saveResourceName = busSaveResourceType.getSaveResourceName();
                        }
                        sb += saveResourceName + ",";
                    }
                    busPlantResource.setSaveResourceName(sb.substring(0, sb.length() - 1));
                } else {
                    BusSaveResourceType busSaveResourceType = busSaveResourceTypeRepository.findBusSaveResourceTypeBySaveResourceId(Integer.valueOf(resourceId));
                    if (busSaveResourceType != null) {
                        String saveResourceName = busSaveResourceType.getSaveResourceName();
                        busPlantResource.setSaveResourceName(saveResourceName);
                    }
                }
            });
        }

        //共计的个数
        return new PageImpl<BusPlantResource>(busPlantResourcePage.getContent(),pageable,busPlantResourcePage.getTotalElements());
    }

    /**
     * 查看详情
     * @param rId
     * @return
     */
    @Override
    public BusPlantResource viewDetail(String rId) {
        log.info("viewDetail rid:{}",rId);
        BusPlantResource busPlantResource = busPlantResourceRepository.findBusPlantResourceByRId(rId);
        Integer resourceStatus = busPlantResource.getResourceStatus();
        if(resourceStatus!=null){
            switch (resourceStatus){
                case 0:
                    busPlantResource.setResourceStatusName(ResourceStatus.successResource.getDesc());
                    break;
                case 1:
                    busPlantResource.setResourceStatusName(ResourceStatus.NocereResource.getDesc());
                    break;
                default:
                    break;
            }
        }
        //资源类型
        Integer resourceId = busPlantResource.getResourceId();
        if(resourceId!=null){
            BusResourceType busResourceType = busResourceTypeRepository.findBusResourceTypeByResourceId(resourceId);
            if(busResourceType!=null){
                busPlantResource.setResourceTypeName(busResourceType.getResourceName());
            }
        }
        //保存资源类型
        String saveResourceType = busPlantResource.getSaveResourceId();
        if (saveResourceType.contains(",")) {
            String[] split = saveResourceType.split(",");
            String sb = "";
            String saveResourceName = null;
            for (String id : split) {
                BusSaveResourceType busSaveResourceType = busSaveResourceTypeRepository.findBusSaveResourceTypeBySaveResourceId(Integer.valueOf(id));
                if (busSaveResourceType != null) {
                    saveResourceName = busSaveResourceType.getSaveResourceName();
                }
                sb += saveResourceName + ",";
            }
            busPlantResource.setSaveResourceName(sb.substring(0, sb.length() - 1));
        } else {
            BusSaveResourceType busSaveResourceType = busSaveResourceTypeRepository.findBusSaveResourceTypeBySaveResourceId(Integer.valueOf(resourceId));
            if (busSaveResourceType != null) {
                String saveResourceName = busSaveResourceType.getSaveResourceName();
                busPlantResource.setSaveResourceName(saveResourceName);
            }
        }
        return busPlantResource;
    }

    /**
     * 修改资源标记
     * @param rId
     */
    @Override
    public void resourceClick(String rId) {

        BusPlantResource busPlantResource = busPlantResourceRepository.findBusPlantResourceByRId(rId);
        if(busPlantResource != null){
            Integer resourceStatusDb = busPlantResource.getResourceStatus();
            if(resourceStatusDb!=null){
                if(resourceStatusDb==0){
                    busPlantResource.setResourceStatus(1);
                }
                if(resourceStatusDb==1){
                    busPlantResource.setResourceStatus(0);
                }
                busPlantResourceRepository.saveAndFlush(busPlantResource);
            }
        }
    }

    /**
     * 在线登记的资源分页查询
     * @param dataViewVo
     * @param pageRequest
     * @return
     */
    @Override
    public Page<BusPlantResource> pageList(DataViewVo dataViewVo, Pageable pageRequest) {
        Page<BusPlantResource> busPlantResourcePage = busPlantResourceRepository.findAll(DataViewSpecification.dataViewSpecification(dataViewVo),pageRequest);
        if(busPlantResourcePage.getContent() != null){
            busPlantResourcePage.getContent().forEach(busPlantResource -> {
                if(busPlantResourcePage.getContent()!=null){
                    //资源状态
                    Integer resourceStatus = busPlantResource.getResourceStatus();
                    if(resourceStatus!=null){
                        switch (resourceStatus){
                            case 0:
                                busPlantResource.setResourceStatusName(ResourceStatus.successResource.getDesc());
                                break;
                            case 1:
                                busPlantResource.setResourceStatusName(ResourceStatus.NocereResource.getDesc());
                                break;
                            default:
                                break;
                        }
                    }
                    //资源类型
                    Integer resourceId = busPlantResource.getResourceId();
                    if(resourceId!=null){
                        BusResourceType busResourceType = busResourceTypeRepository.findBusResourceTypeByResourceId(resourceId);
                        if(busResourceType!=null){
                            busPlantResource.setResourceTypeName(busResourceType.getResourceName());
                        }
                    }
                    //保存资源类型
                    String saveResourceType = busPlantResource.getSaveResourceId();
                    if (saveResourceType.contains(",")) {
                        String[] split = saveResourceType.split(",");
                        String sb = "";
                        String saveResourceName = null;
                        for (String id : split) {
                            BusSaveResourceType busSaveResourceType = busSaveResourceTypeRepository.findBusSaveResourceTypeBySaveResourceId(Integer.valueOf(id));
                            if (busSaveResourceType != null) {
                                saveResourceName = busSaveResourceType.getSaveResourceName();
                            }
                            sb += saveResourceName + ",";
                        }
                        busPlantResource.setSaveResourceName(sb.substring(0, sb.length() - 1));
                    } else {
                        BusSaveResourceType busSaveResourceType = busSaveResourceTypeRepository.findBusSaveResourceTypeBySaveResourceId(Integer.valueOf(resourceId));
                        if (busSaveResourceType != null) {
                            String saveResourceName = busSaveResourceType.getSaveResourceName();
                            busPlantResource.setSaveResourceName(saveResourceName);
                        }
                    }
                }
            });
        }
        //共计的个数
        return new PageImpl<BusPlantResource>(busPlantResourcePage.getContent(),pageRequest,busPlantResourcePage.getTotalElements());
    }

    /**
     * 查看登记成功的种质资源
     * @param plantResourceId
     * @return
     */
    @Override
    public BusPlantResource findByPlantIdForCharacter(Integer plantResourceId) {
        log.info("findByPlantIdForCharacter plantResourceId :{}",plantResourceId);
        BusPlantResource busPlantResourceDb = busPlantResourceRepository.findBusPlantResourceByPlantResourceId(plantResourceId);
        if(busPlantResourceDb != null){
            Integer plantId = busPlantResourceDb.getPlantId();
            //根据plantId查询作物
            BusPlant busPlantDb = busPlantRepository.findBusPlantByPlantId(plantId);
            if(busPlantDb!=null){
                Integer characterManagerId = busPlantDb.getCharacterManagerId();
                //查询性状信息
                BusCharacterManager busCharacterManagerDb = busCharacterManagerRespository.findBusCharacterManagerByCharacterManagerId(characterManagerId);
                if(busCharacterManagerDb != null){
                    String characterName = busCharacterManagerDb.getCharacterName();
                    busPlantResourceDb.setCharacterManagerName(characterName);
                }
            }
        }
        return busPlantResourceDb;
    }

}
