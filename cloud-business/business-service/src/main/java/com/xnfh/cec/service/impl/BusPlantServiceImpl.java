package com.xnfh.cec.service.impl;

import com.xnfh.cec.repository.BusPlantRepository;
import com.xnfh.cec.repository.specification.ExpertSpecification;
import com.xnfh.cec.repository.specification.PlantSpecification;
import com.xnfh.cec.service.BusPlantService;
import com.xnfh.cec.service.BusPlantTypeService;
import com.xnfh.entity.BusPlant;
import com.xnfh.entity.BusPlantType;
import com.xnfh.exception.ApiException;
import com.xnfh.exception.ExceptionDefinition;
import com.xnfh.vo.PlantVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * resource:
 *
 * @Author wanghaohao ON 2022/5/23
 */
@Service
@Slf4j
public class BusPlantServiceImpl implements BusPlantService {

    @Autowired
    private BusPlantRepository busPlantRepository;

    @Autowired
    private BusPlantTypeService busPlantTypeService;

    /**
     * 定义全局的常量值
     */
    private static final String XLS = "xls";
    /**
     * 定义全局的常量值
     */
    private static final String XLSX = "xlsx";

    @Override
    public Page<BusPlant> getPlantPage(PlantVo plantVo, Pageable pageable) {
        Page<BusPlant> plantPage = busPlantRepository.findAll(PlantSpecification.plantSpecification(plantVo),pageable);
        if(plantPage.getContent() != null && plantPage.getContent().size() > 0){
            plantPage.getContent().forEach(busPlant -> {
                //查询类型id得到名称
                String typeName = busPlantTypeService.getPlantTypeForName(busPlant.getPlantTypeId());
                busPlant.setPlantName(typeName);
            });
        }
        return plantPage;
    }

    @Override
    public BusPlant externalAdd(BusPlant busPlant) {
        Assert.notNull(busPlant,"current operate data busPlant");
        //新增前做校验
        log.info("current operate busplant params plantCode:{},plantName：{}",busPlant.getPlantCode(),busPlant.getPlantName());
        checkParam(busPlant);
        busPlant.setCreateTime(new Date());
        busPlant.setUpdateTime(new Date());
        //新增前查询所有的作物类型
        List<BusPlantType> busPlantTypeList = busPlantTypeService.findBusPlantTypeList();
        if(busPlantTypeList.size()!=0 && busPlantTypeList != null){
            busPlant.setPlantTypeList(busPlantTypeList);
        }
        String plantCode = busPlant.getPlantCode();
        if(plantCode.length() < 10 ){
            busPlant.setPlantCode(plantCode);
        }else{
            throw new ApiException(ExceptionDefinition.INSERT_PLANT_CODE_PARAMS_ERROR_4016);
        }

        return busPlantRepository.saveAndFlush(busPlant);
    }

    private void checkParam(BusPlant busPlant) {
        if(busPlant.getPlantTypeId() == null){
            throw new ApiException(ExceptionDefinition.INSERT_PLANT_type_ID_NO_CHOICE_4013);
        }
        if(StringUtils.isEmpty(busPlant.getPlantName()) && busPlant.getPlantName().length() > 10 ){
            throw new ApiException(ExceptionDefinition.INSERT_PLANT_NAME_PARAMS_ERROR_4014);
        }else{
            long countName = busPlantRepository.selectCount(busPlant.getPlantName());
            if(countName >= 1 ){
                throw new ApiException(ExceptionDefinition.INSERT_PLANT_NAME_ALREADY_EXIST_4015);
            }
        }
    }

    @Override
    public void externalUpdate(BusPlant busPlant) {
        log.info("current search busPlant plantId:{}" ,busPlant.getPlantId());
        checkParam(busPlant);
        List<BusPlantType> busPlantTypeList = busPlantTypeService.findBusPlantTypeList();
        if(busPlantTypeList.size()!=0 && busPlantTypeList != null){
            busPlant.setPlantTypeList(busPlantTypeList);
        }
        BusPlant busPlantDb = busPlantRepository.getOne(busPlant.getPlantId());
        if(busPlantDb == null){
            throw new ApiException(ExceptionDefinition.UPDATE_DATA_DB_NOT_EXIST_4017);
        }
        BusPlant busPlantNew = new BusPlant();
        busPlantNew.setPlantId(busPlant.getPlantId());
        busPlantNew.setUpdateTime(new Date());
        busPlantNew.setPlantTypeId(busPlant.getPlantTypeId());
        busPlantNew.setPlantName(busPlant.getPlantName());
        busPlantNew.setCreateTime(new Date());
        busPlantNew.setPlantCode(busPlant.getPlantCode());
        try{
            busPlantRepository.saveAndFlush(busPlantNew);
        }catch (DataIntegrityViolationException e){
            log.error("BusPlantServiceImpl externalUpdate operate name :{},e:{}",busPlant.getPlantName(),e);
            throw new ApiException(ExceptionDefinition.UPDATE_DATA_ERROR_4018);
        }
    }

    @Override
    public BusPlant findPlantByPlantId(Integer plantId) {
        BusPlant busPlant = busPlantRepository.getOne(plantId);
        if(busPlant == null){
            throw new ApiException(ExceptionDefinition.UPDATE_DATA_ERROR_4018);
        }
        return busPlant;
    }

    @Override
    public void batchDelete(List<Integer> ids) {
        log.info("current operate  ids:{}",ids);
        busPlantRepository.deleteChoice(ids);
    }

    @Override
    public Map<String, Object> importExcel(MultipartFile file) {
        List<BusPlant> tblFixChangeList = new ArrayList<BusPlant>();
        Map<String, Object> map = new HashMap<>();

        Workbook workbook = null;

        String filename = file.getOriginalFilename();
        try {
            if (filename != null) {
                if (filename.endsWith(XLS)) {
                    // 2003
                    workbook = new HSSFWorkbook(file.getInputStream());
                } else if (filename.endsWith(XLSX)) {
                    // 2007
                    workbook = new XSSFWorkbook(file.getInputStream());
                } else {
                    throw new Exception("文件不是Excel文件");
                }
            } else {
                log.error("文件为空");
            }
            // 获取sheet
            Sheet sheet = workbook.getSheet("Sheet1");

            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum == 0) {
                throw new Exception("请填写行数");
            }

            for (int i = 1; i < lastRowNum + 1; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    // 读取cell单元格内容
                    BusPlant tblFixChange = new BusPlant();

                    Integer plantId = Integer.valueOf(getCellValue(row.getCell(0)));
                    tblFixChange.setPlantId(plantId);

                    String plantName = getCellValue(row.getCell(1));
                    tblFixChange.setPlantName(plantName);

                    String remark = getCellValue(row.getCell(2));
                    tblFixChange.setRemark(remark);

                    Integer plantCode = Integer.valueOf(getCellValue(row.getCell(3)));
                    tblFixChange.setPlantCode(String.valueOf(plantCode));

                    Integer plantTypeId = Integer.valueOf(getCellValue(row.getCell(4)));
                    tblFixChange.setPlantTypeId(plantTypeId);

                    String cellValue = getCellValue(row.getCell(5));
                    Date update = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cellValue+" 00:00:00");
                    tblFixChange.setUpdateTime(update);


                    String create = getCellValue(row.getCell(6));
                    Date created = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(create+" 00:00:00");
                    tblFixChange.setCreateTime(created);

                    tblFixChangeList.add(tblFixChange);
                }
            }
            // 批量插入
            busPlantRepository.saveAll(tblFixChangeList);
            map.put("status", 1);
            map.put("data", "导入数据成功");
        } catch (Exception e) {
            map.put("status", -1);
            map.put("data", "导入数据异常");
            log.error(e.getMessage(), e);
        }
        return map;

    }

    /**
     * 查询plant作物
     * @param plantName
     * @return
     */
    @Override
    public BusPlant findPlantByPlantName(String plantName) {
        return busPlantRepository.findBusPlantByPlantName(plantName);
    }

    /**
     * 获取每个单元格内容
     *
     * @param cell cell
     * @return String
     */
    private String getCellValue(Cell cell) {
        // 单元格内容
        String value = "";
        if (cell != null) {
            // 以下是判断数据的类型
            switch (cell.getCellType()) {
                // 数字
                case HSSFCell
                        .CELL_TYPE_NUMERIC:
                    value = cell.getNumericCellValue() + "";
                    // 判断cell是不是日期类型的
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 获取日期类型的单元格
                        Date date = cell.getDateCellValue();
                        if (null != date) {
                            value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                        } else {
                            value = "";
                        }
                    } else {
                        value = new DecimalFormat("0").format(cell.getNumericCellValue());
                    }
                    break;
                // 字符串
                case HSSFCell.CELL_TYPE_STRING:
                    value = cell.getStringCellValue();
                    break;
                // Boolean
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    value = cell.getBooleanCellValue() + "";
                    break;
                // 公式
                case HSSFCell.CELL_TYPE_FORMULA:
                    value = cell.getCellFormula() + "";
                    break;
                // 空格
                case HSSFCell.CELL_TYPE_BLANK:
                    value = "";
                    break;
                // 错误
                case HSSFCell.CELL_TYPE_ERROR:
                    value = "非法字符";
                default:
                    value = "未知类型";
                    break;
            }
        }
        return value.trim();
    }


}
